package fr.gouv.esante.pml.smt.adicap;

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
import java.util.Map;
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
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.HasProperty;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import com.google.common.collect.Lists;

import fr.gouv.esante.pml.smt.utils.ADICAPVocabulary;
import fr.gouv.esante.pml.smt.utils.ANSICD11Vocabulary;
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.ICDVocabulary;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;
import fr.gouv.esante.pml.smt.utils.XSkosVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

public class ModelingOntologyADICAP {
  
  
  
  private static OWLDataFactory fact = null;

  static InputStream input = null;  
  private static Map<String, String> adicapCode = new HashMap<String, String>();
  private static Map<String, String> adicapCode2 = new HashMap<String, String>();
  private static Map<String, String> adicapAnatomy = new HashMap<String, String>();
  private static Map<String, String> adicapAnatomyCode = new HashMap<String, String>();
  private static OWLOntologyManager manager = null;
  private static OWLOntology onto = null;
  private static String adicapURI = "https://data.esante.gouv.fr/adicap";
//  private static Writer csvWriter = null;
  
  public static void main(final String[] args) throws Exception {
	    
	    input = new FileInputStream("D:\\cisp\\CGTS_SEM_ADICAP.owl");
//	    csvWriter = new OutputStreamWriter(new FileOutputStream("D:\\cisp\\code-cisp.csv"), StandardCharsets.UTF_8);
//	    input = new FileInputStream("D:\\cim11\\newModelisation\\CGTS_SEM_ICD11-V202009-EN-NewModel.owl");
	    ChargeMapping.chargelistCodeADICAPenDouble("D:\\cisp\\code-cisp.csv");
	    ChargeMapping.chargeDictionnaryCodeToMaps("D:\\cisp\\CGTS_SEM_ADICAP.xlsx");
	    manager = OWLManager.createOWLOntologyManager();
	    onto = manager.loadOntologyFromOntologyDocument(input);
	    
	    fact = onto.getOWLOntologyManager().getOWLDataFactory();

	    
	    
	    
//	    inclusionModeling();
//	    
//	    exclusionModeling();
//	    
//	    
//	    indexTermModeling();
	    
	    uriModeling();
//	    csvWriter.flush();
//	    csvWriter.close();
	    
	    final OutputStream fileoutputstream = new FileOutputStream("D:\\cisp\\CGTS_SEM_ADICAP_V2.owl");
	    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
	    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
		IRI cispIRI = IRI.create(adicapURI);
		manager.applyChange(new SetOntologyID(onto, new OWLOntologyID(cispIRI)));
	      
	    manager.saveOntology(onto, ontologyFormat, fileoutputstream);


	  }
  
  public static void uriModeling() {
	  
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	  filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<https://data.esante.gouv.fr/terminologies/adicap#anatomy>") ).
	    forEach(ax -> {
	    	String code = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
	    	adicapAnatomy.put(code, "");
	    	adicapAnatomyCode.put(((OWLAnnotationAssertionAxiom) ax).getSubject().toString(), code);
	    	
	      });
	  
	  
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	  filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<https://data.esante.gouv.fr/terminologies/adicap#code>") ).
	    forEach(ax -> {
	    	String code = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
	    	if(adicapAnatomy.containsKey(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())) {
	    		adicapAnatomy.put(((OWLAnnotationAssertionAxiom) ax).getSubject().toString(), code);
	    	}
	      });
//	  System.out.println( adicapAnatomyCode.size() );
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	  filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<https://data.esante.gouv.fr/terminologies/adicap#code>") ).
	    forEach(ax -> {
	    	String code = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
	    	String id = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString().split("#")[1];
	    	if(ChargeMapping.listCodeADICAP.contains(code)) {
	    		String dictionnaryCode = ChargeMapping.listDictionnaryCode.get(id);
	    		code = dictionnaryCode + code;
	    	}else 
	    		if(adicapAnatomyCode.containsKey(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())) {
	    		String codeAnatomy = adicapAnatomy.get(adicapAnatomyCode.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()));
	    		code = codeAnatomy + code;
	    	}
//	    	System.out.println(code);
	    	
	    	adicapCode.put(((OWLAnnotationAssertionAxiom) ax).getSubject().toString(), code);
	    	
	    	
	    	if(!adicapCode2.containsKey(code)) {
	    		adicapCode2.put(code,((OWLAnnotationAssertionAxiom) ax).getSubject().toString());
	    	}
	    	else {
	    		System.out.println(code);
	    	}
//	    		try {
//					csvWriter.append(code).append(";");
//					csvWriter.append("\n");
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//               
//	    	}
	    	
	      });
//	  System.out.println(adicapCode.size() + " - " + adicapCode2.size());
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.DECLARATION)).
	    forEach(ax -> {
	    	((OWLDeclarationAxiom) ax).classesInSignature().findFirst().ifPresent( clacc -> {
	    	String iri = clacc.getIRI().getIRIString();
	    	if(iri.contains("https://data.esante.gouv.fr/terminologies/adicap#")) {
	    		String newIRI = adicapURI + "/" + adicapCode.get(iri);
	    		OWLClass owlClass = fact.getOWLClass(IRI.create(newIRI));
	    		OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
	    		manager.applyChange(new AddAxiom(onto, declare));
	    	}
	    	});
	      });
	  
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.DISJOINT_CLASSES)).
	    forEach(ax -> {
	    	String firstIRI = Lists.newArrayList(((OWLDisjointClassesAxiom) ax).classesInSignature().iterator()).get(0).getIRI().getIRIString();
	    	String secondIRI = Lists.newArrayList(((OWLDisjointClassesAxiom) ax).classesInSignature().iterator()).get(1).getIRI().getIRIString();;
	    	
	    	OWLClass disjClass = fact.getOWLClass(IRI.create(adicapURI + "/" + adicapCode.get(firstIRI)));
			OWLClass owlClass = fact.getOWLClass(IRI.create(adicapURI + "/" + adicapCode.get(secondIRI)));
			OWLDisjointClassesAxiom disjointClassesAxiom = fact.getOWLDisjointClassesAxiom(owlClass, disjClass);
	        manager.addAxiom(onto, disjointClassesAxiom);
	    	
	      });
	    
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.DISJOINT_CLASSES)).
	    forEach(ax -> {
	    	String firstIRI = ((OWLDisjointClassesAxiom) ax).classesInSignature().findFirst().get().getIRI().getIRIString();
	    	if(firstIRI.contains("https://data.esante.gouv.fr/terminologies/adicap#")) {
  			manager.applyChange(new RemoveAxiom(onto, ax));
  		}
	    	
	      });
    
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.SUBCLASS_OF)).
	    forEach(ax -> {
	    	String iriSub = adicapURI + "/" + adicapCode.get(((OWLSubClassOfAxiom) ax).getSubClass().asOWLClass().getIRI().getIRIString());
	    	String iriSup = adicapURI + "/" + adicapCode.get(((OWLSubClassOfAxiom) ax).getSuperClass().asOWLClass().getIRI().getIRIString());
	    	
	    	OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create(iriSub)), fact.getOWLClass(IRI.create(iriSup)));
			manager.applyChange(new AddAxiom(onto, ax1));
	    	
	      });
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.SUBCLASS_OF)).
	    forEach(ax -> {
	    	if(((OWLSubClassOfAxiom) ax).getSubClass().asOWLClass().getIRI().getIRIString().contains("https://data.esante.gouv.fr/terminologies/adicap#")) {
    			manager.applyChange(new RemoveAxiom(onto, ax));
    		}
	      });
	    
		  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    forEach(ax -> {
	    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<https://data.esante.gouv.fr/terminologies/adicap#anatomy>")) {
    			OWLAnnotation annot1 = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), IRI.create(adicapURI + "/" + adicapCode.get(((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1))));
        		OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(IRI.create(adicapURI + "/" + adicapCode.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot1);
        		manager.applyChange(new AddAxiom(onto, axiom1));
    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<https://data.esante.gouv.fr/terminologies/adicap#code>")){
    			
    			String code = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
    			
    			if(!ChargeMapping.listCodeADICAP.contains(code)) {
    	    		code = adicapCode.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString());
    	    	}else {
    	    		String id = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString().split("#")[1];
    	    		String dictionnary = ChargeMapping.listDictionnaryCode.get(id);
    	    		OWLAnnotationProperty dictionnaryCode = new OWLAnnotationPropertyImpl(ADICAPVocabulary.dictionaryCode.getIRI());
    	    		OWLAnnotation annot2 = fact.getOWLAnnotation(dictionnaryCode, fact.getOWLLiteral(dictionnary));
    	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(adicapURI + "/" + adicapCode.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot2);
    	    		manager.applyChange(new AddAxiom(onto, axiom2));
    	    	}
    			
    			
    			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
	    		OWLAnnotation annot2 = fact.getOWLAnnotation(notation, fact.getOWLLiteral(code));
	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(adicapURI + "/" + adicapCode.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot2);
	    		manager.applyChange(new AddAxiom(onto, axiom2));
	    		
    		}else if(((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("https://data.esante.gouv.fr/terminologies/adicap#")){
		    	OWLAnnotation annot = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), ((OWLAnnotationAssertionAxiom) ax).getValue());
	    		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create(adicapURI + "/" + adicapCode.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot);
	    		manager.applyChange(new AddAxiom(onto, axiom));
    		}
	    	
	      });
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    forEach(ax -> {
	    		if(((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("https://data.esante.gouv.fr/terminologies/adicap#")) {
	    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>")) {
	    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}
	      });
	    
	    
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.DECLARATION)).
	    forEach(ax -> {
	    	((OWLDeclarationAxiom) ax).classesInSignature().findFirst().ifPresent( clacc -> {
		    	String iri = clacc.getIRI().getIRIString();
		    	if(iri.contains("https://data.esante.gouv.fr/terminologies/adicap#")) {
	    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}
		    	});
	    	
	    	
	      });
	    
  }
  
}
