package fr.gouv.esante.pml.smt.utils;

import static org.semanticweb.owlapi.model.EntityType.ANNOTATION_PROPERTY;
import static org.semanticweb.owlapi.model.EntityType.CLASS;
import static org.semanticweb.owlapi.model.EntityType.DATA_PROPERTY;
import static org.semanticweb.owlapi.model.EntityType.OBJECT_PROPERTY;
import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.asSet;
import java.util.Set;
import java.util.stream.Stream;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.HasPrefixedName;
import org.semanticweb.owlapi.model.HasShortForm;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.providers.AnnotationPropertyProvider;


public enum ICDVocabulary implements HasShortForm, HasIRI, HasPrefixedName {
//@formatter:off
    /** EXCLUSION.         */ EXCLUSION            ("exclusion",            ANNOTATION_PROPERTY),
    /** INCLUSION.       */  INCLUSION          ("inclusion",            ANNOTATION_PROPERTY),
    /** NARROWERTERM.       */  NARROWERTERM          ("narrowerTerm",            ANNOTATION_PROPERTY),
    /** MMS.       */  MMS          ("mms",            ANNOTATION_PROPERTY),
    /** ICD11CODE.       */  ICD11CODE          ("icd11Code",            ANNOTATION_PROPERTY),
    /** ICD10CODE.       */  ICD10CODE          ("IRI",            ANNOTATION_PROPERTY),
    /** ICD11Chapter.       */  ICD11Chapter          ("icd11Chapter",            ANNOTATION_PROPERTY),
    
    /** ICD11Chapter.       */  CODE          ("code",            ANNOTATION_PROPERTY),
    /** ICD11Chapter.       */  ClassKind          ("classKind",            ANNOTATION_PROPERTY),
    /** ICD11Chapter.       */  browserUrl          ("browserUrl",            ANNOTATION_PROPERTY),
    /** ICD11Chapter.       */  codingHint          ("codingHint",            ANNOTATION_PROPERTY),
    /** ICD11Chapter.       */  codingNote          ("codingNote",            ANNOTATION_PROPERTY),
    /** ICD11Chapter.       */  releaseDate          ("releaseDate",            ANNOTATION_PROPERTY),
    /** release.       */  release          ("release",            ANNOTATION_PROPERTY),

    /** @deprecated No longer used */
    @Deprecated
    DOCUMENT("Document", CLASS),
    /** @deprecated No longer used */
    @Deprecated
    IMAGE("Image", CLASS),
    /** @deprecated No longer used */
    @Deprecated
    COLLECTABLEPROPERTY("CollectableProperty", ANNOTATION_PROPERTY),
    /** @deprecated No longer used */
    @Deprecated
    RESOURCE("Resource", CLASS),
    /** @deprecated No longer used */
    @Deprecated
    COMMENT("comment", ANNOTATION_PROPERTY);
//@formatter:on
  /**
   * All IRIs.
   */
  public static final Set<IRI> ALL_IRIS = asSet(stream().map(v -> v.getIRI()));
  private final String localName;
  private final IRI iri;
  private final EntityType<?> entityType;
  private final String prefixedName;

  ICDVocabulary(final String localname, final EntityType<?> entityType) {
    this.localName = localname;
    this.prefixedName = NameSpace.ICD.getPrefixName() + ':' + localname;
    this.entityType = entityType;
    this.iri = IRI.create(NameSpace.ICD.toString(), localname);
  }

  private static Stream<ICDVocabulary> stream() {
    return Stream.of(values());
  }

  /**
   * @param dataFactory data factory to use
   * @return set of SCHEMA annotation properties
   */
  public static Set<OWLAnnotationProperty> getAnnotationProperties(
      final AnnotationPropertyProvider dataFactory) {
    return asSet(stream().filter(v -> v.entityType.equals(ANNOTATION_PROPERTY))
        .map(v -> dataFactory.getOWLAnnotationProperty(v.iri)));
  }

  /**
   * @param dataFactory data factory to use
   * @return set of SCHEMA object properties
   */
  public static Set<OWLObjectProperty> getObjectProperties(final OWLDataFactory dataFactory) {
    return asSet(stream().filter(v -> v.entityType.equals(OBJECT_PROPERTY))
        .map(v -> dataFactory.getOWLObjectProperty(v.iri)));
  }

  /**
   * @param dataFactory data factory to use
   * @return set of SCHEMA data properties
   */
  public static Set<OWLDataProperty> getDataProperties(final OWLDataFactory dataFactory) {
    return asSet(stream().filter(v -> v.entityType.equals(DATA_PROPERTY))
        .map(v -> dataFactory.getOWLDataProperty(v.iri)));
  }

  /**
   * @param dataFactory data factory to use
   * @return set of SCHEMA classes
   */
  public static Set<OWLClass> getClasses(final OWLDataFactory dataFactory) {
    return asSet(
        stream().filter(v -> v.entityType.equals(CLASS)).map(v -> dataFactory.getOWLClass(v.iri)));
  }

  /**
   * @return entity type
   */
  public EntityType<?> getEntityType() {
    return this.entityType;
  }

  /**
   * @return local name
   */
  public String getLocalName() {
    return this.localName;
  }

  @Override
  public IRI getIRI() {
    return this.iri;
  }

  @Override
  public String getShortForm() {
    return this.localName;
  }

  @Override
  public String getPrefixedName() {
    return this.prefixedName;
  }
}
