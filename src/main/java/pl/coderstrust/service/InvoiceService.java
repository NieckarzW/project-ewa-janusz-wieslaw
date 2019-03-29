package pl.coderstrust.service;

import java.util.Collection;
import java.util.Optional;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

public class InvoiceService {

  private Database database;

  public InvoiceService(Database database) {
    if (database == null) {
      throw new IllegalArgumentException("Database cannot be null");
    }
    this.database = database;
  }

  public Invoice addInvoice(Invoice invoice) throws ServiceOperationException {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    try {
      Long invoiceId = invoice.getId();
      if (invoiceId != null && database.invoiceExists(invoiceId)) {
        throw new ServiceOperationException("Invoice with given id already exists in database");
      }
      return database.saveInvoice(invoice);
    } catch (DatabaseOperationException e) {
      throw new ServiceOperationException("An error occurred while adding invoice", e);
    }
  }

  public Invoice updateInvoice(Invoice invoice) throws ServiceOperationException {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    try {
      Long invoiceId = invoice.getId();
      if (invoiceId == null || !database.invoiceExists(invoiceId)) {
        throw new ServiceOperationException("Given invoice doesn't exist in database");
      }
      return database.saveInvoice(invoice);
    } catch (DatabaseOperationException e) {
      throw new ServiceOperationException("An error occurred while updating invoice", e);
    }
  }

  public void deleteInvoice(Long id) throws ServiceOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      if (!database.invoiceExists(id)) {
        throw new ServiceOperationException("Invoice with given id doesn't exist in database");
      }
      database.deleteInvoice(id);
    } catch (DatabaseOperationException e) {
      throw new ServiceOperationException("An error occurred while deleting invoice", e);
    }
  }

  public Optional<Invoice> getInvoice(Long id) throws ServiceOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      return database.getInvoice(id);
    } catch (DatabaseOperationException e) {
      throw new ServiceOperationException("An error occurred while retrieving invoice", e);
    }
  }

  public Collection<Invoice> getAllInvoices() throws ServiceOperationException {
    try {
      return database.getAllInvoices();
    } catch (DatabaseOperationException e) {
      throw new ServiceOperationException("An error occurred while retrieving all invoices", e);
    }
  }

  public void deleteAllInvoices() throws ServiceOperationException {
    try {
      database.deleteAllInvoices();
    } catch (DatabaseOperationException e) {
      throw new ServiceOperationException("An error occurred while deleting all invoices", e);
    }
  }
}
