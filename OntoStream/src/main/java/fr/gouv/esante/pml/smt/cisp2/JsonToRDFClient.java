package fr.gouv.esante.pml.smt.cisp2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.SysRIOT;
import org.apache.jena.sparql.util.Context;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class JsonToRDFClient {

  private static String jsonFileName = PropertiesUtil.getProperties("jsonFileName");
  private static String skosFileName = PropertiesUtil.getProperties("skosFileName");

  public static void main(final String[] args) throws IOException, OWLException {

    final Map<String, Object> properties = new HashMap<String, Object>();
    properties.put("WARN_BAD_NAME", "EM_IGNORE");

    // Put a properties object into the Context.
    final Context cxt = new Context();
    cxt.set(SysRIOT.sysRdfReaderProperties, properties);
    final InputStream in = new FileInputStream(jsonFileName);
    final OntModel m = ModelFactory.createOntologyModel();
    // Build and run a parser
    RDFParser.create().lang(Lang.JSONLD).source(in).context(cxt).parse(m);
    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    m.write(out, "RDF/JSON");

    final InputStream input = new ByteArrayInputStream(out.toByteArray());
    final OutputStream fileoutputstream = new FileOutputStream(skosFileName);
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    final OWLOntologyLoaderConfiguration config = manager.getOntologyLoaderConfiguration();
    manager.setOntologyLoaderConfiguration(
        config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
    final OWLOntology ontology = manager.loadOntologyFromOntologyDocument(input);

    System.out.println("Ontology Loaded...");
    System.out.println("Document IRI: " + ontology.getOntologyID());
    System.out.println("Format      : " + manager.getOntologyFormat(ontology));

    System.out.println("Saving: ");

    manager.saveOntology(ontology, new RDFXMLDocumentFormat(), fileoutputstream);

  }

}
