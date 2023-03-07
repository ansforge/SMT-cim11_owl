package fr.gouv.esante.pml.smt.metadata;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.SysRIOT;
import org.apache.jena.sparql.util.Context;
import org.apache.tika.exception.TikaException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import fr.gouv.esante.pml.smt.utils.MetadataUtil;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class GetMetadata {
    
  private static final String owlMetaDataFileName = PropertiesUtil.getProperties("owlMetaDataFileName");
  private static final String catalogueFile = PropertiesUtil.getProperties("catalogueFile");
  private static OWLOntologyManager man = null;
  private static OWLOntology onto = null;
  private static final OWLOntology catalogue = null;
  private static final OWLDataFactory fact = null;
  private static final OWLDataFactory fact2 = null;
  
  private static final OWLNamedIndividual agent_ASIP = null;
  
   public static void main(final String[] args) throws IOException, TikaException, OWLOntologyCreationException, OWLOntologyStorageException {
    
     final InputStream in = new FileInputStream("C:\\Users\\agochath\\Documents\\cim11\\cim11-profile-01.owl");
     
     man = OWLManager.createOWLOntologyManager();
     onto = man.loadOntologyFromOntologyDocument(in);
     
     final OutputStream fileoutputstream = new FileOutputStream("C:\\Users\\agochath\\Documents\\cim11\\cim11-profile-01-mod-1.4.owl");
//     final TurtleDocumentFormat ontologyFormat = new TurtleDocumentFormat();
     final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
     ontologyFormat.setPrefix("cc", "http://creativecommons.org/ns#");
     ontologyFormat.setPrefix("dc", "http://purl.org/dc/elements/1.1/");
     ontologyFormat.setPrefix("dct", "http://purl.org/dc/terms/");
     ontologyFormat.setPrefix("omv", "http://omv.ontoware.org/2005/05/ontology#");
     ontologyFormat.setPrefix("door", "http://kannel.open.ac.uk/ontology#");
     man.saveOntology(onto, ontologyFormat, fileoutputstream);
     
     

     
     
      
   }
}