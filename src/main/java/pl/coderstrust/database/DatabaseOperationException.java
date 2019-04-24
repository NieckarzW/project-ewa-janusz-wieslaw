package pl.coderstrust.database;

public class DatabaseOperationException extends Exception {

  public DatabaseOperationException() {
  }

  public DatabaseOperationException(String message) {
    super(message);
  }

  public DatabaseOperationException(String message, Throwable cause) {
    super(message, cause);
  }

  public DatabaseOperationException(Throwable cause) {
    super(cause);
  }

  public DatabaseOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
