package pl.coderstrust.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.generators.RandomInvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

  @Mock
  Database database;
  @InjectMocks
  InvoiceService invoiceService;
  private Invoice invoiceWithId = RandomInvoiceGenerator.generateInvoiceWithId();
  private Invoice invoiceWithNullId = RandomInvoiceGenerator.generateInvoiceWithNullId();

  // ______addInvoice tests______

  @Test
  void shouldThrowIllegalArgumentExceptionOnAdd() {
    //given
    Invoice invoice = null;
    //when
    //then
    Assertions.assertThrows(IllegalArgumentException.class, () -> invoiceService.addInvoice(invoice));
  }

  @Test
  void shouldThrowServiceExceptionOnAdd() throws DatabaseOperationException {
    //given
    Mockito.when(database.invoiceExists(invoiceWithId.getId())).thenReturn(true);
    //when
    //then
    Assertions.assertThrows(ServiceOperationException.class, () -> invoiceService.addInvoice(invoiceWithId));
  }

  @Test
  void shouldSavePassedInvoice() throws DatabaseOperationException, ServiceOperationException {
    //given
    Mockito.when(database.saveInvoice(invoiceWithNullId)).thenReturn(invoiceWithNullId);
    //when
    Invoice invoiceReturnedByAdd = invoiceService.addInvoice(invoiceWithNullId);

    //then
    Mockito.verify(database).saveInvoice(invoiceWithNullId);
    Assertions.assertEquals(invoiceWithNullId, invoiceReturnedByAdd);
  }

  @Test
  void shouldThrowServiceExceptionCausedByDatabaseExceptionOnAdd() throws DatabaseOperationException {
    //given
    Mockito.when(database.invoiceExists(invoiceWithId.getId())).thenThrow(DatabaseOperationException.class);
    //when
    //then
    Assertions.assertThrows(ServiceOperationException.class, () -> invoiceService.addInvoice(invoiceWithId));
  }

  //______updateInvoice tests______

  @Test
  void shouldThrowIllegalArgumentExceptionOnUpdate() {
    //given
    Invoice invoice = null;
    //when
    //then
    Assertions.assertThrows(IllegalArgumentException.class, () -> invoiceService.updateInvoice(invoice));
  }

  @Test
  void shouldThrowServiceExceptionOnUpdate() {
    //given
    //when
    //then
    Assertions.assertThrows(ServiceOperationException.class, () -> invoiceService.updateInvoice(invoiceWithNullId));
  }

  @Test
  void shouldUpdatePassedInvoice() throws DatabaseOperationException, ServiceOperationException {
    //given
    Mockito.when(database.saveInvoice(invoiceWithId)).thenReturn(invoiceWithId);
    //when
    Invoice invoiceReturnedByUpdate = invoiceService.updateInvoice(invoiceWithId);

    //then
    Mockito.verify(database).saveInvoice(invoiceWithId);
    Assertions.assertEquals(invoiceWithId, invoiceReturnedByUpdate);
  }

  @Test
  void shouldThrowServiceExceptionCausedByDatabaseExceptionOnUpdate() throws DatabaseOperationException {
    //given
    Mockito.when(database.invoiceExists(invoiceWithId.getId())).thenThrow(DatabaseOperationException.class);
    //when
    //then
    Assertions.assertThrows(ServiceOperationException.class, () -> invoiceService.addInvoice(invoiceWithId));
  }

  //______deleteInvoice tests______

  @Test
  void shouldThrowIllegalArgumentExceptionOnDelete() {
    //given
    Long id = null;
    //when
    //then
    Assertions.assertThrows(IllegalArgumentException.class, () -> invoiceService.deleteInvoice(id));
  }

  @Test
  void shouldThrowServiceExceptionOnDelete() throws DatabaseOperationException {
    //given
    Mockito.when(database.invoiceExists(1L)).thenReturn(false);
    //when
    //then
    Assertions.assertThrows(ServiceOperationException.class, () -> invoiceService.deleteInvoice(1L));
  }

  @Test
  void shouldCallDatabaseDeleteMethod() throws DatabaseOperationException, ServiceOperationException {
    //given
    Long id = 1L;
    Mockito.when(database.invoiceExists(1L)).thenReturn(true);
    //when
    invoiceService.deleteInvoice(id);
    //then
    Mockito.verify(database).deleteInvoice(1L);
  }

  @Test
  void shouldThrowServiceExceptionCausedByDatabaseExceptionOnDelete() throws DatabaseOperationException {
    //given
    Mockito.when(database.invoiceExists(1L)).thenThrow(DatabaseOperationException.class);
    //when
    //then
    Assertions.assertThrows(ServiceOperationException.class, () -> invoiceService.deleteInvoice(1L));
  }

  //______getInvoice tests______

  @Test
  void shouldCallDatabaseGetMethod() throws DatabaseOperationException, ServiceOperationException {
    //given
    Long id = 1L;
    //when
    invoiceService.getInvoice(id);
    //then
    Mockito.verify(database).getInvoice(1L);
  }

  @Test
  void shouldThrowServiceExceptionCausedByDatabaseExceptionOnGet() throws DatabaseOperationException {
    //given
    Mockito.when(database.getInvoice(1L)).thenThrow(DatabaseOperationException.class);
    //when
    //then
    Assertions.assertThrows(ServiceOperationException.class, () -> invoiceService.getInvoice(1L));
  }

  //______getAllInvoice tests______

  @Test
  void shouldCallDatabaseGetAllMethod() throws DatabaseOperationException, ServiceOperationException {
    //given
    //when
    invoiceService.getAllInvoices();
    //then
    Mockito.verify(database).getAllInvoices();
  }

  @Test
  void shouldThrowServiceExceptionCausedByDatabaseExceptionOnGetAll() throws DatabaseOperationException {
    //given
    Mockito.when(database.getAllInvoices()).thenThrow(DatabaseOperationException.class);
    //when
    //then
    Assertions.assertThrows(ServiceOperationException.class, () -> invoiceService.getAllInvoices());
  }

  //______deleteAllInvoices tests______

  @Test
  void shouldCallDatabaseDeleteAllMethod() throws DatabaseOperationException, ServiceOperationException {
    //given
    //when
    invoiceService.deleteAllInvoices();
    //then
    Mockito.verify(database).deleteAllInvoices();
  }
}
