package fr.gouv.esante.pml.smt.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateTmpFile {
	
	
	
	public static HashMap<String, List<String>> listConcepts = new HashMap<String, List<String>>();
	
	
	public static  void convertFile(final String xlsFile) throws Exception {
		
		
		String inputFile = PropertiesUtil.getNCBIProperties("owlNcbiFile");
		String fileTmp =  PropertiesUtil.getNCBIProperties("owlNcbiFileTmp");
		
		 //Fichier Origine
		  File xmlFile = new File(inputFile);
	        String newline = System.getProperty("line.separator");
	        StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new FileReader(xmlFile));
			String line = null;
			while((line = br.readLine())!= null)
			{
			   // if(line.indexOf("abc") != -1)
			    //{
			        line = line.replaceAll("http://chu-rouen.fr/cismef/NCBI_Taxonomy#","https://data.esante.gouv.fr/chu-rouen/NCBI_taxonomy/").
			        		replaceAll("http://chu-rouen.fr/cismef/NCBI_Taxonomy", "https://data.esante.gouv.fr/chu-rouen/NCBI_taxonomy/").
			        		replaceAll("<owl:AnnotationProperty rdf:about=\"https://data.esante.gouv.fr/chu-rouen/NCBI_taxonomy/AUTO_MAPPING\"/>", "");
			        
			    //}         
			    sb.append(line).append(newline);                
			}
			br.close();
            //Fichier temporaire
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileTmp));
			bw.write(sb.toString());
			bw.close();
		
		
	   
	}
	
	
	



	
	
	

	
}
