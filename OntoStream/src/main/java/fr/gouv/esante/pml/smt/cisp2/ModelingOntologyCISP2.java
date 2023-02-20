package fr.gouv.esante.pml.smt.cisp2;

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

import fr.gouv.esante.pml.smt.utils.ANSICD11Vocabulary;
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.ICDVocabulary;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;
import fr.gouv.esante.pml.smt.utils.XSkosVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

public class ModelingOntologyCISP2 {
  
  
  
  private static OWLDataFactory fact = null;

  static InputStream input = null;
//  private static OutputStream fout = null;
  private static Map<String, String> genIdIncl = new HashMap<String, String>();
  private static Map<String, String> InclusionNote = new HashMap<String, String>();
  private static Map<String, String> InclusionNoteXML = new HashMap<String, String>();
  private static Map<String, String> genIdExcl = new HashMap<String, String>();
  private static Map<String, String> ExclusionNote = new HashMap<String, String>();
  private static Map<String, String> ExclusionNoteXML = new HashMap<String, String>();
  
  private static Map<String, String> indexTerm = new HashMap<String, String>();
  private static Map<String, String> cispCode = new HashMap<String, String>();
  private static Map<String, String> foundationChild = new HashMap<String, String>();
  private static OWLOntologyManager manager = null;
  private static OWLOntology onto = null;
  private static String cispURI = "https://data.esante.gouv.fr/wonca-cispclub/cisp2";
  
  public static void main(final String[] args) throws Exception {
	    
	    input = new FileInputStream("D:\\cisp\\CGTS_SEM_CISP.owl");
//	    input = new FileInputStream("D:\\cim11\\newModelisation\\CGTS_SEM_ICD11-V202009-EN-NewModel.owl");
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
	    
	    
	    final OutputStream fileoutputstream = new FileOutputStream("D:\\cisp\\CGTS_SEM_CISP_V2.owl");
//	    final OutputStream fileoutputstream = new FileOutputStream("D:\\cim11\\newModelisation\\CGTS_SEM_ICD11-V202009-EN-NewModel-V2.owl");
	    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
	    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
		IRI cispIRI = IRI.create(cispURI);
		manager.applyChange(new SetOntologyID(onto, new OWLOntologyID(cispIRI)));
	      
	    manager.saveOntology(onto, ontologyFormat, fileoutputstream);


	  }
  
  public static void uriModeling() {
	  
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	  filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<https://data.esante.gouv.fr/terminologies/cisp#code>") ).
	    forEach(ax -> {
	    	String code = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
	    	cispCode.put(((OWLAnnotationAssertionAxiom) ax).getSubject().toString(), code);
	      });
	  
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.DECLARATION)).
	    forEach(ax -> {
	    	((OWLDeclarationAxiom) ax).classesInSignature().findFirst().ifPresent( clacc -> {
	    	String iri = clacc.getIRI().getIRIString();
	    	if(iri.contains("https://data.esante.gouv.fr/terminologies/cisp#")) {
	    		String newIRI = cispURI + "/" + cispCode.get(iri);
	    		OWLClass owlClass = fact.getOWLClass(IRI.create(newIRI));
	    		OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
	    		manager.applyChange(new AddAxiom(onto, declare));
	    	}
	    	});
	      });
    
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.SUBCLASS_OF)).
	    forEach(ax -> {
	    	String iriSub = cispURI + "/" + cispCode.get(((OWLSubClassOfAxiom) ax).getSubClass().asOWLClass().getIRI().getIRIString());
	    	String iriSup = cispURI + "/" + cispCode.get(((OWLSubClassOfAxiom) ax).getSuperClass().asOWLClass().getIRI().getIRIString());
	    	
	    	OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create(iriSub)), fact.getOWLClass(IRI.create(iriSup)));
			manager.applyChange(new AddAxiom(onto, ax1));
	    	
	      });
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.SUBCLASS_OF)).
	    forEach(ax -> {
	    	if(((OWLSubClassOfAxiom) ax).getSubClass().asOWLClass().getIRI().getIRIString().contains("https://data.esante.gouv.fr/terminologies/cisp#")) {
    			manager.applyChange(new RemoveAxiom(onto, ax));
    		}
	      });
	    
		  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    forEach(ax -> {
	    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/exclusion>")) {
    			OWLAnnotation annot1 = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), IRI.create(cispURI + "/" + cispCode.get(((OWLAnnotationAssertionAxiom) ax).getValue().toString())));
        		OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(IRI.create(cispURI + "/" + cispCode.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot1);
        		manager.applyChange(new AddAxiom(onto, axiom1));
    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<https://data.esante.gouv.fr/terminologies/cisp#consider>")) {
    			OWLAnnotation annot1 = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), IRI.create(cispURI + "/" + cispCode.get(((OWLAnnotationAssertionAxiom) ax).getValue().toString())));
        		OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(IRI.create(cispURI + "/" + cispCode.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot1);
        		manager.applyChange(new AddAxiom(onto, axiom1));
    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/icd10Code>")){
    			
	    		OWLAnnotation annot2 = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), IRI.create(((OWLAnnotationAssertionAxiom) ax).getValue().toString().replace("https://id.who.int/icd/release/10/2016", "http://data.esante.gouv.fr/atih/cim10")));
	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(cispURI + "/" + cispCode.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot2);
	    		manager.applyChange(new AddAxiom(onto, axiom2));
	    		
    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<https://data.esante.gouv.fr/terminologies/cisp#code>")){
    			
    			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
	    		OWLAnnotation annot2 = fact.getOWLAnnotation(notation, ((OWLAnnotationAssertionAxiom) ax).getValue());
	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(cispURI + "/" + cispCode.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot2);
	    		manager.applyChange(new AddAxiom(onto, axiom2));
	    		
    		}else if(((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("https://data.esante.gouv.fr/terminologies/cisp#")){
		    	OWLAnnotation annot = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), ((OWLAnnotationAssertionAxiom) ax).getValue());
	    		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create(cispURI + "/" + cispCode.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot);
	    		manager.applyChange(new AddAxiom(onto, axiom));
    		}
	    	
	      });
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    forEach(ax -> {
	    		if(!((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains(cispURI)) {
	    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>")) {
	    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}
	      });
	    
	    
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.DECLARATION)).
	    forEach(ax -> {
	    	((OWLDeclarationAxiom) ax).classesInSignature().findFirst().ifPresent( clacc -> {
		    	String iri = clacc.getIRI().getIRIString();
		    	if(!iri.contains(cispURI)) {
	    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}
		    	});
	    	
	    	
	      });
	    
  }
  
}
