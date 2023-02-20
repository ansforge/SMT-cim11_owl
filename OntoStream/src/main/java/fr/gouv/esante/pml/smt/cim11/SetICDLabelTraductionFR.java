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

public class SetICDLabelTraductionFR {
  
  
  private static String owlFileNameFR = PropertiesUtil.getProperties("owlFileNameFR");
  private static String owlFileName = PropertiesUtil.getProperties("owlFileName");
  private static String traductionFileName = PropertiesUtil.getProperties("traductionFileName");
  private static String traductionAtihFileName = PropertiesUtil.getProperties("traductionAtihFileName");
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
    input = new FileInputStream("D:\\cim11\\newModelisation\\CGTS_SEM_ICD11-MMS-OWL-R202009-EN.xml");
    ChargeMapping.chargeXLXSTraductionFileToList2(traductionFileName);
//    ChargeMapping.chargeXLXSTraductionFileToList4(traductionAtihFileName);
//    FileWriter csvWriter = new FileWriter("C:\\Users\\agochath\\Documents\\cim11\\AllLabelCIM11.csv");
//    Writer csvWriter = new OutputStreamWriter(new FileOutputStream("C:\\Users\\agochath\\Documents\\cim11\\labelNonTraduits6.csv"), StandardCharsets.UTF_8);
     
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input);
    
//    onto.axioms().forEach(ax -> {System.out.println(ax);});
    fact = onto.getOWLOntologyManager().getOWLDataFactory();
    
//  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
//  filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") || 
//               ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:comment") ).
//  forEach(ax -> {
//    if("fr".equals(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[1])) {    
//      manager.applyChange(new RemoveAxiom(onto, ax));
//    }
//                });
//    List<String> AllLabelTraduits = new ArrayList();
    List<String> labelNonTraduits = new ArrayList();
    HashMap<String, String> AllLabelTraduits = new HashMap<String, String>();
    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> 
    			((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:comment") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/longDefinition>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/codingNote>") ||
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#literalForm>")).
//    filter(ax -> !((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("http://id.who.int/icd/entity")).
    forEach(ax -> {
//    	labelNonTraduits.add(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1));
//    	System.out.println(((OWLAnnotationAssertionAxiom) ax).getProperty().toString());
        if(ChargeMapping.listTraduction.containsKey(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {    
          final OWLAnnotation labelAnno = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(),fact.getOWLLiteral(ChargeMapping.listTraduction.get(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", "")), "fr"));
          final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), labelAnno);
          manager.applyChange(new AddAxiom(onto, ax1));
          manager.applyChange(new RemoveAxiom(onto, ax));
        }
//        AllLabelTraduits.put(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1), ((OWLAnnotationAssertionAxiom) ax).getSubject().toString());    	
//        }
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
	  
	  
//	  FileOutputStream out = new FileOutputStream(new File("D:\\traduction_cim11\\AllICD11MMSLabels1.xlsx"));
//      XSSFWorkbook workbook = new XSSFWorkbook();
//      XSSFSheet sheet = workbook.createSheet("ICD11 label");
//      int rownum=0;
//      int cellnum = 0;
//      cellnum = 0;
//      for (String rowData : labelNonTraduits)  {
//    	  rownum++;
////    	  String id = String.format("icd11-lab-"+"%08d", rownum);
//    	  Row row = sheet.createRow(rownum);
//          Cell cell1 = row.createCell(0);
//          cell1.setCellValue(rowData);
////          Cell cell2 = row.createCell(1);
////          Cell cell3 = row.createCell(2);
////          Cell cell4 = row.createCell(3);
////          Cell cell5 = row.createCell(4);
////          Cell cell6 = row.createCell(5);
////          Cell cell7 = row.createCell(6);
////          Cell cell8 = row.createCell(7);
////          Cell cell9 = row.createCell(8);
////          Cell cell10 = row.createCell(9);
////          Cell cell11 = row.createCell(10);
////    	  if(ChargeMapping.listTraductionFr2.containsKey(rowData.toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {
////	    	  String[] data = ChargeMapping.listTraductionFr2.get(rowData.toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""));
////	          cell1.setCellValue(id);
////	          cell2.setCellValue(AllLabelTraduits.get(rowData));
////	          cell3.setCellValue(rowData);
////	          cell4.setCellValue(data[2]);
////	          cell5.setCellValue(Double.valueOf(data[1]));
////	          cell6.setCellValue(data[3]);
////	          cell7.setCellValue(data[4]);
////	          cell8.setCellValue(data[5]);
////	          if(ChargeMapping.listTraductionFr3.containsKey(rowData.toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {
////		    	  String[] data2 = ChargeMapping.listTraductionFr3.get(rowData.toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""));
////		    	  cell9.setCellValue(data2[1]);
////		          cell10.setCellValue(data2[2]);
////		          cell11.setCellValue(data2[3]);
////	          }
////    	  }else {
////    		  cell1.setCellValue(id);
////              cell2.setCellValue(AllLabelTraduits.get(rowData));
////              cell3.setCellValue(rowData);
////              cell4.setCellValue("");
////              cell5.setCellValue("");
////              cell6.setCellValue("");
////              cell7.setCellValue("");
////    	  }
//      }
//
//      workbook.write(out);
//      out.close();
//      workbook.close();
//	  
	  
	  
	  

    final OutputStream fileoutputstream = new FileOutputStream("D:\\cim11\\newModelisation\\CGTS_SEM_ICD11-MMS-OWL-R202009-FR.xml");
    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
    
    
//    IRI icd11IRI = IRI.create("http://esante.gouv.fr/terminologie-icd11-mms");
//    manager.applyChange(new SetOntologyID(onto,  new OWLOntologyID(icd11IRI)));
    
    manager.saveOntology(onto, ontologyFormat, fileoutputstream);

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

}
