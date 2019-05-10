package pl.coderstrust.database;

public class IdentifierGeneratorException extends Exception {

  public IdentifierGeneratorException() {
  }

  public IdentifierGeneratorException(String message) {
    super(message);
  }

  public IdentifierGeneratorException(String message, Throwable cause) {
    super(message, cause);
  }

  public IdentifierGeneratorException(Throwable cause) {
    super(cause);
  }

  public IdentifierGeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
