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
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.search.EntitySearcher;
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class CIM11OWLtoExcel {

	private static OWLDataFactory fact = null;
	static InputStream input = null;
	static HashMap<String, String> frLabels = new HashMap<String, String>();
	static HashMap<String, String> enLabels = new HashMap<String, String>();
	static HashMap<String, String> subClassOfs = new HashMap<String, String>();
	static HashMap<String, String> notations = new HashMap<String, String>();
	static HashMap<String, String> types = new HashMap<String, String>();
	static HashMap<String, String> browserUrls = new HashMap<String, String>();
	static HashMap<String, String> frindexTerms = new HashMap<String, String>();
	static HashMap<String, String> icd10s = new HashMap<String, String>();
	static HashMap<String, String> frdefinitions = new HashMap<String, String>();
    static HashMap<String, String> blockIds = new HashMap<String, String>();
	static HashMap<String, String> enindexTerms = new HashMap<String, String>();
	static HashMap<String, String> endefinitions = new HashMap<String, String>();
	static HashMap<String, String> codingRanges = new HashMap<String, String>();
	static HashMap<String, String> frexclusionNotes = new HashMap<String, String>();
	static HashMap<String, String> frinclusionNotes = new HashMap<String, String>();
	static HashMap<String, String> enExclusionNotes = new HashMap<String, String>();
	static HashMap<String, String> enInclusionNotes = new HashMap<String, String>();
	static HashMap<String, String> exclusions = new HashMap<String, String>();

	public static void main(final String[] args) throws Exception {
    
    input = new FileInputStream("D:\\cim11\\R202202\\CGTS_SEM_ICD11-MMS-R202202-EN-FR-V4.owl");
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input);
    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    forEach(ax -> {
    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#notation>")) {
    		String subject = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
    		String value = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
    		notations.put(subject, value);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/classKind>")) {
    		String subject = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
    		String value = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
    		types.put(subject, value);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/blockId>")) {
    		String subject = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
    		String value = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
    		blockIds.put(subject, value);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/codingRange>")) {
    		String subject = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
    		String value = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
    		codingRanges.put(subject, value);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@fr") && (!((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("postcoordinationScale") && !((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("PostCoordinationScaleInfo"))) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	frLabels.put(key, label);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@en") && (!((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("postcoordinationScale") && !((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("PostCoordinationScaleInfo"))) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	enLabels.put(key, label);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@en") ) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	endefinitions.put(key, label);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@fr") ) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	frdefinitions.put(key, label);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://rdf-vocabulary.ddialliance.org/xkos#exclusionNote>") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@en") ) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	enExclusionNotes.put(key, label);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://rdf-vocabulary.ddialliance.org/xkos#exclusionNote>") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@fr") ) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	frexclusionNotes.put(key, label);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://rdf-vocabulary.ddialliance.org/xkos#inclusionNote>") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@en") ) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	enInclusionNotes.put(key, label);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://rdf-vocabulary.ddialliance.org/xkos#inclusionNote>") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@fr") ) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	frinclusionNotes.put(key, label);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#exclusion>") ) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	if(exclusions.containsKey(key)) {
	    		String index = exclusions.get(key) + "\n" +label;
	    		exclusions.put(key, index);
	    	}else {
	    		exclusions.put(key, label);
	    	}
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#icd10>") ) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	icd10s.put(key, label);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/browserUrl>") ) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	browserUrls.put(key, label);
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#indexTerm>") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@en") ) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	if(enindexTerms.containsKey(key)) {
	    		String index = enindexTerms.get(key) + "\n" +label;
	    		enindexTerms.put(key, index);
	    	}else {
	    		enindexTerms.put(key, label);
	    	}
	    	
    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#indexTerm>") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@fr") ) {
    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
	    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	if(frindexTerms.containsKey(key)) {
	    		String index = frindexTerms.get(key) + "\n" +label;
	    		frindexTerms.put(key, index);
	    	}else {
	    		frindexTerms.put(key, label);
	    	}
    	}
     });
    
    onto.axioms().filter(ax -> ax.isOfType(AxiomType.SUBCLASS_OF)).
    forEach(ax -> {
    	String iriSub = ((OWLSubClassOfAxiom) ax).getSubClass().asOWLClass().getIRI().getIRIString();
    	String iriSup = ((OWLSubClassOfAxiom) ax).getSuperClass().asOWLClass().getIRI().getIRIString();
    	
    	subClassOfs.put(iriSub, iriSup);
    	
      });
    	  
	  
	  FileOutputStream out = new FileOutputStream(new File("D:\\cim11\\R202202\\CGTS_SEM_ICD11-MMS-R202202-EN-FR.xlsx"));
      XSSFWorkbook workbook = new XSSFWorkbook();
      XSSFSheet sheet = workbook.createSheet("ICD11 MMS");
      int rownum=0;
      int cellnum = 0;
      cellnum = 0;
      for (String uri : enLabels.keySet())  {
    	  rownum++;
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
//          Cell cell19 = row.createCell(18);
          
          cell1.setCellValue(uri);
          cell2.setCellValue(enLabels.get(uri));
          if(frLabels.containsKey(uri)) {
        	  cell3.setCellValue(frLabels.get(uri));
          }
          if(subClassOfs.containsKey(uri)) {
        	  cell4.setCellValue(subClassOfs.get(uri));
          }
    	  if(types.containsKey(uri)) {
        	  cell5.setCellValue(types.get(uri));
    	  }
    	  if(notations.containsKey(uri)) {
        	  cell6.setCellValue(notations.get(uri));
    	  }
    	  if(blockIds.containsKey(uri)) {
        	  cell7.setCellValue(blockIds.get(uri));
    	  }
    	  if(codingRanges.containsKey(uri)) {
        	  cell8.setCellValue(codingRanges.get(uri));
          }
    	  if(browserUrls.containsKey(uri)) {
        	  cell9.setCellValue(browserUrls.get(uri));
    	  }
    	  if(endefinitions.containsKey(uri)) {
        	  cell10.setCellValue(endefinitions.get(uri));
    	  }
    	  if(frdefinitions.containsKey(uri)) {
        	  cell11.setCellValue(frdefinitions.get(uri));
    	  }  
    	  if(icd10s.containsKey(uri)) {
        	  cell12.setCellValue(icd10s.get(uri));
    	  }
    	  if(exclusions.containsKey(uri)) {
        	  cell13.setCellValue(exclusions.get(uri));
    	  }
    	  if(enindexTerms.containsKey(uri)) {
        	  cell14.setCellValue(enindexTerms.get(uri));
    	  }
//    	  if(frindexTerms.containsKey(uri)) {
//        	  cell15.setCellValue(frindexTerms.get(uri));
//    	  }
    	  if(enInclusionNotes.containsKey(uri)) {
        	  cell15.setCellValue(enInclusionNotes.get(uri));
    	  }
    	  if(frinclusionNotes.containsKey(uri)) {
        	  cell16.setCellValue(frinclusionNotes.get(uri));
    	  }
    	  if(enExclusionNotes.containsKey(uri)) {
        	  cell17.setCellValue(enExclusionNotes.get(uri));
    	  }
    	  if(frexclusionNotes.containsKey(uri)) {
        	  cell18.setCellValue(frexclusionNotes.get(uri));
    	  }
      }
      

      workbook.write(out);
      out.close();
      workbook.close();
	  
  }
  

}
