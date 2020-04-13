package com.healthmetrix.ldtparser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LdtParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(LdtParser.class);
	
	private static final String START_SET = "8000";
	private static final String END_SET = "8001";
	private static final String START_OBJECT = "8002";
	private static final String END_OBJECT = "8003";

	/**
	 * Node can either represent sets or objects
	 */

	public static class Node extends HashMap<String, Object> {

		private static final long serialVersionUID = -1029381276532057216L;

		/**
		 * This will add a "Set" or "Object" list entry - we do not really differentiate here
		 * as this will 
		 * - If name is not present in list, we will out a list with given name as key to node 
		 * - We will add a new node to the list and return the new node (so it can be now used as top of the stack)
		 * 
		 * @param name
		 * @return
		 */

		@SuppressWarnings("unchecked")
		private Node put(String name) {

			//
			// put or use list stored under given name 
			List<Node> nodes = (List<Node>)get(name);
			if (nodes == null) {
				nodes = new ArrayList<Node>();
				put(name, nodes);
			}

			//
			// add node to list
			Node node = new Node();
			nodes.add(node);
			return node;						
		}


		/**
		 * Get values with a standard with java style path syntax
		 * 
		 * e.g. "Befund[0].Laborergebnisbericht[0].Untersuchungsergebnis Klinische Chemie[1].Untersuchungsabrechnung[0].5001"
		 *  
		 * @param path
		 * @return
		 * @throws Exception
		 */

		@SuppressWarnings("unchecked")
		public Object getProperty(String path) throws Exception {
			
			//
			// search ends here
			if (path == null) {
				return this;
			}

			//
			// extract sub path and next path
			String subPath = path;
			String nextPath = null;
			int next = path.indexOf(".");
			if (next > 0) {
				subPath = path.substring(0, next);
				nextPath = path.substring(next + 1);
			}

			//
			// extract index
			int idx = 0;
			int idxStart = subPath.indexOf("[");
			if (idxStart > 0 && subPath.endsWith("]")) {
				idx = Integer.valueOf(subPath.substring(idxStart + 1, subPath.length() - 1));
				subPath = subPath.substring(0, idxStart);
			}

			//
			// dig deeper in list, node or value
			Object val = get(subPath);
			if (val instanceof List) {
				return ((List<Node>)val).get(idx).getProperty(nextPath);
			}
			else if (val instanceof Node) {
				return ((Node)val).getProperty(nextPath);
			}
			else {
				return val;
			}
		}

		/**
		 * Print out the whole structure 
		 */
		public String toString() {
			StringBuffer out = new StringBuffer();
			print(out, "");
			return out.toString();
		}

		@SuppressWarnings("unchecked")
		private void print(StringBuffer out, String path) {
			for (Entry<String, Object> e : this.entrySet()) {
				if (e.getValue() instanceof List) {
					int i = 0;
					for (Node node : (List<Node>)e.getValue()) {
						node.print(out, path + (path.isEmpty() ? "" : ".") + e.getKey() + "[" + i++ + "]");
					}
				}
				else if (e.getValue() instanceof Node) {
					((Node)e.getValue()).print(out, path + (path.isEmpty() ? "" : ".") + e.getValue());
				}
				else {
					out.append(String.format("%s%s%s >> %s\n", 
						path,
						(path.isEmpty() ? "" : "."),
						e.getKey(), 
						e.getValue()));
				}
			}
		}
	}


	public Node parse(File file) throws IOException {
		return parse(file.getAbsolutePath());
	}

	public Node parse(String path) throws IOException {
		return parse(Paths.get(path));
	}

	public Node parse(Path path) throws IOException {
		try (Stream<String> stream = Files.lines(path, StandardCharsets.ISO_8859_1)) {
			return parse(stream);
		}
	}

	private Node parse(Stream<String> stream) {
		Stack<Node> stack = new Stack<>();
		stack.add(new Node());
		AtomicInteger integer = new AtomicInteger();
		stream.forEach(line -> readLine(stack, line, integer.incrementAndGet()));
		return stack.pop();
	}

	private void readLine(Stack<Node> stack, String line, int lineNo) {

		LOGGER.trace("Reading line {}", line);
		
		//
		// Check if the line meets the minimum requirements:
		// 3 digits for length, 4 digits for the identifier
		if (line.length() < 7) {
			LOGGER.error("Line '{}' was less than 7 characters, continuing anyway", line);
		}

		//
		// Read the length and check whether it had the correct length
		int length = Integer.parseInt(line.substring(0, 3));
		if (length != line.length() + 2) {
			LOGGER.warn("Line '{}' should have length {}, but was {}. Ignoring specified length", line, (line.length() + 2), length);
			length = line.length() + 2;
		}

		// Read identifier and payload
		String identifier = line.substring(3, 7);
		String payload = line.substring(7);

		switch (identifier) {
		case START_SET: {

			//
			// Start of Set
			assureLength(line, length, 13);

			// Extract SetType from payload and create Satz matching it
			SetType satzart = SetType.getByCode(payload);
			stack.push(stack.peek().put(satzart.name()));
			break;
		}
		case END_SET: {

			//
			// End of Set
			stack.pop();
			break;
		}
		case START_OBJECT: {

			//
			// Start of Object
			assureLength(line, length, 17);

			String objName;
			if (ObjectType.valueOf(payload) != null) {
				objName = ObjectType.valueOf(payload).getName();
			}
			else {
				objName = payload;
			}

			stack.push(stack.peek().put(objName));
			break;
		}
		case END_OBJECT: {

			//
			// End of object
			assureLength(line, length, 17);
			stack.pop();
			break;
		}
		default:

			//
			// property
			stack.peek().put(identifier, payload);
			break;
		}
	}


	/**
	 * Check if the line matches the expected length.
	 * 
	 * @param line
	 *			the line to check
	 * @param length
	 *			the actual length
	 * @param expected
	 *			the length specified by the line
	 */
	private static void assureLength(String line, int length, int expected) {
		if (length != expected) {
			LOGGER.warn("Line '{}' must have length {}, was {}", line, expected, length);
		}
	}
}
