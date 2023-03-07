package fr.gouv.esante.pml.smt.cim11;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RDFWriter;
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

    private static String JSON_FILE_NAME = "CIM11_JSON_FILE_NAME.json";
    private static String SKOS_FILE_NAME = "CIM11-MMS-SKOS-FILE_NAME.xml";
    private static String SKOS_FILE_NAME_TMP = "SKOS_FILE_NAME_TMP.xml";

    public static void main(final String[] args) throws IOException, OWLException {

        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("WARN_BAD_NAME", "EM_IGNORE");

        // Put a properties object into the Context.
        final Context cxt = new Context();
        cxt.set(SysRIOT.sysRdfReaderProperties, properties);
        final InputStream in = new FileInputStream(JSON_FILE_NAME);
        final OntModel m = ModelFactory.createOntologyModel();
        // Build and run a parser
//    RDFDataMgr.read(m, in, Lang.JSONLD);
        System.out.println("parsing...");
        RDFParser.create().lang(Lang.JSONLD).source(in).context(cxt).parse(m);
        System.out.println("parsed.");
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final OutputStream fileoutputstream = new FileOutputStream(SKOS_FILE_NAME_TMP);
        System.out.println("writting...");
        m.write(out, "RDFJSON");
        RDFWriter.create().source(m).lang(Lang.RDFJSON).output(out);
        System.out.println("writed.");


        final InputStream input = new ByteArrayInputStream(out.toByteArray());
        final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        final OWLOntologyLoaderConfiguration config = manager.getOntologyLoaderConfiguration();
        manager.setOntologyLoaderConfiguration(
                config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
        final OWLOntology ontology = manager.loadOntologyFromOntologyDocument(input);

        System.out.println("Ontology Loaded...");
        System.out.println("Document IRI: " + ontology.getOntologyID());
        System.out.println("Format      : " + manager.getOntologyFormat(ontology));

        System.out.println("Saving: ");
        final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
        ontologyFormat.setPrefix("icd", "https://id.who.int/icd/schema/");
        manager.saveOntology(ontology, ontologyFormat, fileoutputstream);

        xmlValid();

        //A.R
        in.close();

    }


    public static void xmlValid() throws IOException {
        final OutputStream fileoutputstream = new FileOutputStream(SKOS_FILE_NAME);
        OutputStreamWriter osw = new OutputStreamWriter(fileoutputstream, StandardCharsets.UTF_8);

        FileInputStream fstream = new FileInputStream(SKOS_FILE_NAME_TMP);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream, StandardCharsets.UTF_8));

        String strLine;

        //Read File Line By Line
        while ((strLine = br.readLine()) != null)   {
            // Print the content on the console
            String validLine = stripNonValidXMLCharacters(strLine);
            osw.write(validLine + "\n");
        }

        //Close the input stream
        fstream.close();
        osw.close();

//
//	  final OutputStream fileoutputstream = new FileOutputStream(skosFileName);
//	  OutputStreamWriter osw = new OutputStreamWriter(fileoutputstream);
//		try (java.util.stream.Stream<String> lines = Files.lines(Paths.get(firstSkosFileName), Charset.defaultCharset())) {
//			  lines.forEachOrdered(line -> {
//				  try {
//					String validLine = line.replace("0x9d", "");
//					osw.write(validLine);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			  });
//			}
//		osw.close();
    }

    public static String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.

        if (in == null || ("".equals(in))) return ""; // vacancy test.
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == 0x9) ||
                    (current == 0xA) ||
                    (current == 0xD) ||
                    ((current >= 0x20) && (current <= 0xD7FF)) ||
                    ((current >= 0xE000) && (current <= 0xFFFD)) ||
                    ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }

}