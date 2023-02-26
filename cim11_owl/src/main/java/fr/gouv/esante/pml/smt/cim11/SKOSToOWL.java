package fr.gouv.esante.pml.smt.cim11;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.SetOntologyID;
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

  public static HashMap<String, String> idICD11 = new HashMap<String, String>();
  private static OWLDataFactory fact = null;
//  private static String mappingFileName = PropertiesUtil.getProperties("mappingFileName");
//  private static String traductionFileName = PropertiesUtil.getProperties("traductionFileName");
  private static String skosFileName = PropertiesUtil.getProperties("skosFileName");
  private static String owlFileName = PropertiesUtil.getProperties("owlFileName");
  private static String owlFileNameTmp = PropertiesUtil.getProperties("owlFileNameTmp");

  public static void main(final String[] args) throws Exception {

//     ChargeMapping.chargeXLXSFileToMaps(mappingFileName);
    // ChargeMapping.chargeCSVTraductionFileToList(traductionFileName);
    // ChargeMapping.chargeCSVTraductionFileToList2(traductionFileName);
    final SKOSToOWL sKOSToOWL = new SKOSToOWL();
    OWLOntology o = sKOSToOWL.parseSample(skosFileName);

    final InputStream input = new FileInputStream(skosFileName);
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    fact = o.getOWLOntologyManager().getOWLDataFactory();
    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input);

    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION))
    .forEach(ax -> {
    	manager.applyChange(new AddAxiom(o, ax));
    });
    
//    manager.addAxioms(o, onto.axioms());

//    o.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION))
//        .filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>") || 
//                      ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>") )
//        .forEach(ax -> {
//          System.out.println(((OWLAnnotationAssertionAxiom) ax).getSubject() + "---"
//              + ((OWLAnnotationAssertionAxiom) ax).getProperty() + "---"
//              + ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1,
//                  ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length() - 1)
//                  .replaceAll("\\\\", "").replaceAll("\"", ""));
//          String cc = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length() - 1);
//          OWLAnnotation labelAnno = null;
//          if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>")) {
//            labelAnno = fact.getOWLAnnotation(fact.getRDFSComment(), fact.getOWLLiteral(cc, "en"));
//          }else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>")) {
//            labelAnno = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(cc, "en"));
//          }
//          final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), labelAnno);
//          manager.applyChange(new AddAxiom(o, ax1));
//          manager.applyChange(new RemoveAxiom(o, ax));
//
//        });
    
    o.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION))
    .filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#broader>") )
    .forEach(ax -> {
    	if(!((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("Modifier")) {
	    	final OWLSubClassOfAxiom ax1 =
	        fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), fact.getOWLClass(IRI.create(((OWLAnnotationAssertionAxiom) ax).getValue().toString())));
	    	manager.applyChange(new AddAxiom(o, ax1));
    	}
    });

    final OutputStream fileoutputstream = new FileOutputStream(owlFileNameTmp);
    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
    // List<String> labelNonTraduits = new ArrayList<String>();
    // fact = o.getOWLOntologyManager().getOWLDataFactory();
    // o.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    // filter(ax -> ((OWLAnnotationAssertionAxiom)
    // ax).getProperty().toString().equals("rdfs:label")).
    // filter(ax -> !((OWLAnnotationAssertionAxiom)
    // ax).getSubject().toString().contains("http://id.who.int/icd/entity")).
    // forEach(ax ->
    // {
    //// System.out.println(((OWLAnnotationAssertionAxiom)
    // ax).getSubject()+"---"+((OWLAnnotationAssertionAxiom)
    // ax).getProperty()+"---"+((OWLAnnotationAssertionAxiom)
    // ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom)
    // ax).getValue().toString().split("@")[0].length()-1));
    // if(listTraduction2.containsKey(((OWLAnnotationAssertionAxiom)
    // ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom)
    // ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]",
    // ""))) {
    // final OWLAnnotation labelAnno =
    // fact.getOWLAnnotation(fact.getRDFSLabel(),fact.getOWLLiteral(listTraduction2.get(((OWLAnnotationAssertionAxiom)
    // ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom)
    // ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]",
    // "")), "fr"));
    // final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom)
    // ax).getSubject(), labelAnno);
    // manager.applyChange(new AddAxiom(o, ax1));
    // } else {
    // labelNonTraduits.add(((OWLAnnotationAssertionAxiom)
    // ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom)
    // ax).getValue().toString().split("@")[0].length()-1));
    // }
    // });
    //
    // FileWriter csvWriter = new
    // FileWriter("C:\\Users\\agochath\\Documents\\cim11\\labelNonTraduits2.csv");
    //
    // for (String rowData : labelNonTraduits) {
    // csvWriter.append(rowData);
    // csvWriter.append("\n");
    // }
    //
    // csvWriter.flush();
    // csvWriter.close();
    IRI icd11IRI = IRI.create("http://id.who.int/icd/release/11");
    manager.applyChange(new SetOntologyID(o, new OWLOntologyID(icd11IRI)));
    manager.saveOntology(o, ontologyFormat, fileoutputstream);
    // for(String key : listTraduction2.keySet()) {
    // System.out.println(key +"---"+listTraduction2.get(key));
    // }
    xmlValid();
    
   
  }
  
  
  public static void xmlValid() throws IOException {
	  final OutputStream fileoutputstream = new FileOutputStream(owlFileName);
	  OutputStreamWriter osw = new OutputStreamWriter(fileoutputstream, StandardCharsets.UTF_8);
	  
	  FileInputStream fstream = new FileInputStream(owlFileNameTmp);
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
