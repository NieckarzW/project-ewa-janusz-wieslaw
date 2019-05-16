package pl.coderstrust.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    assertThrows(IllegalArgumentException.class, () -> new InvoiceController(null, null));
  }

  @Test
  void shouldReturnAllInvoices() throws Exception {
    //given
    List<Invoice> invoices = Arrays.asList(InvoiceGenerator.getRandomInvoice(), InvoiceGenerator.getRandomInvoice());
    when(invoiceService.getAllInvoices()).thenReturn(invoices);

    //then
    mvc.perform(get("/invoices"))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(invoices)));

    verify(invoiceService).getAllInvoices();
  }

  @Test
  void shouldReturnEmptyListOfInvoicesWhenThereAreNoInvoicesInTheDatabase() throws Exception {
    //given
    List<Invoice> invoices = new ArrayList<>();
    when(invoiceService.getAllInvoices()).thenReturn(invoices);

    //then
    mvc.perform(get("/invoices"))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(invoices)));

    verify(invoiceService).getAllInvoices();
  }

  @Test
  void shouldReturnInternalServerErrorStatusDuringGettingAllInvoicesWhenSomethingWentWrongOnServer() throws Exception {
    //given
    doThrow(ServiceOperationException.class).when(invoiceService).getAllInvoices();

    //then
    mvc.perform(get("/invoices"))
        .andExpect(status().isInternalServerError());

    verify(invoiceService).getAllInvoices();
  }

  @Test
  void shouldReturnInvoice() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(1L)).thenReturn(Optional.ofNullable(invoice));

    //then
    mvc.perform(get("/invoices/1"))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(invoice)));

    verify(invoiceService).getInvoice(1L);
  }

  @Test
  void shouldReturnNotFoundStatusDuringGettingInvoiceWhenInvoiceWithSpecificIdDoesNotExist() throws Exception {
    //given
    when(invoiceService.getInvoice(1L)).thenReturn(Optional.empty());

    //then
    mvc.perform(get("/invoices/1"))
        .andExpect(status().isNotFound());

    verify(invoiceService).getInvoice(1L);
  }

  @Test
  void shouldReturnInternalServerErrorStatusDuringGettingInvoiceWhenSomethingWentWrongOnServer() throws Exception {
    //given
    doThrow(ServiceOperationException.class).when(invoiceService).getInvoice(1L);

    //then
    mvc.perform(get("/invoices/1"))
        .andExpect(status().isInternalServerError());

    verify(invoiceService).getInvoice(1L);
  }

  @Test
  void shouldReturnInvoiceByNumber() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoiceByNumber("1111")).thenReturn(Optional.ofNullable(invoice));

    //then
    mvc.perform(get("/invoices/byNumber?number=1111"))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(invoice)));

    verify(invoiceService).getInvoiceByNumber("1111");
  }

  @Test
  void shouldReturnNotFoundStatusDuringGettingInvoiceByNumberWhenInvoiceWithSpecificNumberDoesNotExist() throws Exception {
    //given
    when(invoiceService.getInvoiceByNumber("1111")).thenReturn(Optional.empty());

    //then
    mvc.perform(get("/invoices/byNumber?number=1111"))
        .andExpect(status().isNotFound());

    verify(invoiceService).getInvoiceByNumber("1111");
  }

  @Test
  void shouldReturnInternalServerErrorStatusDuringGettingInvoiceByNumberWhenSomethingWentWrongOnServer() throws Exception {
    //given
    doThrow(ServiceOperationException.class).when(invoiceService).getInvoiceByNumber("1111");

    //then
    mvc.perform(get("/invoices/byNumber?number=1111"))
        .andExpect(status().isInternalServerError());

    verify(invoiceService).getInvoiceByNumber("1111");
  }

  @Test
  void shouldReturnBadRequestStatusDuringGettingInvoiceByNumberWhenNumberIsNull() throws Exception {
    mvc.perform(get("/invoices/byNumber"))
        .andExpect(status().isBadRequest());

    verify(invoiceService, never()).getInvoiceByNumber(anyString());
  }

  @Test
  void shouldAddInvoice() throws Exception {
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
  void shouldReturnConflictStatusDuringAddingInvoiceWhenInvoiceAlreadyExist() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(invoice.getId())).thenReturn(true);

    //then
    mvc.perform(post("/invoices")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invoice)))
        .andExpect(status().isConflict());

    verify(invoiceService).invoiceExists(invoice.getId());
    verify(invoiceService, never()).addInvoice(invoice);
  }

  @Test
  void shouldReturnBadRequestStatusDuringAddingInvoiceWhenInvoiceIsNull() throws Exception {
    mvc.perform(post("/invoices")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(null)))
        .andExpect(status().isBadRequest());

    verify(invoiceService, never()).invoiceExists(anyLong());
    verify(invoiceService, never()).addInvoice(any());
  }

  @Test
  void shouldReturnInternalServerErrorStatusDuringAddingInvoiceWhenSomethingWentWrongOnServer() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    doThrow(ServiceOperationException.class).when(invoiceService).addInvoice(invoice);

    //then
    mvc.perform(post("/invoices")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invoice)))
        .andExpect(status().isInternalServerError());

    verify(invoiceService).invoiceExists(invoice.getId());
    verify(invoiceService).addInvoice(invoice);
  }

  @Test
  void shouldUpdateInvoice() throws Exception {
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
  void shouldReturnBadRequestStatusDuringUpdatingInvoiceWhenInvoiceIsNull() throws Exception {
    mvc.perform(put("/invoices/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(null)))
        .andExpect(status().isBadRequest());

    verify(invoiceService, never()).invoiceExists(anyLong());
    verify(invoiceService, never()).updateInvoice(any());
  }

  @Test
  void shouldReturnBadRequestStatusDuringUpdatingInvoiceWithWrongId() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();

    //then
    mvc.perform(put(String.format("/invoices/%d", invoice.getId() + 1))
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invoice)))
        .andExpect(status().isBadRequest());

    verify(invoiceService, never()).invoiceExists(anyLong());
    verify(invoiceService, never()).updateInvoice(any());
  }


  @Test
  void shouldReturnNotFoundStatusDuringUpdatingNonExistingInvoice() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(invoice.getId())).thenReturn(false);

    //then
    mvc.perform(put(String.format("/invoices/%d", invoice.getId()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invoice)))
        .andExpect(status().isNotFound());

    verify(invoiceService).invoiceExists(invoice.getId());
    verify(invoiceService, never()).updateInvoice(any());
  }

  @Test
  void shouldReturnInternalServerErrorStatusDuringUpdatingInvoiceWhenSomethingWentWrongOnServer() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(invoice.getId())).thenReturn(true);
    doThrow(ServiceOperationException.class).when(invoiceService).updateInvoice(invoice);

    //then
    mvc.perform(put(String.format("/invoices/%d", invoice.getId()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invoice)))
        .andExpect(status().isInternalServerError());

    verify(invoiceService).invoiceExists(invoice.getId());
    verify(invoiceService).updateInvoice(invoice);
  }

  @Test
  void shouldRemoveInvoice() throws Exception {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(1L)).thenReturn(Optional.ofNullable(invoice));
    doNothing().when(invoiceService).deleteInvoice(1L);

    //then
    mvc.perform(delete("/invoices/1"))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(invoice)));

    verify(invoiceService).getInvoice(1L);
    verify(invoiceService).deleteInvoice(1L);
  }

  @Test
  void shouldReturnNotFoundStatusDuringRemovingNonExistingInvoice() throws Exception {
    //given
    when(invoiceService.getInvoice(1L)).thenReturn(Optional.empty());

    //then
    mvc.perform(delete("/invoices/1"))
        .andExpect(status().isNotFound());

    verify(invoiceService).getInvoice(1L);
    verify(invoiceService, never()).deleteInvoice(1L);
  }

  @Test
  void shouldReturnInternalServerErrorStatusDuringRemovingInvoiceWhenSomethingWentWrongOnServer() throws Exception {
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
  void shouldRemoveAllInvoices() throws Exception {
    //given
    doNothing().when(invoiceService).deleteAllInvoices();

    //then
    mvc.perform(delete("/invoices"))
        .andExpect(status().isNoContent());

    verify(invoiceService).deleteAllInvoices();
  }

  @Test
  void shouldReturnInternalServerErrorStatusDuringRemovingAllInvoicesWhenSomethingWentWrongOnServer() throws Exception {
    //given
    doThrow(ServiceOperationException.class).when(invoiceService).deleteAllInvoices();

    //then
    mvc.perform(delete("/invoices"))
        .andExpect(status().isInternalServerError());

    verify(invoiceService).deleteAllInvoices();
  }
}
