package pl.coderstrust.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import pl.coderstrust.helpers.FileHelper;
import pl.coderstrust.helpers.InvoiceToStringHelper;
import pl.coderstrust.model.Invoice;

public class InFileDatabase implements Database {

  private final String DATABASE_FILE_PATH = "src\\main\\resources\\InFileDatabase";
  private final String LAST_ID_FILE_PATH = "src\\main\\resources\\InFileDatabaseLastInvoiceId";

  private FileHelper fileHelper;
  private ObjectMapper objectMapper;
  private InvoiceToStringHelper invoiceToStringHelper;

  @Autowired
  public InFileDatabase(FileHelper fileHelper, ObjectMapper objectMapper, InvoiceToStringHelper invoiceToStringHelper) {
    this.fileHelper = fileHelper;
    this.objectMapper = objectMapper;
    this.invoiceToStringHelper = invoiceToStringHelper;
  }

  @Override
  public synchronized Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    try {
      if (invoice.getId() == null || !invoiceExists(invoice.getId())) {
        return insertInvoice(invoice);
      }
    } catch (DatabaseOperationException e) {
      throw new DatabaseOperationException(String.format("Encountered problems while saving invoice: %s", invoice), e);
    }
    return updateInvoice(invoice);
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
      List<Invoice> invoices = invoiceToStringHelper.listOfStringsToListOfInvoices(fileHelper.readLines(DATABASE_FILE_PATH));
      invoices.removeIf(invoice -> invoice.getId().equals(id));
      fileHelper.clear(DATABASE_FILE_PATH);
      invoices.forEach(invoice -> fileHelper.writeLine(invoiceToStringHelper.invoiceToString(invoice)));
    } catch (IOException e) {
      throw new DatabaseOperationException(String.format("Encountered problems while deleting invoice with id: %s", id), e);
    }
  }

  @Override
  public synchronized Optional<Invoice> getInvoice(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      List<Invoice> invoices = invoiceToStringHelper.listOfStringsToListOfInvoices(fileHelper.readLines(DATABASE_FILE_PATH));
      return invoices.stream().filter(invoice -> invoice.getId().equals(id)).findFirst();
    } catch (IOException e) {
      throw new DatabaseOperationException("Error while getting invoice from In File Database.", e);
    }
  }

  @Override
  public synchronized Collection<Invoice> getAllInvoices() {
    return databaseStorage.values();
  }

  @Override
  public synchronized void deleteAllInvoices() {
    databaseStorage.clear();
  }

  @Override
  public synchronized boolean invoiceExists(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      List<Invoice> invoices = invoiceToStringHelper.listOfStringsToListOfInvoices(fileHelper.readLines(DATABASE_FILE_PATH));
      return invoices.stream().anyMatch(invoice -> invoice.getId().equals(id));
    } catch (IOException e) {
      throw new DatabaseOperationException("Error while checking if invoice exists in In File Database.", e);
    }
  }

  @Override
  public synchronized long countInvoices() {
    return getAllInvoices().size();
  }

  private Invoice insertInvoice(Invoice invoice) throws IOException {
    Long id = Long.valueOf(fileHelper.readLastLine(LAST_ID_FILE_PATH)) + 1;
    fileHelper.clear(LAST_ID_FILE_PATH);
    fileHelper.writeLine(LAST_ID_FILE_PATH, id.toString());
    Invoice insertedInvoice = new Invoice(id, invoice.getNumber(), invoice.getIssueDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
    fileHelper.writeLine(DATABASE_FILE_PATH, invoiceToStringHelper.invoiceToString(insertedInvoice));
    return insertedInvoice;
  }

  private Invoice updateInvoice(Invoice invoice) {
    Invoice updatedInvoice = new Invoice(invoice.getId(), invoice.getNumber(), invoice.getIssueDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
    List<Invoice> invoices = invoiceToStringHelper.listOfStringsToListOfInvoices(fileHelper.readLines(DATABASE_FILE_PATH));


    Invoice oldInvoice = invoices.stream().filter(invoice1 -> invoice1.getId().equals(updatedInvoice.getId())).findFirst();
    Collections.replaceAll(invoices, oldInvoice, updatedInvoice);
    fileHelper.clear(DATABASE_FILE_PATH);
    fileHelper.writeLine();
    return updatedInvoice;
  }
}
