package fr.gouv.esante.pml.smt.alignement.cim10;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.util.AxiomSubjectProviderEx;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import fr.gouv.esante.pml.smt.historisation.ChargerMapping;
import fr.gouv.esante.pml.smt.utils.ADMSVocabulary;
import fr.gouv.esante.pml.smt.utils.ANSICD11Vocabulary;
import fr.gouv.esante.pml.smt.utils.DCTVocabulary;
import fr.gouv.esante.pml.smt.utils.ICDVocabulary;
import fr.gouv.esante.pml.smt.utils.OWLRDFVocabulary;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

public class XLSXOWLTraduction {
	
	private static OWLOntologyManager manager = null;
	private static OWLOntology onto = null;
	
	private static OWLDataFactory fact = null;
	private static InputStream input = null;
	
	private static String OWLAlignementFile = PropertiesUtil.getProperties("cim11AlignementOWL");
	private static String cim11OWLCore = PropertiesUtil.getProperties("cim11OWLCore");
	
	private static String xlsxAlignementFile = PropertiesUtil.getProperties("xlsxAlignementFile");
	
	
     private static OWLAnnotationProperty icd10Code = null;
     
     

		
	

	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException, ParseException {
		

		//logger.info("Début de construction de la terminologie");
		
		
		 ChargerAlignementMapping.chargeAlignement(xlsxAlignementFile);
		
		
		
		manager = OWLManager.createOWLOntologyManager();
		
		onto = manager.createOntology(IRI.create("http://id.who.int/icd/release/11/ontology"));

		fact = onto.getOWLOntologyManager().getOWLDataFactory();
		
		
		icd10Code = fact.getOWLAnnotationProperty(ANSICD11Vocabulary.icd10.getIRI());
        

        		
		
		getOntologieCore();

		addAlignement();
		
		
		final OutputStream fileoutputstream = new FileOutputStream(OWLAlignementFile);
		final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		ontologyFormat.setPrefix("dct", "http://purl.org/dc/terms/");
		ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
		manager.saveOntology(onto, ontologyFormat, fileoutputstream);
		

	}
	

	
	
	
	
	public static void addAlignement() {
		
		 for(String id: ChargerAlignementMapping.listAlignementCim10.keySet()) {
			 
			 OWLClass  owlClass = fact.getOWLClass(IRI.create(id));
			 
			 String code = ChargerAlignementMapping.listAlignementCim10.get(id).get(0);
			 
			 if(!code.equals("No Mapping")) {
				 
			  addURIAxioms(icd10Code, codeTransformer(code), owlClass);
			  
			 }
			   
	
			 
			 
		 }
		
	}
	
	
	
	
	 public static String codeTransformer(String code) {
		  String codeTransformed = code;
		  
		  switch(code) {
	      case "I":
	    	  codeTransformed="01";
	      	break;
	      case "II":
	    	  codeTransformed="02";
	      	break;
	      case "III":
	    	  codeTransformed="03";
	      	break;
	      case "IV":
	    	  codeTransformed="04";
	      	break;
	      case "IX":
	    	  codeTransformed="09";
	      	break;
	      case "V":
	    	  codeTransformed="05";
	      	break;
	      case "VI":
	    	  codeTransformed="06";
	      	break;
	      case "VII":
	    	  codeTransformed="07";
	      	break;
	      case "VIII":
	    	  codeTransformed="08";
	      	break;
	      case "X":
	    	  codeTransformed="10";
	      	break;
	      case "XI":
	    	  codeTransformed="11";
	      	break;
	      case "XII":
	    	  codeTransformed="12";
	      	break;
	      case "XIII":
	    	  codeTransformed="13";
	      	break;
	      case "XIV":
	    	  codeTransformed="14";
	      	break;
	      case "XIX":
	    	  codeTransformed="19";
	      	break;
	      case "XV":
	    	  codeTransformed="15";
	      	break;
	      case "XVI":
	    	  codeTransformed="16";
	      	break;
	      case "XVII":
	    	  codeTransformed="17";
	      	break;
	      case "XVIII":
	    	  codeTransformed="18";
	      	break;
	      case "XX":
	    	  codeTransformed="20";
	      	break;
	      case "XXI":
	    	  codeTransformed="21";
	      	break;
	      case "XXII":
	    	  codeTransformed="22";
	      	break;
	      } 
		  
		  
		  return codeTransformed;
	  }
	

	
	
	 public static void addURIAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {

		    IRI iri_creator = IRI.create("http://data.esante.gouv.fr/atih/cim10/"+val);
			   
		    OWLAnnotationProperty prop_creator =fact.getOWLAnnotationProperty(prop.getIRI());
		    
		    OWLAnnotation annotation = fact.getOWLAnnotation(prop_creator, iri_creator);
		    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
		    manager.applyChange(new AddAxiom(onto, axiom));
		    
		    
		  }
	
	
	
	
	
	
	
	 
	 public static void addLateralAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
		    final OWLAnnotation annotation =
		        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val));
		    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
		    manager.applyChange(new AddAxiom(onto, axiom));
		  }
	 
	 public static void addLateralAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass, String lang) {
		    final OWLAnnotation annotation =
		        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val,lang));
		    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
		    manager.applyChange(new AddAxiom(onto, axiom));
		  }
		
		
	
	 public static void addDatelAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
		  

		   final OWLAnnotation annotation =
		    		fact.getOWLAnnotation(prop, fact.getOWLLiteral(val, OWL2Datatype.XSD_DATE_TIME));

		    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
		    manager.applyChange(new AddAxiom(onto, axiom));
		  }
	 
	 

	private static void getOntologieCore() throws FileNotFoundException, OWLOntologyCreationException {
    	//logger.info("importer le core de la terminologie");
		input = new FileInputStream(cim11OWLCore);
	    manager = OWLManager.createOWLOntologyManager();
	    OWLOntology core = manager.loadOntologyFromOntologyDocument(input);
	    
	    core.axioms().
	    forEach(ax -> {
	    	manager.applyChange(new AddAxiom(onto, ax));
	        
	    	});
	    
	    
	    //logger.info("fin import du core de la terminologie");
	}
	
	
	
	
	

	

	
	
	
	  
	  
	 

}
