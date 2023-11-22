package fr.gouv.esante.pml.smt.historisation;

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

public class ChargerMapping {
	
	public static HashMap<String, List<String>> listChnagesMain = new HashMap<String, List<String>>();
	public static HashMap<String, List<String>> listChnagesExtensions = new HashMap<String, List<String>>();

	public static  void  chargeConceptPrincipal(final String xlsFile) throws IOException, ParseException {
		
		
		
		FileInputStream file = new FileInputStream(new File(xlsFile));
		
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		
		XSSFSheet sheet = workbook.getSheet("changes_2023-01_2022-02-main");
		
		//XSSFSheet sheetProfils = workbook.getSheet("Profils");
		
		Iterator<Row> rowIterator = sheet.iterator();
		
		rowIterator.next(); 
		
		
		while (rowIterator.hasNext()) {
			 
			 
			 Row row = rowIterator.next();
	    	 Cell uri2022 = row.getCell(2); //uri_2022
		     Cell uri2023 = row.getCell(3); //uri_2023
		     Cell status = row.getCell(6); //status
		     Cell code = row.getCell(4); //status
		     Cell title = row.getCell(5); //status
		    
		     List<String> valueListe = new ArrayList<>();
		    
              if("Removed".equals(status.getStringCellValue())) {
            	  
            	 valueListe.add(status.getStringCellValue()); //0 status
            	 valueListe.add(code.getStringCellValue()); //1 code
            	 valueListe.add(title.getStringCellValue()); //2 title
            	 
   		        listChnagesMain.put(uri2022.getStringCellValue().replace("release/11/2022-02/mms", "release/11/mms"), valueListe);
   		    	 
            	  
            	  
              }
              
              if("NewlyAdded".equals(status.getStringCellValue())) {
            	  
            	   valueListe.add(status.getStringCellValue()); //0 status
             	   valueListe.add(""); //1 code
             	   valueListe.add(""); //2 title
     		       listChnagesMain.put(uri2023.getStringCellValue().replace("release/11/2023-01/mms", "release/11/mms"), valueListe);
     		    	 
              	  
              	  
               }
              
              if("MovedTo".equals(status.getStringCellValue())) {
            	  
            	valueListe.add(status.getStringCellValue()); //0 status
             	valueListe.add(code.getStringCellValue()); //1 code
                valueListe.add(""); //2 title 	
   		       listChnagesMain.put(uri2023.getStringCellValue().replace("release/11/2023-01/mms", "release/11/mms"), valueListe);
   		    	 
            	  
            	  
             }
		     
              if("MovedUnderShoreline".equals(status.getStringCellValue())) {
            	  
              	
            	  valueListe.add(status.getStringCellValue()); //0 status
             	  valueListe.add(code.getStringCellValue()); //1 code
             	  valueListe.add(title.getStringCellValue()); //2 title
            	  listChnagesMain.put(uri2022.getStringCellValue().replace("release/11/2022-02/mms", "release/11/mms"), valueListe);
     		    	 
              }
              
              if("MovedAboveShoreline".equals(status.getStringCellValue())) {

            	 valueListe.add(status.getStringCellValue()); //0 status
             	 valueListe.add(""); //1 code
             	 valueListe.add(""); //2 title
            	 listChnagesMain.put(uri2023.getStringCellValue().replace("release/11/2023-01/mms", "release/11/mms"), valueListe);
   		    	 
            }
		     

		}
		
	}
	
	
  public static  void  chargeConceptExtension(final String xlsFileExt) throws IOException, ParseException {
		
		
		
		FileInputStream fileExt = new FileInputStream(new File(xlsFileExt));
		
		XSSFWorkbook workbookExt = new XSSFWorkbook(fileExt);
		
		XSSFSheet sheetExt = workbookExt.getSheet("changes_2023-01_2022-02-extens");
		
		//XSSFSheet sheetProfils = workbook.getSheet("Profils");
		
		Iterator<Row> rowIteratorExt = sheetExt.iterator();
		
		///rowIteratorExt.next(); 
		
		
		while (rowIteratorExt.hasNext()) {
			 
			 
			 Row row = rowIteratorExt.next();
	    	 Cell uri2022 = row.getCell(2); //uri_2022
		     Cell uri2023 = row.getCell(3); //uri_2023
		     Cell status = row.getCell(6); //status
		     Cell code = row.getCell(4); //status
		     Cell title = row.getCell(5); //status
		    
		     List<String> valueListe = new ArrayList<>();
		    
              if("Removed".equals(status.getStringCellValue())) {
            	  
            	 valueListe.add(status.getStringCellValue()); //0 status
            	 valueListe.add(code.getStringCellValue()); //1 code
            	 valueListe.add(title.getStringCellValue()); //2 title
            	 
            	 listChnagesExtensions.put(uri2022.getStringCellValue().replace("release/11/2022-02/mms", "release/11/mms"), valueListe);
   		    	 
            	  
            	  
              }
              
              if("NewlyAdded".equals(status.getStringCellValue())) {
            	  
            	   valueListe.add(status.getStringCellValue()); //0 status
             	   valueListe.add(""); //1 code
             	   valueListe.add(""); //2 title
             	  listChnagesExtensions.put(uri2023.getStringCellValue().replace("release/11/2023-01/mms", "release/11/mms"), valueListe);
 	  
               }
              
      
		   
		}
		
		
      

	}

}
