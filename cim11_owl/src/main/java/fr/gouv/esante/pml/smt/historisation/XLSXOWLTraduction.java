package fr.gouv.esante.pml.smt.historisation;

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

import fr.gouv.esante.pml.smt.utils.ADMSVocabulary;
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
	
	private static String OWLFile = PropertiesUtil.getProperties("cim11OWL");
	private static String coreOWLFile = PropertiesUtil.getProperties("cim11CoreOWL");
	
	private static String xlsxChangesMainFile = PropertiesUtil.getProperties("xlsxChangesMainFile");
	private static String xlsxChangesExtensionFile = PropertiesUtil.getProperties("xlsxChangesExtenstionFile");
	
	 private static OWLAnnotationProperty admsStatus  = null;
	 private static OWLAnnotationProperty dctCreated  = null;
	 private static OWLAnnotationProperty dctModified  = null;
	 private static OWLAnnotationProperty rdfsComments  = null;
	 private static OWLAnnotationProperty rdfsLabel  = null;
	 private static OWLAnnotationProperty skosnotation  = null;
     private static OWLAnnotationProperty icdCode = null;
     private static List<String> allConceptsCim11 = null;
     

		
	

	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException, ParseException {
		

		//logger.info("Début de construction de la terminologie");
		
		
		 ChargerMapping.chargeConceptPrincipal(xlsxChangesMainFile);
		
		 ChargerMapping.chargeConceptExtension(xlsxChangesExtensionFile);
		
		
		manager = OWLManager.createOWLOntologyManager();
		
		onto = manager.createOntology(IRI.create("http://id.who.int/icd/release/11/ontology"));

		fact = onto.getOWLOntologyManager().getOWLDataFactory();
		
		admsStatus = fact.getOWLAnnotationProperty(ADMSVocabulary.status.getIRI());
		dctCreated = fact.getOWLAnnotationProperty(DCTVocabulary.created.getIRI());
		dctModified = fact.getOWLAnnotationProperty(DCTVocabulary.modified.getIRI());
		rdfsComments = fact.getOWLAnnotationProperty(fr.gouv.esante.pml.smt.utils.OWLRDFVocabulary.RDFS_COMMENT.getIRI());
		rdfsLabel = fact.getOWLAnnotationProperty(fr.gouv.esante.pml.smt.utils.OWLRDFVocabulary.RDFS_LABEL.getIRI());
		skosnotation = fact.getOWLAnnotationProperty(SkosVocabulary.notation.getIRI());
        icdCode = fact.getOWLAnnotationProperty(ICDVocabulary.CODE.getIRI());
        allConceptsCim11 = new ArrayList<>();

        		
		
		getOntologieCore();
		
		getAllConcepts();
		
		updateOldConcepts();
		
		createConceptRetiresNoeud();
		createConceptRetires2023Noeud();
		createConceptsPrincipaux();
		createConceptsExtensions();
		    
		
		updateConceptPrincipaux();
		updateConceptExtensions();
		
		final OutputStream fileoutputstream = new FileOutputStream(OWLFile);
		final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		ontologyFormat.setPrefix("dct", "http://purl.org/dc/terms/");
		ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
		manager.saveOntology(onto, ontologyFormat, fileoutputstream);
		

	}
	
	public static void updateOldConcepts() {
		
		for(String iri : allConceptsCim11) {
			
			if(!ChargerMapping.listChnagesMain.containsKey(iri) && !ChargerMapping.listChnagesExtensions.containsKey(iri) ) {
				
				System.out.println(iri);
				OWLClass  owlClass = fact.getOWLClass(IRI.create(iri));
				addLateralAxioms(admsStatus, "active", owlClass);
				addDatelAxioms(dctCreated, "2022-02-01T00:00:00", owlClass);
			}
		}
	}
	
	
	
	
	public static void updateConceptPrincipaux() {
		
		 for(String id: ChargerMapping.listChnagesMain.keySet()) {
			 
			 OWLClass  owlClass = fact.getOWLClass(IRI.create(id));
			 
			     
			 
			 if("NewlyAdded".equals(ChargerMapping.listChnagesMain.get(id).get(0))) {
			   addLateralAxioms(admsStatus, "active", owlClass);
			   addDatelAxioms(dctCreated, "2023-01-01T00:00:00", owlClass);
			   
			 }
			 
			 if("MovedTo".equals(ChargerMapping.listChnagesMain.get(id).get(0))) {
				   addLateralAxioms(admsStatus, "active", owlClass);
				   addDatelAxioms(dctCreated, "2022-02-01T00:00:00", owlClass);
				   addDatelAxioms(dctModified, "2023-01-01T00:00:00", owlClass);
				   addLateralAxioms(rdfsComments,"Code remplcé : "+ ChargerMapping.listChnagesMain.get(id).get(1).split("->")[0], owlClass);
				   
			  }
			 
			 if("MovedAboveShoreline".equals(ChargerMapping.listChnagesMain.get(id).get(0))){
				   addLateralAxioms(admsStatus, "active", owlClass);
				   addDatelAxioms(dctCreated, "2023-01-01T00:00:00", owlClass);
				  
				   
			  }
			 
			 if("Removed".equals(ChargerMapping.listChnagesMain.get(id).get(0))
					 || "MovedUnderShoreline".equals(ChargerMapping.listChnagesMain.get(id).get(0))) {
				    
				    
			        owlClass = fact.getOWLClass(IRI.create(id));
			        OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			        manager.applyChange(new AddAxiom(onto, declare));   
				 
				    String aboutSubClass = null;
			        aboutSubClass = "http://id.who.int/icd/release/11/mms/Concepts_principaux";
			        OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
			        
			        OWLAxiom axiom = fact.getOWLSubClassOfAxiom(owlClass, subClass);
			        manager.applyChange(new AddAxiom(onto, axiom));
			        
			        
				    addLateralAxioms(admsStatus, "inactive", owlClass);
				    addLateralAxioms(rdfsLabel, ChargerMapping.listChnagesMain.get(id).get(2), owlClass, "en");
				    addDatelAxioms(dctCreated, "2022-02-01T00:00:00", owlClass);
				    addDatelAxioms(dctModified, "2023-01-01T00:00:00", owlClass);
				    addLateralAxioms(icdCode, ChargerMapping.listChnagesMain.get(id).get(1) , owlClass);
				   
			  }
			 
			 
		 }
		
	}
	
	
	
	
	
	
	public static void updateConceptExtensions() {
		
		 for(String id: ChargerMapping.listChnagesExtensions.keySet()) {
			 
			 OWLClass  owlClass = fact.getOWLClass(IRI.create(id));
			 
			     
			 
			 if("NewlyAdded".equals(ChargerMapping.listChnagesExtensions.get(id).get(0))) {
			   addLateralAxioms(admsStatus, "active", owlClass);
			   addDatelAxioms(dctCreated, "2023-01-01T00:00:00", owlClass);
			   
			 }
			 
			
			 
			 if("Removed".equals(ChargerMapping.listChnagesExtensions.get(id).get(0))) {
				    
				    
			        owlClass = fact.getOWLClass(IRI.create(id));
			        OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			        manager.applyChange(new AddAxiom(onto, declare));   
				 
				    String aboutSubClass = null;
			        aboutSubClass = "http://id.who.int/icd/release/11/mms/Concepts_extensions";
			        OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
			        
			        OWLAxiom axiom = fact.getOWLSubClassOfAxiom(owlClass, subClass);
			        manager.applyChange(new AddAxiom(onto, axiom));
			        
			        
				    addLateralAxioms(admsStatus, "inactive", owlClass);
				    addLateralAxioms(rdfsLabel, ChargerMapping.listChnagesExtensions.get(id).get(2), owlClass, "en");
				    addDatelAxioms(dctCreated, "2022-02-01T00:00:00", owlClass);
				    addDatelAxioms(dctModified, "2023-01-01T00:00:00", owlClass);
				    addLateralAxioms(icdCode, ChargerMapping.listChnagesExtensions.get(id).get(1) , owlClass);
				   
			  }
			 
			 
		 }
		
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
		input = new FileInputStream(coreOWLFile);
	    manager = OWLManager.createOWLOntologyManager();
	    OWLOntology core = manager.loadOntologyFromOntologyDocument(input);
	    
	    core.axioms().
	    forEach(ax -> {
	    	manager.applyChange(new AddAxiom(onto, ax));
	        
	    	});
	    
	    
	    //logger.info("fin import du core de la terminologie");
	}
	
	
	private static void getAllConcepts() throws FileNotFoundException, OWLOntologyCreationException {
    	
		onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).forEach(ax -> {
			
			if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/release>")){
    			
				allConceptsCim11.add(((OWLAnnotationAssertionAxiom) ax).getSubject().toString());
				//System.out.println("subject: "+  ((OWLAnnotationAssertionAxiom) ax).getSubject().toString());
	    		
    		}
		});
	}
	
	
	public static void createConceptRetires2023Noeud() {
		  
		  
	    
		   final String classRacine = "http://id.who.int/icd/release/11/mms/Concept_retirés_2023" ;
		   OWLClass noeudRacine = fact.getOWLClass(IRI.create(classRacine));
		   
		   String aboutSubClass = null;
	       aboutSubClass = "http://id.who.int/icd/release/11/mms/Concept_retirés/Concept_retirés"  ;
	       OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
	       
	       OWLAxiom axiom = fact.getOWLSubClassOfAxiom(noeudRacine, subClass);
	       manager.applyChange(new AddAxiom(onto, axiom));
	     
		  addLateralAxioms(skosnotation, "Concept retirés 2023", noeudRacine);
	      
		  
	 }
	
	public static void createConceptsPrincipaux() {
		  
		  
	    
		   final String classRacine = "http://id.who.int/icd/release/11/mms/Concepts_principaux" ;
		   OWLClass noeudRacine = fact.getOWLClass(IRI.create(classRacine));
		   
		   String aboutSubClass = null;
	       aboutSubClass = "http://id.who.int/icd/release/11/mms/Concept_retirés_2023"  ;
	       OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
	       
	       OWLAxiom axiom = fact.getOWLSubClassOfAxiom(noeudRacine, subClass);
	       manager.applyChange(new AddAxiom(onto, axiom));
	     
		  addLateralAxioms(skosnotation, "Concepts principaux", noeudRacine);
	      
		  
	 }
	
	
	public static void createConceptsExtensions() {
		  
		  
	    
		   final String classRacine = "http://id.who.int/icd/release/11/mms/Concepts_extensions" ;
		   OWLClass noeudRacine = fact.getOWLClass(IRI.create(classRacine));
		   
		   String aboutSubClass = null;
	       aboutSubClass = "http://id.who.int/icd/release/11/mms/Concept_retirés_2023"  ;
	       OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
	       
	       OWLAxiom axiom = fact.getOWLSubClassOfAxiom(noeudRacine, subClass);
	       manager.applyChange(new AddAxiom(onto, axiom));
	     
		  addLateralAxioms(skosnotation, "Concepts extensions", noeudRacine);
	      
		  
	 }
	  
	  
	   public static void createConceptRetiresNoeud() {
		  
		  
		    
		   final String classRacine = "http://id.who.int/icd/release/11/mms/Concept_retirés" ;
		   OWLClass noeudRacine = fact.getOWLClass(IRI.create(classRacine));
		    addLateralAxioms(skosnotation, "Concept retirés", noeudRacine);
	      
		  
	 }

}
