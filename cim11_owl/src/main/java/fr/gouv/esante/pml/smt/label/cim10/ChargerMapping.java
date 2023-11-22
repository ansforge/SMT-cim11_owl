package fr.gouv.esante.pml.smt.label.cim10;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ChargerMapping {
	
	
	
	public static HashMap<String, String> listConcepts = new HashMap<String, String>();
	
	
   public static  void chargeExcelConceptToList(final String xlsFile) throws Exception {
		
   //public static void main(String[]args) throws IOException {		
		
		
		
		FileInputStream file = new FileInputStream(new File(xlsFile));
		
		//FileInputStream file = new FileInputStream(new File("D:\\cim10\\Libellés et code.xlsx"));
			
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		Iterator<Row> rowIterator = sheet.iterator();
		
		rowIterator.next();
		
		 while (rowIterator.hasNext()) {
	    	 
	    	 Row row = rowIterator.next();
	    	 Cell c1 = row.getCell(0); //get Ref Code
		     Cell c2 = row.getCell(1); // get Libelle
		     
		     String code = "http://data.esante.gouv.fr/atih/cim10/"+c1.getStringCellValue();
		     String noveauLibelle = c2.getStringCellValue();
		     
		      
		     listConcepts.put(code, noveauLibelle);
				
	   }
          
		// System.out.println(listConcepts.get("C00.6"));
		 
		 file.close();
		
	}
	
	
	

	
	
	

	
}
