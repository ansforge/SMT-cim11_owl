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
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class CleaningOntology {
  
  
  
  private static OWLDataFactory fact = null;

  static InputStream input = null;
//  private static OutputStream fout = null;
  
  public static void main(final String[] args) throws Exception {
    
    input = new FileInputStream("D:\\cim10\\cim10ClaML\\cim-atih\\CIM-10-FR_2021syst_owl5_20201201.xml");

    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input);
    

    fact = onto.getOWLOntologyManager().getOWLDataFactory();
    

    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> 
    			((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#altLabel>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#note>") ).
    forEach(ax -> {
    	final OWLAnnotation labelAnno = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), fact.getOWLLiteral(((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^")-1).replaceAll("(?:\\n|\\r)", " ").replaceAll("	", "")));
          final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), labelAnno);
          manager.applyChange(new AddAxiom(onto, ax1));
          manager.applyChange(new RemoveAxiom(onto, ax));
              
     });
    
    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> 
    			((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#altLabel>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#note>") ).
    forEach(ax -> {
    	if(((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("]")) {
    	final OWLAnnotation labelAnno = 
    			fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), 
    					fact.getOWLLiteral(((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^")-1).
    							substring(0, 
    									((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^")-1).indexOf("["))));
          final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), labelAnno);
          manager.applyChange(new AddAxiom(onto, ax1));
//          manager.applyChange(new RemoveAxiom(onto, ax));
    	}
              
     });
      

    final OutputStream fileoutputstream = new FileOutputStream("D:\\cim10\\cim10ClaML\\cim-atih\\CIM-10-FR_2021syst_owl5_1_20201201.xml");
    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
    
    
    
    manager.saveOntology(onto, ontologyFormat, fileoutputstream);

  }

}
