package fr.gouv.esante.pml.smt.kmi.owl;

import java.util.Iterator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.VersionInfo;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;
import fr.gouv.esante.pml.smt.cim11.SKOSToOWL;
import fr.gouv.esante.pml.smt.kmi.skos.SKOSConcept;
import fr.gouv.esante.pml.smt.utils.ADMSVocabulary;
import fr.gouv.esante.pml.smt.utils.COREVocabulary;
import fr.gouv.esante.pml.smt.utils.DCATVocabulary;
import fr.gouv.esante.pml.smt.utils.DCTVocabulary;
import fr.gouv.esante.pml.smt.utils.FOAFVocabulary;
import fr.gouv.esante.pml.smt.utils.ICDVocabulary;
import fr.gouv.esante.pml.smt.utils.NSVocabulary;
import fr.gouv.esante.pml.smt.utils.RDFVocabulary;
import fr.gouv.esante.pml.smt.utils.SchemaVocabulary;
import fr.gouv.esante.pml.smt.utils.VOAFVocabulary;

/**
 * OWLOntologyBuilder is used to create and maintain the ontology for the alignment
 */
public class OWLOntologyBuilder {
  OWLOntologyManager man = null;
  private OWLOntology onto = null;
  private OWLDataFactory fact = null;
  private final String uriStr = "http://id.who.int/icd/entity";
  private final String icd10URI = "http://id.who.int/icd/release/10/2016/";

//  private OWLAnnotationProperty skosPrefLabel = null;
//  private OWLAnnotationProperty skosAltLabel = null;
//  private OWLAnnotationProperty skosDefinition = null;
//  private OWLAnnotationProperty skosBroaderTransitive = null;
//  private OWLAnnotationProperty skosNarrowTransitive = null;
//  private OWLAnnotationProperty schemaExclusion = null;
//  private OWLAnnotationProperty schemaInclusion = null;
//  private OWLAnnotationProperty schemaNarrowerTerm = null;
//  private OWLAnnotationProperty schemaMMS = null;
//  private OWLAnnotationProperty schemaICD11Code = null;
//  private OWLAnnotationProperty schemaICD10Code = null;
//  private OWLAnnotationProperty schemaICD11Chapter = null;

  private static OWLAnnotationProperty rdfType = null;
  private static OWLAnnotationProperty rdfValue = null;
  private static OWLAnnotationProperty dctLicense = null;
  private static OWLAnnotationProperty admsStatus = null;
  private static OWLAnnotationProperty dcatAccessURL = null;
  private static OWLAnnotationProperty dcatMediaType = null;
  private static OWLAnnotationProperty dctAlternative = null;
  private static OWLAnnotationProperty dctDescription = null;
  private static OWLAnnotationProperty foafName = null;
  private static OWLAnnotationProperty coreModifiedBy = null;
  private static OWLAnnotationProperty dctAudience = null;
  private static OWLAnnotationProperty dctIdentifier = null;
  private static OWLAnnotationProperty dctIssued = null;
  private static OWLAnnotationProperty dctLanguage = null;
  private static OWLAnnotationProperty dctPublisher = null;
  private static OWLAnnotationProperty dctRelation = null;
  private static OWLAnnotationProperty dctTitle = null;
  private static OWLAnnotationProperty dctType = null;
  private static OWLAnnotationProperty voafClassNumber = null;
  private static OWLAnnotationProperty dcatDistribution = null;
  private static OWLAnnotationProperty dcatLandingPage = null;
  private static OWLAnnotationProperty dcatTheme = null;
  private static OWLAnnotationProperty nsComplexity = null;
  private static OWLAnnotationProperty schemaAuthor = null;
  private static OWLAnnotationProperty schemaPriceSpecification = null;
  private static OWLAnnotationProperty schemaPrice = null;
  private static OWLAnnotationProperty skosHistoryNote = null;

  public OWLOntologyBuilder(final OWLOntology onto) {

    try {
      this.man = OWLManager.createOWLOntologyManager();
      this.onto = onto;
      this.fact = this.onto.getOWLOntologyManager().getOWLDataFactory();

    } catch (final Exception ex) {
      ex.printStackTrace();
    }
  }

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
   * rdf:Description <-> owl:Class skos:prefLabel <-> rdfs:label skos:altLabel <-> rdfs:label
   * skos:broaderTransitive <-> rdfs:subClassOf skos:definition <-> rdfs:comment skos:scopeNote <->
   * rdfs:comment
   *
   * And added this Annotation properties icd:icd10Code icd:icd11Chapter icd:icd11Code icd:mms
   *
   * @param skosConcept
   */
  public void createClass(final SKOSConcept skosConcept) {
    OWLClass owlClass = null;
    String str = null;
    String postc = null;

    try {
      final String about = skosConcept.getAbout();
//      System.out.println(about);
      if (!about.contains("postcoordinationScale") && !about.contains("Modifier")) {
        owlClass = this.fact.getOWLClass(IRI.create(about));
//      } else {
//        owlClass = this.fact.getOWLClass(IRI.create(this.uriStr + "/" + skosConcept.getAbout()));
//      }
        OWLAxiom declare = this.fact.getOWLDeclarationAxiom(owlClass);
        //man.addAxiom(onto,declare);
        this.man.applyChange(new AddAxiom(this.onto, declare));
      }
//        if(about.contains("postcoordinationScale")) {
//        	postc = about.substring(0, about.indexOf("postcoordinationScale")-1);
//        	final OWLSubClassOfAxiom ax =
//                    this.fact.getOWLSubClassOfAxiom(owlClass, this.fact.getOWLClass(IRI.create(postc)));
//                this.man.applyChange(new AddAxiom(this.onto, ax));
//        }

//      this.skosPrefLabel = this.fact.getOWLAnnotationProperty(SKOSVocabulary.PREFLABEL.getIRI());
//      this.skosAltLabel = this.fact.getOWLAnnotationProperty(SKOSVocabulary.ALTLABEL.getIRI());
//      this.skosDefinition = this.fact.getOWLAnnotationProperty(SKOSVocabulary.DEFINITION.getIRI());
//      this.skosBroaderTransitive =
//          this.fact.getOWLAnnotationProperty(SKOSVocabulary.BROADERTRANSITIVE.getIRI());
//      this.skosNarrowTransitive =
//          this.fact.getOWLAnnotationProperty(SKOSVocabulary.NARROWTRANSITIVE.getIRI());
//      this.schemaExclusion = this.fact.getOWLAnnotationProperty(ICDVocabulary.EXCLUSION.getIRI());
//      this.schemaInclusion = this.fact.getOWLAnnotationProperty(ICDVocabulary.INCLUSION.getIRI());
//      this.schemaNarrowerTerm =
//          this.fact.getOWLAnnotationProperty(ICDVocabulary.NARROWERTERM.getIRI());
//      this.schemaMMS = this.fact.getOWLAnnotationProperty(ICDVocabulary.MMS.getIRI());
//      this.schemaICD11Code = this.fact.getOWLAnnotationProperty(ICDVocabulary.ICD11CODE.getIRI());
//      this.schemaICD10Code = this.fact.getOWLAnnotationProperty(ICDVocabulary.ICD10CODE.getIRI());
//      this.schemaICD11Chapter =
//          this.fact.getOWLAnnotationProperty(ICDVocabulary.ICD11Chapter.getIRI());

//      for (final Iterator<String> iter = skosConcept.getPrefLabels().iterator(); iter.hasNext();) {
//        str = iter.next();
//
//        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(this.fact.getRDFSLabel(),
//            this.fact.getOWLLiteral(str, "en"));
//        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
//        this.man.applyChange(new AddAxiom(this.onto, ax1));
//
////        final OWLAnnotation newAnnot =
////            this.fact.getOWLAnnotation(this.skosPrefLabel, this.fact.getOWLLiteral(str, "en"));
////        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
////        this.man.applyChange(new AddAxiom(this.onto, ax2));
//      }
//      for (final Iterator<String> iter = skosConcept.getAltLabels().iterator(); iter.hasNext();) {
//        str = iter.next();
//        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(this.fact.getRDFSLabel(),
//            this.fact.getOWLLiteral(str, "en"));
//        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
//        this.man.applyChange(new AddAxiom(this.onto, ax1));
//
//        final OWLAnnotation newAnnot =
//            this.fact.getOWLAnnotation(this.skosAltLabel, this.fact.getOWLLiteral(str, "en"));
//        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
//        this.man.applyChange(new AddAxiom(this.onto, ax2));
//      }

//      for (final Iterator<String> iter = skosConcept.getExclusion().iterator(); iter.hasNext();) {
//        str = iter.next();
//        final OWLAnnotation newAnnot =
//            this.fact.getOWLAnnotation(this.schemaExclusion, this.fact.getOWLLiteral(str, "en"));
//        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
//        this.man.applyChange(new AddAxiom(this.onto, ax2));
//      }

//      for (final Iterator<String> iter = skosConcept.getInclusion().iterator(); iter.hasNext();) {
//        str = iter.next();
//        final OWLAnnotation newAnnot =
//            this.fact.getOWLAnnotation(this.schemaInclusion, this.fact.getOWLLiteral(str, "en"));
//        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
//        this.man.applyChange(new AddAxiom(this.onto, ax2));
//      }

//      for (final Iterator<String> iter = skosConcept.getNarrowerTerm().iterator(); iter
//          .hasNext();) {
//        str = iter.next();
//        final OWLAnnotation newAnnot =
//            this.fact.getOWLAnnotation(this.schemaNarrowerTerm, this.fact.getOWLLiteral(str, "en"));
//        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
//        this.man.applyChange(new AddAxiom(this.onto, ax2));
//      }

//      for (final Iterator<String> iter = skosConcept.getBroaderTransitive().iterator(); iter
//          .hasNext();) {
//        str = iter.next();
//        final OWLSubClassOfAxiom ax =
//            this.fact.getOWLSubClassOfAxiom(owlClass, this.fact.getOWLClass(IRI.create(str)));
//        this.man.applyChange(new AddAxiom(this.onto, ax));
//
////        final OWLAnnotation newAnnot =
////            this.fact.getOWLAnnotation(this.skosBroaderTransitive, this.fact.getOWLLiteral(str));
////        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
////        this.man.applyChange(new AddAxiom(this.onto, ax2));
//
//      }
//      for (final Iterator<String> iter = skosConcept.getNarrowerTransitive().iterator(); iter
//          .hasNext();) {
//        str = iter.next();
//
//        final OWLAnnotation newAnnot =
//            this.fact.getOWLAnnotation(this.skosNarrowTransitive, this.fact.getOWLLiteral(str));
//        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
//        this.man.applyChange(new AddAxiom(this.onto, ax2));
//
//      }
//      for (final Iterator<String> iter = skosConcept.getDefinition().iterator(); iter.hasNext();) {
//        str = iter.next();
//        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(this.fact.getRDFSComment(),
//            this.fact.getOWLLiteral(str, "en"));
//        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
//        this.man.applyChange(new AddAxiom(this.onto, ax1));

//        final OWLAnnotation newAnnot =
//            this.fact.getOWLAnnotation(this.skosDefinition, this.fact.getOWLLiteral(str, "en"));
//        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
//        this.man.applyChange(new AddAxiom(this.onto, ax2));
//      }
//      for (final Iterator<String> iter = skosConcept.getScopeNote().iterator(); iter.hasNext();) {
//        str = iter.next();
//        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(this.fact.getRDFSComment(),
//            this.fact.getOWLLiteral(str, "en"));
//        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
//        this.man.applyChange(new AddAxiom(this.onto, ax1));
//      }

//      if (SKOSToOWL.idICD11.containsKey(about)) {
//        final String ligneICD11 = SKOSToOWL.idICD11.get(about);
//        // Add MMS concept
//        final String strMMS = ligneICD11.split("£")[0];
//        final OWLAnnotation labelAnno1 =
//            this.fact.getOWLAnnotation(this.schemaMMS, this.fact.getOWLLiteral(strMMS));
//        final OWLAxiom ax1 =
//            this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno1);
//        this.man.applyChange(new AddAxiom(this.onto, ax1));

        // Add ICD11 concept
//        final String strICD11 = ligneICD11.split("£")[1];
//        if (!isBlank(strICD11)) {
//          final OWLAnnotation labelAnno2 =
//              this.fact.getOWLAnnotation(this.schemaICD11Code, this.fact.getOWLLiteral(strICD11));
//          final OWLAxiom ax2 =
//              this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno2);
//          this.man.applyChange(new AddAxiom(this.onto, ax2));
//        }
        // Add ICD10 concept
//        final String strICD10 = ligneICD11.split("£")[4];
//        if (!isBlank(strICD10)) {
//          final OWLAnnotation labelAnno3 = this.fact.getOWLAnnotation(this.schemaICD10Code,
//              this.fact.getOWLLiteral(this.icd10URI + strICD10));
//          final OWLAxiom ax3 =
//              this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno3);
//          this.man.applyChange(new AddAxiom(this.onto, ax3));
//          // Add ICD11 concept
//          final String strICD11Chapter = ligneICD11.split("£")[2];
//          if (!isBlank(strICD11Chapter)) {
//            final OWLAnnotation labelAnno4 = this.fact.getOWLAnnotation(this.schemaICD11Chapter,
//                this.fact.getOWLLiteral(strICD11Chapter));
//            final OWLAxiom ax4 =
//                this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno4);
//            this.man.applyChange(new AddAxiom(this.onto, ax4));
//          }
//
//        }

//        if (SKOSToOWL.idICD11.containsKey(about + "A")) {
//          final String ligneICD11A = SKOSToOWL.idICD11.get(about + "A");

//          // Add ICD11 concept
//          final String strICD11A = ligneICD11A.split("£")[1];
//          if (!isBlank(strICD11A)) {
//            final OWLAnnotation labelAnno2A = this.fact.getOWLAnnotation(this.schemaICD11Code,
//                this.fact.getOWLLiteral(strICD11A));
//            final OWLAxiom ax2A =
//                this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno2A);
//            this.man.applyChange(new AddAxiom(this.onto, ax2A));
//          }

//          if (SKOSToOWL.idICD11.containsKey(about + "AA")) {
//            final String ligneICD11AA = SKOSToOWL.idICD11.get(about + "AA");
//
//            final String strICD11AA = ligneICD11AA.split("£")[1];
//            if (!isBlank(strICD11AA)) {
//              final OWLAnnotation labelAnno2AA = this.fact.getOWLAnnotation(this.schemaICD11Code,
//                  this.fact.getOWLLiteral(strICD11AA));
//              final OWLAxiom ax2AA =
//                  this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno2AA);
//              this.man.applyChange(new AddAxiom(this.onto, ax2AA));
//            }
//
//          }
//        }
//      }
//      if (SKOSToOWL.listTraduction.containsKey(about)) {
//        for (final String line : SKOSToOWL.listTraduction.get(about).split("@")) {
//          vocab = line.split(";")[3];
//          if (vocab.contains("title")) {
//            str = line.split(";")[5];
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
//          else if (vocab.contains("synonym")) {
//            str = line.split("£")[4];
//            final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(this.fact.getRDFSLabel(),
//                this.fact.getOWLLiteral(str, "fr"));
//            final OWLAxiom ax1 =
//                this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
//            this.man.applyChange(new AddAxiom(this.onto, ax1));
//
//            final OWLAnnotation newAnnot =
//                this.fact.getOWLAnnotation(this.skosAltLabel, this.fact.getOWLLiteral(str, "fr"));
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
//
//          else if (vocab.contains("narrowerTerm")) {
//            str = line.split("£")[4];
//            final OWLAnnotation newAnnot = this.fact.getOWLAnnotation(this.schemaNarrowerTerm,
//                this.fact.getOWLLiteral(str, "fr"));
//            final OWLAxiom ax2 =
//                this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
//            this.man.applyChange(new AddAxiom(this.onto, ax2));
//          }
//
//          else if (vocab.contains("definition")) {
//            str = line.split(";")[5];
//            final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(this.fact.getRDFSComment(),
//                this.fact.getOWLLiteral(str, "fr"));
//            final OWLAxiom ax1 =
//                this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
//            this.man.applyChange(new AddAxiom(this.onto, ax1));
//
//            final OWLAnnotation newAnnot =
//                this.fact.getOWLAnnotation(this.skosDefinition, this.fact.getOWLLiteral(str, "fr"));
//            final OWLAxiom ax2 =
//                this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
//            this.man.applyChange(new AddAxiom(this.onto, ax2));
//          }
////
//        }
//      }

    } catch (final Exception ex) {
      ex.printStackTrace();
    }
  }


  public void createIndividual(final NamedIndividualConcept Concept) {
    OWLNamedIndividual owlClass = null;
    String str = null;
    String vocab = null;

    try {
      final String iri = Concept.getIri();

      owlClass = this.fact.getOWLNamedIndividual(IRI.create(iri));
      OWLDeclarationAxiom declarationAxiom = fact.getOWLDeclarationAxiom(owlClass);
      man.addAxiom(onto, declarationAxiom);


      rdfType = fact.getOWLAnnotationProperty(OWLRDFVocabulary.RDF_TYPE.getIRI());
      rdfValue = fact.getOWLAnnotationProperty(RDFVocabulary.value.getIRI());
      dctLicense = fact.getOWLAnnotationProperty(DCTVocabulary.license.getIRI());
      admsStatus = fact.getOWLAnnotationProperty(ADMSVocabulary.status.getIRI());
      dcatAccessURL = fact.getOWLAnnotationProperty(DCATVocabulary.accessURL.getIRI());
      dcatMediaType = fact.getOWLAnnotationProperty(DCATVocabulary.mediaType.getIRI());
      dctAlternative = fact.getOWLAnnotationProperty(DCTVocabulary.alternative.getIRI());
      dctDescription = fact.getOWLAnnotationProperty(DCTVocabulary.description.getIRI());
      foafName = fact.getOWLAnnotationProperty(FOAFVocabulary.name.getIRI());
      coreModifiedBy = fact.getOWLAnnotationProperty(COREVocabulary.modifiedBy.getIRI());
      dctAudience = fact.getOWLAnnotationProperty(DCTVocabulary.audience.getIRI());
      dctIdentifier = fact.getOWLAnnotationProperty(DCTVocabulary.identifier.getIRI());
      dctIssued = fact.getOWLAnnotationProperty(DCTVocabulary.issued.getIRI());
      dctLanguage = fact.getOWLAnnotationProperty(DCTVocabulary.language.getIRI());
      dctPublisher = fact.getOWLAnnotationProperty(DCTVocabulary.publisher.getIRI());
      dctRelation = fact.getOWLAnnotationProperty(DCTVocabulary.relation.getIRI());
      dctTitle = fact.getOWLAnnotationProperty(DCTVocabulary.title.getIRI());
      dctType = fact.getOWLAnnotationProperty(DCTVocabulary.type.getIRI());
      voafClassNumber = fact.getOWLAnnotationProperty(VOAFVocabulary.classNumber.getIRI());
      dcatDistribution = fact.getOWLAnnotationProperty(DCATVocabulary.distribution.getIRI());
      dcatLandingPage = fact.getOWLAnnotationProperty(DCATVocabulary.landingPage.getIRI());
      dcatTheme = fact.getOWLAnnotationProperty(DCATVocabulary.theme.getIRI());
      nsComplexity = fact.getOWLAnnotationProperty(NSVocabulary.complexity.getIRI());
      schemaAuthor = fact.getOWLAnnotationProperty(SchemaVocabulary.author.getIRI());
      schemaPriceSpecification =
          fact.getOWLAnnotationProperty(SchemaVocabulary.priceSpecification.getIRI());
      schemaPrice = fact.getOWLAnnotationProperty(SchemaVocabulary.price.getIRI());
      skosHistoryNote = fact.getOWLAnnotationProperty(SKOSVocabulary.HISTORYNOTE.getIRI());
      int lab = 0;
      for (final Iterator<String> iter = Concept.getLabel().iterator(); iter.hasNext();) {
        str = iter.next();
        
        OWLAnnotation labelAnno = null;
        if(lab == 0) {
          labelAnno = this.fact.getOWLAnnotation(this.fact.getRDFSLabel(),
            this.fact.getOWLLiteral(str, "fr"));
        }else {
          labelAnno = this.fact.getOWLAnnotation(this.fact.getRDFSLabel(),
              this.fact.getOWLLiteral(str, "en"));
        }
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
        lab++;
      }
      int first = 0;
      for (final Iterator<String> iter = Concept.getType().iterator(); iter.hasNext();) {
        str = iter.next();
        OWLAnnotation labelAnno = null;
        if(first == 0) {
          labelAnno = this.fact.getOWLAnnotation(rdfType, IRI.create(str));
        }else {
          labelAnno = this.fact.getOWLAnnotation(dctType, IRI.create(str));
        }
        
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
        first++;
      }

      for (final Iterator<String> iter = Concept.getDescription().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(dctDescription, this.fact.getOWLLiteral(str, "fr"));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));
      }

      for (final Iterator<String> iter = Concept.getAlternative().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(dctAlternative, this.fact.getOWLLiteral(str, "fr"));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));
      }

      for (final Iterator<String> iter = Concept.getName().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation newAnnot =
            this.fact.getOWLAnnotation(foafName, this.fact.getOWLLiteral(str));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));
      }

      for (final Iterator<String> iter = Concept.getLicense().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation newAnnot = this.fact.getOWLAnnotation(dctLicense, IRI.create(str));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));

      }
      for (final Iterator<String> iter = Concept.getStatus().iterator(); iter.hasNext();) {
        str = iter.next();

        final OWLAnnotation newAnnot = this.fact.getOWLAnnotation(admsStatus, IRI.create(str));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));

      }
      for (final Iterator<String> iter = Concept.getAccessURL().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation newAnnot = this.fact.getOWLAnnotation(dcatAccessURL, IRI.create(str));
        final OWLAxiom ax2 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), newAnnot);
        this.man.applyChange(new AddAxiom(this.onto, ax2));
      }
      for (final Iterator<String> iter = Concept.getMediaType().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(dcatMediaType, IRI.create(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getValue().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(rdfValue, this.fact.getOWLLiteral(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getModifiedBy().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(coreModifiedBy, IRI.create(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getAudience().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(dctAudience, IRI.create(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getIdentifier().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(dctIdentifier, this.fact.getOWLLiteral(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getIssued().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(dctIssued, this.fact.getOWLLiteral(str, OWL2Datatype.XSD_DATE_TIME));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getLanguage().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(dctLanguage, IRI.create(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getPublisher().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(dctPublisher, IRI.create(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getRelation().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(dctRelation, IRI.create(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getTitle().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(dctTitle, this.fact.getOWLLiteral(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getClassNumber().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(voafClassNumber, this.fact.getOWLLiteral(str, OWL2Datatype.XSD_INTEGER));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getComment().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(this.fact.getRDFSComment(), this.fact.getOWLLiteral(str, "fr"));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getVersionInfo().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(this.fact.getOWLVersionInfo(), this.fact.getOWLLiteral(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getHistoryNote().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(skosHistoryNote, this.fact.getOWLLiteral(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getDistribution().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(dcatDistribution, IRI.create(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getLandingPage().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(dcatLandingPage, IRI.create(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getTheme().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(dcatTheme, IRI.create(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getComplexity().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(nsComplexity, IRI.create(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getAuthor().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(schemaAuthor, IRI.create(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }
      for (final Iterator<String> iter = Concept.getPrice().iterator(); iter.hasNext();) {
        str = iter.next();
        final OWLAnnotation labelAnno = this.fact.getOWLAnnotation(schemaPrice, this.fact.getOWLLiteral(str));
        final OWLAxiom ax1 = this.fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnno);
        this.man.applyChange(new AddAxiom(this.onto, ax1));
      }



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
