package pl.coderstrust.controller;

import javax.xml.datatype.DatatypeConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.coderstrust.mappers.InvoiceMapper;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.ServiceOperationException;
import pl.coderstrust.soap.methods.GetInvoiceRequest;
import pl.coderstrust.soap.methods.GetInvoiceResponse;
import pl.coderstrust.soap.methods.SaveInvoiceRequest;
import pl.coderstrust.soap.methods.SaveInvoiceResponse;

@Endpoint
public class InvoiceEndpoint {
  private static final String NAMESPACE_URI = "soap.coderstrust.pl";

  private InvoiceService invoiceService;

  @Autowired
  public InvoiceEndpoint(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "saveInvoiceRequest")
  @ResponsePayload
  public SaveInvoiceResponse saveInvoice(@RequestPayload SaveInvoiceRequest request) {
    SaveInvoiceResponse response = new SaveInvoiceResponse();
    

    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoiceRequest")
  @ResponsePayload
  public GetInvoiceResponse getInvoice(@RequestPayload GetInvoiceRequest request) throws ServiceOperationException, DatatypeConfigurationException {
    GetInvoiceResponse response = new GetInvoiceResponse();
    response.setInvoice(InvoiceMapper.mapModelInvoiceToSoapInvoice(invoiceService.getInvoice(request.getId()).get()));

    return response;
  }
}
