package fr.gouv.esante.pml.smt.cim10;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.Constants;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

/**
 * 
 *
 */
public class SKOSToOWL {
  public SKOSToOWL() {
  }

  //public static HashMap<String, String> idICD11 = new HashMap<String, String>();
  public static HashMap<String, String> listTraduction = new HashMap<String, String>();

  //private static String mappingFileName = PropertiesUtil.getProperties("mappingFileName");
  private static String cim10traductionFileName = PropertiesUtil.getProperties("cim10traductionFileName");
  private static String CIM10skosFileName = PropertiesUtil.getProperties("CIM10skosFileName");
  private static String CIM10owlFileName = PropertiesUtil.getProperties("CIM10owlFileName");

  public static void main(final String[] args) throws Exception {

    //ChargeMapping.chargeXLXSFileToMaps(mappingFileName);
//    ChargeMapping.chargeXLXScim10TraductionFileToList(cim10traductionFileName);
    final SKOSToOWL sKOSToOWL = new SKOSToOWL();
    sKOSToOWL.parseSample(CIM10skosFileName);


  }

  private void parseSample(final String fileName) {

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
      final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
      final OutputStream fileoutputstream = new FileOutputStream(CIM10owlFileName);
      final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
      ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
      manager.saveOntology(o, ontologyFormat, fileoutputstream);

      System.out.println("Number of classes converted from " + fileName + " into OWLOntology:"
          + o.getAxiomCount());


    } catch (final Exception e) {
      System.out.println(e.getMessage());
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
