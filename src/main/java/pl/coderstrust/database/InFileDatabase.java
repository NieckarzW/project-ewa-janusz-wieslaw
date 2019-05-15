package pl.coderstrust.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import pl.coderstrust.configuration.InFileDatabaseProperties;
import pl.coderstrust.helpers.FileHelper;
import pl.coderstrust.model.Invoice;

public class InFileDatabase implements Database {

  private FileHelper fileHelper;
  private ObjectMapper objectMapper;
  private InFileDatabaseProperties inFileDatabaseProperties;
  private IdentifierGenerator identifierGenerator;

  @Autowired
  public InFileDatabase(FileHelper fileHelper, ObjectMapper objectMapper, InFileDatabaseProperties inFileDatabaseProperties, IdentifierGenerator identifierGenerator) throws IOException {
    if (fileHelper == null) {
      throw new IllegalArgumentException("File helper cannot be null");
    }
    if (objectMapper == null) {
      throw new IllegalArgumentException("Object mapper cannot be null");
    }
    if (inFileDatabaseProperties == null) {
      throw new IllegalArgumentException("Database properties cannot be null");
    }
    if (identifierGenerator == null) {
      throw new IllegalArgumentException("Identifier Generator cannot be null");
    }
    this.fileHelper = fileHelper;
    this.objectMapper = objectMapper;
    this.inFileDatabaseProperties = inFileDatabaseProperties;
    this.identifierGenerator = identifierGenerator;
    if (!this.fileHelper.exists(inFileDatabaseProperties.getFilePath())) {
      this.fileHelper.create(inFileDatabaseProperties.getFilePath());
    }
    this.identifierGenerator.initialize(getLastInvoiceId());
  }

  @Override
  public synchronized Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    try {
      if (invoice.getId() == null || !isInvoiceExist(invoice.getId())) {
        return insertInvoice(invoice);
      }
      return updateInvoice(invoice);
    } catch (IdentifierGeneratorException | IOException e) {
      throw new DatabaseOperationException(String.format("Encountered problems while saving invoice: %s", invoice), e);
    }
  }

  @Override
  public synchronized void deleteInvoice(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    if (!invoiceExists(id)) {
      throw new DatabaseOperationException(String.format("There was no invoice in database with id: %s", id));
    }
    try {
      deleteInvoiceById(id);
    } catch (IOException e) {
      throw new DatabaseOperationException("An error occurred during deleting invoice");
    }
  }

  @Override
  public synchronized Optional<Invoice> getInvoice(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      return getInvoices()
          .stream()
          .filter(invoice -> invoice.getId().equals(id))
          .findFirst();
    } catch (IOException e) {
      throw new DatabaseOperationException("An error occurred during getting invoice", e);
    }
  }

  @Override
  public synchronized List<Invoice> getAllInvoices() throws DatabaseOperationException {
    try {
      return getInvoices();
    } catch (IOException e) {
      throw new DatabaseOperationException("An error occurred during getting all invoices", e);
    }
  }

  @Override
  public synchronized void deleteAllInvoices() throws DatabaseOperationException {
    try {
      fileHelper.clear(inFileDatabaseProperties.getFilePath());
    } catch (IOException e) {
      throw new DatabaseOperationException("An error occurred during deleting all invoices", e);
    }
  }

  @Override
  public synchronized boolean invoiceExists(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      return isInvoiceExist(id);
    } catch (IOException e) {
      throw new DatabaseOperationException("An error occurred during checking if invoice exists", e);
    }
  }

  @Override
  public synchronized long countInvoices() throws DatabaseOperationException {
    try {
      return getInvoices().size();
    } catch (IOException e) {
      throw new DatabaseOperationException("An error occurred while counting invoices", e);
    }
  }

  private Invoice insertInvoice(Invoice invoice) throws IdentifierGeneratorException, IOException {
    Invoice invoiceToInsert = new Invoice(identifierGenerator.getNextId(), invoice.getNumber(), invoice.getIssueDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
    String invoiceAsJson = deserializeInvoiceToJson(invoiceToInsert);
    fileHelper.writeLine(inFileDatabaseProperties.getFilePath(), invoiceAsJson);
    return invoiceToInsert;
  }

  private Invoice updateInvoice(Invoice invoice) throws DatabaseOperationException, IOException {
    Invoice invoiceToUpdate = new Invoice(invoice.getId(), invoice.getNumber(), invoice.getIssueDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
    deleteInvoiceById(invoice.getId());
    String invoiceAsJson = deserializeInvoiceToJson(invoiceToUpdate);
    fileHelper.writeLine(inFileDatabaseProperties.getFilePath(), invoiceAsJson);
    return invoiceToUpdate;
  }

  private long getLastInvoiceId() throws IOException {
    String lastInvoiceAsJson = fileHelper.readLastLine(inFileDatabaseProperties.getFilePath());
    if (lastInvoiceAsJson == null) {
      return 0;
    }
    Invoice invoice = deserializeJsonToInvoice(lastInvoiceAsJson);
    if (invoice == null) {
      return 0;
    }
    return invoice.getId();
  }

  private Invoice deserializeJsonToInvoice(String json) {
    try {
      return objectMapper.readValue(json, Invoice.class);
    } catch (IOException e) {
      return null;
    }
  }

  private String deserializeInvoiceToJson(Invoice invoice) throws JsonProcessingException {
    return objectMapper.writeValueAsString(invoice);
  }

  private List<Invoice> getInvoices() throws IOException {
    return fileHelper.readLines(inFileDatabaseProperties.getFilePath())
        .stream()
        .map(this::deserializeJsonToInvoice)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private boolean isInvoiceExist(long id) throws IOException {
    return getInvoices()
        .stream()
        .anyMatch(invoice -> invoice.getId().equals(id));
  }

  private void deleteInvoiceById(long id) throws IOException, DatabaseOperationException {
    List<Invoice> invoices = getInvoices();
    Optional<Invoice> invoice = invoices
        .stream()
        .filter(i -> i.getId().equals(id))
        .findFirst();
    if (invoice.isPresent()) {
      fileHelper.removeLine(inFileDatabaseProperties.getFilePath(), invoices.indexOf(invoice.get()) + 1);
    } else {
      throw new DatabaseOperationException("Invoice with following id doesn't exist");
    }
  }
}
