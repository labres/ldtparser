package ldstest;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.healthmetrix.ldtparser.LdtParser;
import com.healthmetrix.ldtparser.LdtParser.Node;

public class LDT324Test {

	private static final Logger LOGGER = LoggerFactory.getLogger(LDT324Test.class);
	
	LdtParser parser = new LdtParser();

	@Test
	public void testAll() {

		File resDir = new File("src/test/resources/ldt-3.2.4");
		File[] ldts = resDir.listFiles();

		if (ldts != null) {
			for (File ldt : ldts) {
				try {
					Node node = parser.parse(ldt.getAbsolutePath());
					Assert.assertNotNull(node);
					LOGGER.info(node.toString());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	@Test
	public void testReadProperty() {

		try {
			
			Node root = parser.parse(new File("src/test/resources/ldt-3.2.4/Z01_Befund_mitObj_BAK.ldt").getAbsolutePath());

			//
			// befund as map/node
			Object befund = root.getProperty("Befund[0]");
			Assert.assertTrue(befund instanceof Map);
			
			//
			// gebührennummer (5001) with index
			Object gnr1 = root.getProperty("Befund[0].Laborergebnisbericht[0].Untersuchungsergebnis Klinische Chemie[1].Untersuchungsabrechnung[0].5001");
			Assert.assertEquals(gnr1, "32444");

			//
			// gebührennummer (5001) with mixed index - defaults to index 0 if not given
			Object gnr2 = root.getProperty("Befund.Laborergebnisbericht.Untersuchungsergebnis Klinische Chemie[1].Untersuchungsabrechnung.5001");
			Assert.assertEquals(gnr2, "32444");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
