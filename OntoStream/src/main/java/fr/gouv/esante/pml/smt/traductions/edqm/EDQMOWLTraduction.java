package fr.gouv.esante.pml.smt.traductions.edqm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
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

import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class EDQMOWLTraduction {
	
	private static OWLOntologyManager manager = null;
	private static OWLOntology onto = null;
	
	private static OWLDataFactory fact = null;
	private static InputStream input = null;
	
	private static String OWLFile = PropertiesUtil.getEdqmProperties("edqmOWL");
	private static String coreOWLFile = PropertiesUtil.getEdqmProperties("edqmCoreOWL");
	
	private static String xlsxEdqmFileName = PropertiesUtil.getEdqmProperties("edqmTraduction");
	
	private static OWLAnnotationProperty skosDefinition  = null;
	private static OWLAnnotationProperty rdfsLabel  = null;
	

	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
		

		//logger.info("Début de construction de la terminologie");
		
		
		ChargeMapping.chargeExcelEdqm(xlsxEdqmFileName);
		
		
		manager = OWLManager.createOWLOntologyManager();
		
		onto = manager.createOntology(IRI.create("http://data.esante.gouv.fr/coe/standardterms#"));

		fact = onto.getOWLOntologyManager().getOWLDataFactory();
		
		skosDefinition =  fact.getOWLAnnotationProperty(org.semanticweb.owlapi.vocab.SKOSVocabulary.DEFINITION.getIRI());
		rdfsLabel =  fact.getOWLAnnotationProperty(fr.gouv.esante.pml.smt.utils.OWLRDFVocabulary.RDFS_LABEL.getIRI());
		
		getOntologieCore();
		
		ajoutTraductions();
		
		final OutputStream fileoutputstream = new FileOutputStream(OWLFile);
		final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		manager.saveOntology(onto, ontologyFormat, fileoutputstream);
		

	}
	
	
	public static void ajoutTraductions() {
		
		 for(String id: ChargeMapping.listConceptsEdqm.keySet()) {
			 
			 OWLClass  owlClass = fact.getOWLClass(IRI.create(id));
			 
			 if(!ChargeMapping.listConceptsEdqm.get(id).get(0).isEmpty())
			   addLateralAxioms(skosDefinition, ChargeMapping.listConceptsEdqm.get(id).get(0), owlClass, "fr");
			 
			 if(!ChargeMapping.listConceptsEdqm.get(id).get(1).isEmpty())
				   addLateralAxioms(rdfsLabel, ChargeMapping.listConceptsEdqm.get(id).get(1), owlClass, "fr");
			 
		 }
		
	}
	
	 public static void addLateralAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass, String lang) {
		    final OWLAnnotation annotation =
		        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val, lang));
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

}
