package pl.coderstrust.database;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Repository;
import pl.coderstrust.model.Invoice;

@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "hibernate")
@Repository
public class HibernateDatabase implements Database {

  private static Logger logger = LoggerFactory.getLogger(HibernateDatabase.class);
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
      logger.debug("Saving invoice: {}", invoice);
      return repository.save(invoice);
    } catch (NonTransientDataAccessException e) {
      String message = String.format("Encountered problems while saving invoice: %s", invoice);
      logger.error(message, e);
      throw new DatabaseOperationException(message, e);
    }
  }

  @Override
  public void deleteInvoice(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      logger.debug("Deleting invoice with following id: {}", id);
      if (!repository.existsById(id)) {
        throw new DatabaseOperationException(String.format("There was no invoice in database with id: %s", id));
      }
      repository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      String message = String.format("Encountered problems while removing invoice with following id: %d", id);
      logger.debug(message, e);
      throw new DatabaseOperationException(message, e);
    }
  }

  @Override
  public Optional<Invoice> getInvoice(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      logger.debug("Getting invoice with following id {}", id);
      return repository.findById(id);
    } catch (NoSuchElementException e) {
      String message = String.format("Encountered problems while getting invoice with following id: %d", id);
      logger.error(message, e);
      throw new DatabaseOperationException(message, e);
    }
  }

  @Override
  public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
    try {
      logger.debug("Getting all invoices");
      return repository.findAll();
    } catch (NonTransientDataAccessException e) {
      String message = "Encountered problems while getting all invoices.";
      logger.error(message, e);
      throw new DatabaseOperationException(message, e);
    }
  }

  @Override
  public void deleteAllInvoices() throws DatabaseOperationException {
    try {
      logger.debug("Deleting all invoices");
      repository.deleteAll();
    } catch (NonTransientDataAccessException e) {
      String message = "Encountered problem while deleting all invoices";
      logger.error(message, e);
      throw new DatabaseOperationException(message, e);
    }
  }

  @Override
  public boolean invoiceExists(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null.");
    }
    try {
      logger.debug("Checking if invoice with following id: {} exists.", id);
      return repository.existsById(id);
    } catch (NonTransientDataAccessException e) {
      String message = String.format("Encountered problems while checking if invoice with following id: %d exists", id);
      logger.error(message, e);
      throw new DatabaseOperationException(message, e);
    }
  }

  @Override
  public long countInvoices() throws DatabaseOperationException {
    try {
      logger.debug("Counting invoices");
      return repository.count();
    } catch (NonTransientDataAccessException e) {
      String message = "Encountered problems while counting invoices.";
      logger.error(message, e);
      throw new DatabaseOperationException(message, e);
    }
  }
}
