package fr.gouv.esante.pml.smt.cim11;

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
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.HasProperty;
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
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.RDFVocabulary;

public class SetICDSkosOWL {
  
  
  private static OWLDataFactory fact = null;
  static InputStream input = null;
  private static OWLAnnotationProperty prefLabel = null;
  private static OWLAnnotationProperty definition = null;
  
  public static void main(final String[] args) throws Exception {
	  
	  
    
    input = new FileInputStream("D:\\cim11\\newModelisation\\CGTS_SEM_ICD11-MMS-OWL-V202009-newModel.xml");
    
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input);
    
    fact = onto.getOWLOntologyManager().getOWLDataFactory();
    prefLabel = fact.getOWLAnnotationProperty(SKOSVocabulary.PREFLABEL.getIRI());
	definition = fact.getOWLAnnotationProperty(SKOSVocabulary.DEFINITION.getIRI());
    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> 
    			((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>") ).
    forEach(ax -> {
    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>")) {
    		final OWLAnnotation anno = fact.getOWLAnnotation(fact.getRDFSLabel(), ((OWLAnnotationAssertionAxiom) ax).getValue());
	        final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), anno);
	        manager.applyChange(new AddAxiom(onto, ax1));
    	}else {
    		final OWLAnnotation anno = fact.getOWLAnnotation(fact.getRDFSComment(), ((OWLAnnotationAssertionAxiom) ax).getValue());
	        final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), anno);
	        manager.applyChange(new AddAxiom(onto, ax1));
    	}
     });
    


    final OutputStream fileoutputstream = new FileOutputStream("D:\\cim11\\newModelisation\\CGTS_SEM_ICD11-MMS-OWL-R202009-EN.xml");
    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
//    IRI icd11IRI = IRI.create("http://esante.gouv.fr/terminologie-icd11-mms");
//    manager.applyChange(new SetOntologyID(onto,  new OWLOntologyID(icd11IRI))); 
    manager.saveOntology(onto, ontologyFormat, fileoutputstream);

  }

}
