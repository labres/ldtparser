# About

LDT is the abbreviation for "Labor Daten Transfer", a data format for data exchange with laboratories in Germany.

Tested against LDT Version 3.2.4 

# Usage 

Call parse at instance of LdtParse to create a node tree representation of any LDT file. Any value can be easily accessed via java path syntax.    

Read LDT file and print its structure as java paths. This can be useful to see the internal structure of a file 

	LdtParser parser = new LdtParser();
	Node root = parser.parse(new File("Z01_Befund_mitObj_BAK.ldt").getAbsolutePath());
	System.out.print(root.toString());

Output

	LaborDatenpaketHeader[0].Kopfdaten[0].0001 >> LDT3.2.4
	LaborDatenpaketHeader[0].Kopfdaten[0].8218 >> Timestamp_Erstellung_Datensatz
	LaborDatenpaketHeader[0].Kopfdaten[0].Timestamp[0].7279 >> 201525
	LaborDatenpaketHeader[0].Kopfdaten[0].Timestamp[0].7278 >> 20180712
	LaborDatenpaketHeader[0].Kopfdaten[0].Timestamp[0].7273 >> UTC+2
	LaborDatenpaketHeader[0].Kopfdaten[0].8151 >> Sendendes_System
	LaborDatenpaketHeader[0].Kopfdaten[0].Sendendes System[0].0105 >> V/31/1512/24/aaa
	LaborDatenpaketHeader[0].Kopfdaten[0].Sendendes System[0].0103 >> MusterLIS
	LaborDatenpaketHeader[0].Kopfdaten[0].Sendendes System[0].8315 >> Arzt123456
	LaborDatenpaketHeader[0].Kopfdaten[0].Sendendes System[0].0132 >> 5.12.15.1
	LaborDatenpaketHeader[0].Kopfdaten[0].Sendendes System[0].8316 >> Labor27/12
	LaborDatenpaketHeader[0].Betriebsstaette[0].0204 >> 5
	LaborDatenpaketHeader[0].Betriebsstaette[0].8143 >> Organisation
	LaborDatenpaketHeader[0].Betriebsstaette[0].0203 >> Labor Mueller Meier
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].Kommunikationsdaten[0].7330 >> +LK_Vorwahl_Rufnummer
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].Kommunikationsdaten[0].7331 >> +LK_Vorwahl_Rufnummer
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].Kommunikationsdaten[0].7334 >> www.musterlabor.de
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].Kommunikationsdaten[0].7333 >> +LK_Vorwahl_Rufnummer
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].Kommunikationsdaten[0].7335 >> dr.musterlabor@mail.de
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].1250 >> Laborarztpraxis Mueller/Meier
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].8229 >> Anschrift_Arbeitsstelle
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].Anschrift[0].3109 >> 24
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].Anschrift[0].3107 >> Laborstraße
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].Anschrift[0].3115 >> Eingang links
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].Anschrift[0].3114 >> D
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].Anschrift[0].3113 >> Musterhausen
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].Anschrift[0].3112 >> 12345
	LaborDatenpaketHeader[0].Betriebsstaette[0].Organisation[0].8131 >> Kommunikationsdaten
	...


Read a LDT file and access field values via java path. For the first object in 

	LdtParser parser = new LdtParser();
	Node root = parser.parse(new File("Z01_Befund_mitObj_BAK.ldt").getAbsolutePath());

	//
	// befund as map/node
	Object befund = root.getProperty("Befund[0]");
	Assert.assertTrue(befund instanceof Map);
	
	//
	// gebührennummer (5001) with index
	Object gnr1 = root.getProperty("Befund[0].Laborergebnisbericht[0].Untersuchungsergebnis Klinische 	Chemie[1].Untersuchungsabrechnung[0].5001"); 
	Assert.assertEquals(gnr1, "32444");
	
	//
	// gebührennummer (5001) with mixed index - defaults to index 0 if not given
	Object gnr2 = root.getProperty("Befund.Laborergebnisbericht.Untersuchungsergebnis Klinische Chemie[1].Untersuchungsabrechnung.5001");
	Assert.assertEquals(gnr2, "32444");


# License

Copyright 2020 Healthmetrix GmbH

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
