package fr.gouv.esante.pml.smt.label.cim10;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.google.common.io.Files;

import fr.gouv.esante.pml.smt.utils.ChargeMapping;

public class csvChargerMapping {
	
	
	public static HashMap<String, String> listConcepts = new HashMap<String, String>();
	  
	  
	  
	  
	  public static void chargeCSVConceptToList(final String csvFile) throws Exception {
	  
	  //public static void main(String[]args) throws IOException {		  
		 
		//String csvFile = "D:\\CLadimedJar\\2023_V1\\icd10_ans_OK.csv";  

	    BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"UTF-8"));
	    String row = csvReader.readLine();
	    while ((row = csvReader.readLine()) != null) {
	        String[] data = row.split(";");
	        
	        if(null != data[0] && !data[0].isEmpty()) {
	        	csvChargerMapping.listConcepts.put(data[0], data[1]);
	        }
	        
	    }
	    csvReader.close();
	     
	    
	    
	   // System.out.println(listConcepts.get("C889"));
	    
	    //
	    //for (Map.Entry mapentry : listConcepts.entrySet()) {
	    	// System.out.println("clé: "+mapentry.getKey() 
	    	 //+ " | valeur: " + mapentry.getValue());
	    	 //}
		
	  }

}
