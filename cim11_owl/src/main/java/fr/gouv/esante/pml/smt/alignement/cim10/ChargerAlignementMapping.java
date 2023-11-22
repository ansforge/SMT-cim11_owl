package fr.gouv.esante.pml.smt.alignement.cim10;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ChargerAlignementMapping {
	
	public static HashMap<String, List<String>> listAlignementCim10 = new HashMap<String, List<String>>();

	public static  void  chargeAlignement(final String xlsFile) throws IOException, ParseException {
		
		
		
		FileInputStream file = new FileInputStream(new File(xlsFile));
		
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		
		XSSFSheet sheet = workbook.getSheet("11To10MapToOneCategory");
		
		//XSSFSheet sheetProfils = workbook.getSheet("Profils");
		
		Iterator<Row> rowIterator = sheet.iterator();
		
		rowIterator.next(); 
		
		
		while (rowIterator.hasNext()) {
			 
			 
			 Row row = rowIterator.next();
	    	 
			 Cell uri = row.getCell(0); //uri
		     
	    	 //Cell icd11Code = row.getCell(1); //icd11Code
		     //Cell icd11Chapter = row.getCell(2); //icd11Chapter
		     //Cell icd11Title = row.getCell(3); //icd11Title
		     
		     Cell icd10Code = row.getCell(4); //icd10Code
		     //Cell icd10Chapter = row.getCell(5); //icd10Chapter
		    // Cell icd10Title = row.getCell(6); //icd10Title
		    
		     List<String> valueListe = new ArrayList<>();
		    
             
            	  
            	// valueListe.add(icd11Code.getStringCellValue()); //0 icd11Code
            	// valueListe.add(icd11Chapter.getStringCellValue()); //1 icd11Chapter
            	 //valueListe.add(icd11Title.getStringCellValue()); //2 icd11Title
            	 
            	 valueListe.add(icd10Code.getStringCellValue()); //3 icd10Code
            	 //valueListe.add(icd10Chapter.getStringCellValue()); //4 icd10Chapter
            	 //valueListe.add(icd10Title.getStringCellValue()); //5 icd10Title
            	 
            	 listAlignementCim10.put(uri.getStringCellValue().replace("release/11/2023-01/mms", "release/11/mms"), valueListe);
   		    	 
            	  
            	  
              
              
             
             
		     
              
              
            
		     

		}
		
	}
	
	
 

}
