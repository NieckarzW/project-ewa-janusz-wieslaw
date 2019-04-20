package pl.coderstrust.database;

import org.springframework.dao.NonTransientDataAccessException;
import pl.coderstrust.model.Invoice;

public class DatabaseOperationException extends Exception {

  public DatabaseOperationException(Invoice invoice, NonTransientDataAccessException e) {
  }

  public DatabaseOperationException(String message) {
    super(message);
  }

  public DatabaseOperationException(String message, Throwable cause) {
    super(message, cause);
  }

  public DatabaseOperationException(String message, Invoice invoice, Throwable cause) {
    super(cause);
  }

  public DatabaseOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
