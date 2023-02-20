package fr.gouv.esante.pml.smt.cim10;

import java.util.Iterator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;
import fr.gouv.esante.pml.smt.cim10.SKOSConcept;
import fr.gouv.esante.pml.smt.cim10.SKOSToOWL;
import fr.gouv.esante.pml.smt.utils.ICDVocabulary;

/**
 * OWLOntologyBuilder is used to create and maintain the ontology for the alignment
 */
public class OWLOntologyBuilder {
  OWLOntologyManager man = null;
  private OWLOntology onto = null;
  private OWLDataFactory fact = null;
  //private final String uriStr = "http://id.who.int/icd/entity";
  private final String uriStr = "http://id.who.int/icd/release/10/2016";

  private OWLAnnotationProperty skosPrefLabel = null;
  private OWLAnnotationProperty skosDefinition = null;
  private OWLAnnotationProperty skosBroaderTransitive = null;
  private OWLAnnotationProperty skosNarrowTransitive = null;
  private OWLAnnotationProperty schemaExclusion = null;
  private OWLAnnotationProperty schemaInclusion = null;
  
  private OWLAnnotationProperty schemaCode = null;
  private OWLAnnotationProperty schemaClassKind = null;
  private OWLAnnotationProperty schemaBrowserUrl = null;
  private OWLAnnotationProperty schemaCodingHint = null;
  private OWLAnnotationProperty schemaCodingNote = null;
  private OWLAnnotationProperty schemaReleaseDate = null;
  

  public OWLOntologyBuilder(final String uriStr) {

    try {
      final IRI IOR = IRI.create(uriStr);
      this.man = OWLManager.createOWLOntologyManager();
      this.onto = this.man.createOntology(IOR);
      this.fact = this.onto.getOWLOntologyManager().getOWLDataFactory();

    } catch (final Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Creates an OWL class from the SKOS concept The following mappings have been assumed
   * rdf:Description <-> owl:Class 
   * skos:prefLabel <-> rdfs:label 
   * skos:altLabel <-> rdfs:label
   * skos:broaderTransitive <-> rdfs:subClassOf 
   * skos:definition <-> rdfs:comment 
   * skos:scopeNote <-> rdfs:comment
   *
   * And added this Annotation properties icd:icd10Code icd:icd11Chapter icd:icd11Code icd:mms
   *
   * @param skosConcept
   */
  public void createClass(final SKOSConcept skosConcept) {
    OWLClass owlClass = null;
    String str = null;
    String vocab = null;

    try {
      final String about = skosConcept.getAbout();

      if (about.equals("2016")) {
        owlClass = this.fact.getOWLClass(IRI.create(this.uriStr));
      } else {
        owlClass = this.fact.getOWLClass(IRI.create(this.uriStr + "/" + skosConcept.getAbout()));
      }

      this.skosPrefLabel = this.fact.getOWLAnnotationProperty(SKOSVocabulary.PREFLABEL.getIRI());
      this.skosDefinition = this.fact.getOWLAnnotationProperty(SKOSVocabulary.DEFINITION.getIRI());
      this.skosBroaderTransitive =
          this.fact.getOWLAnnotationProperty(SKOSVocabulary.BROADERTRANSITIVE.getIRI());
      this.skosNarrowTransitive =
          this.fact.getOWLAnnotationProperty(SKOSVocabulary.NARROWTRANSITIVE.getIRI());
      this.schemaExclusion =
          this.fact.getOWLAnnotationProperty(ICDVocabulary.EXCLUSION.getIRI());
      this.schemaInclusion =
          this.fact.getOWLAnnotationProperty(ICDVocabulary.INCLUSION.getIRI());
      this.schemaCode =
          this.fact.getOWLAnnotationProperty(ICDVocabulary.CODE.getIRI());
      this.schemaClassKind = this.fact.getOWLAnnotationProperty(ICDVocabulary.ClassKind.getIRI());
      this.schemaBrowserUrl =
          this.fact.getOWLAnnotationProperty(ICDVocabulary.browserUrl.getIRI());
      this.schemaCodingNote =
          this.fact.getOWLAnnotationProperty(ICDVocabulary.codingNote.getIRI());
      this.schemaCodingHint =
          this.fact.getOWLAnnotationProperty(ICDVocabulary.codingHint.getIRI());
      this.schemaReleaseDate =
          this.fact.getOWLAnnotationProperty(ICDVocabulary.releaseDate.getIRI());

      for (final Iterator<String> iter = skosConcept.getPrefLabels().iterator(); iter.hasNext();) {
        str = iter.next();

        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(this.fact.getRDFSLabel(),
            this.fact.getOWLLiteral(str, "en"));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));

        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(this.skosPrefLabel, this.fact.getOWLLiteral(str, "en"));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));
      }
      for (final Iterator<String> iter = skosConcept.getCode().iterator(); iter.hasNext();) {
        str = iter.next();

        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(this.schemaCode, this.fact.getOWLLiteral(str));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));
      }

      for (final Iterator<String> iter = skosConcept.getExclusion().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(this.schemaExclusion, this.fact.getOWLLiteral(str, "en"));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));
      }

      for (final Iterator<String> iter = skosConcept.getInclusion().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(this.schemaInclusion, this.fact.getOWLLiteral(str, "en"));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));
      }

      for (final Iterator<String> iter = skosConcept.getBrowserUrl().iterator(); iter
          .hasNext();) {
        str = iter.next();
        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(this.schemaBrowserUrl, this.fact.getOWLLiteral(str));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));
      }

      for (final Iterator<String> iter = skosConcept.getBroaderTransitive().iterator(); iter
          .hasNext();) {
        str = iter.next();
        final OWLSubClassOfAxiom ax =
            this.fact.getOWLSubClassOfAxiom(owlClass, this.fact.getOWLClass(IRI.create(str)));
        this.man.applyChange(new AddAxiom(this.onto, ax));

        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(this.skosBroaderTransitive, this.fact.getOWLLiteral(str));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));

      }
      for (final Iterator<String> iter = skosConcept.getNarrowerTransitive().iterator(); iter
          .hasNext();) {
        str = iter.next();

        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(this.skosNarrowTransitive, this.fact.getOWLLiteral(str));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));

      }
      for (final Iterator<String> iter = skosConcept.getDefinition().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(this.fact.getRDFSComment(),
            this.fact.getOWLLiteral(str, "en"));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));

        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(this.skosDefinition, this.fact.getOWLLiteral(str, "en"));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));
      }
      for (final Iterator<String> iter = skosConcept.getClassKind().iterator(); iter
          .hasNext();) {
        str = iter.next();

        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(this.schemaClassKind, this.fact.getOWLLiteral(str));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));

      }
      for (final Iterator<String> iter = skosConcept.getCodingHint().iterator(); iter
          .hasNext();) {
        str = iter.next();

        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(this.schemaCodingHint, this.fact.getOWLLiteral(str));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));

      }
      for (final Iterator<String> iter = skosConcept.getCodingNote().iterator(); iter
          .hasNext();) {
        str = iter.next();

        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(this.schemaCodingNote, this.fact.getOWLLiteral(str));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));

      }
      for (final Iterator<String> iter = skosConcept.getReleaseDate().iterator(); iter
          .hasNext();) {
        str = iter.next();

        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(this.schemaReleaseDate, this.fact.getOWLLiteral(str));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));

      }
      
//      if (SKOSToOWL.listTraduction.containsKey(about)) {
//        for (final String line : SKOSToOWL.listTraduction.get(about).split("@")) {
//          vocab = line.split("£")[2];
//          if (vocab.contains("preferred")) {
//            str = line.split("£")[4];
//
//            final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(this.fact.getRDFSLabel(),
//                this.fact.getOWLLiteral(str, "fr"));
//            final OWLAxiom ax1 =
//                this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
//            this.man.applyChange(new AddAxiom(this.onto, ax1));
//
//            final OWLAnnotation newAnnot =
//                this.fact.getOWLAnnotation(this.skosPrefLabel, this.fact.getOWLLiteral(str, "fr"));
//            final OWLAxiom ax2 =
//                this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
//            this.man.applyChange(new AddAxiom(this.onto, ax2));
//          } 
//
//          else if (vocab.contains("exclusion")) {
//            str = line.split("£")[4];
//            final OWLAnnotation newAnnot = this.fact.getOWLAnnotation(this.schemaExclusion,
//                this.fact.getOWLLiteral(str, "fr"));
//            final OWLAxiom ax2 =
//                this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
//            this.man.applyChange(new AddAxiom(this.onto, ax2));
//          }
//
//          else if (vocab.contains("inclusion")) {
//            str = line.split("£")[4];
//            final OWLAnnotation newAnnot = this.fact.getOWLAnnotation(this.schemaInclusion,
//                this.fact.getOWLLiteral(str, "fr"));
//            final OWLAxiom ax2 =
//                this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
//            this.man.applyChange(new AddAxiom(this.onto, ax2));
//          }
//        }
//      }

      
      

    } catch (final Exception ex) {
      ex.printStackTrace();
    }
  }

  public OWLOntology getOntology() {
    return this.onto;
  }

  public String getUriStr() {
    return this.uriStr;
  }

  public static boolean isBlank(final CharSequence cs) {
    int strLen;
    if ((cs == null) || ((strLen = cs.length()) == 0)) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
