package fr.gouv.esante.pml.smt.cim11;

import java.io.File;

import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class DeleteTemporaireFile {

	static String OWL_FILE_NAME_TMP = "CGTS_SEM_ICD11-MM-R202301_TMP.owl";
	static String SKOS_FILE_NAME_TMP = "SKOS_FILE_NAME_TMP.xml";
	static final String SKOS_FILE_NAME = "CIM11-MMS-SKOS-FILE_NAME.xml";
	static final String OWL_FILE_NAME = "CGTS_SEM_ICD11-MMS_OWL.owl";
	static String JSON_FILE_NAME = "CIM11_JSON_FILE_NAME.json";

	public static void main(String[] args) {
		
		File jsonFile = new File(JSON_FILE_NAME); //pas supp
		File skosFileNameTmp = new File(SKOS_FILE_NAME_TMP);
		File skosFileName = new File(SKOS_FILE_NAME);
		File owlFileNameTmp = new File(OWL_FILE_NAME_TMP);
	    File owlFileName = new File(OWL_FILE_NAME);  // pas supprime
	    
	    jsonFile.delete();
	    skosFileName.delete();
		skosFileNameTmp.delete();
	    owlFileName.delete();
		owlFileNameTmp.delete();
	    

	}

}
