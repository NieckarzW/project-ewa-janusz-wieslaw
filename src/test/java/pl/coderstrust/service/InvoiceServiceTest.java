package pl.coderstrust.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

  @Mock
  private Database database;

  @InjectMocks
  private InvoiceService invoiceService;

  @Test
  void invoiceServiceShouldThrowIllegalArgumentExceptionForNullAsDatabase() {
    assertThrows(IllegalArgumentException.class, () -> new InvoiceService(null));
  }

  @Test
  void addInvoiceMethodShouldThrowIllegalArgumentExceptionForNullAsInvoice() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.addInvoice(null));
  }

  @Test
  void addInvoiceMethodShouldThrowInvoiceServiceOperationExceptionWhenInvoiceAlreadyExistInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    Mockito.when(database.invoiceExists(invoice.getId())).thenReturn(true);

    //then
    assertThrows(ServiceOperationException.class, () -> invoiceService.addInvoice(invoice));
  }

  @Test
  void addInvoiceMethodShouldThrowInvoiceServiceOperationExceptionWhenWhenAnErrorOccurDuringCheckingIfInvoicesExistsInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    Mockito.when(database.invoiceExists(invoice.getId())).thenThrow(DatabaseOperationException.class);

    //then
    assertThrows(ServiceOperationException.class, () -> invoiceService.addInvoice(invoice));
  }

  @Test
  void shouldAddInvoice() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Invoice invoiceToAdd = InvoiceGenerator.getRandomInvoice();
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(database.saveInvoice(invoiceToAdd)).thenReturn(expectedInvoice);
    when(database.invoiceExists(invoiceToAdd.getId())).thenReturn(false);

    //When
    Invoice actualInvoice = invoiceService.addInvoice(invoiceToAdd);

    //Then
    assertEquals(expectedInvoice, actualInvoice);
    verify(database).saveInvoice(invoiceToAdd);
    verify(database).invoiceExists(invoiceToAdd.getId());
  }

  @Test
  void updateInvoiceMethodShouldThrowIllegalArgumentExceptionForNullAsInvoice() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.updateInvoice(null));
  }

  @Test
  void updateInvoiceMethodShouldThrowInvoiceServiceOperationExceptionWhenInvoiceDoesNotExistInDatabase() throws DatabaseOperationException {
    //Given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(database.invoiceExists(invoice.getId())).thenReturn(false);

    //Then
    assertThrows(ServiceOperationException.class, () -> invoiceService.updateInvoice(invoice));
  }

  @Test
  void updateInvoiceMethodShouldThrowInvoiceServiceOperationExceptionWhenWhenAnErrorOccurDuringCheckingIfInvoicesExistsInDatabase() throws DatabaseOperationException {
    //Given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(database.invoiceExists(invoice.getId())).thenReturn(true);
    doThrow(DatabaseOperationException.class).when(database).saveInvoice(invoice);

    //Then
    assertThrows(ServiceOperationException.class, () -> invoiceService.updateInvoice(invoice));
  }

  @Test
  void shouldUpdateInvoice() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(database.saveInvoice(invoice)).thenReturn(invoice);
    when(database.invoiceExists(invoice.getId())).thenReturn(true);

    //When
    invoiceService.updateInvoice(invoice);

    //Then
    verify(database).saveInvoice(invoice);
    verify(database).invoiceExists(invoice.getId());
  }

  @Test
  void deleteInvoiceMethodShouldThrowIllegalArgumentExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.deleteInvoice(null));
  }

  @Test
  void deleteInvoiceMethodShouldThrowInvoiceServiceOperationExceptionWhenInvoiceDosesNotExistInDatabase() throws DatabaseOperationException {
    //Given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(database.invoiceExists(invoice.getId())).thenReturn(false);

    //Then
    assertThrows(ServiceOperationException.class, () -> invoiceService.deleteInvoice(invoice.getId()));
  }

  @Test
  void deleteInvoiceMethodShouldThrowInvoiceServiceOperationExceptionWhenWhenAnErrorOccurDuringCheckingIfInvoicesExistsInDatabase() throws DatabaseOperationException {
    //Given
    when(database.invoiceExists(1L)).thenReturn(true);
    doThrow(DatabaseOperationException.class).when(database).deleteInvoice(1L);

    //Then
    assertThrows(ServiceOperationException.class, () -> invoiceService.deleteInvoice(1L));
  }

  @Test
  void shouldDeleteInvoice() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Long id = 3448L;
    when(database.invoiceExists(id)).thenReturn(true);
    doNothing().when(database).deleteInvoice(id);

    //When
    invoiceService.deleteInvoice(id);

    //Then
    verify(database).invoiceExists(id);
    verify(database).deleteInvoice(id);
  }

  @Test
  void getInvoiceMethodShouldThrowIllegalArgumentExceptionForNullAsId() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> invoiceService.getInvoice(null));
  }

  @Test
  void getInvoiceMethodShouldThrowInvoiceServiceOperationExceptionWhenWhenAnErrorOccurDuringExecutionGettingInvoicesFromDatabase() throws DatabaseOperationException {
    //Given
    doThrow(DatabaseOperationException.class).when(database).getInvoice(1L);

    //Then
    assertThrows(ServiceOperationException.class, () -> invoiceService.getInvoice(1L));
  }

  @Test
  void shouldReturnInvoice() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Optional<Invoice> expectedInvoice = Optional.of(InvoiceGenerator.getRandomInvoice());
    Long id = expectedInvoice.get().getId();
    when(database.getInvoice(id)).thenReturn(expectedInvoice);

    //When
    Optional<Invoice> actualInvoice = invoiceService.getInvoice(id);

    //Then
    assertEquals(expectedInvoice, actualInvoice);
    verify(database).getInvoice(id);
  }

  @Test
  void getAllInvoicesMethodShouldThrowInvoiceServiceOperationExceptionWhenWhenAnErrorOccurDuringGettingAllInvoicesFromDatabase() throws DatabaseOperationException {
    //Given
    doThrow(DatabaseOperationException.class).when(database).getAllInvoices();

    //Then
    assertThrows(ServiceOperationException.class, () -> invoiceService.getAllInvoices());
  }

  @Test
  void shouldGetAllInvoices() throws DatabaseOperationException, ServiceOperationException {
    //Given
    List<Invoice> expectedInvoices = new ArrayList<>();
    Invoice randomInvoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice randomInvoice2 = InvoiceGenerator.getRandomInvoice();
    expectedInvoices.add(randomInvoice1);
    expectedInvoices.add(randomInvoice2);
    when(database.getAllInvoices()).thenReturn(expectedInvoices);

    //When
    Collection<Invoice> actualInvoices = invoiceService.getAllInvoices();

    //Then
    assertEquals(expectedInvoices, actualInvoices);
    verify(database).getAllInvoices();
  }

  @Test
  void deleteAllInvoicesMethodShouldThrowInvoiceServiceOperationExceptionWhenAnErrorOccurDuringDeletingAllInvoicesFromDatabase() throws DatabaseOperationException {
    //Given
    doThrow(DatabaseOperationException.class).when(database).deleteAllInvoices();

    //Then
    assertThrows(ServiceOperationException.class, () -> invoiceService.deleteAllInvoices());
  }

  @Test
  void shouldDeleteAll() throws DatabaseOperationException, ServiceOperationException {
    //Given
    doNothing().when(database).deleteAllInvoices();

    //When
    invoiceService.deleteAllInvoices();

    //Then
    verify(database).deleteAllInvoices();
  }

  @Test
  void invoiceExistsMethodShouldThrowIllegalArgumentExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.invoiceExists(null));
  }

  @Test
  void invoiceExistsMethodShouldThrowInvoiceServiceOperationExceptionWhenAnErrorOccurDuringCheckingIfInvoiceExistsInDatabase() throws DatabaseOperationException {
    //Given
    long id = 1L;
    doThrow(DatabaseOperationException.class).when(database).invoiceExists(id);

    //Then
    assertThrows(ServiceOperationException.class, () -> invoiceService.invoiceExists(id));
  }

  @Test
  void shouldReturnTrueWhenInvoiceExists() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Long id = 3448L;
    when(database.invoiceExists(id)).thenReturn(true);

    //When
    boolean check = invoiceService.invoiceExists(id);

    //Then
    assertTrue(check);
    verify(database).invoiceExists(id);
  }

  @Test
  void shouldReturnFalseWhenInvoiceDoesNotExist() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Long id = 3448L;
    when(database.invoiceExists(id)).thenReturn(false);

    //When
    boolean check = invoiceService.invoiceExists(id);

    //Then
    assertFalse(check);
    verify(database).invoiceExists(id);
  }

  @Test
  void getInvoiceByNumberMethodShouldThrowIllegalArgumentExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getInvoiceByNumber(null));
  }

  @Test
  void shouldGetInvoiceByNumber() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    List<Invoice> invoicesInDatabase = Arrays.asList(InvoiceGenerator.getRandomInvoice(), expectedInvoice, InvoiceGenerator.getRandomInvoice());
    when(database.getAllInvoices()).thenReturn(invoicesInDatabase);

    //When
    Optional<Invoice> actualInvoice = invoiceService.getInvoiceByNumber(expectedInvoice.getNumber());

    //Then
    assertTrue(actualInvoice.isPresent());
    assertEquals(expectedInvoice, actualInvoice.get());
    verify(database).getAllInvoices();
  }

  @Test
  void getInvoiceByNumberMethodShouldThrowInvoiceServiceOperationExceptionWhenAnErrorOccurDuringGettingAllInvoicesFromDatabase() throws DatabaseOperationException {
    //Given
    doThrow(DatabaseOperationException.class).when(database).getAllInvoices();

    //Then
    assertThrows(ServiceOperationException.class, () -> invoiceService.getAllInvoices());
  }
}
