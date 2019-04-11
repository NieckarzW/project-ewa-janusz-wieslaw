package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.NonTransientDataAccessException;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.ServiceOperationException;

@ExtendWith(MockitoExtension.class)
class HibernateDatabaseTest {

  @Mock
  private
  HibernateDatabase hibernateDatabase;

  @InjectMocks
  private  HibernateInvoiceRepository repository;

  @Test
  void saveInvoiceShouldThrowIllegalArgumentExceptionForNullAsInvoice() {
    assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.saveInvoice(null));
  }

  @Test
  void deleteInvoiceMethodShouldThrowIllegalArgumentExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.deleteInvoice(null));
  }

  @Test
  void shouldSaveInvoice() throws DatabaseOperationException {
    //Given
    Invoice invoiceToSave = InvoiceGenerator.getRandomInvoice();
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(hibernateDatabase.saveInvoice(invoiceToSave)).thenReturn(expectedInvoice);


    //When
    Invoice actualInvoice = hibernateDatabase.saveInvoice(invoiceToSave);

    //Then
    assertEquals(expectedInvoice, actualInvoice);
    verify(hibernateDatabase).saveInvoice(invoiceToSave);
    verify(hibernateDatabase).invoiceExists(invoiceToSave.getId());
  }

  @Test
  void shouldDeleteInvoice() throws DatabaseOperationException {
    long id =567;
    when(hibernateDatabase.invoiceExists(id)).thenReturn(true);
    doNothing().when(hibernateDatabase).deleteInvoice(id);

    //When
   hibernateDatabase.deleteInvoice(id);

    //Then
    verify(hibernateDatabase).invoiceExists(id);
    verify(hibernateDatabase).deleteInvoice(id);
  }

  @Test
  void getInvoiceMethodShouldThrowIllegalArgumentExceptionForNullAsId() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.getInvoice(null));
  }

  @Test
  void shouldGetInvoice() throws DatabaseOperationException {
    //Given
    Optional<Invoice> expectedInvoice = Optional.of(InvoiceGenerator.getRandomInvoice());
    Long id = expectedInvoice.get().getId();
    when(hibernateDatabase.getInvoice(id)).thenReturn(expectedInvoice);

    //When
    Optional<Invoice> actualInvoice = hibernateDatabase.getInvoice(id);

    //Then
    assertEquals(expectedInvoice, actualInvoice);
    verify(hibernateDatabase).getInvoice(id);
  }

  @Test
  void getAllInvoicesMethodShouldThrowDatabaseOperationException() throws DatabaseOperationException {
    //Given
    doThrow(DatabaseOperationException.class).when(repository).findAll();

    //Then
    assertThrows(DatabaseOperationException.class, () -> hibernateDatabase.getAllInvoices());
  }

  @Test
  void shouldGetAllInvoices() throws DatabaseOperationException, ServiceOperationException {
    //Given
    List<Invoice> expectedInvoices = new ArrayList<>();
    Invoice randomInvoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice randomInvoice2 = InvoiceGenerator.getRandomInvoice();
    expectedInvoices.add(randomInvoice1);
    expectedInvoices.add(randomInvoice2);
    when(hibernateDatabase.getAllInvoices()).thenReturn(expectedInvoices);

    //When
    Collection<Invoice> actualInvoices = hibernateDatabase.getAllInvoices();

    //Then
    assertEquals(expectedInvoices, actualInvoices);
    verify(hibernateDatabase).getAllInvoices();
  }

  @Test
  void deleteAllInvoicesMethodShouldThrowDatabaseOperationException() throws DatabaseOperationException {
    //Given
    doThrow(DatabaseOperationException.class).when(hibernateDatabase).deleteAllInvoices();

    //Then
    assertThrows(ServiceOperationException.class, () -> hibernateDatabase.deleteAllInvoices());
  }

  @Test
  void shouldDeleteAll() throws DatabaseOperationException, ServiceOperationException {
    //Given
    doNothing().when(hibernateDatabase).deleteAllInvoices();

    //When
    hibernateDatabase.deleteAllInvoices();

    //Then
    verify(hibernateDatabase).deleteAllInvoices();
  }

  @Test
  void invoiceExistsMethodShouldThrowIllegalArgumentExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.invoiceExists(null));
  }

  @Test
  void invoiceExistsMethodShouldThrowDatabaseOperationException() throws DatabaseOperationException {
    //Given
    long id = 56L;
    doThrow(DatabaseOperationException.class).when(hibernateDatabase).invoiceExists(id);

    //Then
    assertThrows(ServiceOperationException.class, () -> hibernateDatabase.invoiceExists(id));
  }

  @Test
  void shouldReturnTrueWhenInvoiceExists() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Long id = 98L;
    when(hibernateDatabase.invoiceExists(id)).thenReturn(true);

    //When
    boolean checkIfExists = hibernateDatabase.invoiceExists(id);

    //Then
    assertTrue(checkIfExists);
    verify(hibernateDatabase).invoiceExists(id);
  }

  @Test
  void shouldReturnFalseWhenInvoiceDoesNotExist() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Long id = 98L;
    when(hibernateDatabase.invoiceExists(id)).thenReturn(false);

    //When
    boolean checkIfExists = hibernateDatabase.invoiceExists(id);

    //Then
    assertFalse(checkIfExists);
    verify(hibernateDatabase).invoiceExists(id);
  }

  public long countInvoices() throws DatabaseOperationException {
    try {
      return repository.count();
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException("Encountered problems while counting invoices.", e);
    }
  }

  @Test
  void countInvoiceMethodShouldThrowDatabaseOperationException() throws DatabaseOperationException {
    //Given
    long id = 123L;
    doThrow(DatabaseOperationException.class).when(hibernateDatabase).countInvoices();

    //Then
    assertThrows(ServiceOperationException.class, () -> hibernateDatabase.countInvoices());
  }

  @Test
  void shouldCountInvoices() throws DatabaseOperationException {
    //Given
    List<Invoice> expectedInvoices = new ArrayList<>();
    Invoice randomInvoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice randomInvoice2 = InvoiceGenerator.getRandomInvoice();
    expectedInvoices.add(randomInvoice1);
    expectedInvoices.add(randomInvoice2);
    long numberOfInvoices = expectedInvoices.size();
    when(hibernateDatabase.countInvoices()).thenReturn(numberOfInvoices);

    //When
    long actualNumberOfInvoices = hibernateDatabase.countInvoices();

    //Then
    assertEquals(numberOfInvoices, actualNumberOfInvoices);
    verify(hibernateDatabase).getAllInvoices();
  }
}
