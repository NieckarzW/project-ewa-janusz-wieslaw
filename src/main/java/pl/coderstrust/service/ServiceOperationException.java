package pl.coderstrust.service;

import pl.coderstrust.database.DatabaseOperationException;

public class ServiceOperationException extends DatabaseOperationException {
  public ServiceOperationException() {
  }

  public ServiceOperationException(String message) {
    super(message);
  }

  public ServiceOperationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceOperationException(Throwable cause) {
    super(cause);
  }

  public ServiceOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
