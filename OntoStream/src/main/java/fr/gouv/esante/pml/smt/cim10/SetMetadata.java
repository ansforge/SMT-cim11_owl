package fr.gouv.esante.pml.smt.cim10;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.SysRIOT;
import org.apache.jena.sparql.util.Context;
import org.apache.tika.exception.TikaException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.xml.sax.SAXException;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class SetMetadata {
  
  private static String CIM10owlFileName = PropertiesUtil.getProperties("CIM10owlFileName");
  private static String CIM10owlMetaDataFileName = PropertiesUtil.getProperties("CIM10owlMetaDataFileName");
  private static final Model m_model = ModelFactory.createDefaultModel();
  final static String path = "./metadata10.properties";

   public static void main(final String[] args) throws IOException,SAXException, TikaException, ParserConfigurationException, OWLOntologyCreationException, OWLOntologyStorageException {
   
     System.out.println("Metadata Loaded...");
     
     final Map<String, String> metadata = new HashMap<String, String>();
     try (InputStream input = SetMetadata.class.getClassLoader().getResourceAsStream(path)) {

       Properties prop = new Properties();

       if (input == null) {
           System.out.println("Sorry, unable to find " + path);
           System.exit (0);
       }

       prop.load(input);

       // Java 8 , print key and values
       prop.forEach((key, value) -> metadata.put((String) key, (String) value));
       
       if (metadata.size() < 1) {
         System.out.println("Sorry, no metadata to add in " + path);
         System.exit (0);
     }

   } catch (IOException ex) {
       ex.printStackTrace();
   }
     
     
     
     final Map<String, Object> properties = new HashMap<String, Object>();
     properties.put("WARN_BAD_NAME", "EM_IGNORE");
  // Put a properties object into the Context.
     final Context cxt = new Context();
     cxt.set(SysRIOT.sysRdfReaderProperties, properties);
     final InputStream in = new FileInputStream(CIM10owlFileName);
     final OntModel m = ModelFactory.createOntologyModel();
     // Build and run a parser
     RDFParser.create().lang(Lang.RDFXML).source(in).context(cxt).parse(m);
     
     Ontology ont = m.getOntology("http://www.w3.org/2002/07/owl");
     metadata.forEach((key, value) -> ont.addProperty(m_model.createProperty( "http://purl.org/dc/terms/"+key ) , value ));
//     ont.addProperty( DCTerms.creator, "ASIP Santé" );
     final ByteArrayOutputStream out = new ByteArrayOutputStream();
     m.write(out, "RDF/JSON");
     in.close();
     final InputStream input = new ByteArrayInputStream(out.toByteArray());
     final OutputStream fileoutputstream = new FileOutputStream(CIM10owlMetaDataFileName);
     final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
     final OWLOntologyLoaderConfiguration config = manager.getOntologyLoaderConfiguration();
     manager.setOntologyLoaderConfiguration(
         config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
     final OWLOntology ontology = manager.loadOntologyFromOntologyDocument(input);

     
     System.out.println("Metadata Saving... ");

     manager.saveOntology(ontology, new RDFXMLDocumentFormat(), fileoutputstream);
     fileoutputstream.close();
     System.out.println("Done.");
   }
}   