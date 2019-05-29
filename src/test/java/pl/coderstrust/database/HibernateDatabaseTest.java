package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(MockitoExtension.class)
class HibernateDatabaseTest {

  @InjectMocks
  private HibernateDatabase database;

  @Mock
  private HibernateInvoiceRepository repository;

  @BeforeEach
  void setup() {
    database = new HibernateDatabase(repository);
  }

  @Test
  void shouldSaveInvoice() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(repository.save(invoice)).thenReturn(invoice);

    //when
    Invoice savedInvoice = database.saveInvoice(invoice);

    //then
    assertEquals(invoice, savedInvoice);
    verify(repository).save(invoice);
  }

  @Test
  void shouldReturnInvoice() throws DatabaseOperationException {
    //given
    Invoice invoice = (InvoiceGenerator.getRandomInvoice());
    Long id = invoice.getId();
    when(repository.findById(id)).thenReturn(Optional.of(invoice));

    //When
    Optional<Invoice> actualInvoice = database.getInvoice(id);

    //Then
    assertTrue(actualInvoice.isPresent());
    assertEquals(invoice, actualInvoice.get());
    verify(repository).findById(id);
  }

  @Test
  void shouldReturnTrueIfInvoiceExistsInDatabase() throws DatabaseOperationException {
    //given
    Long id = 1L;
    when(repository.existsById(id)).thenReturn(true);

    //when
    boolean isInvoiceExists = database.invoiceExists(id);

    //then
    Assert.assertTrue(isInvoiceExists);
    verify(repository).existsById(id);
  }

  @Test
  void shouldReturnFalseIfInvoiceExistsInDatabase() throws DatabaseOperationException {
    //given
    Long id = 1L;
    when(repository.existsById(id)).thenReturn(false);

    //when
    boolean isInvoiceExists = database.invoiceExists(id);

    //then
    Assert.assertFalse(isInvoiceExists);
    verify(repository).existsById(id);
  }

  @Test
  void shouldReturnAllInvoices() throws DatabaseOperationException {
    //given
    List<Invoice> invoices = new ArrayList<>();
    invoices.add(InvoiceGenerator.getRandomInvoice());
    invoices.add(InvoiceGenerator.getRandomInvoice());
    when(repository.findAll()).thenReturn(invoices);

    //When
    Collection<Invoice> actualInvoices = database.getAllInvoices();

    //Then
    assertEquals(invoices, actualInvoices);
    verify(repository).findAll();
  }

  @Test
  void shouldReturnNumberOfInvoices() throws DatabaseOperationException {
    //given
    long numberOfInvoices = 3L;
    when(repository.count()).thenReturn(numberOfInvoices);

    //when
    long actualNumberOfInvoices = database.countInvoices();

    //then
    Assert.assertEquals(numberOfInvoices, actualNumberOfInvoices);
    verify(repository).count();
  }

  @Test
  void shouldDeleteById() throws DatabaseOperationException {
    //given
    Long id = 1L;
    doNothing().when(repository).deleteById(id);
    when(repository.existsById(id)).thenReturn(true);

    //then
    database.deleteInvoice(id);
    verify(repository).existsById(id);
    verify(repository).deleteById(id);
  }

  @Test
  void shouldDeleteByIdShouldThrowExceptionWhenInvoiceDoesNotExist() {
    //given
    Long id = 1L;
    when(repository.existsById(id)).thenReturn(false);

    //when
    assertThrows(DatabaseOperationException.class, () -> database.deleteInvoice(id));

    //then
    verify(repository).existsById(id);
    verify(repository, never()).deleteById(id);
  }

  @Test
  void shouldDeleteAllInvoices() throws DatabaseOperationException {
    //given
    doNothing().when(repository).deleteAll();

    //when
    database.deleteAllInvoices();

    //then
    verify(repository).deleteAll();
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
  void saveInvoiceMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //Given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    doThrow(mockedException).when(repository).save(invoice);

    //Then
    assertThrows(DatabaseOperationException.class, () -> database.saveInvoice(invoice));
    verify(repository).save(invoice);
  }

  @Test
  void deleteInvoiceMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //Given
    Long id = 1L;
    EmptyResultDataAccessException mockedException = Mockito.mock(EmptyResultDataAccessException.class);
    doThrow(mockedException).when(repository).deleteById(id);
    when(repository.existsById(id)).thenReturn(true);

    //Then
    assertThrows(DatabaseOperationException.class, () -> database.deleteInvoice(id));
    verify(repository).deleteById(id);
  }

  @Test
  void deleteAllInvoicesMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //Given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);
    doThrow(mockedException).when(repository).deleteAll();

    //Then
    assertThrows(DatabaseOperationException.class, () -> database.deleteAllInvoices());
    verify(repository).deleteAll();
  }

  @Test
  void countInvoicesMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //Given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);
    doThrow(mockedException).when(repository).count();

    //Then
    assertThrows(DatabaseOperationException.class, () -> database.countInvoices());
    verify(repository).count();
  }

  @Test
  void getInvoicedMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //Given
    Long id = 1L;
    NoSuchElementException mockedException = Mockito.mock(NoSuchElementException.class);
    doThrow(mockedException).when(repository).findById(id);

    //Then
    assertThrows(DatabaseOperationException.class, () -> database.getInvoice(id));
    verify(repository).findById(id);
  }

  @Test
  void invoiceExistsMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //Given
    Long id = 1L;
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);
    doThrow(mockedException).when(repository).existsById(id);

    //Then
    assertThrows(DatabaseOperationException.class, () -> database.invoiceExists(id));
    verify(repository).existsById(id);
  }

  @Test
  void getAllInvoicesMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //Given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);
    doThrow(mockedException).when(repository).findAll();

    //Then
    assertThrows(DatabaseOperationException.class, () -> database.getAllInvoices());
    verify(repository).findAll();
  }
}
