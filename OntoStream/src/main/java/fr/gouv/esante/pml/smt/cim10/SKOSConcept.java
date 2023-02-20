package fr.gouv.esante.pml.smt.cim10;

import java.util.Vector;

/**
 * SKOSConcept is a representation of the Concept tag from the SKOS
 */
public class SKOSConcept {
  private String about;
  private final Vector<String> prefLabels;
  private final Vector<String> broaderTransitive;
  private final Vector<String> narrowerTransitive;
  private final Vector<String> code;
  private final Vector<String> classKind;
  private final Vector<String> browserUrl;
  private final Vector<String> exclusion;
  private final Vector<String> inclusion;
  private final Vector<String> codingHint;
  private final Vector<String> codingNote;
  private final Vector<String> definition;
  private final Vector<String> releaseDate;

  public SKOSConcept(final String about) {
    if ((about.indexOf("http://") != -1) && (about.indexOf("#") != -1)) {
      this.about = about.substring(about.indexOf("#") + 1, about.length());
    } else if ((about.indexOf("http://") != -1) && (about.indexOf("#") == -1)) {
      this.about = about.substring(about.lastIndexOf("/") + 1, about.length());
    } else {
      this.about = about.replaceFirst("#", "");
    }

    this.prefLabels = new Vector<String>();
    this.broaderTransitive = new Vector<String>();
    this.code = new Vector<String>();
    this.classKind = new Vector<String>();
    this.browserUrl = new Vector<String>();
    this.exclusion = new Vector<String>();
    this.inclusion = new Vector<String>();
    this.codingHint = new Vector<String>();
    this.codingNote = new Vector<String>();
    this.definition = new Vector<String>();
    this.releaseDate = new Vector<String>();
    this.narrowerTransitive = new Vector<String>();
  }

  public void setAbout(final String about) {
    if ((about.indexOf("http://") != -1) && (about.indexOf("#") != -1)) {
      this.about = about.substring(about.indexOf("#") + 1, about.length());
    } else if ((about.indexOf("http://") != -1) && (about.indexOf("#") == -1)) {
      this.about = about.substring(about.lastIndexOf("/") + 1, about.length());
    } else {
      this.about = about.replaceFirst("#", "");
    }
  }

  public String getAbout() {
    return this.about;
  }

  public void addPrefLabel(final String prefLabel) {
    this.prefLabels.add(prefLabel);
  }

  public Vector<String> getPrefLabels() {
    return this.prefLabels;
  }


  public void addBroaderTransitive(final String broaderTransitive) {
    this.broaderTransitive.add(broaderTransitive);
  }

  public Vector<String> getBroaderTransitive() {
    return this.broaderTransitive;
  }
  
  public void addNarrowerTransitive(final String narrowerTransitive) {
    this.narrowerTransitive.add(narrowerTransitive);
  }

  public Vector<String> getNarrowerTransitive() {
    return this.narrowerTransitive;
  }


  public void addDefinition(final String definition) {
    this.definition.add(definition);
  }

  public Vector<String> getDefinition() {
    return this.definition;
  }


  public void addExclusion(final String exclusion) {
    this.exclusion.add(exclusion);
  }

  public Vector<String> getExclusion() {
    return this.exclusion;
  }


  public void addInclusion(final String inclusion) {
    this.inclusion.add(inclusion);
  }

  public Vector<String> getInclusion() {
    return this.inclusion;
  }
  
  public void addCode(final String code) {
    this.code.add(code);
  }

  public Vector<String> getCode() {
    return code;
  }
  
  public void addClassKind(final String classKind) {
    this.classKind.add(classKind);
  }

  public Vector<String> getClassKind() {
    return classKind;
  }
  
  public void addBrowserUrl(final String browserUrl) {
    this.browserUrl.add(browserUrl);
  }

  public Vector<String> getBrowserUrl() {
    return browserUrl;
  }
  
  public void addCodingHint(final String codingHint) {
    this.codingHint.add(codingHint);
  }

  public Vector<String> getCodingHint() {
    return codingHint;
  }
  
  public void addCodingNote(final String codingNote) {
    this.codingNote.add(codingNote);
  }

  public Vector<String> getCodingNote() {
    return codingNote;
  }
  
  public void addReleaseDate(final String ReleaseDate) {
    this.releaseDate.add(ReleaseDate);
  }

  public Vector<String> getReleaseDate() {
    return releaseDate;
  }
}
