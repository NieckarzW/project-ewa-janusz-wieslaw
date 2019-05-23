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
      logger.debug("Adding invoice: {}", invoice);
      Long invoiceId = invoice.getId();
      if (invoiceId != null && database.invoiceExists(invoiceId)) {
        throw new ServiceOperationException("Invoice with given id already exists in database.");
      }
      return database.saveInvoice(invoice);
    } catch (DatabaseOperationException e) {
      String message = String.format("Encountered problems while adding invoice: %s", invoice);
      logger.error(message, e);
      throw new ServiceOperationException(message, e);
    }
  }

  public Invoice updateInvoice(Invoice invoice) throws ServiceOperationException {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null.");
    }
    try {
      logger.debug("Updating invoice: {}", invoice);
      Long invoiceId = invoice.getId();
      if (invoiceId == null || !database.invoiceExists(invoiceId)) {
        throw new ServiceOperationException("Given invoice doesn't exist in database.");
      }
      return database.saveInvoice(invoice);
    } catch (DatabaseOperationException e) {
      String message = String.format("Encountered problems while updating invoice: %s", invoice);
      logger.error(message, e);
      throw new ServiceOperationException(message, e);
    }
  }

  public void deleteInvoice(Long id) throws ServiceOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null.");
    }
    try {
      logger.debug("Deleting invoice with following id: {}", id);
      if (!database.invoiceExists(id)) {
        String message = String.format("Invoice with following id: %d does not exist in database.", id);
        logger.error(message);
        throw new ServiceOperationException(message);
      }
      database.deleteInvoice(id);
    } catch (DatabaseOperationException e) {
      String message = String.format("Encountered problems while removing invoice with following id: %d", id);
      logger.error(message, e);
      throw new ServiceOperationException(message, e);
    }
  }

  public Optional<Invoice> getInvoice(Long id) throws ServiceOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null.");
    }
    try {
      logger.debug("Getting invoice with following id {}", id);
      return database.getInvoice(id);
    } catch (DatabaseOperationException e) {
      String message = String.format("Encountered problems while getting invoice with following id: %d", id);
      logger.error(message, e);
      throw new ServiceOperationException(message, e);
    }
  }

  public Collection<Invoice> getAllInvoices() throws ServiceOperationException {
    try {
      logger.debug("Getting all invoices");
      return database.getAllInvoices();
    } catch (DatabaseOperationException e) {
      String message = "Encountered problems while getting all invoices";
      logger.error(message, e);
      throw new ServiceOperationException(message, e);
    }
  }

  public void deleteAllInvoices() throws ServiceOperationException {
    try {
      logger.debug("Deleting all invoices");
      database.deleteAllInvoices();
    } catch (DatabaseOperationException e) {
      String message = "Encountered problem while deleting all invoices";
      logger.error(message, e);
      throw new ServiceOperationException(message, e);
    }
  }

  public boolean invoiceExists(Long id) throws ServiceOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null.");
    }
    try {
      logger.debug("Checking if invoice with following id: {} exists.", id);
      return database.invoiceExists(id);
    } catch (DatabaseOperationException e) {
      String message = String.format("Encountered problems while checking if invoice with following id: %d exists", id);
      logger.error(message, e);
      throw new ServiceOperationException(message, e);
    }
  }

  public Optional<Invoice> getInvoiceByNumber(String number) throws ServiceOperationException {
    if (number == null) {
      throw new IllegalArgumentException("Number cannot be null.");
    }
    try {
      logger.debug("Getting invoice with following number {}", number);
      return database.getAllInvoices()
          .stream()
          .filter(invoice -> invoice.getNumber().equals(number))
          .findFirst();
    } catch (DatabaseOperationException e) {
      String message = String.format("Encountered problems while getting invoice with following number: %s", number);
      logger.error(message, e);
      throw new ServiceOperationException(message, e);
    }
  }
}
