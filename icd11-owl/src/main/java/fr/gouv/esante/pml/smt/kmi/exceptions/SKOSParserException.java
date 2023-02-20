package fr.gouv.esante.pml.smt.kmi.exceptions;

public class SKOSParserException extends Exception {
  /**
   *
   */
  private static final long serialVersionUID = 6101235816268150164L;

  public SKOSParserException(final String message, final Exception ex) {
    super(message, ex);
  }
}
