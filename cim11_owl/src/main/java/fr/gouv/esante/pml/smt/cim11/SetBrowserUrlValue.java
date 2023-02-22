package fr.gouv.esante.pml.smt.cim11;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;

public class SetBrowserUrlValue {
  
  
  private static String owlFileNameFR = PropertiesUtil.getProperties("owlFileNameFR");
//  private static String owlFileNameFR = PropertiesUtil.getProperties("owlFileNameES2");
  private static OWLDataFactory fact = null;
  private static InputStream input = null;
  private static OWLAnnotationProperty rdfType = null;
  private static OWLAnnotationProperty skosInScheme = null;
  private static OWLAnnotationProperty skosNotation = null;
  public static void main(final String[] args) throws Exception {
    
    input = new FileInputStream("D:\\cim11\\newModelisation\\CGTS_SEM_ICD11-MMS-OWL-R202009-EN.xml");
    
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input);
    
    fact = onto.getOWLOntologyManager().getOWLDataFactory();
//    rdfType = fact.getOWLAnnotationProperty(OWLRDFVocabulary.RDF_TYPE.getIRI());
//    skosInScheme = fact.getOWLAnnotationProperty(SKOSVocabulary.INSCHEME.getIRI());
    skosNotation = fact.getOWLAnnotationProperty(SkosVocabulary.notation.getIRI());
    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#broader>") 
    		||((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/code>") ).
    forEach(ax -> {
    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#broader>")) {
    		manager.applyChange(new RemoveAxiom(onto, ax));
    	} else {
    		final OWLAnnotation labelAnno3 = fact.getOWLAnnotation(skosNotation, fact.getOWLLiteral(((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().length()-13)));
    		final OWLAxiom ax3 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), labelAnno3);
    		manager.applyChange(new AddAxiom(onto, ax3));
    	}
      });
    final OutputStream fileoutputstream = new FileOutputStream("D:\\cim11\\newModelisation\\CGTS_SEM_ICD11-MMS-OWL-R202009-EN-V3.xml");
    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
      
    manager.saveOntology(onto, ontologyFormat, fileoutputstream);


  }

}
