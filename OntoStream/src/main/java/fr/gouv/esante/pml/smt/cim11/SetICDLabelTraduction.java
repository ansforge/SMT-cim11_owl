package fr.gouv.esante.pml.smt.cim11;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.HasProperty;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.search.EntitySearcher;
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class SetICDLabelTraduction {
  
  
//  private static String owlFileNameFR = PropertiesUtil.getProperties("owlFileNameFR");
//  private static String owlFileName = PropertiesUtil.getProperties("owlFileName");
//  private static String traductionFileName = PropertiesUtil.getProperties("traductionFileName");
//  private static String traductionAtihFileName = PropertiesUtil.getProperties("traductionAtihFileName");
//  private static String traductionmmsFileName = PropertiesUtil.getProperties("traductionmmsFileName");
  private static OWLDataFactory fact = null;
//  private static ArrayList<String> SKOSlabelURI =
//      new ArrayList<String>(Arrays.asList("http://www.w3.org/2004/02/skos/core#prefLabel"));
//  private static ArrayList<String> ICDlabelURI =
//      new ArrayList<String>(Arrays.asList("https://id.who.int/icd/schemaDefinition",
//          "https://id.who.int/icd/schemaTitle", "https://id.who.int/icd/schemalabel"));
//  public static HashMap<String, String> listTraduction2 = new HashMap<String, String>();
//  private static StringBuilder csvLabel = new StringBuilder();
//  private static Integer idLabel = 0;
  static InputStream input = null;
//  private static OutputStream fout = null;
  
  public static void main(final String[] args) throws Exception {
    
//    fout = new FileOutputStream(labelFileName);
    input = new FileInputStream("D:\\cim11\\R202105\\CGTS_SEM_ICD11-MMS-R202105-EN_FR-V5.owl");
    ChargeMapping.chargeXLXSTraductionFileToList3("D:\\cim11\\R202105\\fr-2021-05-04-current_french-translation_vf.xlsx");
//    ChargeMapping.chargeXLXSTraductionFileToList4(traductionAtihFileName);
//    ChargeMapping.chargeXLXSTraductionFileToList5(traductionmmsFileName);
//    FileWriter csvWriter = new FileWriter("C:\\Users\\agochath\\Documents\\cim11\\AllLabelCIM11.csv");
//    Writer csvWriter = new OutputStreamWriter(new FileOutputStream("C:\\Users\\agochath\\Documents\\cim11\\labelNonTraduits6.csv"), StandardCharsets.UTF_8);
     
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input);
//    
//    onto.axioms().forEach(ax -> {System.out.println(ax);});
//    fact = onto.getOWLOntologyManager().getOWLDataFactory();
    
//  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
//  filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") || 
//               ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:comment") ).
//  forEach(ax -> {
//    if("fr".equals(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[1])) {    
//      manager.applyChange(new RemoveAxiom(onto, ax));
//    }
//                });
//    List<String> AllLabelTraduits = new ArrayList();
//    List<String> labelNonTraduits = new ArrayList();
    HashMap<String, String[]> frLabel = new HashMap<String, String[]>();
    HashMap<String, String> notations = new HashMap<String, String>();
    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> 
    			((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/longDefinition>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#inclusion>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#indexTerm>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#notation>") ).
    forEach(ax -> {
    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#notation>")) {
    		notations.put(((OWLAnnotationAssertionAxiom) ax).getSubject().toString(),((OWLAnnotationAssertionAxiom) ax).getValue().toString());
    	}else if(((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@fr") && (!((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("postcoordinationScale") && !((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("PostCoordinationScaleInfo"))) {
    		String value = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
    		String[] data = {value, ((OWLAnnotationAssertionAxiom) ax).getSubject().toString(), propertyType(((OWLAnnotationAssertionAxiom) ax).getProperty().toString())};
    		frLabel.put(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]",""), data);
    	}
//    	if(ChargeMapping.listTraductionFr2.containsKey(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {    
//          final OWLAnnotation labelAnno = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(),fact.getOWLLiteral(ChargeMapping.listTraductionFr2.get(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))[4], "fr"));
//          final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), labelAnno);
//          manager.applyChange(new AddAxiom(onto, ax1));
//        }
//        AllLabelTraduits.put(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1), ((OWLAnnotationAssertionAxiom) ax).getSubject().toString());    	
        
//        else {
//        	AllLabelTraduits.add(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1));
////        	final OWLAnnotation labelAnno = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(),fact.getOWLLiteral("xxxxxxxxxxxxxxxxxxxxxxxxxx", "fr"));
////            final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), labelAnno);
////            manager.applyChange(new AddAxiom(onto, ax1));
//        }
     });
    
//	  FileWriter csvWriter = new FileWriter("C:\\Users\\gouch\\Desktop\\teletravail\\icd11\\labelNonTraduits.csv");
//	  
//	  for (String rowData : labelNonTraduits) {
//	      csvWriter.append(rowData);
//	      csvWriter.append(";\n");
//	  }
//	
//	  csvWriter.flush();
//	  csvWriter.close();
	  
	  
	  FileOutputStream out = new FileOutputStream(new File("D:\\cim11\\R202105\\fr-2021-05-04-current_french-translation_vf_v1.xlsx"));
      XSSFWorkbook workbook = new XSSFWorkbook();
      XSSFSheet sheet = workbook.createSheet("ICD11 label");
      int rownum=0;
      int cellnum = 0;
      cellnum = 0;
      for (String rowData : ChargeMapping.listTraductionFr2.keySet())  {
    	  String[] data1 = ChargeMapping.listTraductionFr2.get(rowData);
    	  
    	  rownum++;
//    	  String id = String.format("icd11-lab-"+"%08d", rownum);
    	  Row row = sheet.createRow(rownum);
          Cell cell1 = row.createCell(0);
          Cell cell2 = row.createCell(1);
          Cell cell3 = row.createCell(2);
          Cell cell4 = row.createCell(3);
          Cell cell5 = row.createCell(4);
          Cell cell6 = row.createCell(5);
          Cell cell7 = row.createCell(6);
          Cell cell8 = row.createCell(7);
          Cell cell9 = row.createCell(8);
          Cell cell10 = row.createCell(9);
          Cell cell11 = row.createCell(10);
          Cell cell12 = row.createCell(11);
          Cell cell13 = row.createCell(12);
          Cell cell14 = row.createCell(13);
          Cell cell15 = row.createCell(14);
          Cell cell16 = row.createCell(15);
          Cell cell17 = row.createCell(16);
          Cell cell18 = row.createCell(17);
          
          cell1.setCellValue((String) data1[0]);
          cell2.setCellValue((String) data1[1]);
          if(!data1[2].equals("null")) {
        	  cell3.setCellValue(Double.valueOf(data1[2]));
          }
          cell4.setCellValue((String) data1[3]);
          cell5.setCellValue((String) data1[4]);
          cell6.setCellValue((String) data1[5]);
          cell7.setCellValue((String) data1[6]);
          if(!data1[7].equals("null")) {
        	  cell8.setCellValue(Double.valueOf(data1[7]));
          }
          cell9.setCellValue((String) data1[8]);
          if(!(data1[6]==null) && frLabel.containsKey(data1[6].toLowerCase().replaceAll("[^a-zA-Z0-9_-]",""))) {
        	  String[] data2 = frLabel.get(data1[6].toLowerCase().replaceAll("[^a-zA-Z0-9_-]",""));  
        	  if(notations.containsKey(data2[1])) {
        		  String notation = notations.get(data2[1]);
        		  notation = notation.substring(1, notation.length()-13);
        		  cell10.setCellValue((String) notation);
        	  }
        	  cell11.setCellValue((String) data2[2]);
    	  }  
          cell12.setCellValue((String) data1[9]);
          cell13.setCellValue((String) data1[10]);
          cell14.setCellValue((String) data1[11]);
          cell15.setCellValue((String) data1[12]);
          cell16.setCellValue((String) data1[13]);
          cell17.setCellValue((String) data1[14]);
          cell18.setCellValue((String) data1[15]);
//          Cell cell8 = row.createCell(7);
//          Cell cell9 = row.createCell(8);
//          Cell cell10 = row.createCell(9);
//          Cell cell11 = row.createCell(10);
//          Cell cell12 = row.createCell(11);
//          Cell cell13 = row.createCell(12);
//          Cell cell14 = row.createCell(13);
//          Cell cell15 = row.createCell(14);
//          Cell cell16 = row.createCell(15);
//          Cell cell17 = row.createCell(16);
//          Cell cell18 = row.createCell(17);
//          Cell cell19 = row.createCell(18);
//          Cell cell20 = row.createCell(19);
          
//    	  String[] data = ChargeMapping.listTraductionFr3.get(rowData);
//	          cell1.setCellValue(data[0]);
//	          cell2.setCellValue(data[1]);
//	          if(!data[2].equals("null")) {
//	        	  cell3.setCellValue(Double.valueOf(data[2]));
//	          }
//	          cell4.setCellValue(data[3]);
//	          cell6.setCellValue(data[4]);
//	          cell7.setCellValue(data[5]);
//	          if(ChargeMapping.listTraductionFr2.containsKey(rowData)) {
               
      }
      

      workbook.write(out);
      out.close();
      workbook.close();
	  
	  
	  
	  

//    final OutputStream fileoutputstream = new FileOutputStream(owlFileNameFR);
//    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
//    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
//    
//    
//    IRI icd11IRI = IRI.create("http://esante.gouv.fr/terminologie-icd11-mms");
//    manager.applyChange(new SetOntologyID(onto,  new OWLOntologyID(icd11IRI)));
//    
//    manager.saveOntology(onto, ontologyFormat, fileoutputstream);

//    fact = o.getOWLOntologyManager().getOWLDataFactory();
//    o.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
//    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label")).
//    filter(ax -> !((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("http://id.who.int/icd/entity")).
//    forEach(ax -> 
//    {
////      System.out.println(((OWLAnnotationAssertionAxiom) ax).getSubject()+"---"+((OWLAnnotationAssertionAxiom) ax).getProperty()+"---"+((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1));
//      if(listTraduction2.containsKey(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {
//        final OWLAnnotation labelAnno = fact.getOWLAnnotation(fact.getRDFSLabel(),fact.getOWLLiteral(listTraduction2.get(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", "")), "fr"));
//        final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), labelAnno);
//        manager.applyChange(new AddAxiom(o, ax1));
//      } else {
//        labelNonTraduits.add(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1));
//      }
//    });
//    
//    FileWriter csvWriter = new FileWriter("C:\\Users\\agochath\\Documents\\cim11\\labelNonTraduits2.csv");
//    
//    for (String rowData : labelNonTraduits) {
//        csvWriter.append(rowData);
//        csvWriter.append("\n");
//    }
//
//    csvWriter.flush();
//    csvWriter.close();
  }
  
  public static String propertyType(String args)
  {
      switch(args)
      {
          case "rdfs:label":
              return "label";
          case "<http://id.who.int/icd/schema/longDefinition>":
        	  return "longDefinition";
          case "<http://data.esante.gouv.fr/ans-icd11#inclusion>":
        	  return "inclusion";
          case "<http://data.esante.gouv.fr/ans-icd11#indexTerm>":
        	  return "indexTerm";
          case "<http://www.w3.org/2004/02/skos/core#definition>":
        	  return "definition";
          default:
        	  return "label";
      }
  }

}
