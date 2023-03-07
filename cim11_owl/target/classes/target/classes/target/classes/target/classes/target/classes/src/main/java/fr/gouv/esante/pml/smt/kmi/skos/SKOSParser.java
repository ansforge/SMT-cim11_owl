package fr.gouv.esante.pml.smt.kmi.skos;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import org.semanticweb.owlapi.model.OWLOntology;
import fr.gouv.esante.pml.smt.kmi.exceptions.SKOSParserException;
import fr.gouv.esante.pml.smt.kmi.owl.OWLOntologyBuilder;
import fr.gouv.esante.pml.smt.utils.Constants;

/**
 * SKOSParser is a basic parser for the SKOS. It parses the whole SKOS into one OWLOntology
 */
public class SKOSParser {

  private String xmlNs = null;
  private String rdfNs = null;
  protected String language = null;

  /**
   * Constructor of the parser
   */
  public SKOSParser() {
    this.xmlNs = Constants.xmlNs;
    this.rdfNs = Constants.rdfNs;
    this.language = Constants.en;
  }

  /**
   * Constructor of the parser
   *
   * @param language xml:lang tags that will be loaded into the ontology
   */
  public SKOSParser(final String language) {
    this.xmlNs = Constants.xmlNs;
    this.rdfNs = Constants.rdfNs;
    this.language = language;
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
    String ontoURI; //= "http://id.who.int/icd/release/10/atih/";
    SKOSConcept skosConcept = null;
    try {
      ontoURI = this.getBaseURI(fileName);
      ontologyBuilder = new OWLOntologyBuilder(ontoURI);

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
            if (reader.getLocalName().equals("Concept")) {
              ontologyBuilder.createClass(this.getConcept(reader));
            } else if (reader.getLocalName().equals("Description")) {
              if (this.language.equals(Constants.nl)) {
                skosConcept = this.getDutchRDFDescription(reader);
              } else {
                skosConcept = this.getRDFDescription(reader);
              }
              // skosConcept is null if the description is about a conceptScheme
              if (skosConcept != null) {
                ontologyBuilder.createClass(skosConcept);
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

  /**
   * Get the base URI from the SKOS. e.g. http://agclass.nal.usda.gov/nalt/2007.xml GEMET
   * http://www.eionet.europa.eu/gemet/concept/8 GTT http://stitch.cs.vu.nl/gtt#gtt_145844706
   *
   * @param fileName SKOS filename
   * @return String
   */
  protected String getBaseURI(final String fileName) throws SKOSParserException {
    int event;
    boolean hasFoundOntoURI = false;
    String ret = null;
    int count = 0;
    try {

      final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      final XMLStreamReader reader =
          inputFactory.createXMLStreamReader(new java.io.FileInputStream(fileName));

      while (reader.hasNext() && (count < 2)) {
        event = reader.next();
        switch (event) {
          case XMLStreamConstants.START_ELEMENT:
            if (reader.getLocalName().equals("RDF")) {
              ret = reader.getAttributeValue(this.xmlNs, "base");
              if (ret != null) {
                hasFoundOntoURI = true;
                count = 2;
              }
            } else if (reader.getLocalName().equals("Concept") && !hasFoundOntoURI) {
              ret = reader.getAttributeValue(this.rdfNs, "about");
              ret = ret.substring(0, ret.indexOf("#"));
              if (ret.length() != 0) {
                count++;
                hasFoundOntoURI = true;
              }

            } else if (reader.getLocalName().equals("Description") && !hasFoundOntoURI) {
              ret = reader.getAttributeValue(this.rdfNs, "about");
              if (ret.indexOf("#") != -1) {
                ret = ret.substring(0, ret.indexOf("#"));
              } else {
                ret = ret.substring(0, ret.lastIndexOf("/"));
              }
              count = 2;
            }

            break;

        }

      }
      reader.close();

    } catch (final Exception ex) {
      throw new SKOSParserException("Error getting URI for file:" + fileName, ex);

    }
    return ret;
  }

  /**
   * Get the whole Concept tag from the SKOS file
   *
   * @param reader XMLStreamReader
   * @return SKOSConcept
   */
  protected SKOSConcept getConcept(final XMLStreamReader reader) throws SKOSParserException {

    int event;
    SKOSConcept concept = null;
    boolean process = true;
    try {
      if (reader.getAttributeCount() > 0) {
        concept = new SKOSConcept(reader.getAttributeValue(this.rdfNs, "about"));
        while (process) {
          event = reader.next();
          switch (event) {
            case XMLStreamConstants.START_ELEMENT:
              if (reader.getLocalName().equals("prefLabel") && (reader.getAttributeCount() > 0)
                  && reader.getAttributeValue(this.xmlNs, "lang").equals(this.language)) {
                reader.next();
                concept.addPrefLabel(reader.getText());
                continue;
              } else if (reader.getLocalName().equals("altLabel")
                  && (reader.getAttributeCount() > 0)
                  && reader.getAttributeValue(this.xmlNs, "lang").equals(this.language)) {
                reader.next();
                concept.addAltLabel(reader.getText());
                continue;
              } else if (reader.getLocalName().equals("broaderTransitive")) {

                concept.addBroaderTransitive(reader.getText());
                continue;
              } else if (reader.getLocalName().equals("narrowerTransitive")) {

                concept.addNarrowerTransitive(reader.getText());
                continue;
              } else if (reader.getLocalName().equals("related") && (reader.getAttributeCount() > 0)
                  && (reader.getAttributeValue(this.rdfNs, "resource") != null)) {

                concept.addRelated(reader.getAttributeValue(this.rdfNs, "resource"));
                continue;
              } else if (reader.getLocalName().equals("definition")
                  && (reader.getAttributeCount() > 0)
                  && (reader.getAttributeValue(this.rdfNs, "resource") != null)) {

                concept.addDefinition(reader.getAttributeValue(this.rdfNs, "resource"));
                continue;
              } else if (reader.getLocalName().equals("definition")
                  && (reader.getAttributeCount() > 0)
                  && reader.getAttributeValue(this.xmlNs, "lang").equals(this.language)) {
                reader.next();
                concept.addDefinition(reader.getText());
                continue;
              } else if (reader.getLocalName().equals("scopeNote")
                  && (reader.getAttributeCount() > 0)
                  && reader.getAttributeValue(this.xmlNs, "lang").equals(this.language)) {
                reader.next();
                concept.addScopeNote(reader.getText());
                continue;
              }
            case XMLStreamConstants.END_ELEMENT:
              if (reader.getLocalName().equals("Concept")) {
                process = false;
                break;
              }

          }
        }

      }

    } catch (final Exception ex) {
      throw new SKOSParserException("Error getting concept from SKOS:", ex);
    }

    return concept;
  }

  /**
   * Get the whole Description tag from the RDF file. The labels for the library skos are in Dutch.
   *
   * @param reader XMLStreamReader
   * @return SKOSConcept or noll it it is not a concept tag.
   * @throws SKOSParserException
   */
  protected SKOSConcept getDutchRDFDescription(final XMLStreamReader reader)
      throws SKOSParserException {

    int event;
    SKOSConcept concept = null;
    boolean process = true;
    boolean isConcept = true;
    final String skosConcept = Constants.conceptURI;
    try {
      if (reader.getAttributeCount() > 0) {
        concept = new SKOSConcept(reader.getAttributeValue(this.rdfNs, "about"));
        while (process) {
          event = reader.next();
          switch (event) {
            case XMLStreamConstants.START_ELEMENT:
              if (reader.getLocalName().equals("type") && (reader.getAttributeCount() > 0)
                  && !reader.getAttributeValue(this.rdfNs, "resource").equals(skosConcept)) {
                isConcept = false;
                continue;
              } else if (reader.getLocalName().equals("prefLabel")
                  && (reader.getAttributeCount() > 0)
                  && reader.getAttributeValue(this.xmlNs, "lang").equals(this.language)) {
                reader.next();
                concept.addPrefLabel(reader.getText());
                continue;
              } else if (reader.getLocalName().equals("altLabel")
                  && (reader.getAttributeCount() > 0)
                  && reader.getAttributeValue(this.xmlNs, "lang").equals(this.language)) {
                reader.next();
                concept.addAltLabel(reader.getText());
                continue;
              } else if (reader.getLocalName().equals("broaderTransitive")
                  && (reader.getAttributeCount() > 0)
                  && (reader.getAttributeValue(this.rdfNs, "resource") != null)) {

                concept.addBroaderTransitive(reader.getAttributeValue(this.rdfNs, "resource"));
                continue;
              } else if (reader.getLocalName().equals("narrowerTransitive")
                  && (reader.getAttributeCount() > 0)
                  && (reader.getAttributeValue(this.rdfNs, "resource") != null)) {

                concept.addNarrowerTransitive(reader.getAttributeValue(this.rdfNs, "resource"));
                continue;
              } else if (reader.getLocalName().equals("related") && (reader.getAttributeCount() > 0)
                  && (reader.getAttributeValue(this.rdfNs, "resource") != null)) {

                concept.addRelated(reader.getAttributeValue(this.rdfNs, "resource"));
                continue;
              } else if (reader.getLocalName().equals("definition")
                  && (reader.getAttributeCount() > 0)
                  && (reader.getAttributeValue(this.rdfNs, "resource") != null)) {

                concept.addDefinition(reader.getAttributeValue(this.rdfNs, "resource"));
                continue;
              } else if (reader.getLocalName().equals("definition")
                  && (reader.getAttributeCount() > 0)
                  && reader.getAttributeValue(this.xmlNs, "lang").equals(this.language)) {
                reader.next();
                concept.addDefinition(reader.getText());
                continue;
              } else if (reader.getLocalName().equals("scopeNote")
                  && (reader.getAttributeCount() > 0)
                  && reader.getAttributeValue(this.xmlNs, "lang").equals(this.language)) {
                reader.next();
                concept.addScopeNote(reader.getText());
                continue;
              }
            case XMLStreamConstants.END_ELEMENT:
              if (reader.getLocalName().equals("Description")) {
                process = false;
                break;
              }

          }
        }

      }

    } catch (final Exception ex) {
      throw new SKOSParserException("Error getting concept from rdf:Description:", ex);
    }
    if (!isConcept) {
      concept = null;
    }

    return concept;
  }

  /**
   * Get the whole Description tag from the RDF file. The labels for the environment skos are
   * dependent on the language version of the file.
   *
   * @param reader XMLStreamReader
   * @return SKOSConcept
   * @throws SKOSParserException
   */
  protected SKOSConcept getRDFDescription(final XMLStreamReader reader) throws SKOSParserException {

    int event;
    SKOSConcept concept = null;
    boolean process = true;

    try {
      if (reader.getAttributeCount() > 0 && null != reader.getAttributeValue(this.rdfNs, "about")) {
        concept = new SKOSConcept(reader.getAttributeValue(this.rdfNs, "about"));
        Integer count = 0;
        String localName = "";
        while (process) {
          event = reader.next();
          
          
          switch (event) {
            case XMLStreamConstants.START_ELEMENT:
              localName = reader.getLocalName();
              if(localName.equals("Description")) {
                count++;
                continue;
              }
//              if (localName.equals("prefLabel")) {
//                reader.next();
//                concept.addPrefLabel(reader.getText());
//                continue;
//              } 
//              else if (localName.equals("label")) {
//                reader.next();
//                concept.addLabel(reader.getText());
//                continue;
//              } else if (localName.equals("altLabel")) {
//                reader.next();
//                concept.addAltLabel(reader.getText());
//                continue;
//              } 
//              if (localName.equals("broaderTransitive")) {
//
//                concept.addBroaderTransitive(reader.getElementText());
//                continue;
//              } 
//              else if (localName.equals("narrowerTransitive")) {
//
//                concept.addNarrowerTransitive(reader.getElementText());
//                continue;
//              }
//              else if (localName.equals("related") && (reader.getAttributeCount() > 0)
//                  && (reader.getAttributeValue(this.rdfNs, "resource") != null)) {
//
//                concept.addRelated(reader.getAttributeValue(this.rdfNs, "resource"));
//                continue;
//              } 
//              else if (localName.equals("definition") && (reader.getAttributeCount() > 0)
//                  && (reader.getAttributeValue(this.rdfNs, "resource") != null)) {
//
//                concept.addDefinition(reader.getAttributeValue(this.rdfNs, "resource"));
//                continue;
//              } 
//              else if (localName.equals("definition")) {
//                reader.next();
//                String cc = reader.getText();
//                concept.addDefinition(cc);
//                continue;
//              } 
//              else if (localName.equals("scopeNote")) {
//                reader.next();
//                concept.addScopeNote(reader.getText());
//                continue;
//              } else if (localName.equals("exclusion") && (reader.getAttributeCount() > 0)
//                  && (reader.getAttributeValue(this.rdfNs, "resource") != null)) {
//                reader.next();
//                concept.addExclusion(reader.getText());
//                continue;
//              } else if (localName.equals("inclusion") && (reader.getAttributeCount() > 0)
//                  && (reader.getAttributeValue(this.rdfNs, "resource") != null)) {
//                reader.next();
//                concept.addInclusion(reader.getText());
//                continue;
//              } else if (localName.equals("narrowerTerm")) {
//                reader.next();
//                concept.addNarrowerTerm(reader.getText());
//                continue;
//              }
            case XMLStreamConstants.END_ELEMENT:
              if (reader.getLocalName().equals("Description")) {
                if(count == 0) {
                  process = false;
                  break;
                }else {
                  count--;
                }
              }

          }
        }

      }

    } catch (final Exception ex) {
      throw new SKOSParserException("Error getting concept from rdf:Description:", ex);
    }

    return concept;
  }

}
