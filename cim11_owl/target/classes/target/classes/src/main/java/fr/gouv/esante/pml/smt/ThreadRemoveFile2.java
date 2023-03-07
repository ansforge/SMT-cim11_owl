package fr.gouv.esante.pml.smt;

import java.io.File;

import fr.gouv.esante.pml.smt.utils.PropertiesUtil;





public class ThreadRemoveFile2 implements Runnable{

	 static String OWL_FINAL_FILE_FR_EN = "CGTS_SEM_ICD11-MMS-R202202-EN-FR-V2.owl";
	 public ThreadRemoveFile2(String...args) throws Exception {
		 File owlModelingFileNameEN_FR = new File(OWL_FINAL_FILE_FR_EN);
         owlModelingFileNameEN_FR.delete();
	   }
	
	@Override
	public void run() {
		
		
	}

}
