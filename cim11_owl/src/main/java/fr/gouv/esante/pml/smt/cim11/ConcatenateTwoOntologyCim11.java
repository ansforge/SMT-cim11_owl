package fr.gouv.esante.pml.smt.cim11;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
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

public class ConcatenateTwoOntologyCim11 {
 
  static InputStream input1 = null;
  static InputStream input2 = null;
  private static OWLOntologyManager manager = null;
  private static OWLOntology onto1 = null;
  private static OWLOntology onto2 = null;
  
  public static void main(final String[] args) throws Exception {
	  
	  
	
	  
	  Options options = new Options();
	  options.addOption("owlFR", "owlFR", true, "owl file en Francais");
	  options.addOption("owlEN", "owlEN", true, "owl file en Anglais");
	  options.addOption("owlFrEn", "owlFrEn", true, "owl file en Anglais");
	  
	  CommandLineParser parser = new DefaultParser();
	  CommandLine line = parser.parse(options, args);
	  
	  String  owlFileFr = line.getOptionValue("owlFR");
	  String  owlFileEn = line.getOptionValue("owlEN");
	  
	  System.out.println("owlFR "+owlFileFr);
	  System.out.println("owlEN "+owlFileEn);
	    
	  
	  if(owlFileFr==null) {
		  
		  owlFileFr = PropertiesUtil.getProperties("owlModelingFileNameFR");
	  }
	  if(owlFileEn==null) {
		  
		  owlFileEn = PropertiesUtil.getProperties("owlModelingFileNameEN");
	  }
	  
	  
	  input1 = new FileInputStream(owlFileFr);
	   
	   
	   input2 = new FileInputStream(owlFileEn);
	    
	    manager = OWLManager.createOWLOntologyManager();
	    onto1 = manager.loadOntologyFromOntologyDocument(input1);
	    onto2 = manager.loadOntologyFromOntologyDocument(input2);
	    
	    onto1.axioms().forEach(ax -> {	
			manager.applyChange(new AddAxiom(onto2, ax));
	    	});
	    
	    final OutputStream fileoutputstream = new FileOutputStream(PropertiesUtil.getProperties("owlModelingFileNameEN_FR"));
	    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
	    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
	      
	    manager.saveOntology(onto2, ontologyFormat, fileoutputstream);
	    
	    fileoutputstream.close();

	  }
  
}
