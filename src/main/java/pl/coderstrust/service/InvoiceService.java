package pl.coderstrust.service;

import java.util.Collection;
import java.util.Optional;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

public class InvoiceService implements Database {

  private Database database;

  public InvoiceService(Database database) {
    this.database = database;
  }

  @Override
  public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
    database.saveInvoice(invoice);
    return invoice;
  }

  @Override
  public void deleteInvoice(Long id) throws DatabaseOperationException {
    database.deleteInvoice(id);
  }

  @Override
  public Optional<Invoice> getInvoice(Long id) {
    return Optional.empty();
  }

  @Override
  public Collection<Invoice> getAllInvoices() {
    return null;
  }

  @Override
  public void deleteAllInvoices() {

  }

  @Override
  public boolean invoiceExists(Long id) {
    return false;
  }

  @Override
  public long countInvoices() {
    return 0;
  }
}
