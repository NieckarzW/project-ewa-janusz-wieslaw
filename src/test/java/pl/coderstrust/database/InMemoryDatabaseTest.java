package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;


class InMemoryDatabaseTest {
  private HashMap<Long, Invoice> databaseStorage;
  private InMemoryDatabase database;
  private Invoice invoiceWithId;

  @BeforeEach
  public void setUp() {
    databaseStorage = new HashMap<>();
    database = new InMemoryDatabase(databaseStorage);
    invoiceWithId = InvoiceGenerator.getRandomInvoice();
  }

  @Test
  @DisplayName("Illegal argument exception")
  void shouldThrowIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new InMemoryDatabase(null));
  }

  @Test
  @DisplayName("Add invoice")
  void shouldAddInvoice() {
    //when
    Invoice addedInvoice = database.saveInvoice(invoiceWithId);

    //then
    assertNotNull(addedInvoice.getId());
    assertEquals(1, (long) addedInvoice.getId());
    assertEquals(databaseStorage.get(addedInvoice.getId()), addedInvoice);
    assertTrue(invoicesAreSame(invoiceWithId, addedInvoice));
  }

  @Test
  @DisplayName("Update invoice")
  void shouldUpdateInvoice() {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    databaseStorage.put(invoice.getId(), invoice);
    Map<Long, Invoice> expected = ImmutableMap.of(invoice.getId(), invoice);

    //when
    Invoice updatedInvoice = database.saveInvoice(invoice);

    //then
    assertEquals(databaseStorage.get(invoice.getId()), updatedInvoice);
    assertNotEquals(expected, invoice);
  }

  @Test
  @DisplayName("Thrown exception for invoice with null id")
  void saveInvoiceMethodShouldThrowExceptionForNullInvoice() {
    assertThrows(IllegalArgumentException.class, () -> database.saveInvoice(null));
  }

  @Test
  @DisplayName("Delete invoice, exist id")
  void shouldDeleteInvoice() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    databaseStorage.put(invoice1.getId(), invoice1);
    databaseStorage.put(invoice2.getId(), invoice2);
    Map<Long, Invoice> expected = ImmutableMap.of(invoice2.getId(), invoice2);

    //when
    database.deleteInvoice(invoice1.getId());

    //then
    assertEquals(expected, databaseStorage);
  }

  @Test
  @DisplayName("Delete Invoice with null id")
  void deleteInvoiceMethodShouldThrowExceptionForNullId() {
    assertThrows(IllegalArgumentException.class, () -> database.deleteInvoice(null));
  }

  @Test
  @DisplayName("Delete Invoice during deleting invoice that isn't existing")
  void deleteInvoiceMethodShouldThrowExceptionDuringDeletingNotExistingInvoice() {
    assertThrows(DatabaseOperationException.class, () -> database.deleteInvoice(2L));
  }

  @Test
  @DisplayName("Get invoice")
  void shouldReturnInvoice() {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    databaseStorage.put(invoice1.getId(), invoice1);
    databaseStorage.put(invoice2.getId(), invoice2);

    //when
    Optional<Invoice> optionalInvoice = database.getInvoice(invoice1.getId());

    //then
    assertTrue(optionalInvoice.isPresent());
    assertEquals(invoice1, optionalInvoice.get());
  }

  @Test
  @DisplayName("Get Invoice and throw exception for null Id")
  void getInvoiceMethodShouldThrowExceptionForNullId() {
    assertThrows(IllegalArgumentException.class, () -> database.getInvoice(null));
  }

  @Test
  @DisplayName("Return Empty Optional When Invoice Does Not Exist")
  void shouldReturnEmptyOptionalWhenInvoiceDoesNotExist() {
    Optional<Invoice> optionalInvoice = database.getInvoice(1111L);
    assertFalse(optionalInvoice.isPresent());
  }

  @Test
  @DisplayName("Return all invoices")
  void shouldReturnAllInvoices() {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    databaseStorage.put(invoice1.getId(), invoice1);
    databaseStorage.put(invoice2.getId(), invoice2);

    //when
    Collection<Invoice> invoices = database.getAllInvoices();

    //then
    assertEquals(databaseStorage.values(), invoices);
  }

  @Test
  @DisplayName("Delete all invoices")
  void shouldDeleteAllInvoices() {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    databaseStorage.put(invoice1.getId(), invoice1);
    databaseStorage.put(invoice2.getId(), invoice2);

    //when
    database.deleteAllInvoices();

    //then
    assertEquals(new HashMap<>(), databaseStorage);
  }

  @Test
  @DisplayName("Return true for existing invoice")
  void shouldReturnTrueForExistingInvoice() {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    databaseStorage.put(invoice.getId(), invoice);

    //then
    assertTrue(database.invoiceExists(invoice.getId()));
  }

  @Test
  @DisplayName("Return fasle, inovice not exist")
  void shouldReturnFalseForNonExistingInvoice() {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    databaseStorage.put(invoice.getId(), invoice);

    //then
    assertFalse(database.invoiceExists(invoice.getId() + 1L));
  }

  @Test
  @DisplayName("Throw exception, invoce exist, null id")
  void invoiceExistsMethodShouldThrowExceptionForNullId() {
    assertThrows(IllegalArgumentException.class, () -> database.invoiceExists(null));
  }

  @Test
  @DisplayName("Return number of invoices")
  void shouldReturnNumberOfInvoices() {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice3 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice4 = InvoiceGenerator.getRandomInvoice();
    databaseStorage.put(invoice1.getId(), invoice1);
    databaseStorage.put(invoice2.getId(), invoice2);
    databaseStorage.put(invoice3.getId(), invoice3);
    databaseStorage.put(invoice4.getId(), invoice4);

    //when
    Collection<Invoice> invoices = database.getAllInvoices();

    //then
    assertEquals(4, invoices.size());
  }

  private boolean invoicesAreSame(Invoice invoiceToAdd, Invoice addedInvoice) {
    return (invoiceToAdd.getBuyer() == addedInvoice.getBuyer()
        && invoiceToAdd.getSeller() == addedInvoice.getSeller()
        && invoiceToAdd.getDueDate() == addedInvoice.getDueDate()
        && invoiceToAdd.getIssueDate() == addedInvoice.getIssueDate()
        && invoiceToAdd.getNumber().equals(addedInvoice.getNumber())
        && invoiceToAdd.getEntries().equals(addedInvoice.getEntries()));
  }
}
