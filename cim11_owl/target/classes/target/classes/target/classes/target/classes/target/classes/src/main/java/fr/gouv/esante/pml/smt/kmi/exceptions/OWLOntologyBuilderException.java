package fr.gouv.esante.pml.smt.kmi.exceptions;

public class OWLOntologyBuilderException extends Exception {
  /**
   *
   */
  private static final long serialVersionUID = 5371317965707286424L;

  public OWLOntologyBuilderException(final String message, final Exception ex) {
    super(message, ex);
  }
}
