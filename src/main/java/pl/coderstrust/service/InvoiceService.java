package pl.coderstrust.service;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

@Service
public class InvoiceService {

  private static Logger logger = LoggerFactory.getLogger(InvoiceService.class);
  private Database database;

  @Autowired
  public InvoiceService(Database database) {
    if (database == null) {
      throw new IllegalArgumentException("Database cannot be null.");
    }
    this.database = database;
  }

  public Invoice addInvoice(Invoice invoice) throws ServiceOperationException {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null.");
    }
    try {
      Long invoiceId = invoice.getId();
      if (invoiceId != null && database.invoiceExists(invoiceId)) {
        throw new ServiceOperationException("Invoice with given id already exists in database.");
      }
      logger.debug("Saving invoice: {}", invoice);
      return database.saveInvoice(invoice);

    } catch (DatabaseOperationException e) {
      String message = String.format("Encountered problems while adding invoice: %s", invoice);
      logger.error(message);
      throw new ServiceOperationException(message, e);
    }
  }

  public Invoice updateInvoice(Invoice invoice) throws ServiceOperationException {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null.");
    }
    try {
      Long invoiceId = invoice.getId();
      if (invoiceId == null || !database.invoiceExists(invoiceId)) {
        throw new ServiceOperationException("Given invoice doesn't exist in database.");
      }
      logger.debug("Updating invoice: {}", invoice);
      return database.saveInvoice(invoice);
    } catch (DatabaseOperationException e) {
      String message = String.format("An error occurred while updating invoice: %s", invoice);
      logger.error(message);
      throw new ServiceOperationException(message, e);
    }
  }

  public void deleteInvoice(Long id) throws ServiceOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null.");
    }
    try {
      if (!database.invoiceExists(id)) {
        throw new ServiceOperationException("Invoice with given id doesn't exist in database.");
      }
      database.deleteInvoice(id);
    } catch (DatabaseOperationException e) {
      String message = String.format("An error occurred while deleting invoice: %s");
      logger.error(message);
      throw new ServiceOperationException(message, e);
    }
  }

  public Optional<Invoice> getInvoice(Long id) throws ServiceOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null.");
    }
    try {
      logger.debug("Getting an invoice");
      return database.getInvoice(id);
    } catch (DatabaseOperationException e) {
      String message = String.format("An error occurred while retrieving invoice", e);
      logger.error(message);
      throw new ServiceOperationException(message, e);
    }
  }

  public Collection<Invoice> getAllInvoices() throws ServiceOperationException {
    try {
      logger.debug("Getting all invoices");
      return database.getAllInvoices();
    } catch (DatabaseOperationException e) {
      String message = String.format("An error occurred while retrieving all invoices.");
      logger.error(message);
      throw new ServiceOperationException(message, e);
    }
  }

  public void deleteAllInvoices() throws ServiceOperationException {
    try {
      database.deleteAllInvoices();
      logger.debug("Deleting all invoices");
    } catch (DatabaseOperationException e) {
      String message = String.format("An error occurred while deleting all invoices.");
      logger.error(message);
      throw new ServiceOperationException(message, e);
    }
  }

  public boolean invoiceExists(Long id) throws ServiceOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null.");
    }
    try {
      logger.debug("Checking if invoice exists.");
      return database.invoiceExists(id);
    } catch (DatabaseOperationException e) {
      String message = String.format("An error occurred while checking if invoice exists.");
      logger.error(message);
      throw new ServiceOperationException(message, e);
    }
  }
}
