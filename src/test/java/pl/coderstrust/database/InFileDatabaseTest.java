package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

  ObjectMapper objectMapper;

  InFileDatabaseProperties inFileDatabaseProperties;

  IdentifierGenerator identifierGenerator;

  Invoice invoiceWithId;

  @BeforeEach
  void setUp() throws IOException {
    objectMapper = new ApplicationConfiguration().getObjectMapper();
    inFileDatabaseProperties = new InFileDatabaseProperties();
    inFileDatabaseProperties.setFilePath("database.txt");
    identifierGenerator = new IdentifierGenerator();
    database = new InFileDatabase(fileHelper, objectMapper, inFileDatabaseProperties, identifierGenerator);
    invoiceWithId = InvoiceGenerator.getRandomInvoice();
  }

  @Test
  void constructorCallOfInFileDatabaseShouldThrowExceptionWhenFileHelperIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new InFileDatabase(null, objectMapper, inFileDatabaseProperties, identifierGenerator));
  }

  @Test
  void constructorCallOfInFileDatabaseShouldThrowExceptionWhenObjectMapperIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new InFileDatabase(fileHelper, null, inFileDatabaseProperties, identifierGenerator));
  }

  @Test
  void constructorCallOfInFileDatabaseShouldThrowExceptionWhenInFileDatabasePropertiesIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new InFileDatabase(fileHelper, objectMapper, null, identifierGenerator));
  }

  @Test
  void constructorCallOfInFileDatabaseShouldThrowExceptionWhenIdentifierGeneratorIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new InFileDatabase(fileHelper, objectMapper, inFileDatabaseProperties, null));
  }

  @Test
  void ifDatabaseFileDoesNotExistConstructorShouldCreateIt() throws IOException {
    //given
    when(fileHelper.exists(inFileDatabaseProperties.getFilePath())).thenReturn(false);

    //when

    //then
    verify(fileHelper, times(1)).create(inFileDatabaseProperties.getFilePath());
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
  void shouldSaveInvoice() throws IOException, DatabaseOperationException {
    List<String> testDatabase = new ArrayList();
    Invoice expectedInvoice = new Invoice(1L, invoiceWithId.getNumber(), invoiceWithId.getIssueDate(), invoiceWithId.getDueDate(), invoiceWithId.getSeller(), invoiceWithId.getBuyer(), invoiceWithId.getEntries());
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(testDatabase);

    Invoice resultInvoice = database.saveInvoice(invoiceWithId);

    assertEquals(expectedInvoice, resultInvoice);
  }

  @Test
  void shouldUpdateInvoice() throws IOException, DatabaseOperationException {
    //given
    List<String> testDatabase = new ArrayList();
    Invoice expectedInvoice = new Invoice(invoiceWithId.getId(), "abcdefghijklmno", invoiceWithId.getIssueDate(), invoiceWithId.getDueDate(), invoiceWithId.getSeller(), invoiceWithId.getBuyer(), invoiceWithId.getEntries());
    testDatabase.add(objectMapper.writeValueAsString(invoiceWithId));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(testDatabase);
    //when
    Invoice resultInvoice = database.saveInvoice(expectedInvoice);
    //then
    assertEquals(expectedInvoice, resultInvoice);
    verify(fileHelper).writeLine(inFileDatabaseProperties.getFilePath(), objectMapper.writeValueAsString(expectedInvoice));
    verify(fileHelper).removeLine(inFileDatabaseProperties.getFilePath(), 1);
  }

  @Test
  void shouldDeleteInvoice() throws IOException, DatabaseOperationException {
    //given
    List<String> testDatabase = new ArrayList();
    testDatabase.add(objectMapper.writeValueAsString(invoiceWithId));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(testDatabase);
    doNothing().when(fileHelper).removeLine(inFileDatabaseProperties.getFilePath(), 1);
    //when
    database.deleteInvoice(invoiceWithId.getId());
    //then
    verify(fileHelper).removeLine(inFileDatabaseProperties.getFilePath(), 1);
  }

  @Test
  void shouldThrowDatabaseOperationExceptionIfInvoiceDoesNotExistWhenDeleteInvoiceIsCalled() throws IOException {
    //given
    List<String> testDatabase = new ArrayList();
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(testDatabase);    //when
    //then
    assertThrows(DatabaseOperationException.class, () -> database.deleteInvoice(1L));
  }

  @Test
  void shouldThrowDatabaseOperationExceptionIfFilePathIsWrongWhenDeleteInvoiceIsCalled() throws IOException {
    //given
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenThrow(IOException.class);
    //then
    assertThrows(DatabaseOperationException.class, () -> database.deleteInvoice(1L));
  }

  @Test
  void shouldGetInvoiceById() throws IOException, DatabaseOperationException {
    //given
    List<String> testDatabase = new ArrayList();
    testDatabase.add(objectMapper.writeValueAsString(invoiceWithId));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(testDatabase);
    //when
    Invoice resultInvoice = database.getInvoice(invoiceWithId.getId()).get();
    //then
    assertEquals(invoiceWithId, resultInvoice);
  }

  @Test
  void shouldThrowDatabaseOperationExceptionIfFilePathIsWrongWhenGetInvoiceIsCalled() throws IOException {
    //given
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenThrow(IOException.class);
    //then
    assertThrows(DatabaseOperationException.class, () -> database.getInvoice(1L));
  }

  @Test
  void shouldGetAllInvoices() throws IOException, DatabaseOperationException {
    //given
    List<String> testDatabase = new ArrayList();
    testDatabase.add(objectMapper.writeValueAsString(InvoiceGenerator.getRandomInvoice()));
    testDatabase.add(objectMapper.writeValueAsString(InvoiceGenerator.getRandomInvoice()));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(testDatabase);
    //when
    List<Invoice> resultInvoices = database.getAllInvoices();
    List<Invoice> expectedInvoices = testDatabase
        .stream()
        .map(invoice -> {
          try {
            return objectMapper.readValue(invoice, Invoice.class);
          } catch (IOException e) {
            return null;
          }
        }).collect(Collectors.toList());
    //then
    assertEquals(expectedInvoices, resultInvoices);
  }

  @Test
  void shouldThrowDatabaseOperationExceptionIfFilePathIsWrongWhenGetAllInvoicesIsCalled() throws IOException {
    //given
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenThrow(IOException.class);
    //then
    assertThrows(DatabaseOperationException.class, () -> database.getAllInvoices());
  }

  @Test
  void shouldDeleteAllInvoices() throws IOException, DatabaseOperationException {
    //given
    doNothing().when(fileHelper).clear(inFileDatabaseProperties.getFilePath());
    //when
    database.deleteAllInvoices();
    //then
    verify(fileHelper, times(1)).clear(inFileDatabaseProperties.getFilePath());
  }

  @Test
  void shouldThrowDatabaseOperationExceptionIfFilePathIsWrongWhenDeleteAllInvoicesIsCalled() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelper).clear(inFileDatabaseProperties.getFilePath());
    //then
    assertThrows(DatabaseOperationException.class, () -> database.deleteAllInvoices());
  }

  @Test
  void shouldCheckIfInvoiceExists() throws IOException, DatabaseOperationException {
    //given
    List<String> testDatabase = new ArrayList();
    testDatabase.add(objectMapper.writeValueAsString(invoiceWithId));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(testDatabase);

    //when
    boolean resultBool = database.invoiceExists(invoiceWithId.getId());

    //then
    assertTrue(resultBool);
  }

  @Test
  void shouldThrowDatabaseOperationExceptionIfFilePathIsWrongWhenInvoiceExistsIsCalled() throws IOException {
    //given
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenThrow(IOException.class);
    //then
    assertThrows(DatabaseOperationException.class, () -> database.invoiceExists(1L));
  }

  @Test
  void shouldCountInvoices() throws IOException, DatabaseOperationException {
    //given
    List<String> testDatabase = new ArrayList();
    testDatabase.add(objectMapper.writeValueAsString(InvoiceGenerator.getRandomInvoice()));
    testDatabase.add(objectMapper.writeValueAsString(InvoiceGenerator.getRandomInvoice()));
    testDatabase.add(objectMapper.writeValueAsString(InvoiceGenerator.getRandomInvoice()));
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenReturn(testDatabase);

    //when
    long result = database.countInvoices();

    //then
    assertEquals(3L, result);
  }

  @Test
  void shouldThrowDatabaseOperationExceptionIfFilePathIsWrongWhenCountInvoicesIsCalled() throws IOException {
    //given
    when(fileHelper.readLines(inFileDatabaseProperties.getFilePath())).thenThrow(IOException.class);
    //then
    assertThrows(DatabaseOperationException.class, () -> database.countInvoices());
  }
}
