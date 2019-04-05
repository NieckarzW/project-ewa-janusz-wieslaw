package pl.coderstrust.database;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.dao.EmptyResultDataAccessException;
import pl.coderstrust.model.Invoice;

public class HibernateDatabase implements Database {

  private HibernateInvoiceRepository repository;

  public HibernateDatabase(HibernateInvoiceRepository repository) throws IllegalArgumentException {
    if (repository == null) {
      throw new IllegalArgumentException("Repository cannot be null");
    }
    this.repository = repository;
  }


  @Override
  public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    if (repository == null) {
      throw new DatabaseOperationException("Repository cannot be empty");
    }
    try {
      repository.save(invoice);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
    return repository.save(invoice);
  }

  @Override
  public void deleteInvoice(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    repository.deleteById(id);
    if (repository == null) {
      throw new DatabaseOperationException("Repository cannot be empty");
    }
  }

  @Override
  public Optional<Invoice> getInvoice(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    if (repository == null) {
      throw new DatabaseOperationException("Repository cannot be empty");
    }
    return Optional.empty();
  }

  @Override
  public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
    if (repository == null) {
      throw new DatabaseOperationException("Repository cannot be empty");
    }
    Iterable<Invoice> invoices = repository.findAll();
    return StreamSupport.stream(invoices.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public void deleteAllInvoices() throws DatabaseOperationException {
    if (repository == null) {
      throw new DatabaseOperationException("Repository cannot be empty");
    }
    repository.deleteAll();
  }

  @Override
  public boolean invoiceExists(Long id) throws DatabaseOperationException {
    if (repository == null) {
      throw new DatabaseOperationException("Repository cannot be empty");
    }
    return repository.existsById(id);
  }

  @Override
  public long countInvoices() throws DatabaseOperationException {
    if (repository == null) {
      throw new DatabaseOperationException("Repository cannot be empty");
    }
    return repository.count();
  }
}
