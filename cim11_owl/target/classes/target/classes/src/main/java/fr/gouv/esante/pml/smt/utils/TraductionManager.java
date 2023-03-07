package fr.gouv.esante.pml.smt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TraductionManager {
  
  public static HashMap<String, String> icd10label = new HashMap<String, String>();
  
  public static void main(final String[] args) throws Exception {
    chargeXLXScim10Label("C:\\Users\\agochath\\Documents\\cim11\\icd10-translations-06.xlsx");
    addColumnCim10Label("C:\\Users\\agochath\\Documents\\cim11\\icd11-translations-07.xlsx");
    
  }
  
  public static void chargeXLXScim10Label(final String xlsxFile) throws Exception {
    final File myFile = new File(xlsxFile);
    final int sheetIdx = 0;
    final FileInputStream fileInStream = new FileInputStream(myFile);

    // Open the xlsx and get the requested sheet from the workbook
    final XSSFWorkbook workBook = new XSSFWorkbook(fileInStream);
    final XSSFSheet selSheet = workBook.getSheetAt(sheetIdx);

    // Iterate through all the rows in the selected sheet
    final Iterator<Row> rowIterator = selSheet.iterator();
    while (rowIterator.hasNext()) {

      final Row row = rowIterator.next();

      if (row.getRowNum() > 0) {
        final Iterator<Cell> cellIterator = row.cellIterator();
        String enLabel = "";
        String frLabel = "";
        while (cellIterator.hasNext()) {
          final Cell cell = cellIterator.next();          
          if(cell.getColumnIndex()==0) {
            enLabel = cell.getStringCellValue();
          }else if(cell.getColumnIndex()==1) {
            frLabel = cell.getStringCellValue();
          }else if(cell.getColumnIndex()==2) {
            frLabel += "£" + cell.getStringCellValue();
          }
        } 
          icd10label.put(enLabel, frLabel);
          
         
      }
    }
    
    workBook.close();
    fileInStream.close();
  }
  
  
  public static void addColumnCim10Label(final String xlsxFile) throws Exception {
    final File myFile = new File(xlsxFile);
    final int sheetIdx = 0;
    final FileInputStream fileInStream = new FileInputStream(myFile);

    // Open the xlsx and get the requested sheet from the workbook
    final XSSFWorkbook workBook = new XSSFWorkbook(fileInStream);
    final XSSFSheet selSheet = workBook.getSheetAt(sheetIdx);

    // Iterate through all the rows in the selected sheet
    final Iterator<Row> rowIterator = selSheet.iterator();
    while (rowIterator.hasNext()) {

      final Row row = rowIterator.next();

      Cell cell1 = row.createCell(1, CellType.STRING);
      Cell cell2 = row.createCell(2, CellType.STRING);
      if(row.getRowNum() == 0) {
          cell1.setCellValue("string_fr_fde");
          cell2.setCellValue("rank_2");
      }else {
          final Cell cells = row.getCell(0);
          if(icd10label.containsKey(cells.getStringCellValue())) {
            cell1.setCellValue(icd10label.get(cells.getStringCellValue()).split("£")[0]);
            cell2.setCellValue(icd10label.get(cells.getStringCellValue()).split("£")[1]);
          }
          
        
        
      }
    }
    final OutputStream fileoutputstream = new FileOutputStream("C:\\Users\\agochath\\Documents\\cim11\\icd11-translations-08.xlsx");
    workBook.write(fileoutputstream);  
    workBook.close();
    fileInStream.close();
  }

}
