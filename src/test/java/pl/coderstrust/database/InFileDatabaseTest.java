package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderstrust.configuration.ApplicationConfiguration;
import pl.coderstrust.configuration.InFileDatabaseProperties;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.helpers.FileHelper;
import pl.coderstrust.model.Invoice;

@ExtendWith(MockitoExtension.class)
class InFileDatabaseTest {

  InFileDatabase database;

  @Mock
  FileHelper fileHelper;

  ObjectMapper mapper;

  InFileDatabaseProperties inFileDatabaseProperties;

  IdentifierGenerator identifierGenerator;

  Invoice invoiceWithId;

  @BeforeEach
  void setUp() throws IOException {
    mapper = new ApplicationConfiguration().getObjectMapper();
    inFileDatabaseProperties = new InFileDatabaseProperties();
    inFileDatabaseProperties.setFilePath("database.txt");
    identifierGenerator = new IdentifierGenerator();
    database = new InFileDatabase(fileHelper, mapper, inFileDatabaseProperties, identifierGenerator);
    invoiceWithId = InvoiceGenerator.getRandomInvoice();
  }

  @Test
  void constructorShouldThrowExceptionForNullAsFileHelper() {
    assertThrows(IllegalArgumentException.class, () -> new InFileDatabase(null, mapper, inFileDatabaseProperties, identifierGenerator));
  }

  @Test
  void constructorShouldThrowExceptionForNullAsObjectMapper() {
    assertThrows(IllegalArgumentException.class, () -> new InFileDatabase(fileHelper, null, inFileDatabaseProperties, identifierGenerator));
  }

  @Test
  void constructorShouldThrowExceptionForNullAsInFileDatabaseProperties() {
    assertThrows(IllegalArgumentException.class, () -> new InFileDatabase(fileHelper, mapper, null, identifierGenerator));
  }

  @Test
  void constructorShouldThrowExceptionForNullAsIdentifierGenerator() {
    assertThrows(IllegalArgumentException.class, () -> new InFileDatabase(fileHelper, mapper, inFileDatabaseProperties, null));
  }

  @Test
  void saveInvoiceMethodShouldThrowExceptionWhenInvoiceIsNull() {
    assertThrows(IllegalArgumentException.class, () -> database.saveInvoice(null));
  }

  @Test
  void getInvoiceMethodShouldThrowExceptionWhenIdIsNull() {
    assertThrows(IllegalArgumentException.class, () -> database.getInvoice(null));
  }

  @Test
  void invoiceExistsMethodShouldThrowExceptionWhenIdIsNull() {
    assertThrows(IllegalArgumentException.class, () -> database.invoiceExists(null));
  }

  @Test
  void deleteInvoiceMethodShouldThrowExceptionWhenIdIsNull() {
    assertThrows(IllegalArgumentException.class, () -> database.deleteInvoice(null));
  }

  @Test
  void shouldReturnNumberOfInvoices() throws IOException, DatabaseOperationException {
    //given
    List<String> invoicesInDatabaseFile = new ArrayList<>();
    invoicesInDatabaseFile.add(mapper.writeValueAsString(InvoiceGenerator.getRandomInvoice()));
    invoicesInDatabaseFile.add(mapper.writeValueAsString(InvoiceGenerator.getRandomInvoice()));
    when(fileHelper.isEmpty(inFileDatabaseProperties.getFilePath())).thenReturn(false);
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(invoicesInDatabaseFile);
    long expectedInvoicesCount = invoicesInDatabaseFile.size();

    //when
    long actualInvoicesCount = database.countInvoices();

    //then
    assertEquals(expectedInvoicesCount, actualInvoicesCount);
    verify(fileHelper).isEmpty(inFileDatabaseProperties.getFilePath());
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void shouldReturnZeroWhenDatabaseIsEmpty() throws IOException, DatabaseOperationException {
    //given
    long expected = 0;
    when(fileHelper.isEmpty(inFileDatabaseProperties.getFilePath())).thenReturn(true);

    //when
    long actualInvoices = database.countInvoices();

    //then
    assertEquals(expected, actualInvoices);
    verify(fileHelper).isEmpty(inFileDatabaseProperties.getFilePath());
    verify(fileHelper, never()).readLines(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void shouldReturnZeroWhenDatabaseContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    List<String> invalidInvoices = new ArrayList<>();
    invalidInvoices.add("asdasd");
    long expected = 0;
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(invalidInvoices);
    when(fileHelper.isEmpty(inFileDatabaseProperties.getFilePath())).thenReturn(false);

    //when
    long actual = database.countInvoices();

    //then
    assertEquals(expected, actual);
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
    verify(fileHelper).isEmpty(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void countMethodShouldThrowExceptionWhenFileHelperThrowException() throws IOException {
    //given
    doThrow(FileNotFoundException.class).when(fileHelper).isEmpty(inFileDatabaseProperties.getFilePath());

    //then
    assertThrows(DatabaseOperationException.class, () -> database.countInvoices());
    verify(fileHelper).isEmpty(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void shouldDeleteInvoice() throws IOException, DatabaseOperationException {
    //given
    Invoice invoiceToDelete = InvoiceGenerator.getRandomInvoice();
    List<String> invoicesOfDatabase = new ArrayList<>();
    invoicesOfDatabase.add(mapper.writeValueAsString(InvoiceGenerator.getRandomInvoice()));
    invoicesOfDatabase.add(mapper.writeValueAsString(invoiceToDelete));
    invoicesOfDatabase.add(mapper.writeValueAsString(InvoiceGenerator.getRandomInvoice()));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(invoicesOfDatabase);
    doNothing().when(fileHelper).removeLine(inFileDatabaseProperties.getFilePath(), 2);

    //when
    database.deleteInvoice(invoiceToDelete.getId());

    //then
    verify(fileHelper, times(2)).readLines(inFileDatabaseProperties.getFilePath());
    verify(fileHelper).removeLine(inFileDatabaseProperties.getFilePath(), 2);
  }

  @Test
  void shouldThrowExceptionDuringRemovingInvoiceWhenInvoiceDoesNotExist() throws IOException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    List<String> invoicesOfDatabase = new ArrayList<>();
    invoicesOfDatabase.add(mapper.writeValueAsString(invoice));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(invoicesOfDatabase);

    //then
    assertThrows(DatabaseOperationException.class, () -> database.deleteInvoice(invoice.getId() + 1));
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
    verify(fileHelper, never()).removeLine(anyString(), anyInt());
  }

  @Test
  void deleteByIdMethodShouldThrowExceptionWhenFileHelperThrowException() throws IOException {
    //given
    doThrow(FileNotFoundException.class).when(fileHelper).readLines(inFileDatabaseProperties.getFilePath());

    //then
    assertThrows(DatabaseOperationException.class, () -> database.deleteInvoice(1L));
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void shouldDeleteAllInvoices() throws IOException, DatabaseOperationException {
    //given
    doNothing().when(fileHelper).clear(inFileDatabaseProperties.getFilePath());

    //when
    database.deleteAllInvoices();

    //then
    verify(fileHelper).clear(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void deleteAllInvoicesShouldThrowExceptionWhenFileHelperThrowException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelper).clear(inFileDatabaseProperties.getFilePath());

    //then
    assertThrows(DatabaseOperationException.class, () -> database.deleteAllInvoices());
  }

  @Test
  void shouldReturnAllInvoices() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice3 = InvoiceGenerator.getRandomInvoice();
    List<Invoice> expectedInvoices = new ArrayList<>();
    expectedInvoices.add(invoice1);
    expectedInvoices.add(invoice2);
    expectedInvoices.add(invoice3);
    List<String> invoicesInDatabaseFile = new ArrayList<>();
    invoicesInDatabaseFile.add(mapper.writeValueAsString(invoice1));
    invoicesInDatabaseFile.add(mapper.writeValueAsString(invoice2));
    invoicesInDatabaseFile.add(mapper.writeValueAsString(invoice3));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(invoicesInDatabaseFile);

    //when
    List<Invoice> actualInvoices = database.getAllInvoices();

    //then
    assertNotNull(actualInvoices);
    assertEquals(expectedInvoices, actualInvoices);
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void shouldReturnEmptyListWhenDatabaseIsEmpty() throws IOException, DatabaseOperationException {
    //given
    List<String> invalidInvoices = new ArrayList<>();
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(invalidInvoices);

    //when
    List<Invoice> actualInvoices = database.getAllInvoices();

    //then
    assertNotNull(actualInvoices);
    assertEquals(invalidInvoices, actualInvoices);
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void findAllMethodShouldThrowExceptionWhenFileHelperThrowException() throws IOException {
    //given
    doThrow(FileNotFoundException.class).when(fileHelper).readLines(inFileDatabaseProperties.getFilePath());

    //then
    assertThrows(DatabaseOperationException.class, () -> database.getAllInvoices());
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void shouldReturnTrueWhenInvoiceExists() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    List<String> invoicesList = new ArrayList<>();
    invoicesList.add(mapper.writeValueAsString(invoice));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(invoicesList);

    //when
    boolean invoiceExist = database.invoiceExists(invoice.getId());

    //then
    assertTrue(invoiceExist);
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void shouldReturnFalseWhenInvoiceDoesNotExist() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    List<String> invoicesList = new ArrayList<>();
    invoicesList.add(mapper.writeValueAsString(invoice));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(invoicesList);

    //when
    boolean invoiceExist = database.invoiceExists(invoice.getId() + 1);

    //then
    assertFalse(invoiceExist);
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void existByIdMethodShouldThrowExceptionWhenFileHelperThrowException() throws IOException {
    //given
    doThrow(FileNotFoundException.class).when(fileHelper).readLines(inFileDatabaseProperties.getFilePath());

    //then
    assertThrows(DatabaseOperationException.class, () -> database.invoiceExists(1L));
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void shouldReturnInvoice() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice3 = InvoiceGenerator.getRandomInvoice();
    List<String> invoicesInDatabase = new ArrayList<>();
    invoicesInDatabase.add(mapper.writeValueAsString(invoice1));
    invoicesInDatabase.add(mapper.writeValueAsString(invoice2));
    invoicesInDatabase.add(mapper.writeValueAsString(invoice3));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(invoicesInDatabase);

    //when
    Optional<Invoice> actualInvoice = database.getInvoice(invoice2.getId());

    //then
    assertTrue(actualInvoice.isPresent());
    assertEquals(invoice2, actualInvoice.get());
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void shouldReturnEmptyOptionalWhenInvoiceDoesNotExist() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    List<String> invoicesInDatabase = new ArrayList<>();
    invoicesInDatabase.add(mapper.writeValueAsString(invoice));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(invoicesInDatabase);

    //when
    Optional<Invoice> actualInvoice = database.getInvoice(invoice.getId() + 1);

    //then
    assertFalse(actualInvoice.isPresent());
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void findByIdShouldThrowExceptionWhenFileHelperThrowException() throws IOException {
    //then
    doThrow(FileNotFoundException.class).when(fileHelper).readLines(inFileDatabaseProperties.getFilePath());

    //then
    assertThrows(DatabaseOperationException.class, () -> database.getInvoice(1L));
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void shouldAddInvoice() throws IOException, DatabaseOperationException {
    Invoice invoiceToAdd = InvoiceGenerator.getRandomInvoice();
    Invoice expectedInvoice = new Invoice(1L,
        invoiceToAdd.getNumber(),
        invoiceToAdd.getIssueDate(),
        invoiceToAdd.getDueDate(),
        invoiceToAdd.getSeller(),
        invoiceToAdd.getBuyer(),
        invoiceToAdd.getEntries());

    Invoice invoiceInDatabase1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoiceInDatabase2 = InvoiceGenerator.getRandomInvoice();
    List<String> invoicesInDatabase = new ArrayList<>();
    invoicesInDatabase.add(mapper.writeValueAsString(invoiceInDatabase1));
    invoicesInDatabase.add(mapper.writeValueAsString(invoiceInDatabase2));
    String invoiceToAddAsJson = mapper.writeValueAsString(expectedInvoice);
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(invoicesInDatabase);
    doNothing().when(fileHelper).writeLine(inFileDatabaseProperties.getFilePath(), invoiceToAddAsJson);

    //when
    Invoice addedInvoice = database.saveInvoice(invoiceToAdd);

    //then
    assertNotNull(addedInvoice);
    assertEquals(expectedInvoice, addedInvoice);
    verify(fileHelper).readLines(inFileDatabaseProperties.getFilePath());
    verify(fileHelper).writeLine(inFileDatabaseProperties.getFilePath(), invoiceToAddAsJson);
  }

  @Test
  void shouldUpdateInvoice() throws IOException, DatabaseOperationException {
    //given
    Invoice invoiceInDatabase = InvoiceGenerator.getRandomInvoice();
    String invoiceInDatabaseAsJson = mapper.writeValueAsString(invoiceInDatabase);
    Invoice invoiceToUpdate = new Invoice(invoiceInDatabase.getId(),
        "3",
        invoiceInDatabase.getIssueDate(),
        invoiceInDatabase.getDueDate(),
        invoiceInDatabase.getSeller(),
        invoiceInDatabase.getBuyer(),
        invoiceInDatabase.getEntries());
    String invoiceToUpdateAsJson = mapper.writeValueAsString(invoiceToUpdate);
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(Collections.singletonList(invoiceInDatabaseAsJson));
    doNothing().when(fileHelper).removeLine(inFileDatabaseProperties.getFilePath(), 1);
    doNothing().when(fileHelper).writeLine(inFileDatabaseProperties.getFilePath(), invoiceToUpdateAsJson);

    //when
    Invoice updatedInvoice = database.saveInvoice(invoiceToUpdate);

    //then
    assertNotNull(updatedInvoice);
    assertEquals(invoiceToUpdate, updatedInvoice);
    verify(fileHelper).removeLine(inFileDatabaseProperties.getFilePath(), 1);
    verify(fileHelper, times(2)).readLines(inFileDatabaseProperties.getFilePath());
    verify(fileHelper).writeLine(inFileDatabaseProperties.getFilePath(), invoiceToUpdateAsJson);
  }

  @Test
  void saveMethodShouldThrowExceptionWhenFileHelperThrowException() throws IOException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    doThrow(FileNotFoundException.class).when(fileHelper).readLines(inFileDatabaseProperties.getFilePath());

    //then
    assertThrows(DatabaseOperationException.class, () -> database.saveInvoice(invoice));
  }
}
