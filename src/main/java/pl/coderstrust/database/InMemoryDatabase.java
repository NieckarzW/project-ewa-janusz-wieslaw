package pl.coderstrust.database;


import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.coderstrust.controller.InvoiceController;
import pl.coderstrust.model.Invoice;

public class InMemoryDatabase implements Database {

  private static Logger logger = LoggerFactory.getLogger(InMemoryDatabase.class);
  private final Map<Long, Invoice> databaseStorage;
  private AtomicLong nextId = new AtomicLong(0);

  public InMemoryDatabase(Map<Long, Invoice> databaseStorage) {
    if (databaseStorage == null) {
      throw new IllegalArgumentException("Invoice storage cannot be null");
    }
    this.databaseStorage = databaseStorage;
  }

  @Override
  public synchronized Invoice saveInvoice(Invoice invoice) {
    logger.debug("Saving an invoice.");
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    if (invoice.getId() == null || !databaseStorage.containsKey(invoice.getId())) {
      return insertInvoice(invoice);
    }
    return updateInvoice(invoice);
  }

  @Override
  public synchronized void deleteInvoice(Long id) throws DatabaseOperationException {

    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    if (!databaseStorage.containsKey(id)) {
      throw new DatabaseOperationException(String.format("There was no invoice in database with id: %s", id));
    }
    databaseStorage.remove(id);
    logger.debug("Deleting an invoice with id: %s", id);
  }

  @Override
  public synchronized Optional<Invoice> getInvoice(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    logger.debug("Getting an invoice with id: %s", id);
    return Optional.ofNullable(databaseStorage.get(id));
  }

  @Override
  public synchronized Collection<Invoice> getAllInvoices() {
    logger.debug("Getting all invoices");
    return databaseStorage.values();
  }

  @Override
  public synchronized void deleteAllInvoices() {
    logger.debug("Deleting all invoices");
    databaseStorage.clear();
  }

  @Override
  public synchronized boolean invoiceExists(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    logger.debug("Checking if invoice with id: %s exists");
    return databaseStorage.containsKey(id);
  }

  @Override
  public synchronized long countInvoices() {
    logger.debug("Counting invoices");
    return getAllInvoices().size();
  }

  private Invoice insertInvoice(Invoice invoice) {
    Long id = nextId.incrementAndGet();
    Invoice insertedInvoice = new Invoice(id, invoice.getNumber(), invoice.getIssueDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
    databaseStorage.put(id, insertedInvoice);
    logger.debug("Inserting an invoice with id: %s", id);
    return insertedInvoice;
  }

  private Invoice updateInvoice(Invoice invoice) {
    Invoice updatedInvoice = new Invoice(invoice.getId(), invoice.getNumber(), invoice.getIssueDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
    databaseStorage.put(invoice.getId(), updatedInvoice);
    logger.debug("Updating an invoice with id: %s");
    return updatedInvoice;
  }
}
