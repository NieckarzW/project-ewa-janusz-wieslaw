package pl.coderstrust.database;

import java.util.Collection;
import java.util.Optional;

import pl.coderstrust.model.Invoice;

public class HibernateDatabase implements Database{

  private HibernateInvoiceRepository repository;

  public HibernateDatabase(HibernateInvoiceRepository repository) {
    this.repository = repository;
  }


  @Override
  public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    return repository.save(invoice);
  }

  @Override
  public void deleteInvoice(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    repository.deleteById(id);
  }

  @Override
  public Optional<Invoice> getInvoice(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    return Optional.empty();
  }

  @Override
  public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
    return repository.
  }

  @Override
  public void deleteAllInvoices() throws DatabaseOperationException {
  repository.deleteAll(invoice);
  }

  @Override
  public boolean invoiceExists(Long id) throws DatabaseOperationException {
    return false;
  }

  @Override
  public long countInvoices() throws DatabaseOperationException {
    return 0;
  }
}
