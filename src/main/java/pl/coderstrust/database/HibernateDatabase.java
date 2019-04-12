package pl.coderstrust.database;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Repository;
import pl.coderstrust.model.Invoice;

@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "hibernate")
@Repository
public class HibernateDatabase implements Database {

  private HibernateInvoiceRepository repository;

  @Autowired
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
    try {
      return repository.save(invoice);
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException(String.format("Encountered problems saving invoice: %s", invoice), e);
    }
  }

  @Override
  public void deleteInvoice(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      repository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new DatabaseOperationException(String.format("There was no invoice in database with id %s", id), e);
    }
  }

  @Override
  public Optional<Invoice> getInvoice(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      return repository.findById(id);
    } catch (NoSuchElementException e) {
      throw new DatabaseOperationException(String.format("Encountered problems while searching for invoice:, %s", id), e);
    }
  }

  @Override
  public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
    try {
      return repository.findAll();
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException("Encountered problems while searching for invoices.", e);
    }
  }

  @Override
  public void deleteAllInvoices() throws DatabaseOperationException {
    try {
      repository.deleteAll();
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException("Encountered problem while deleting invoices.", e);
    }
  }

  @Override
  public boolean invoiceExists(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null.");
    }
    try {
      return repository.existsById(id);
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException(String.format("Encountered problems looking for invoice: %s", id), e);
    }
  }

  @Override
  public long countInvoices() throws DatabaseOperationException {
    try {
      return repository.count();
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException("Encountered problems while counting invoices.", e);
    }
  }
}
