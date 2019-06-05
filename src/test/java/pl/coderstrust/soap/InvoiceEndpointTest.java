package pl.coderstrust.soap;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.ws.test.server.MockWebServiceClient;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InvoiceEndpointTest {

  @Autowired
  private WebApplicationContext applicationContext;

  private MockWebServiceClient mockClient;

  @BeforeEach
  void init() {
    mockClient = MockWebServiceClient.createClient(applicationContext);
  }

  @Test
  @Order(1)
  void shouldAddInvoice() throws IOException {
    //given
    Resource request = applicationContext.getResource("classpath:soap/addInvoiceRequest.xml");
    Resource response = applicationContext.getResource("classpath:soap/addInvoiceResponse.xml");

    //when
    mockClient.sendRequest(withPayload(request))
        //then
        .andExpect(noFault())
        .andExpect(payload(response));
  }

  @Test
  @Order(2)
  void shouldUpdateInvoice() throws IOException {
    //given
    Resource request = applicationContext.getResource("classpath:soap/updateInvoiceRequest.xml");
    Resource response = applicationContext.getResource("classpath:soap/updateInvoiceResponse.xml");

    //when
    mockClient.sendRequest(withPayload(request))
        //then
        .andExpect(noFault())
        .andExpect(payload(response));
  }

  @Test
  @Order(3)
  void shouldGetInvoice() throws IOException {
    //given
    Resource request = applicationContext.getResource("classpath:soap/getInvoiceRequest.xml");
    Resource response = applicationContext.getResource("classpath:soap/getInvoiceResponse.xml");

    //when
    mockClient.sendRequest(withPayload(request))
        //then
        .andExpect(noFault())
        .andExpect(payload(response));
  }

  @Test
  @Order(4)
  void shouldGetAllInvoices() throws IOException {
    //given
    Resource request = applicationContext.getResource("classpath:soap/getAllInvoicesRequest.xml");
    Resource response = applicationContext.getResource("classpath:soap/getAllInvoicesResponse.xml");

    //when
    mockClient.sendRequest(withPayload(request))
        //then
        .andExpect(noFault())
        .andExpect(payload(response));
  }

  @Test
  @Order(5)
  void shouldDeleteInvoice() throws IOException {
    //given
    Resource request = applicationContext.getResource("classpath:soap/deleteInvoiceRequest.xml");
    Resource response = applicationContext.getResource("classpath:soap/deleteInvoiceResponse.xml");

    //when
    mockClient.sendRequest(withPayload(request))
        //then
        .andExpect(noFault())
        .andExpect(payload(response));
  }

  @Test
  @Order(6)
  void shouldDeleteAllInvoice() throws IOException {
    //given
    Resource request = applicationContext.getResource("classpath:soap/deleteAllInvoicesRequest.xml");
    Resource response = applicationContext.getResource("classpath:soap/deleteAllInvoicesResponse.xml");

    //when
    mockClient.sendRequest(withPayload(request))
        //then
        .andExpect(noFault())
        .andExpect(payload(response));
  }
}
