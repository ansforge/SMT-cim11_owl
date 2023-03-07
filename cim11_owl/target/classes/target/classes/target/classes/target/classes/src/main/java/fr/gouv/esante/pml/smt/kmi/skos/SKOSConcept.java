package fr.gouv.esante.pml.smt.kmi.skos;

import java.util.Vector;

/**
 * SKOSConcept is a representation of the Concept tag from the SKOS
 */
public class SKOSConcept {
  private String about;
  private final Vector<String> prefLabels;
  private final Vector<String> labels;
  private final Vector<String> altLabels;
  private final Vector<String> broaderTransitive;
  private final Vector<String> narrowerTransitive;
  private final Vector<String> related;
  private final Vector<String> definition;
  private final Vector<String> scopeNote;
  private final Vector<String> exclusion;
  private final Vector<String> inclusion;
  private final Vector<String> narrowerTerm;

  public SKOSConcept(final String about) {
//    if ((about.indexOf("http://") != -1) && (about.indexOf("#") != -1)) {
//      this.about = about.substring(about.indexOf("#") + 1, about.length());
//    } else if ((about.indexOf("http://") != -1) && (about.indexOf("#") == -1)) {
//      this.about = about.substring(about.lastIndexOf("/") + 1, about.length());
//    } else {
      this.about = about;//.replaceFirst("#", "");
//    }

    this.prefLabels = new Vector<String>();
    this.labels = new Vector<String>();
    this.altLabels = new Vector<String>();
    this.broaderTransitive = new Vector<String>();
    this.narrowerTransitive = new Vector<String>();
    this.related = new Vector<String>();
    this.definition = new Vector<String>();
    this.scopeNote = new Vector<String>();
    this.exclusion = new Vector<String>();
    this.inclusion = new Vector<String>();
    this.narrowerTerm = new Vector<String>();
  }

  public void setAbout(final String about) {
//    if ((about.indexOf("http://") != -1) && (about.indexOf("#") != -1)) {
//      this.about = about.substring(about.indexOf("#") + 1, about.length());
//    } else if ((about.indexOf("http://") != -1) && (about.indexOf("#") == -1)) {
//      this.about = about.substring(about.lastIndexOf("/") + 1, about.length());
//    } else {
      this.about = about;//.replaceFirst("#", "");
//    }
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
  
  public void addLabel(final String label) {
    this.labels.add(label);
  }

  public Vector<String> getLabels() {
    return this.labels;
  }

  public void addAltLabel(final String altLabel) {
    this.altLabels.add(altLabel);
  }

  public Vector<String> getAltLabels() {
    return this.altLabels;
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

  public void addRelated(final String related) {
    this.related.add(related);
  }

  public Vector<String> getRelated() {
    return this.related;
  }

  public void addDefinition(final String definition) {
    this.definition.add(definition);
  }

  public Vector<String> getDefinition() {
    return this.definition;
  }

  public void addScopeNote(final String scopeNote) {
    this.scopeNote.add(scopeNote);
  }

  public Vector<String> getScopeNote() {
    return this.scopeNote;
  }

  public void addExclusion(final String exclusion) {
    this.exclusion.add(exclusion);
  }

  public Vector<String> getExclusion() {
    return this.exclusion;
  }

  public void addNarrowerTerm(final String narrowerTerm) {
    this.narrowerTerm.add(narrowerTerm);
  }

  public Vector<String> getNarrowerTerm() {
    return this.narrowerTerm;
  }

  public void addInclusion(final String inclusion) {
    this.inclusion.add(inclusion);
  }

  public Vector<String> getInclusion() {
    return this.inclusion;
  }
}
