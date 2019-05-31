package pl.coderstrust.controller;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;
import static org.springframework.ws.test.server.ResponseMatchers.validPayload;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.ws.test.server.MockWebServiceClient;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InvoiceEndpointTest {

  @Autowired
  private WebApplicationContext applicationContext;

  private MockWebServiceClient mockClient;
  private Resource xsdSchema = new ClassPathResource("invoices.xsd");

  @BeforeEach
  void init() {
    mockClient = MockWebServiceClient.createClient(applicationContext);
  }

  @Test
  @Order(1)
  void shouldSaveInvoice() throws IOException {
    //given
    Resource request = applicationContext.getResource("classpath:controller/SaveInvoiceRequest.xml");
    Resource response = applicationContext.getResource("classpath:controller/SaveInvoiceResponse.xml");

    //when
    mockClient.sendRequest(withPayload(request))
        //then
        .andExpect(noFault())
        .andExpect(payload(response))
        .andExpect(validPayload(xsdSchema));
  }

  @Test
  @Order(2)
  void shouldUpdateInvoice() throws IOException {
    //given
    Resource request = applicationContext.getResource("classpath:controller/UpdateInvoiceRequest.xml");
    Resource response = applicationContext.getResource("classpath:controller/UpdateInvoiceResponse.xml");

    //when
    mockClient.sendRequest(withPayload(request))
        //then
        .andExpect(noFault())
        .andExpect(payload(response))
        .andExpect(validPayload(xsdSchema));
  }

  @Test
  @Order(3)
  void shouldGetInvoice() throws IOException {
    //given
    Resource request = applicationContext.getResource("classpath:controller/GetInvoiceRequest.xml");
    Resource response = applicationContext.getResource("classpath:controller/GetInvoiceResponse.xml");

    //when
    mockClient.sendRequest(withPayload(request))
        //then
        .andExpect(noFault())
        .andExpect(payload(response))
        .andExpect(validPayload(xsdSchema));
  }

  @Test
  @Order(4)
  void shouldGetAllInvoices() throws IOException {
    //given
    Resource request = applicationContext.getResource("classpath:controller/GetAllInvoicesRequest.xml");
    Resource response = applicationContext.getResource("classpath:controller/GetAllInvoicesResponse.xml");

    //when
    mockClient.sendRequest(withPayload(request))
        //then
        .andExpect(noFault())
        .andExpect(payload(response))
        .andExpect(validPayload(xsdSchema));
  }

  @Test
  @Order(5)
  void shouldDeleteInvoice() throws IOException {
    //given
    Resource request = applicationContext.getResource("classpath:controller/DeleteInvoiceRequest.xml");
    Resource response = applicationContext.getResource("classpath:controller/DeleteInvoiceResponse.xml");

    //when
    mockClient.sendRequest(withPayload(request))
        //then
        .andExpect(noFault())
        .andExpect(payload(response))
        .andExpect(validPayload(xsdSchema));
  }

  @Test
  @Order(6)
  void shouldDeleteAllInvoice() throws IOException {
    //given
    Resource request = applicationContext.getResource("classpath:controller/DeleteAllInvoicesRequest.xml");
    Resource response = applicationContext.getResource("classpath:controller/DeleteAllInvoicesResponse.xml");

    //when
    mockClient.sendRequest(withPayload(request))
        //then
        .andExpect(noFault())
        .andExpect(payload(response))
        .andExpect(validPayload(xsdSchema));
  }
}
