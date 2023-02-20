/**
 * 
 */
package fr.gouv.esante.pml.smt.metadata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

/**
 * @author agochath
 *
 */
public class SetMetadataInOntology {
  
  private static String ontoFileName = PropertiesUtil.getProperties("owlFileNameFR");
  private static String metaDataFileName = PropertiesUtil.getProperties("cim11modMetaDataFileName");
  private static String metaDataOntoFileName = PropertiesUtil.getProperties("owlMetaDataFileName");
  
  private static OWLOntologyManager man = null;
  private static OWLOntology onto = null;
  private static OWLOntology metadata = null;
  private static OWLDataFactory fact = null;
  private static OWLDataFactory fact2 = null;

  /**
   * @param args
   * @throws FileNotFoundException 
   * @throws OWLOntologyCreationException 
   * @throws OWLOntologyStorageException 
   */
  public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException {

  final InputStream inputOnt = new FileInputStream(ontoFileName);
  final InputStream inputMeta = new FileInputStream(metaDataFileName);
  final OutputStream outputMetaOnto = new FileOutputStream(metaDataOntoFileName);
  
  man = OWLManager.createOWLOntologyManager();
  onto = man.loadOntologyFromOntologyDocument(inputOnt);
  metadata = man.loadOntologyFromOntologyDocument(inputMeta);
  
  man.addAxioms(metadata, onto.axioms());
  
  final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
  ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
  
  
  //IRI icd11IRI = IRI.create("http://esante.gouv.fr/terminologie-icd11");
  //manager.applyChange(new SetOntologyID(onto,  new OWLOntologyID(icd11IRI)));
  
  man.saveOntology(metadata, ontologyFormat, outputMetaOnto);
//  fact = onto.getOWLOntologyManager().getOWLDataFactory();
//  fact2 = metadata.getOWLOntologyManager().getOWLDataFactory();
//  OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
  
  metadata.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION));
////  filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") || 
////               ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:comment") ).
//////  filter(ax -> !((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("http://id.who.int/icd/entity")).
//  .forEach(ax -> {
//    System.out.println(ax);
////    if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>") || 
////        ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>")) {
////      manager.applyChange(new RemoveAxiom(onto, ax));
////    }else 
////      if(ChargeMapping.listTraduction.containsKey(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {    
////        final OWLAnnotation labelAnno = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(),fact.getOWLLiteral(ChargeMapping.listTraduction.get(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", "")), "fr"));
////        final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), labelAnno);
////        man.applyChange(new AddAxiom(onto, ax1));
////      }
//   });
  

  }

}
