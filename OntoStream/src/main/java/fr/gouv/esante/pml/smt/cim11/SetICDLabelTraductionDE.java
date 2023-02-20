package fr.gouv.esante.pml.smt.cim11;

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

public class SetICDLabelTraductionDE {
  
  
  private static String owlFileNameDE = PropertiesUtil.getProperties("owlFileNameDE");
  private static String owlFileNameFR = PropertiesUtil.getProperties("owlFileNameFR");
  private static String labelsFileNameEn = PropertiesUtil.getProperties("labelsFileNameEn");
  private static String labelsFileNameDe = PropertiesUtil.getProperties("labelsFileNameDe");
  private static OWLDataFactory fact = null;
  static InputStream input = null;
  private static OutputStream fout = null;
  
  public static void main(final String[] args) throws Exception {
    
    input = new FileInputStream(owlFileNameFR);
    ChargeMapping.chargeCSVTraductionFileBeToList(labelsFileNameEn, labelsFileNameDe);
    
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input);
    
//    onto.axioms().forEach(ax -> {System.out.println(ax);});
    fact = onto.getOWLOntologyManager().getOWLDataFactory();
    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:comment") ).
//    filter(ax -> !((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("http://id.who.int/icd/entity")).
    forEach(ax -> {
      //System.out.println(((OWLAnnotationAssertionAxiom) ax).getSubject()+"---"+((OWLAnnotationAssertionAxiom) ax).getProperty()+"---"+((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1));
      if(ChargeMapping.listTraductionDe.containsKey(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {    
        final OWLAnnotation labelAnno = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(),fact.getOWLLiteral(ChargeMapping.listTraductionDe.get(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", "")), "de"));
        final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), labelAnno);
        manager.applyChange(new AddAxiom(onto, ax1));
      }
                  });

    final OutputStream fileoutputstream = new FileOutputStream(owlFileNameDE);
    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
    
    
    IRI icd11IRI = IRI.create("http://esante.gouv.fr/terminologie-icd11");
    manager.applyChange(new SetOntologyID(onto,  new OWLOntologyID(icd11IRI)));
    
    manager.saveOntology(onto, ontologyFormat, fileoutputstream);


  }

}
