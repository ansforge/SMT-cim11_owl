package fr.gouv.esante.pml.smt.cim11;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import com.google.common.collect.Lists;

import fr.gouv.esante.pml.smt.utils.ANSICD11Vocabulary;
import fr.gouv.esante.pml.smt.utils.DCTVocabulary;
import fr.gouv.esante.pml.smt.utils.ICDVocabulary;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;
import fr.gouv.esante.pml.smt.utils.XSkosVocabulary;
import ru.avicomp.ontapi.jena.utils.BuiltIn.DCVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

public class CorrectionBlockIDOntologyCim11 {
  
  
  
  private static OWLDataFactory fact = null;

  static InputStream input = null;
  private static Map<String, String> blockIDList = new HashMap<String, String>();
  private static List<String> notationCode = new ArrayList<String>();
  private static List<String> notationCodeDouble = new ArrayList<String>();
  private static List<String> blockList = new ArrayList<String>();
  
  private static OWLOntologyManager manager = null;
  private static OWLOntology onto = null;
  
  public static void main(final String[] args) throws Exception {
	    
	    input = new FileInputStream(PropertiesUtil.getProperties("owlModelingFileNameEN_FR"));
	    manager = OWLManager.createOWLOntologyManager();
	    onto = manager.loadOntologyFromOntologyDocument(input);
	    
	    fact = onto.getOWLOntologyManager().getOWLDataFactory();
	    
	    cleanning();
	    addNotationAndTypeModeling();
	    
	    final OutputStream fileoutputstream = new FileOutputStream(PropertiesUtil.getProperties("owlModelingFileNameEN_FR_2"));
	    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
	    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
	      
	    manager.saveOntology(onto, ontologyFormat, fileoutputstream);

	  }
  
  public static void addNotationAndTypeModeling() {
	  	
	  onto.axioms().
	  filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/classKind>") ||
		((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/blockId>")).
	    forEach(ax -> {
	    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/classKind>")) {
	    	String classKind = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
	    	String uri = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	if(classKind.equals("block")) {
	    		blockList.add(uri);
	    	}
	    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/blockId>")) {
	    		String blockId = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
		    	String uri = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
		    	blockIDList.put(uri, blockId);
	    	}
	    	
	      });
	  
	  onto.axioms().
	  filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#notation>")).
	    forEach(ax -> {
	    	String uri = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
	    	if(blockList.contains(uri)) {
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    	}
	    	if(blockIDList.containsKey(uri)) {
	    		OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
				OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(blockIDList.get(uri)));
				OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), annot);
				manager.applyChange(new AddAxiom(onto, axiom));	
	    	}
	    	
	      });
	    
  }

  public static void cleanning() {
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#notation>") ||
				((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/codingRange>") ||
				((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/blockId>")).
	    forEach(ax -> {
	    		String code = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
	    	    if(code.isEmpty()) {
	    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}
	    		
	      });
  }
  public static void testNotation() {
	  	
	  onto.axioms().
	  filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#notation>")).
	    forEach(ax -> {
	    	String code = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
	    	if(!notationCodeDouble.contains(code)) {
	    		notationCodeDouble.add(code);
	    	}else {
	    		System.out.println(code);
	    	}
	    	
	    	
	      });
	    
  }

 
  
  public static String hashSignature(String secret, String message) throws NoSuchAlgorithmException, InvalidKeyException {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA512");
		SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
		sha256_HMAC.init(secret_key);

		String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
		return hash.substring(4, 7);
	}

}
