package fr.gouv.esante.pml.smt.cim10;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import org.semanticweb.owlapi.model.OWLOntology;
import fr.gouv.esante.pml.smt.kmi.exceptions.SKOSParserException;
import fr.gouv.esante.pml.smt.cim10.OWLOntologyBuilder;
import fr.gouv.esante.pml.smt.utils.Constants;

/**
 * SKOSChunkParser parses the SKOS file in chunks. It allows the similarity algorithm to iterate
 * through the SKOS file without building a large ontology in the memory.
 */
public class SKOSChunkParser extends SKOSParser {
  private XMLInputFactory inputFactory;
  private OWLOntologyBuilder ontologyBuilder;
  private String skosFileName;
  private int conceptNumber;
  private int currentPosition;
  private int chunkSize;
  private String ontoURI;

  /**
   * Constructor of the parser
   *
   * @param fileName SKOS file name
   * @throws SKOSParserException
   */
  public SKOSChunkParser(final String fileName) throws SKOSParserException {
    super();
    try {
      this.init(fileName);
    } catch (final Exception ex) {
      throw new SKOSParserException(
          "Error creating SKOSChunkParser for file:" + this.getSkosFileName(), ex);
    }
  }

  /**
   * Constructor of the parser
   *
   * @param fileName SKOS file name
   * @param language xml:lang tags that will be loaded into the ontology
   * @throws SKOSParserException
   */
  public SKOSChunkParser(final String fileName, final String language) throws SKOSParserException {
    super(language);
    try {
      this.init(fileName);

    } catch (final Exception ex) {
      throw new SKOSParserException(
          "Error creating SKOSChunkParser for file:" + this.getSkosFileName(), ex);
    }
  }


  private void init(final String fileName) throws SKOSParserException {
    try {
      this.inputFactory = XMLInputFactory.newInstance();
      this.skosFileName = fileName;
      this.conceptNumber = this.parseFile();
      this.setOntoURI(this.getBaseURI(fileName));
      this.setCurrentPosition(0);
      this.setChunkSize(50);
    } catch (final Exception ex) {
      throw new SKOSParserException(
          "Error initialising SKOSChunkParser for file:" + this.getSkosFileName(), ex);
    }
  }

  /**
   * Determine if there are more chunks that needs to be loaded from the SKOS file
   *
   * @return true if there are more chunks, false otherwise
   */
  public boolean hasNextChunk() {
    boolean ret = false;
    if (this.getCurrentPosition() < this.getConceptNumber()) {
      ret = true;
    }
    return ret;
  }

  /**
   * Get a chunk from the SKOS file and return the OWLOntology representation.
   *
   * @return OWLOntology
   */
  public OWLOntology getNextChunk() throws SKOSParserException {
    OWLOntology ret = null;
    SKOSConcept skosConcept = null;
    int event;
    int startPosition;
    int endPosition;
    int conceptCount;// counts the concepts position in the source file
    boolean conceptFound;

    try {

      final XMLStreamReader reader = this.inputFactory
          .createXMLStreamReader(new java.io.FileInputStream(this.getSkosFileName()));
      this.ontologyBuilder = new OWLOntologyBuilder(this.getOntoURI());
      startPosition = this.getCurrentPosition();
      endPosition = startPosition + this.getChunkSize();
      conceptCount = 0;
      while (reader.hasNext() && (this.getCurrentPosition() < endPosition)) {
        event = reader.next();
        switch (event) {
          case XMLStreamConstants.START_ELEMENT:
//            if (reader.getLocalName().equals("Concept")) {
//              if ((conceptCount >= startPosition) && (conceptCount <= endPosition)) {
//                this.ontologyBuilder.createClass(this.getConcept(reader));
//                this.increaseCurrentPosition();
//              }
//              conceptCount++;
//            } else
              
              if (reader.getLocalName().equals("Description")) {
              
                conceptFound = true;
                if ((conceptCount >= startPosition) && (conceptCount <= endPosition)) {
                  skosConcept = this.getRDFDescription(reader);
                  this.ontologyBuilder.createClass(skosConcept);
                  this.increaseCurrentPosition();
                }
              

              if (conceptFound) {
                conceptCount++;
              }
            }
            break;
        }

      }

      reader.close();
      ret = this.ontologyBuilder.getOntology();
    } catch (final Exception ex) {
      throw new SKOSParserException("Error parsing file:" + this.getSkosFileName(), ex);
    }
    return ret;



  }

  /**
   * Set how many concepts should be added into the OWLOntology in one iteration.
   *
   * @param chunkSize
   */
  public void setChunkSize(final int chunkSize) {
    this.chunkSize = chunkSize;
  }

  private int getChunkSize() {
    return this.chunkSize;
  }

  /**
   * Parse the SKOS file and determine the number of Concepts in it.
   *
   * @return number of Concepts
   */
  private int parseFile() throws SKOSParserException {
    int event;
    int conceptNumber = 0;
    SKOSConcept skosConcept = null;
    try {

      final XMLStreamReader reader = this.inputFactory
          .createXMLStreamReader(new java.io.FileInputStream(this.getSkosFileName()));

      while (reader.hasNext()) {
        event = reader.next();
        switch (event) {
          case XMLStreamConstants.END_DOCUMENT:
            reader.close();
            break;
          case XMLStreamConstants.START_ELEMENT:
            if (reader.getLocalName().equals("Concept")) {
              conceptNumber++;
            } else if (reader.getLocalName().equals("Description")) {
              
                conceptNumber++;
            
            }

            break;

        }

      }
      reader.close();
    } catch (final Exception ex) {
      throw new SKOSParserException("Error parsing file:" + this.getSkosFileName(), ex);
    }
    return conceptNumber;

  }



  private String getSkosFileName() {
    return this.skosFileName;
  }


  private void setCurrentPosition(final int currentPosition) {
    this.currentPosition = currentPosition;
  }

  private int getCurrentPosition() {
    return this.currentPosition;
  }


  private void setOntoURI(final String ontoURI) {
    this.ontoURI = ontoURI;
  }

  private String getOntoURI() {
    return this.ontoURI;
  }

  private void increaseCurrentPosition() {
    this.currentPosition++;
  }

  public int getConceptNumber() {
    return this.conceptNumber;
  }
}
