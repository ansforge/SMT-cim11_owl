package fr.gouv.esante.pml.smt.cisp2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFJsonLDDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import fr.gouv.esante.pml.smt.kmi.skos.SKOSChunkParser;
import fr.gouv.esante.pml.smt.kmi.skos.SKOSParser;
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.Constants;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

/**
 * Test the SKOS parsers. The jar dependencies are as follows:
 *
 * - jsr173_1.0_api.jar - jsr173_1.0_ri.jar - stax-utils.jar
 *
 */
public class SKOSToOWL {
  public SKOSToOWL() {
  }

  
  public static void main(final String[] args) throws Exception {

    final Map<String, Object> properties = new HashMap<String, Object>();
    properties.put("WARN_BAD_NAME", "EM_IGNORE");
 // Put a properties object into the Context.
    final Context cxt = new Context();
    cxt.set(SysRIOT.sysRdfReaderProperties, properties);
    final InputStream in = new FileInputStream("C:\\Users\\agochath\\Documents\\cisp2\\CGTS_SEM_CISP.jsonld");
    final OntModel m = ModelFactory.createOntologyModel();
    // Build and run a parser
    RDFParser.create().lang(Lang.JSONLD).source(in).context(cxt).parse(m);
    
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    m.write(out, "RDF/JSON");
    in.close();
    final InputStream input = new ByteArrayInputStream(out.toByteArray());
    final OutputStream fileoutputstream = new FileOutputStream("C:\\Users\\agochath\\Documents\\cisp2\\CGTS_SEM_CISP.rdf");
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
