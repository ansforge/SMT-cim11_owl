package fr.gouv.esante.pml.smt.metadata;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
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
import fr.gouv.esante.pml.smt.kmi.exceptions.SKOSParserException;
import fr.gouv.esante.pml.smt.kmi.owl.NamedIndividualConcept;
import fr.gouv.esante.pml.smt.kmi.owl.OWLOntologyBuilder;
import fr.gouv.esante.pml.smt.kmi.skos.SKOSConcept;
import fr.gouv.esante.pml.smt.kmi.skos.SKOSParser;
import fr.gouv.esante.pml.smt.utils.Constants;
import fr.gouv.esante.pml.smt.utils.MetadataUtil;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class SetMetadataInCatalogue {
    
  private static String cisp2owlFileName = PropertiesUtil.getProperties("CISP2owlFileName");
  private static String owlMetaDataFileName = PropertiesUtil.getProperties("owlMetaDataFileName");
  private static String ADICAPowlFileName = PropertiesUtil.getProperties("ADICAPowlFileName");
  private static String catalogueFile = PropertiesUtil.getProperties("catalogueFile");
  private static OWLOntologyManager man = null;
  private static OWLOntology onto = null;
  private static OWLOntology catalogue = null;
  private static OWLDataFactory fact = null;
  private static OWLDataFactory fact2 = null;
  
  private static OWLNamedIndividual agent_ASIP = null;
  
  private String rdfNs = Constants.rdfNs;
  private String owlNs = Constants.owlNs;
  
   public static void main(final String[] args) throws IOException, TikaException, OWLOntologyCreationException, OWLOntologyStorageException, SKOSParserException {
    
//     final InputStream in = new FileInputStream(cisp2owlFileName);
     final InputStream inputCat = new FileInputStream(catalogueFile);
     
     man = OWLManager.createOWLOntologyManager();
//     onto = man.loadOntologyFromOntologyDocument(in);
     catalogue = man.loadOntologyFromOntologyDocument(inputCat);
//     fact = onto.getOWLOntologyManager().getOWLDataFactory();
     fact2 = catalogue.getOWLOntologyManager().getOWLDataFactory();
//     OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
     
     
     SetMetadataInCatalogue cisp = new SetMetadataInCatalogue();
     cisp.parseSample(cisp2owlFileName);
     
      
   }
   
   
   
   private void parseSample(final String fileName) {

     
     try {

       

       catalogue = parse(fileName);
       
       final OutputStream fileoutputstream = new FileOutputStream(catalogueFile);
       final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
       ontologyFormat.setPrefix("dct", "http://purl.org/dc/terms/");
       
       man.saveOntology(catalogue, ontologyFormat, fileoutputstream);

       System.out.println("Number of classes converted from " + fileName + " into OWLOntology:"
           + catalogue.getAxiomCount());


     } catch (final Exception e) {
       System.out.println(e.getMessage());
     }

   }
   
   
   
   /**
    * Parses the SKOS file and creates an OWLOntology
    *
    * @param fileName
    * @return OWLOntology
    */
   public OWLOntology parse(final String fileName) throws SKOSParserException {
     int event;
     OWLOntologyBuilder ontologyBuilder = null;
     NamedIndividualConcept Concept = null;
     try {
       ontologyBuilder = new OWLOntologyBuilder(catalogue);

       final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
       final XMLStreamReader reader =
           inputFactory.createXMLStreamReader(new java.io.FileInputStream(fileName));

       while (reader.hasNext()) {
         event = reader.next();
         switch (event) {
           case XMLStreamConstants.END_DOCUMENT:
             reader.close();
             break;
           case XMLStreamConstants.START_ELEMENT:
             if (reader.getLocalName().equals("NamedIndividual")) {
              Concept = this.getOWLNamedIndividual(reader);
               
               // skosConcept is null if the description is about a conceptScheme
               if (Concept != null) {
                 ontologyBuilder.createIndividual(Concept);
               }
             }

             break;

         }

       }

     } catch (final Exception ex) {
       ex.printStackTrace();
       throw new SKOSParserException("Error parsing file:" + fileName, ex);
     }
     return ontologyBuilder.getOntology();
   }
   
   
   
   protected NamedIndividualConcept getOWLNamedIndividual(final XMLStreamReader reader) throws SKOSParserException {

     int event;
     NamedIndividualConcept concept = null;
     boolean process = true;
     try {
       if (reader.getAttributeCount() > 0) {
         concept = new NamedIndividualConcept(reader.getAttributeValue(this.rdfNs, "about"));
         while (process) {
           event = reader.next();
           switch (event) {
             case XMLStreamConstants.START_ELEMENT:
               final String localName = reader.getLocalName();
               if (localName.equals("label")) {
                 reader.next();
                 concept.addLabel(reader.getText());
                 continue;
               } else if (localName.equals("type") && (reader.getAttributeCount() > 0)
                   && (reader.getAttributeValue(this.rdfNs, "resource") != null)) {
                 concept.addType(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               }
               else if (localName.equals("alternative")) {
                 
                 concept.addAlternative(reader.getElementText());
                 continue;
               } else if (localName.equals("description")) {
                 
                 concept.addDescription(reader.getElementText());
                 continue;
               } else if (localName.equals("name") ) {
                 
                 concept.addName(reader.getElementText());
                 continue;
               } else if (localName.equals("license") ) {

                 concept.addLicense(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               } else if (localName.equals("status")) {
                 
                 concept.addStatus(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               } else if (localName.equals("accessURL")) {
                 
                 concept.addAccessURL(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               } else if (localName.equals("mediaType") ) {
                 
                 concept.addMediaType(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               } else if (localName.equals("value") ) {
                 reader.next();
                 concept.addValue(reader.getText());
                 continue;
               } else if (localName.equals("modifiedBy")) {
                 
                 concept.addModifiedBy(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               }else if (localName.equals("audience")) {
                 
                 concept.addAudience(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               }else if (localName.equals("identifier")) {
                 reader.next();
                 concept.addIdentifier(reader.getText());
                 continue;
               }else if (localName.equals("issued")) {
                 reader.next();
                 concept.addIssued(reader.getText());
                 continue;
               }else if (localName.equals("language")) {
                 
                 concept.addLanguage(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               }else if (localName.equals("publisher")) {
                 
                 concept.addPublisher(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               }else if (localName.equals("relation")) {
                 
                 concept.addRelation(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               }else if (localName.equals("title")) {
                 reader.next();
                 concept.addTitle(reader.getText());
                 continue;
               }else if (localName.equals("classNumber")) {
                 reader.next();
                 concept.addClassNumber(reader.getText());
                 continue;
               }else if (localName.equals("comment")) {
                 reader.next();
                 concept.addComment(reader.getText());
                 continue;
               }else if (localName.equals("versionInfo")) {
                 reader.next();
                 concept.addVersionInfo(reader.getText());
                 continue;
               }else if (localName.equals("historyNote")) {
                 reader.next();
                 concept.addHistoryNote(reader.getText());
                 continue;
               }else if (localName.equals("distribution")) {
                 
                 concept.addDistribution(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               }else if (localName.equals("landingPage")) {
                 
                 concept.addLandingPage(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               }else if (localName.equals("theme")) {
                 
                 concept.addTheme(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               }else if (localName.equals("complexity")) {
                 
                 concept.addComplexity(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               }else if (localName.equals("author")) {
                 
                 concept.addAuthor(reader.getAttributeValue(this.rdfNs, "resource"));
                 continue;
               }else if (localName.equals("price")) {
                 reader.next();
                 concept.addPrice(reader.getText());
                 continue;
               }
             case XMLStreamConstants.END_ELEMENT:
               if (reader.getLocalName().equals("NamedIndividual")) {
                 process = false;
                 break;
               }

           }
         }

       }

     } catch (final Exception ex) {
       throw new SKOSParserException("Error getting concept from owl:NamedIndividual:", ex);
     }

     return concept;
   }
   
   
   
   
}