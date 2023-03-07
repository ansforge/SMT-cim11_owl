package fr.gouv.esante.pml.smt.cim11;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.SetOntologyID;
import fr.gouv.esante.pml.smt.kmi.skos.SKOSChunkParser;
import fr.gouv.esante.pml.smt.kmi.skos.SKOSParser;
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

  static String OWL_FILE_NAME_TMP = "CGTS_SEM_ICD11-MM-R202301_TMP.owl";
  static final String SKOS_FILE_NAME = "CIM11-MMS-SKOS-FILE_NAME.xml";
  static final String OWL_FILE_NAME = "CGTS_SEM_ICD11-MMS_OWL.owl";
  public static HashMap<String, String> idICD11 = new HashMap<String, String>();
  private static OWLDataFactory fact = null;

  public static void main(final String[] args) throws Exception {
    final SKOSToOWL sKOSToOWL = new SKOSToOWL();
    OWLOntology o = sKOSToOWL.parseSample(SKOS_FILE_NAME);

    final InputStream input = new FileInputStream(SKOS_FILE_NAME);
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    fact = o.getOWLOntologyManager().getOWLDataFactory();
    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input);

    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION))
    .forEach(ax -> {
    	manager.applyChange(new AddAxiom(o, ax));
    });
    
    o.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION))
    .filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#broader>") )
    .forEach(ax -> {
    	if(!((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("Modifier")) {
	    	final OWLSubClassOfAxiom ax1 =
	        fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), fact.getOWLClass(IRI.create(((OWLAnnotationAssertionAxiom) ax).getValue().toString())));
	    	manager.applyChange(new AddAxiom(o, ax1));
    	}
    });

    final OutputStream fileoutputstream = new FileOutputStream(OWL_FILE_NAME_TMP);
    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
    IRI icd11IRI = IRI.create("http://id.who.int/icd/release/11");
    manager.applyChange(new SetOntologyID(o, new OWLOntologyID(icd11IRI)));
    manager.saveOntology(o, ontologyFormat, fileoutputstream);
    xmlValid();
    
    //A.R
    input.close();
  }
  
  
  public static void xmlValid() throws IOException {
	  final OutputStream fileoutputstream = new FileOutputStream(OWL_FILE_NAME);
	  OutputStreamWriter osw = new OutputStreamWriter(fileoutputstream, StandardCharsets.UTF_8);
	  FileInputStream fstream = new FileInputStream(OWL_FILE_NAME_TMP);
	  BufferedReader br = new BufferedReader(new InputStreamReader(fstream, StandardCharsets.UTF_8));
	  String strLine;
	  //Read File Line By Line
	  while ((strLine = br.readLine()) != null)   {
	    // Print the content on the console
		  String validLine = strLine.replace("quot ;", "quot;");
		  osw.write(validLine + "\n");
	  }
	  //Close the input stream
	  fstream.close();
	  osw.close();
	  
	}

  private OWLOntology parseSample(final String fileName) {
    OWLOntology o = null;
    SKOSParser skosParser = null;
    try {
      // library samples contain rdf:type tag which describes the entity
      if (fileName.indexOf("library") != -1) {
        skosParser = new SKOSParser(Constants.nl);
      } else {
        skosParser = new SKOSParser();
      }
      o = skosParser.parse(fileName);
      return o;
    } catch (final Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  @SuppressWarnings("unused")
  private void parseSampleInChunks(final String fileName) {

    OWLOntology o = null;
    SKOSChunkParser skosChunkParser = null;
    int i = 1;
    try {
      if (fileName.indexOf("library") != -1) {
        skosChunkParser = new SKOSChunkParser(fileName, Constants.nl);
      } else {
        skosChunkParser = new SKOSChunkParser(fileName);
      }
      System.out
          .println("Total number of concepts in SKOS file:" + skosChunkParser.getConceptNumber());
      skosChunkParser.setChunkSize(5);
      while (skosChunkParser.hasNextChunk()) {
        o = skosChunkParser.getNextChunk();
        System.out.println("Iteration number:" + i + " Number of classes converter from " + fileName
            + " into OWLOntology chunk:" + o.getAxiomCount());
        i++;
      }

    } catch (final Exception e) {
      System.out.println(e.getMessage());
    }
  }
  public void printUsage() {
    System.out.println("Parameter should be an skos file");
  }

}
