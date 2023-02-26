package fr.gouv.esante.pml.smt.cim11;

import java.io.File;

import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class DeleteTemporaireFile {

	public static void main(String[] args) {
		
		File jsonFile = new File(PropertiesUtil.getProperties("jsonFileName")); //pas supp
		System.out.println("skosFileName "+PropertiesUtil.getProperties("skosFileName"));
		File skosFileName = new File(PropertiesUtil.getProperties("skosFileName")); 
	    File skosFileNameTmp = new File(PropertiesUtil.getProperties("skosFileNameTmp")); 
	    File owlFileName = new File(PropertiesUtil.getProperties("owlFileName"));  // pas supprime
	    File owlFileNameTmp = new File(PropertiesUtil.getProperties("owlFileNameTmp"));
        
	   
	    
	    
	    jsonFile.delete();
	    skosFileName.delete();
	    skosFileNameTmp.delete();
	    owlFileName.delete();
	    owlFileNameTmp.delete();
	    

	}

}
