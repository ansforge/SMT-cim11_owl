package fr.gouv.esante.pml.smt.alignementBDPMCIP;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

public class AlignementBdpmCIP {
	
	private static OWLDataFactory factBDPM = null;
	private static OWLDataFactory factCIP = null;
	private static OWLOntologyManager manager = null;
	private static OWLOntology ontoBDPM = null;
	private static OWLOntology ontoCIP = null;
	private static String BDPMFileName = PropertiesUtil.getProperties("BDPMFileName");
	private static String CIPFileName = PropertiesUtil.getProperties("CIPFileName");
	
	private static Map<String, String> CIPCodeInBDPM = new HashMap<String, String>();
	private static Map<String, String> allCIPcodes = new HashMap<String, String>();

	public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException {
		
		InputStream inputBDPM = new FileInputStream(BDPMFileName);
		InputStream inputCIP = new FileInputStream(CIPFileName);

	    manager = OWLManager.createOWLOntologyManager();
	    ontoBDPM = manager.loadOntologyFromOntologyDocument(inputBDPM);
	    ontoCIP = manager.loadOntologyFromOntologyDocument(inputCIP);
	    

	    factBDPM = ontoBDPM.getOWLOntologyManager().getOWLDataFactory();
	    factCIP = ontoCIP.getOWLOntologyManager().getOWLDataFactory();
	    
	    
	    ontoBDPM.axioms().filter(ax -> ax.isOfType(AxiomType.SUBCLASS_OF)).
	    forEach(ax -> {
	    	if(((OWLSubClassOfAxiom) ax).getSuperClass().isOWLClass()) {
	    	String iriSub = ((OWLSubClassOfAxiom) ax).getSubClass().asOWLClass().getIRI().getIRIString();
	    	String iriSup = ((OWLSubClassOfAxiom) ax).getSuperClass().asOWLClass().getIRI().getIRIString();
	    	if(iriSup.equals("http://www.data.esante.gouv.fr/ANSM/BDPM-core-ontology/CIP_Entity")) {
	    		String code = iriSub.split("/")[5];
	    		CIPCodeInBDPM.put(code, iriSub);
	    	}
	    	}
	      });

	    ontoCIP.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax ->((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#notation>")).
	    forEach(ax -> {
	    	String code = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
	    	String uri = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	String cc = code.substring(1, code.length()-13);
	    	allCIPcodes.put(cc, uri);
	     });
	    
	    for(String bdpmCode : CIPCodeInBDPM.keySet()) {
	    	if(allCIPcodes.containsKey(bdpmCode)) {
	    	OWLAnnotationProperty EXACTMATCH = new OWLAnnotationPropertyImpl(SKOSVocabulary.EXACTMATCH.getIRI());
    		OWLAnnotation annotBDPM = factBDPM.getOWLAnnotation(EXACTMATCH, IRI.create(allCIPcodes.get(bdpmCode)));
    		OWLAxiom axiomBDPM = factBDPM.getOWLAnnotationAssertionAxiom(IRI.create(CIPCodeInBDPM.get(bdpmCode)), annotBDPM);
    		manager.applyChange(new AddAxiom(ontoBDPM, axiomBDPM));
    		
    		OWLAnnotation annotCIP = factCIP.getOWLAnnotation(EXACTMATCH, IRI.create(CIPCodeInBDPM.get(bdpmCode)));
    		OWLAxiom axiomCIP = factCIP.getOWLAnnotationAssertionAxiom(IRI.create(allCIPcodes.get(bdpmCode)), annotCIP);
    		manager.applyChange(new AddAxiom(ontoCIP, axiomCIP));
    		
//    		allCIPcodes.remove(bdpmCode);
//    		CIPCodeInBDPM.remove(bdpmCode);
	    	}else {
	    		System.out.println(bdpmCode);
	    	}
	    }
	    
	    final OutputStream fileoutputstreamBDPM = new FileOutputStream(PropertiesUtil.getProperties("BDPMFileNameAlignement"));
	    final OutputStream fileoutputstreamCIP = new FileOutputStream(PropertiesUtil.getProperties("CIPFileNameAlignement"));
	    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
	      
	    manager.saveOntology(ontoBDPM, ontologyFormat, fileoutputstreamBDPM);
	    manager.saveOntology(ontoCIP, ontologyFormat, fileoutputstreamCIP);
	    
//	    for(String bdpmCode : CIPCodeInBDPM.keySet()) {
//	    	System.out.println(bdpmCode);
//	    }
	}

}
