package fr.gouv.esante.pml.smt.kmi.owl;

import java.util.Vector;

/**
 * SKOSConcept is a representation of the Concept tag from the SKOS
 */
public class NamedIndividualConcept {
  private String iri;

  private final Vector<String> type;
  private final Vector<String> alternative;
  private final Vector<String> description;
  private final Vector<String> label;
  private final Vector<String> name;
  private final Vector<String> license;
  private final Vector<String> status;
  private final Vector<String> accessURL;
  private final Vector<String> mediaType;
  private final Vector<String> value;
  private final Vector<String> modifiedBy;
  private final Vector<String> audience;
  private final Vector<String> identifier;
  private final Vector<String> issued;
  private final Vector<String> language;
  private final Vector<String> publisher;
  private final Vector<String> relation;
  private final Vector<String> title;
  private final Vector<String> classNumber;
  private final Vector<String> comment;
  private final Vector<String> versionInfo;
  private final Vector<String> historyNote;
  private final Vector<String> distribution;
  private final Vector<String> landingPage;
  private final Vector<String> complexity;
  private final Vector<String> theme;
  private final Vector<String> author;
  private final Vector<String> priceSpecification;
  private final Vector<String> price;

  public NamedIndividualConcept(final String iri) {
    

    this.iri = iri;
    this.type = new Vector<String>();
    this.alternative = new Vector<String>();
    this.description = new Vector<String>();
    this.label = new Vector<String>();
    this.name = new Vector<String>();
    this.license = new Vector<String>();
    this.status = new Vector<String>();
    this.accessURL = new Vector<String>();
    this.mediaType = new Vector<String>();
    this.value = new Vector<String>();
    this.modifiedBy = new Vector<String>();
    this.audience = new Vector<String>();
    this.identifier = new Vector<String>();
    this.issued = new Vector<String>();
    this.language = new Vector<String>();
    this.publisher = new Vector<String>();
    this.relation = new Vector<String>();
    this.title = new Vector<String>();
    this.classNumber = new Vector<String>();
    this.comment = new Vector<String>();
    this.versionInfo = new Vector<String>();
    this.historyNote = new Vector<String>();
    this.distribution = new Vector<String>();
    this.landingPage = new Vector<String>();
    this.complexity = new Vector<String>();
    this.theme = new Vector<String>();
    this.author = new Vector<String>();
    this.priceSpecification = new Vector<String>();
    this.price = new Vector<String>();
  }

  public void setIri(final String iri) {
    this.iri = iri;
  }

  public String getIri() {
    return this.iri;
  }
  
  

  public Vector<String> getType() {
    return type;
  }

  public Vector<String> getAlternative() {
    return alternative;
  }

  public Vector<String> getDescription() {
    return description;
  }

  public Vector<String> getLabel() {
    return label;
  }

  public Vector<String> getName() {
    return name;
  }

  public Vector<String> getLicense() {
    return license;
  }

  public Vector<String> getStatus() {
    return status;
  }

  public Vector<String> getAccessURL() {
    return accessURL;
  }

  public Vector<String> getMediaType() {
    return mediaType;
  }

  public Vector<String> getValue() {
    return value;
  }

  public Vector<String> getModifiedBy() {
    return modifiedBy;
  }

  public Vector<String> getAudience() {
    return audience;
  }

  public Vector<String> getIdentifier() {
    return identifier;
  }

  public Vector<String> getIssued() {
    return issued;
  }

  public Vector<String> getLanguage() {
    return language;
  }

  public Vector<String> getPublisher() {
    return publisher;
  }

  public Vector<String> getRelation() {
    return relation;
  }

  public Vector<String> getTitle() {
    return title;
  }

  public Vector<String> getClassNumber() {
    return classNumber;
  }

  public Vector<String> getComment() {
    return comment;
  }

  public Vector<String> getVersionInfo() {
    return versionInfo;
  }

  public Vector<String> getHistoryNote() {
    return historyNote;
  }

  public Vector<String> getDistribution() {
    return distribution;
  }

  public Vector<String> getLandingPage() {
    return landingPage;
  }

  public Vector<String> getComplexity() {
    return complexity;
  }

  public Vector<String> getTheme() {
    return theme;
  }

  public Vector<String> getAuthor() {
    return author;
  }

  public Vector<String> getPriceSpecification() {
    return priceSpecification;
  }

  public Vector<String> getPrice() {
    return price;
  }

  public void addType(final String type) {
    this.type.add(type);
  }

  public void addAlternative(final String alternative) {
    this.alternative.add(alternative);
  }

  public void addDescription(final String description) {
    this.description.add(description);
  }

  public void addLabel(final String label) {
    this.label.add(label);
  }

  public void addName(final String name) {
    this.name.add(name);
  }

  public void addLicense(final String license) {
    this.license.add(license);
  }

  public void addStatus(final String status) {
    this.status.add(status);
  }

  public void addAccessURL(final String accessURL) {
    this.accessURL.add(accessURL);
  }

  public void addMediaType(final String mediaType) {
    this.mediaType.add(mediaType);
  }

  public void addValue(final String value) {
    this.value.add(value);
  }
  
  public void addModifiedBy(final String modifiedBy) {
    this.modifiedBy.add(modifiedBy);
  }

  public void addAudience(final String audience) {
    this.audience.add(audience);
  }

  public void addIdentifier(final String identifier) {
    this.identifier.add(identifier);
  }

  public void addIssued(final String issued) {
    this.issued.add(issued);
  }

  public void addLanguage(final String language) {
    this.language.add(language);
  }

  public void addPublisher(final String publisher) {
    this.publisher.add(publisher);
  }

  public void addRelation(final String relation) {
    this.relation.add(relation);
  }

  public void addTitle(final String title) {
    this.title.add(title);
  }

  public void addClassNumber(final String classNumber) {
    this.classNumber.add(classNumber);
  }

  public void addComment(final String comment) {
    this.comment.add(comment);
  }
  
  public void addVersionInfo(final String versionInfo) {
    this.versionInfo.add(versionInfo);
  }

  public void addHistoryNote(final String historyNote) {
    this.historyNote.add(historyNote);
  }

  public void addDistribution(final String distribution) {
    this.distribution.add(distribution);
  }

  public void addLandingPage(final String landingPage) {
    this.landingPage.add(landingPage);
  }

  public void addComplexity(final String complexity) {
    this.complexity.add(complexity);
  }
  
  public void addTheme(final String theme) {
    this.theme.add(theme);
  }

  public void addAuthor(final String author) {
    this.author.add(author);
  }

  public void addPriceSpecification(final String priceSpecification) {
    this.priceSpecification.add(priceSpecification);
  }

  public void addPrice(final String price) {
    this.price.add(price);
  }

}
