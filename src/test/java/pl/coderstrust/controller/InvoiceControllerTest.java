package pl.coderstrust.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.ServiceOperationException;

@RunWith(SpringRunner.class)
@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mvc;

  @MockBean
  private InvoiceService invoiceService;

  @Test
  void shouldThrowIllegalArgumentExceptionForNullAsInvoiceService() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> new InvoiceController(null));
  }

  @Test
  void shouldReturnJsonForGetAll() throws Exception {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getAllInvoices()).thenReturn(Arrays.asList(invoice1, invoice2));

    //then
    mvc.perform(get("/invoices"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
    verify(invoiceService).getAllInvoices();
  }

  @Test
  void shouldReturnInternalServerError() throws Exception {
    //given
    when(invoiceService.getAllInvoices()).thenThrow(ServiceOperationException.class);

    //then
    mvc.perform(get("/invoices"))
        .andExpect(status().isInternalServerError());
    verify(invoiceService).getAllInvoices();

  }

  @Test
  void shouldReturnJsonInvoiceForGetById() throws Exception {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(1L)).thenReturn(Optional.ofNullable(invoice1));

    //then
    mvc.perform(get("/invoices/1"))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(invoice1)));
    verify(invoiceService).getInvoice(1L);

  }

  @Test
  void shouldReturnNotFoundStatusWhenInvoiceIsNotPresentInDatabase() throws Exception {
    //given
    when(invoiceService.getInvoice(1L)).thenReturn(Optional.empty());

    //then
    mvc.perform(get("/invoices/1"))
        .andExpect(status().isNotFound());
    verify(invoiceService).getInvoice(1L);
  }

  @Test
  void shouldReturnInternalServerErrorWhenGetInvoiceThrowsServiceOperationException() throws Exception {
    //given
    when(invoiceService.getInvoice(1L)).thenThrow(ServiceOperationException.class);

    //then
    mvc.perform(get("/invoices/1"))
        .andExpect(status().isInternalServerError());
    verify(invoiceService).getInvoice(1L);

  }

  @Test
  void shouldReturnJsonInvoiceForGetByNumber() throws Exception {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoiceByNumber("1111")).thenReturn(Optional.ofNullable(invoice1));

    //then
    mvc.perform(get("/invoices/byNumber?number=1111"))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(invoice1)));
    verify(invoiceService).getInvoiceByNumber("1111");

  }

  @Test
  void shouldReturnNotFoundStatusWhenInvoiceIsNotPresentInDatabaseOnGetByNumber() throws Exception {
    //given
    when(invoiceService.getInvoiceByNumber("1111")).thenReturn(Optional.empty());

    //then
    mvc.perform(get("/invoices/byNumber?number=1111"))
        .andExpect(status().isNotFound());
    verify(invoiceService).getInvoiceByNumber("1111");

  }

  @Test
  void shouldReturnInternalServerErrorWhenWhenGetByNumberThrowsServiceOperationException() throws Exception {
    //given
    when(invoiceService.getInvoiceByNumber("1111")).thenThrow(ServiceOperationException.class);

    //then
    mvc.perform(get("/invoices/byNumber?number=1111"))
        .andExpect(status().isInternalServerError());
    verify(invoiceService).getInvoiceByNumber("1111");

  }

  @Test
  void shouldThrowIllegalArgumentExceptionForNullInGetByNumber() throws Exception {
    mvc.perform(get("/invoices/byNumber"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldAddNewInvoice() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(invoice.getId())).thenReturn(false);
    when(invoiceService.addInvoice(invoice)).thenReturn(invoice);

    //then
    mvc.perform(post("/invoices")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invoice)))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(invoice)));
    verify(invoiceService).invoiceExists(invoice.getId());
    verify(invoiceService).addInvoice(invoice);


  }

  @Test
  void shouldReturnStatusConflictOnAdd() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(invoice.getId())).thenReturn(true);

    //then
    mvc.perform(post("/invoices")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invoice)))
        .andExpect(status().isConflict());
    verify(invoiceService).invoiceExists(invoice.getId());

  }

  @Test
  void shouldReturnStatusBadRequestOnAdd() throws Exception {
    mvc.perform(post("/invoices")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(null)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnStatusBadRequestOnUpdate() throws Exception {
    mvc.perform(put("/invoices/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(null)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnStatusBadRequestOnUpdateAgain() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();

    //then
    mvc.perform(put("/invoices/2")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invoice)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnStatusNotFoundOnUpdate() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(invoice.getId())).thenReturn(false);

    //then
    mvc.perform(put(String.format("/invoices/%d", invoice.getId()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invoice)))
        .andExpect(status().isNotFound());
    verify(invoiceService).invoiceExists(invoice.getId());

  }

  @Test
  void shouldUpdateExistingInvoice() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(invoice.getId())).thenReturn(true);
    when(invoiceService.updateInvoice(invoice)).thenReturn(invoice);

    //then
    mvc.perform(put(String.format("/invoices/%d", invoice.getId()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invoice)))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(invoice)));
    verify(invoiceService).invoiceExists(invoice.getId());
    verify(invoiceService).updateInvoice(invoice);

  }

  @Test
  void shouldReturnInternalServerErrorWhenUpdateInvoiceThrowsServiceOperationException() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(invoice.getId())).thenReturn(true);
    when(invoiceService.updateInvoice(invoice)).thenThrow(ServiceOperationException.class);

    //then
    mvc.perform(put(String.format("/invoices/%d", invoice.getId()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invoice)))
        .andExpect(status().isInternalServerError());
    verify(invoiceService).invoiceExists(invoice.getId());
    verify(invoiceService).updateInvoice(invoice);
  }

  @Test
  void shouldReturnStatusNotFoundWhenInvoiceIsNotInDatabase() throws Exception {
    //given
    when(invoiceService.getInvoice(1L)).thenReturn(Optional.empty());

    //then
    mvc.perform(delete("/invoices/1"))
        .andExpect(status().isNotFound());
    verify(invoiceService).getInvoice(1L);
  }

  @Test
  void shouldDeleteInvoice() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(1L)).thenReturn(Optional.ofNullable(invoice));
    doNothing().when(invoiceService).deleteInvoice(1L);

    //then
    mvc.perform(delete("/invoices/1"))
        .andExpect(status().isOk());
    verify(invoiceService).getInvoice(1L);
    verify(invoiceService).deleteInvoice(1L);

  }

  @Test
  void shouldReturnInternalServerErrorOnDeleteInvoice() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(1L)).thenReturn(Optional.ofNullable(invoice));
    doThrow(ServiceOperationException.class).when(invoiceService).deleteInvoice(1L);

    //then
    mvc.perform(delete("/invoices/1"))
        .andExpect(status().isInternalServerError());
    verify(invoiceService).getInvoice(1L);
    verify(invoiceService).deleteInvoice(1L);
  }

  @Test
  void shouldDeleteAllInvoices() throws Exception {
    //given
    doNothing().when(invoiceService).deleteAllInvoices();

    //then
    mvc.perform(delete("/invoices"))
        .andExpect(status().isNoContent());
    verify(invoiceService).deleteAllInvoices();

  }

  @Test
  void shouldReturnInternalServerErrorOnDeleteAllInvoices() throws Exception {
    //given
    doThrow(ServiceOperationException.class).when(invoiceService).deleteAllInvoices();

    //then
    mvc.perform(delete("/invoices"))
        .andExpect(status().isInternalServerError());
    verify(invoiceService).deleteAllInvoices();

  }
}
