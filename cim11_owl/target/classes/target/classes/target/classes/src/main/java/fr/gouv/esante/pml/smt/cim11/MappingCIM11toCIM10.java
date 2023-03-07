package fr.gouv.esante.pml.smt.cim11;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.ICDVocabulary;
import fr.gouv.esante.pml.smt.utils.XSkosVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

public class MappingCIM11toCIM10 {
  
  
  
  private static OWLDataFactory fact = null;

  static InputStream input = null;
//  private static OutputStream fout = null;
  private static final Map<String, String> genIdIncl = new HashMap<String, String>();
  private static final Map<String, String> InclusionNote = new HashMap<String, String>();
  private static final Map<String, String> InclusionNoteXML = new HashMap<String, String>();
  private static final Map<String, String> genIdExcl = new HashMap<String, String>();
  private static final Map<String, String> ExclusionNote = new HashMap<String, String>();
  private static final Map<String, String> ExclusionNoteXML = new HashMap<String, String>();
  
  private static final Map<String, String> entityLabel = new HashMap<String, String>();
  
  private static final Map<String, String> indexTerm = new HashMap<String, String>();
  private static final Map<String, String> foundationChild = new HashMap<String, String>();
  private static OWLOntologyManager manager = null;
  private static OWLOntology onto = null;
  
  public static void main(final String[] args) throws Exception {
	    
	    input = new FileInputStream("D:\\cim11\\R202202\\CGTS_SEM_ICD11-MMS-R202202-EN-FR-V3.owl");
	    manager = OWLManager.createOWLOntologyManager();
	    onto = manager.loadOntologyFromOntologyDocument(input);
	    ChargeMapping.chargeMappingCIM11to10Maps("D:\\cim11\\11To10MapToOneCategory.xlsx");
	    fact = onto.getOWLOntologyManager().getOWLDataFactory();

	    
	    mappingCIM11toCIM10Modeling();
	    
	    final OutputStream fileoutputstream = new FileOutputStream("D:\\cim11\\R202202\\CGTS_SEM_ICD11-MMS-R202202-EN-FR-V4.owl");
	    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
	    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
	    ontologyFormat.setPrefix("dc", "http://purl.org/dc/terms/");
	      
	    manager.saveOntology(onto, ontologyFormat, fileoutputstream);


	  }
  
  
  public static void mappingCIM11toCIM10Modeling() {
	 
	  onto.axioms().
	  filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label")).
	    forEach(ax -> {
	    	if(ChargeMapping.listMappingCIM11to10.containsKey(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())) {
	    		
	    		OWLAnnotationProperty omsMapping = new OWLAnnotationPropertyImpl(ANSICD11Vocabulary.icd10.getIRI());
	    		String code = codeTransformer(ChargeMapping.listMappingCIM11to10.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()));
	    		if(!code.equals("No Mapping")) {
		    		String Uri = "http://data.esante.gouv.fr/atih/cim10/" + code;
		    		OWLAnnotation annot = fact.getOWLAnnotation(omsMapping, IRI.create(Uri));
		    		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()), annot);
		    		manager.applyChange(new AddAxiom(onto, axiom));
	    		}
	    		}
	    	
	    });
	    
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

}
