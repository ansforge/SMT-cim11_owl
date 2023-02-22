package fr.gouv.esante.pml.smt.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import fr.gouv.esante.pml.smt.cim11.SKOSToOWL;

public class ChargeMapping {
  
  public static HashMap<String, String> listTraduction = new HashMap<String, String>();
  public static HashMap<String, String> listTraductionFr = new HashMap<String, String>();
  public static HashMap<String, String[]> listTraductionFr2 = new HashMap<String, String[]>();
  public static HashMap<String, String[]> listTraductionFr3 = new HashMap<String, String[]>();
  public static HashMap<String, String> listTraductionDe = new HashMap<String, String>();
  public static HashMap<String, String> listTraductionPl = new HashMap<String, String>();
  public static HashMap<String, String> listTraductionEL = new HashMap<String, String>();
  public static HashMap<String, String> listTraductionES = new HashMap<String, String>();
  public static HashMap<String, String> listMappingCIM11to10 = new HashMap<String, String>();
  public static SortedMap<String, String> listCodeEMDN = new TreeMap<String, String>();
  public static SortedMap<String, String> listCodeATC = new TreeMap<String, String>();
  public static HashMap<String, List<String>> listConceptsEma = new HashMap<String, List<String>>();
  public static List<String> listCodeADICAP = new ArrayList<String>();
  public static List<String> listLabelMMS = new ArrayList<String>();
  public static HashMap<String, String> listDictionnaryCode = new HashMap<String, String>();
  public static HashMap<String, Set<String>> listNCBIConcepts = new HashMap<String, Set<String>>();
  public static HashMap<String, List<String>> listConceptsEdqm = new HashMap<String, List<String>>();
  
  public static String URI_Edqm = "http://data.esante.gouv.fr/coe/standardterms";

  
  public static HashMap<String, String> listTopograohie = new HashMap<String, String>();
  public static HashMap<String, String> listModeAcces = new HashMap<String, String>();
  public static HashMap<String, String> listAction = new HashMap<String, String>();
  public static List<String[]> listCatigorieVerbeAction = new ArrayList<String[]>();
  
  public static void chargeDictionnaryCodeToMaps(final String xlsxFile) throws Exception {
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

	      // Iterate through all the columns in the row and build ","
	      // separated string
	      if (row.getRowNum() > 0) {
	        final Iterator<Cell> cellIterator = row.cellIterator();
	        final StringBuffer sb = new StringBuffer();
	        while (cellIterator.hasNext()) {
	          final Cell cell = cellIterator.next();
	          if (sb.length() != 0) {
	            sb.append("£");
	          }
	          // If you are using poi 4.0 or over, change it to
	          // cell.getCellType
	          switch (cell.getCellType()) {
	            case STRING:
	              sb.append(cell.getStringCellValue());
	              break;
	            case NUMERIC:
	              sb.append(cell.getNumericCellValue());
	              break;
	            case BOOLEAN:
	              sb.append(cell.getBooleanCellValue());
	              break;
	            default:
	              sb.append(" ");
	              break;
	          }
	        }
	        if(sb.toString().split("£").length == 2) {
	        	listDictionnaryCode.put(sb.toString().split("£")[0], sb.toString().split("£")[1]);
	        }

	      }

	    }
	    workBook.close();
	  }
  
  
  
//  public static void chargelistTopographie(final String csvFile) throws Exception {
//	   
//	    BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"cp1252"));
//	    String row;
//	    while ((row = csvReader.readLine()) != null) {
//	        String[] data = row.split(";");
//	        
//	        if(data.length == 2) {
//	        	listTopograohie.put(data[0].trim(), data[1].trim());
//	        }
//	        
//	    }
//	    csvReader.close();
//	     
//	  }
  
  public static void chargelistTopographie(final String xlsxFile) throws Exception {
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

	      // Iterate through all the columns in the row and build ","
	      // separated string
	      if (row.getRowNum() > 0) {
	        final Iterator<Cell> cellIterator = row.cellIterator();
	        final StringBuffer sb = new StringBuffer();
	        while (cellIterator.hasNext()) {
	          final Cell cell = cellIterator.next();
	          if (sb.length() != 0) {
	            sb.append("£");
	          }
	          // If you are using poi 4.0 or over, change it to
	          // cell.getCellType
	          switch (cell.getCellType()) {
	            case STRING:
	              sb.append(cell.getStringCellValue());
	              break;
	            case NUMERIC:
	              sb.append(cell.getNumericCellValue());
	              break;
	            case BOOLEAN:
	              sb.append(cell.getBooleanCellValue());
	              break;
	            default:
	              sb.append(" ");
	              break;
	          }
	        }
	        listTopograohie.put(sb.toString().split("£")[0].trim(), sb.toString().split("£")[1].trim());

	      }

	    }
	    workBook.close();
	  }
  
  
  public static void chargeMappingCIM11to10Maps(final String xlsxFile) throws Exception {
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

	      // Iterate through all the columns in the row and build ","
	      // separated string
	      if (row.getRowNum() > 0) {
	        final Iterator<Cell> cellIterator = row.cellIterator();
	        final StringBuffer sb = new StringBuffer();
	        while (cellIterator.hasNext()) {
	          final Cell cell = cellIterator.next();
	          if (sb.length() != 0) {
	            sb.append("£");
	          }
	          // If you are using poi 4.0 or over, change it to
	          // cell.getCellType
	          switch (cell.getCellType()) {
	            case STRING:
	              sb.append(cell.getStringCellValue());
	              break;
	            case NUMERIC:
	              sb.append(cell.getNumericCellValue());
	              break;
	            case BOOLEAN:
	              sb.append(cell.getBooleanCellValue());
	              break;
	            default:
	              sb.append(" ");
	              break;
	          }
	        }
//	        if(sb.toString().split("£").length == 5) {
	        	listMappingCIM11to10.put(sb.toString().split("£")[0].replace("/beta", ""), sb.toString().split("£")[1]);
//	        }

	      }

	    }
	    workBook.close();
	  }
	  
  
  
  public static void chargelistCodeADICAPenDouble(final String csvFile) throws Exception {
	   
	    BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"UTF-8"));
	    String row;
	    while ((row = csvReader.readLine()) != null) {
	        String[] data = row.split(";");
	        
	        if(data.length == 1) {
	        	listCodeADICAP.add(data[0]);
	        }
	        
	    }
	    csvReader.close();
	     
	  }
  

  public static void chargeModeAcces(final String xlsxFile) throws Exception {
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

      // Iterate through all the columns in the row and build ","
      // separated string
      if (row.getRowNum() > 0) {
        final Iterator<Cell> cellIterator = row.cellIterator();
        final StringBuffer sb = new StringBuffer();
        while (cellIterator.hasNext()) {
          final Cell cell = cellIterator.next();
          if (sb.length() != 0) {
            sb.append("£");
          }
          // If you are using poi 4.0 or over, change it to
          // cell.getCellType
          switch (cell.getCellType()) {
            case STRING:
              sb.append(cell.getStringCellValue());
              break;
            case NUMERIC:
              sb.append(cell.getNumericCellValue());
              break;
            case BOOLEAN:
              sb.append(cell.getBooleanCellValue());
              break;
            default:
              sb.append(" ");
              break;
          }
        }
//        if(sb.toString().split("£").length == 5) {
        listModeAcces.put(sb.toString().split("£")[3], sb.toString());
//        }

      }

    }
    workBook.close();
  }
  
  
  public static void chargeListeAction(final String xlsxFile) throws Exception {
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

	      // Iterate through all the columns in the row and build ","
	      // separated string
	      if (row.getRowNum() > 0) {
	        final Iterator<Cell> cellIterator = row.cellIterator();
	        final StringBuffer sb = new StringBuffer();
	        while (cellIterator.hasNext()) {
	          final Cell cell = cellIterator.next();
	          if (sb.length() != 0) {
	            sb.append("£");
	          }
	          // If you are using poi 4.0 or over, change it to
	          // cell.getCellType
	          switch (cell.getCellType()) {
	            case STRING:
	              sb.append(cell.getStringCellValue());
	              break;
	            case NUMERIC:
	              sb.append(cell.getNumericCellValue());
	              break;
	            case BOOLEAN:
	              sb.append(cell.getBooleanCellValue());
	              break;
	            default:
	              sb.append(" ");
	              break;
	          }
	        }
//	        if(sb.toString().split("£").length == 5) {
	        listAction.put(sb.toString().split("£")[0], sb.toString());
//	        }

	      }

	    }
	    workBook.close();
	  }
  
  public static void chargeMMSLabel(final String xlsxFile) throws Exception {
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

	      // Iterate through all the columns in the row and build ","
	      // separated string
	      if (row.getRowNum() > 0) {
	        final Iterator<Cell> cellIterator = row.cellIterator();
	        final StringBuffer sb = new StringBuffer();
	        while (cellIterator.hasNext()) {
	          final Cell cell = cellIterator.next();
	          if (sb.length() != 0) {
	            sb.append("£");
	          }
	          // If you are using poi 4.0 or over, change it to
	          // cell.getCellType
	          switch (cell.getCellType()) {
	            case STRING:
	              sb.append(cell.getStringCellValue());
	              break;
	            case NUMERIC:
	              sb.append(cell.getNumericCellValue());
	              break;
	            case BOOLEAN:
	              sb.append(cell.getBooleanCellValue());
	              break;
	            default:
	              sb.append(" ");
	              break;
	          }
	        }
	        if(sb.toString().split("£").length == 1) {
	        	listLabelMMS.add(sb.toString().split("£")[0].toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""));
	        }

	      }

	    }
	    workBook.close();
	  }
  
  
  
  public static void chargeXLXSFileToMaps(final String xlsxFile) throws Exception {
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

	      // Iterate through all the columns in the row and build ","
	      // separated string
	      if (row.getRowNum() > 0) {
	        final Iterator<Cell> cellIterator = row.cellIterator();
	        final StringBuffer sb = new StringBuffer();
	        while (cellIterator.hasNext()) {
	          final Cell cell = cellIterator.next();
	          if (sb.length() != 0) {
	            sb.append("£");
	          }
	          // If you are using poi 4.0 or over, change it to
	          // cell.getCellType
	          switch (cell.getCellType()) {
	            case STRING:
	              sb.append(cell.getStringCellValue());
	              break;
	            case NUMERIC:
	              sb.append(cell.getNumericCellValue());
	              break;
	            case BOOLEAN:
	              sb.append(cell.getBooleanCellValue());
	              break;
	            default:
	              sb.append(" ");
	              break;
	          }
	        }
	        final String id = sb.toString().split("£")[0].split("/")[8];
	        if (!SKOSToOWL.idICD11.containsKey(id)) {
	          SKOSToOWL.idICD11.put(id, sb.toString());
	        } else if (!SKOSToOWL.idICD11.containsKey((id + "A"))) {
	          SKOSToOWL.idICD11.put((id + "A"), sb.toString());
	        } else {
	          SKOSToOWL.idICD11.put((id + "AA"), sb.toString());
	        }

	      }

	    }
	    workBook.close();
	  }

  public static void chargeXLXSTraductionFileToList(final String xlsxFile) throws Exception {
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

      // Iterate through all the columns in the row and build ","
      // separated string
      if (row.getRowNum() > 0) {
        final Iterator<Cell> cellIterator = row.cellIterator();
        final StringBuffer sb = new StringBuffer();
        while (cellIterator.hasNext()) {
          final Cell cell = cellIterator.next();
          if (sb.length() != 0) {
            sb.append("£");
          }
          // If you are using poi 4.0 or over, change it to
          // cell.getCellType
          switch (cell.getCellType()) {
            case STRING:
              sb.append(cell.getStringCellValue());
              break;
            case NUMERIC:
              sb.append(cell.getNumericCellValue());
              break;
            case BOOLEAN:
              sb.append(cell.getBooleanCellValue());
              break;
            default:
              sb.append(" ");
              break;
          }
        }

        final String id = sb.toString().split("£")[1]
            .split("/")[sb.toString().split("£")[1].split("/").length - 1];
        if (ChargeMapping.listTraduction.containsKey(id)) {
          sb.append("@").append(ChargeMapping.listTraduction.get(id));
          ChargeMapping.listTraduction.put(id, sb.toString());
        } else {
          ChargeMapping.listTraduction.put(id, sb.toString());
        }


      }

    }

    workBook.close();
  }
  
  public static void chargeXLXSTraductionFileToList2(final String xlsxFile) throws Exception {
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

      // Iterate through all the columns in the row and build ","
      // separated string
      if (row.getRowNum() > 0) {
        final Iterator<Cell> cellIterator = row.cellIterator();
        final StringBuffer sb = new StringBuffer();
        while (cellIterator.hasNext()) {
          final Cell cell = cellIterator.next();
          if (sb.length() != 0) {
            sb.append("£");
          }
          // If you are using poi 4.0 or over, change it to
          // cell.getCellType
          switch (cell.getCellType()) {
            case STRING:
              sb.append(cell.getStringCellValue());
              break;
            case NUMERIC:
              sb.append(cell.getNumericCellValue());
              break;
            case BOOLEAN:
              sb.append(cell.getBooleanCellValue());
              break;
            default:
              sb.append(" ");
              break;
          }
        }

        String[] data = sb.toString().split("£");
        if(null != data[2] && !data[2].isEmpty()) {
          ChargeMapping.listTraduction.put(data[1].toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""), data[2]);
        }


      }

    }

    workBook.close();
  }
  
  
  
  
  
  public static void chargeXLXSTraductionFileToList3(final String xlsxFile) throws Exception {
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

	      // Iterate through all the columns in the row and build ","
	      // separated string
	      if (row.getRowNum() > 0) {
	    	  	String col1 = (null != row.getCell(0)) ? row.getCell(0).getStringCellValue() : null;
		    	String col2 = (null != row.getCell(1)) ? row.getCell(1).getStringCellValue() : null;
		    	Double col3 = (null != row.getCell(2) && row.getCell(2).getCellType() == CellType.NUMERIC) ? row.getCell(2).getNumericCellValue() : null;
		    	String col4 = (null != row.getCell(3)) ? row.getCell(3).getStringCellValue() : null;
		    	Boolean col5 = (null != row.getCell(4)) ? row.getCell(4).getBooleanCellValue() : null;
		    	String col6 = (null != row.getCell(5)) ? row.getCell(5).getStringCellValue() : null;
		    	String col7 = (null != row.getCell(6)) ? row.getCell(6).getStringCellValue() : null;
		    	Double col8 = (null != row.getCell(7) && row.getCell(7).getCellType() == CellType.NUMERIC) ? row.getCell(7).getNumericCellValue() : null;
		    	String col9 = (null != row.getCell(8)) ? row.getCell(8).getStringCellValue() : null;
		    	String col10 = (null != row.getCell(9)) ? row.getCell(9).getStringCellValue() : null;
		    	String col11 = (null != row.getCell(10)) ? row.getCell(10).getStringCellValue() : null;
		    	String col12 = (null != row.getCell(11)) ? row.getCell(11).getStringCellValue() : null;
		    	Boolean col13 = (null != row.getCell(12)) ? row.getCell(12).getBooleanCellValue() : null;
		    	String col14 = (null != row.getCell(13)) ? row.getCell(13).getStringCellValue() : null;
		    	String col15 = (null != row.getCell(14)) ? row.getCell(14).getStringCellValue() : null;
		    	String col16 = (null != row.getCell(15)) ? row.getCell(15).getStringCellValue() : null;
		    	
		    	String[] data = {col1, col2, ""+col3, col4, col5.toString(), col6, col7, ""+col8, col9, col10, col11, col12, col13.toString(), col14, col15, col16};
	        if(null != col9 && !col9.isEmpty()) {
	          ChargeMapping.listTraductionFr2.put(col9, data);
	        }


	      }

	    }

	    workBook.close();
	  }
  
  
  public static void chargeXLXSTraductionFileToList4(final String xlsxFile) throws Exception {
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
	      String col1 = (null != row.getCell(0)) ? row.getCell(0).getStringCellValue() : null;
	    	String col2 = (null != row.getCell(1)) ? row.getCell(1).getStringCellValue() : null;
	    	Double col3 = (null != row.getCell(2)&& row.getCell(2).getCellType() == CellType.NUMERIC) ? row.getCell(2).getNumericCellValue() : null;
	    	String col4 = (null != row.getCell(3)) ? row.getCell(3).getStringCellValue() : null;
	    	String col5 = (null != row.getCell(4)) ? row.getCell(4).getStringCellValue() : null;
	    	String col6 = (null != row.getCell(5)) ? row.getCell(5).getStringCellValue() : null;
	    	String col7 = (null != row.getCell(6)) ? row.getCell(6).getStringCellValue() : null;
	    	Double col8 = (null != row.getCell(7)&& row.getCell(7).getCellType() == CellType.NUMERIC) ? row.getCell(7).getNumericCellValue() : null;
	    	String col9 = (null != row.getCell(8)) ? row.getCell(8).getStringCellValue() : null;
	    	String col10 = (null != row.getCell(9)) ? row.getCell(9).getStringCellValue() : null;
	    	String col11 = (null != row.getCell(10)) ? row.getCell(10).getStringCellValue() : null;
	    	String col12 = (null != row.getCell(11)) ? row.getCell(11).getStringCellValue() : null;
	    	String col13 = (null != row.getCell(12)) ? row.getCell(12).getStringCellValue() : null;
	    	String col14 = (null != row.getCell(13)) ? row.getCell(13).getStringCellValue() : null;
	    	String col15 = (null != row.getCell(14)) ? row.getCell(14).getStringCellValue() : null;
	      
	    	if ( null != col6) {
	    		String[] data = {col1, col2, ""+col3, col4, col5, col6, col7, ""+col8, col9,col10, col11, col12, col13, col14, col15};
	        
	        ChargeMapping.listTraductionFr3.put(col6.toLowerCase().replaceAll("[^a-zA-Z0-9_-]",""), data);
	        


	      }

	    }

	    workBook.close();
	  }
  
  
  public static void chargeXLXSTraductionFileToList5(final String xlsxFile) throws Exception {
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
	      String col1 = (null != row.getCell(0)) ? row.getCell(0).getStringCellValue() : null;

	    	if ( null != col1) {
	        
	        ChargeMapping.listTraductionFr.put(col1.toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""), col1);
	        


	      }

	    }

	    workBook.close();
	  }

  
  
  
  
  public static void chargeXLXSTraductionFileToListES(final String xlsxFile) throws Exception {
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

	      // Iterate through all the columns in the row and build ","
	      // separated string
	      if (row.getRowNum() > 0) {
	        final Iterator<Cell> cellIterator = row.cellIterator();
	        final StringBuffer sb = new StringBuffer();
	        while (cellIterator.hasNext()) {
	          final Cell cell = cellIterator.next();
	          if (sb.length() != 0) {
	            sb.append("£");
	          }
	          // If you are using poi 4.0 or over, change it to
	          // cell.getCellType
	          switch (cell.getCellType()) {
	            case STRING:
	              sb.append(cell.getStringCellValue());
	              break;
	            case NUMERIC:
	              sb.append(cell.getNumericCellValue());
	              break;
	            case BOOLEAN:
	              sb.append(cell.getBooleanCellValue());
	              break;
	            default:
	              sb.append(" ");
	              break;
	          }
	        }

	        String[] data = sb.toString().split("£");
	        if(null != data[1] && !data[1].isEmpty()) {
	          ChargeMapping.listTraductionES.put(data[2].toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""), data[3]);
	        }


	      }

	    }

	    workBook.close();
	  }
  
  public static void chargeXLXSTraductionFileToListAR_RU_ZH(final String xlsxFile) throws Exception {
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

	      // Iterate through all the columns in the row and build ","
	      // separated string
	      if (row.getRowNum() > 0) {
	        final Iterator<Cell> cellIterator = row.cellIterator();
	        final StringBuffer sb = new StringBuffer();
	        while (cellIterator.hasNext()) {
	          final Cell cell = cellIterator.next();
	          if (sb.length() != 0) {
	            sb.append("£");
	          }
	          // If you are using poi 4.0 or over, change it to
	          // cell.getCellType
	          switch (cell.getCellType()) {
	            case STRING:
	              sb.append(cell.getStringCellValue());
	              break;
	            case NUMERIC:
	              sb.append(cell.getNumericCellValue());
	              break;
	            case BOOLEAN:
	              sb.append(cell.getBooleanCellValue());
	              break;
	            default:
	              sb.append(" ");
	              break;
	          }
	        }

	        String[] data = sb.toString().split("£");
	        if(data.length > 1 && null != data[1] && !data[1].isEmpty()) {
	        	ChargeMapping.listTraduction.put(data[0].toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""), data[1]);
	        
	        }


	      }

	    }

	    workBook.close();
	  }
  
  
  public static void chargeCSVTraductionFileToList(final String csvFile) throws Exception {
   
    BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"UTF-8"));
    String row;
    while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(";");
        
        if(data[3].contains("title") || data[3].contains("definition")) {
        
        final String id = data[2].split("/")[data[2].split("/").length - 1];
        if (ChargeMapping.listTraduction.containsKey(id)) {
          row += "@" + ChargeMapping.listTraduction.get(id);
          ChargeMapping.listTraduction.put(id, row);
        } else {
          ChargeMapping.listTraduction.put(id, row);
        }
        }
        
    }
    csvReader.close();
     
  }
  
  public static void chargeCSVTraductionFileToList2(final String csvFile) throws Exception {
    
    BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"UTF-8"));
    String row;
    while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(";");
        
        if(null != data[5] && !data[5].isEmpty()) {
          ChargeMapping.listTraductionFr.put(data[4].toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""), data[5]);
        }
        
    }
    csvReader.close();
     
  }
  
  
public static void chargeCSVTraductionPlFileToList(final String csvFile) throws Exception {
    
    BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"UTF-8"));
    String row;
    while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(";");
        System.out.println(row);
        if(null != data[2] && !data[2].isEmpty()) {
          ChargeMapping.listTraductionPl.put(data[1].toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""), data[2]);
        }
        
    }
    csvReader.close();
     
  }

public static void chargeCSVTraductionELFileToList(final String csvFile) throws Exception {
  
  BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"UTF-8"));
  String row;
  while ((row = csvReader.readLine()) != null) {
      String[] data = row.split(";");
      
      if(null != data[2] && !data[2].isEmpty()) {
        ChargeMapping.listTraductionEL.put(data[1].toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""), data[2]);
      }
      
  }
  csvReader.close();
   
}

public static void chargeCSVCatigorieVerbeActionList(final String csvFile) throws Exception {
	  
	  BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"UTF-8"));
	  String row;
	  while ((row = csvReader.readLine()) != null) {
	      String[] data = row.split(";");
	      
	      if(null != data[2] && !data[2].isEmpty()) {
	        ChargeMapping.listCatigorieVerbeAction.add(data);
	      }
	      
	  }
	  csvReader.close();
	   
	}
  
  
public static void chargeCSVTraductionFileBeToList(final String csvFileEn, final String csvFileDe) throws Exception {
    
    BufferedReader csvReaderEn = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileEn),"UTF-8"));
    BufferedReader csvReaderDe = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileDe),"UTF-8"));
    String rowEn;
    String rowDe;
    while (((rowEn = csvReaderEn.readLine()) != null) && ((rowDe = csvReaderDe.readLine()) != null)) {
        
      if(null != rowEn && !rowEn.isEmpty() && null != rowDe && !rowDe.isEmpty()) {
          ChargeMapping.listTraductionDe.put(rowEn.toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""), rowDe);
        }
        
    }
    csvReaderEn.close();
    csvReaderDe.close();
     
  }
  
  
  
  public static void chargeXLXScim10TraductionFileToList(final String xlsxFile) throws Exception {
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

      // Iterate through all the columns in the row and build "£"
      // separated string
      if (row.getRowNum() > 0) {
        final Iterator<Cell> cellIterator = row.cellIterator();
        final StringBuffer sb = new StringBuffer();
        while (cellIterator.hasNext()) {
          final Cell cell = cellIterator.next();
          if (sb.length() != 0) {
            sb.append("£");
          }
          // If you are using poi 4.0 or over, change it to
          // cell.getCellType
          switch (cell.getCellType()) {
            case STRING:
              sb.append(cell.getStringCellValue());
              break;
            case NUMERIC:
              sb.append(cell.getNumericCellValue());
              break;
            case BOOLEAN:
              sb.append(cell.getBooleanCellValue());
              break;
            default:
              sb.append(" ");
              break;
          }
        }

        final String id = sb.toString().split("£")[1];
		/*
		 * if (fr.gouv.esante.pml.smt.cim10.SKOSToOWL.listTraduction.containsKey(id)) {
		 * sb.append("@").append(fr.gouv.esante.pml.smt.cim10.SKOSToOWL.listTraduction.
		 * get(id)); fr.gouv.esante.pml.smt.cim10.SKOSToOWL.listTraduction.put(id,
		 * sb.toString()); } else {
		 * fr.gouv.esante.pml.smt.cim10.SKOSToOWL.listTraduction.put(id, sb.toString());
		 * }
		 */


      }

    }

    workBook.close();
  }
  
  
  public static void chargeXLXSEMDNCodeFileToList(final String xlsxFile) throws Exception {
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

	      // Iterate through all the columns in the row and build "£"
	      // separated string
	      
	      if (row.getRowNum() > 0) {
	    	  
	        final Iterator<Cell> cellIterator = row.cellIterator();
	        final StringBuffer sb = new StringBuffer();
	        while (cellIterator.hasNext()) {
	          final Cell cell = cellIterator.next();
	          if (sb.length() != 0) {
	            sb.append("£");
	          }
	           
	          // If you are using poi 4.0 or over, change it to
	          // cell.getCellType
	          switch (cell.getCellType()) {
	            case STRING:
	              sb.append(cell.getStringCellValue());
	              break;
	            case NUMERIC:
	              sb.append(cell.getNumericCellValue());
	              break;
	            case BOOLEAN:
	              sb.append(cell.getBooleanCellValue());
	              break;
	            default:
	              sb.append(" ");
	              break;
	          }
	        }
	        
	        final String id = sb.toString().split("£")[0];
	        listCodeEMDN.put(id, sb.toString());


	      }

	    }

	    workBook.close();
	  }
  
  
  public static void chargeXLXSATCCodeFileToList(final String xlsxFile) throws Exception {
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

	      // Iterate through all the columns in the row and build "£"
	      // separated string
	      if (row.getRowNum() > 0) {
	        final Iterator<Cell> cellIterator = row.cellIterator();
	        final StringBuffer sb = new StringBuffer();
	        while (cellIterator.hasNext()) {
	          final Cell cell = cellIterator.next();
	          if (sb.length() != 0) {
	            sb.append("£");
	          }
	          // If you are using poi 4.0 or over, change it to
	          // cell.getCellType
	          switch (cell.getCellType()) {
	            case STRING:
	              sb.append(cell.getStringCellValue());
	              break;
	            case NUMERIC:
	              sb.append(cell.getNumericCellValue());
	              break;
	            case BOOLEAN:
	              sb.append(cell.getBooleanCellValue());
	              break;
	            default:
	              sb.append(" ");
	              break;
	          }
	        }
             System.out.println("***sb  "+sb);
	        final String id = sb.toString().split("£")[0];
	        listCodeATC.put(id, sb.toString().split("£")[1]+"£"+sb.toString().split("£")[2]);


	      }

	    }

	    workBook.close();
	  }
  
  public static  void  chargeEMAExcelConceptToList(final String xlsFile) throws IOException {
		
		
		
		FileInputStream file = new FileInputStream(new File(xlsFile));
		
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		
		XSSFSheet sheet = workbook.getSheet("1_Current + New Substance CV");
		
		Iterator<Row> rowIterator = sheet.iterator();
		
		rowIterator.next(); 
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();
		
		while (rowIterator.hasNext()) {
			 
			 
			 Row row = rowIterator.next();
	    	 Cell c1 = row.getCell(0); //get Ref Code
		     Cell c2 = row.getCell(1); // get Libelle
		     Cell c3 = row.getCell(2); // get langage
		     Cell c4 = row.getCell(3); //get type
		     
		    // System.out.println("*** "+c1.getStringCellValue());
		    // System.out.println("*** "+c2.getStringCellValue());
		    // System.out.println("*** "+c3.getStringCellValue());
		    // System.out.println("*** "+c4.getStringCellValue());
		     
		     if(listConceptsEma.containsKey(c1.getStringCellValue())) {
		    	 
		    	List<String> listeValue = listConceptsEma.get(c1.getStringCellValue());
		    	
		    	if("Substance Preferred Name".equals(c4.getStringCellValue())) {
			    	 
		    		listeValue.set(0, c2.getStringCellValue());
			     }
			     
	             if("Substance Alias".equals(c4.getStringCellValue())) {
			    	 
	            	 listeValue.set(1, c2.getStringCellValue()+"|"+listeValue.get(1));
			     }
	             
	            if("Substance Translation".equals(c4.getStringCellValue()) && "FR".equals(c3.getStringCellValue())) {
			    	 
	            	
	            	listeValue.set(2,  c2.getStringCellValue()+"|"+listeValue.get(2));
			     }
	            
	            if("Substance Translation".equals(c4.getStringCellValue()) && "EN".equals(c3.getStringCellValue())) {
			    	 
	            	listeValue.set(3, c2.getStringCellValue()+"|"+listeValue.get(3));
			     }
	            
	            listConceptsEma.put(c1.getStringCellValue(), listeValue);
		    	 
		     }
		     
		     else {// Nouveau Code
		     
		    List<String> listedonnees= new ArrayList<>();
		    listedonnees.add("");
		    listedonnees.add("");
		    listedonnees.add("");
		    listedonnees.add("");
		   
		     
		     if("Substance Preferred Name".equals(c4.getStringCellValue())) {
		    	 
		    	 listedonnees.set(0, c2.getStringCellValue());
		     }
		     
            if("Substance Alias".equals(c4.getStringCellValue())) {
		    	 
		    	 listedonnees.set(1, c2.getStringCellValue());
		     }
            
           if("Substance Translation".equals(c4.getStringCellValue()) && "FR".equals(c3.getStringCellValue())) {
		    	 
		    	 listedonnees.set(2, c2.getStringCellValue());
		     }
           
           if("Substance Translation".equals(c4.getStringCellValue()) && "EN".equals(c3.getStringCellValue())) {
		    	 
		    	 listedonnees.set(3, c2.getStringCellValue());
		     }
		     
		     
		    
		     
		     listConceptsEma.put(c1.getStringCellValue(), listedonnees);
		     
		     }
		     
		     
		   
		}
		
		 

	}
  
  
  public static  void convertNCBIFile(final String xlsFile) throws Exception {
		

		String csvFile = PropertiesUtil.getNCBIProperties("partLoincCsvFile");
		

		BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"UTF-8"));
	    String row = csvReader.readLine();
	    while ((row = csvReader.readLine()) != null) {
	    	
	    	row = row.replaceAll(", ","").replaceAll(",[1-9]", "").replaceAll(",[a-z]", "").
	    			replaceAll("[1-9],", "").replaceAll("[A-Z],", "").replaceAll("[10-19],", "");
	    			
	    	String[] data = row.split(",");
	    	
	    	String key0 = data[0].replaceAll("\"", "");// code Loinc
	    	String key1 = data[2].replaceAll("\"", ""); // code Part

	    	Set<String> listeCodeLoinc = new HashSet<String>();
	    			
	    	listeCodeLoinc = listNCBIConcepts.get(key1);
	
	    try {
	    	if(listeCodeLoinc.size()==0) {
	    		
	    		
	    	}else {
	    		
	    		
	    		Set<String> newlist = new HashSet<String>();
	    		newlist.addAll(listNCBIConcepts.get(key1));
	    		newlist.add(key0);
	    		
	    		listNCBIConcepts.put(key1, newlist);
	    	}
	    	
	    }catch(NullPointerException e) {
	    	listNCBIConcepts.put(key1, new HashSet<>(Arrays.asList(key0)));
	    }
	    	
      
	    }
	    csvReader.close();

	    
	}
  
  
  
  public static  void  chargeExcelEdqm(final String xlsxRihnFileName) throws IOException {
					
			FileInputStream file = new FileInputStream(new File(xlsxRihnFileName));
			
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			
			XSSFSheet sheet_Fichier_Definition = workbook.getSheet("fichier définitions");
			
			XSSFSheet sheet_Fichier_A_Valider = workbook.getSheet("fichier à valider");
			
			Iterator<Row> rowIterator_Definition = sheet_Fichier_Definition.iterator();
			
			rowIterator_Definition.next();
			
			Iterator<Row> rowIterator_Label = sheet_Fichier_A_Valider.iterator();
			rowIterator_Label.next();
			
			
			while (rowIterator_Definition.hasNext()) {
				 
				List<String> listedonnees= new ArrayList<>();
				 Row row = rowIterator_Definition.next();
		    	 Cell code = row.getCell(2); //get Ref Code
			     Cell definition_traduction = row.getCell(8); // get Libelle
			    
				   
			   if(code != null) { 
  
				   listedonnees.add(definition_traduction.getStringCellValue()); //0
				   listedonnees.add("");//1
			        
			     if("EDQM".equals(code.getStringCellValue()))
			      listConceptsEdqm.put(URI_Edqm, listedonnees);
			     else
			       listConceptsEdqm.put(URI_Edqm+"/"+code.getStringCellValue(), listedonnees);	 
			     
			    }
			       
			}
			
			
			
			
			while (rowIterator_Label.hasNext()) {
				 
				 Row row = rowIterator_Label.next();
		    	 Cell code = row.getCell(3); //get Ref Code
			     Cell label_traduction = row.getCell(6); // get Libelle
				   
			   if(code != null) { 
			     
			     
			        if(label_traduction != null) {
			        	
			           if(listConceptsEdqm.containsKey(URI_Edqm+"/"+code.getStringCellValue()))	
			        	 if("EDQM".equals(code.getStringCellValue()))  
			              listConceptsEdqm.get(URI_Edqm).add(1, label_traduction.getStringCellValue());
			        	 else
			        		 listConceptsEdqm.get(URI_Edqm+"/"+code.getStringCellValue()).add(1, label_traduction.getStringCellValue());
			           else {
			        	   
			        	   List<String> listedonnees= new ArrayList<>();
			        	   listedonnees.add(""); //0
						   listedonnees.add(label_traduction.getStringCellValue());//1
					        
						   if("EDQM".equals(code.getStringCellValue())) 
					         listConceptsEdqm.put(URI_Edqm, listedonnees);
						   else
							 listConceptsEdqm.put(URI_Edqm+"/"+code.getStringCellValue(), listedonnees);
			           }
			        	   
			        }
			        else {
			        	
			        	if(listConceptsEdqm.containsKey(URI_Edqm+"/"+code.getStringCellValue()))	
			        	{
			        	   if("EDQM".equals(code.getStringCellValue())) 	
			        		 listConceptsEdqm.get(URI_Edqm).add(1, "");
			        	   else
			        		 listConceptsEdqm.get(URI_Edqm+"/"+code.getStringCellValue()).add(1, ""); 
			        	}
			        	else {
			        		   List<String> listedonnees= new ArrayList<>();
				        	   listedonnees.add(""); //0
							   listedonnees.add("");//1
						    
						       listConceptsEdqm.put(URI_Edqm+"/"+code.getStringCellValue(), listedonnees);
			        		
			        	}
			        		
			          }
			     
			     
			    }
			     

			}

		}



}
