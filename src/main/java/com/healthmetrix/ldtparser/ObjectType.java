package com.healthmetrix.ldtparser;

public enum ObjectType {

	/**
	 * Names should not contain any of: ".[]"  
	 */

	Obj_0001("Abrechnungsinformationen"),
	Obj_0002("Abrechnung GKV"),
	Obj_0003("Abrechnung PKV"),
	Obj_0004("Abrechnung Ige-Leistungen"),
	Obj_0005("Abrechnung sonstige Kostenübernahme"),
	Obj_0006("Abrechnung Selektivvertrag"),
	Obj_0007("Anschrift"),
	Obj_0008("Adressat"),
	Obj_0010("Anhang"),
	Obj_0011("Antibiogramm"),
	Obj_0013("Auftragsinformation"),
	Obj_0014("Arztidentifikation"),
	Obj_0017("Befundinformationen"),
	Obj_0019("Betriebsstaette"),
	Obj_0022("Einsenderidentifikation"),
	Obj_0026("Fehlermeldung/Aufmerksamkeit"),
	Obj_0027("Veranlassungsgrund"),
	Obj_0031("Kommunikationsdaten"),
	Obj_0032("Kopfdaten"),
	Obj_0034("Krebsfrueherkennung Zervix-Karzinom (Muster39)"),
	Obj_0035("Laborergebnisbericht"),
	Obj_0036("Laborkennung"),
	Obj_0037("Material"),
	Obj_0040("Mutterschaft"),
	Obj_0041("Namenskennung"),
	Obj_0042("Normalwert"),
	Obj_0043("Organisation"),
	Obj_0045("Patient"),
	Obj_0047("Person"),
	Obj_0048("RgEmpfaenger"),
	Obj_0050("Schwangerschaft"),
	Obj_0051("Sendendes System"),
	Obj_0053("Tier/Sonstiges"),
	Obj_0054("Timestamp"),
	Obj_0055("Blutgruppenzugehoerigkeit"),
	Obj_0056("Tumor"),
	Obj_0058("Untersuchungsabrechnung"),
	Obj_0059("Untersuchungsanforderung"),
	Obj_0060("Untersuchungsergebnis Klinische Chemie"),
	Obj_0061("Untersuchungsergebnis Mikrobiologie"),
	Obj_0062("Untersuchungsergebnis Krebsfrueherkennung Zervix-Karzinom"),
	Obj_0063("Untersuchungsergebnis Zytologie"),
	Obj_0068("Fließtext"),
	Obj_0069("Koerperkenngroessen"),
	Obj_0070("Medikament"),
	Obj_0071("Wirkstoff"),
	Obj_0072("BAK"),
	Obj_0073("Sonstige_Untersuchungsergebnisse"),
	Obj_0100("Diagnose");
	
	String name;

	ObjectType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
