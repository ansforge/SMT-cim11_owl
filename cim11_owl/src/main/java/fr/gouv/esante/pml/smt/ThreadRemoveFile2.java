package fr.gouv.esante.pml.smt;

import java.io.File;

import fr.gouv.esante.pml.smt.utils.PropertiesUtil;





public class ThreadRemoveFile2 implements Runnable{

	
	 public ThreadRemoveFile2(String...args) throws Exception {
		 File owlModelingFileNameEN_FR = new File(PropertiesUtil.getProperties("owlModelingFileNameEN_FR")); 
         owlModelingFileNameEN_FR.delete();
	   }
	
	@Override
	public void run() {
		
		
	}

}
