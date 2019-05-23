package pl.coderstrust.controller;

import java.util.Optional;
import javax.xml.datatype.DatatypeConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.coderstrust.mappers.InvoiceMapper;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.ServiceOperationException;
import pl.coderstrust.soap.methods.DeleteAllInvoicesRequest;
import pl.coderstrust.soap.methods.DeleteAllInvoicesResponse;
import pl.coderstrust.soap.methods.DeleteInvoiceRequest;
import pl.coderstrust.soap.methods.DeleteInvoiceResponse;
import pl.coderstrust.soap.methods.GetAllInvoicesRequest;
import pl.coderstrust.soap.methods.GetAllInvoicesResponse;
import pl.coderstrust.soap.methods.GetInvoiceByNumberRequest;
import pl.coderstrust.soap.methods.GetInvoiceByNumberResponse;
import pl.coderstrust.soap.methods.GetInvoiceRequest;
import pl.coderstrust.soap.methods.GetInvoiceResponse;
import pl.coderstrust.soap.methods.ResponseStatus;
import pl.coderstrust.soap.methods.SaveInvoiceRequest;
import pl.coderstrust.soap.methods.SaveInvoiceResponse;
import pl.coderstrust.soap.methods.UpdateInvoiceRequest;
import pl.coderstrust.soap.methods.UpdateInvoiceResponse;
import pl.coderstrust.soap.models.InvoicesList;

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
    Invoice invoice = InvoiceMapper.mapSoapInvoiceToModelInvoice(request.getInvoice());
    if (invoice == null) {
      response.setStatus(ResponseStatus.FAILURE);
      response.setMessage("Invoice cannot be null.");
      return response;
    }
    try {
      if (invoice.getId() != null && invoiceService.invoiceExists(invoice.getId())) {
        response.setStatus(ResponseStatus.FAILURE);
        response.setMessage("Invoice already exists");
        return response;
      }
      response.setInvoice(InvoiceMapper.mapModelInvoiceToSoapInvoice(invoiceService.addInvoice(invoice)));
      response.setStatus(ResponseStatus.SUCCESS);
      response.setMessage("OK");
      return response;
    } catch (ServiceOperationException | DatatypeConfigurationException e) {
      response.setStatus(ResponseStatus.FAILURE);
      response.setMessage(String.format("An error occurred during adding invoice. Invoice: %s", invoice));
      return response;
    }
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateInvoiceRequest")
  @ResponsePayload
  public UpdateInvoiceResponse updateInvoice(@RequestPayload UpdateInvoiceRequest request) {
    UpdateInvoiceResponse response = new UpdateInvoiceResponse();
    Invoice invoice = InvoiceMapper.mapSoapInvoiceToModelInvoice(request.getInvoice());
    if (invoice == null) {
      response.setStatus(ResponseStatus.FAILURE);
      response.setMessage("Invoice cannot be null.");
      return response;
    }
    if (!(request.getId() == (request.getInvoice().getId()))) {
      response.setStatus(ResponseStatus.FAILURE);
      response.setMessage(String.format("Invoice with %d id does not exist.", request.getId()));
      return response;
    }
    try {
      response.setInvoice(InvoiceMapper.mapModelInvoiceToSoapInvoice(invoiceService.updateInvoice(invoice)));
      response.setStatus(ResponseStatus.SUCCESS);
      response.setMessage("OK");
      return response;
    } catch (ServiceOperationException | DatatypeConfigurationException e) {
      response.setStatus(ResponseStatus.FAILURE);
      response.setMessage(String.format("An error occurred during updating invoice. Invoice: %s", invoice));
      return response;
    }
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoiceRequest")
  @ResponsePayload
  public GetInvoiceResponse getInvoice(@RequestPayload GetInvoiceRequest request) {
    GetInvoiceResponse response = new GetInvoiceResponse();
    Optional<Invoice> invoice = Optional.empty();
    try {
      invoice = invoiceService.getInvoice(request.getId());
      if (invoice.isPresent()) {
        response.setInvoice(InvoiceMapper.mapModelInvoiceToSoapInvoice(invoice.get()));
        response.setMessage("OK");
        response.setStatus(ResponseStatus.SUCCESS);
        return response;
      } else {
        response.setStatus(ResponseStatus.FAILURE);
        response.setMessage(String.format("Invoice with %d id does not exist.", request.getId()));
        return response;
      }
    } catch (ServiceOperationException | DatatypeConfigurationException e) {
      response.setStatus(ResponseStatus.FAILURE);
      response.setMessage(String.format("An error occurred during getting invoice by id. Id: %d", request.getId()));
      return response;
    }
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoiceByNumberRequest")
  @ResponsePayload
  public GetInvoiceByNumberResponse getInvoiceByNumber(@RequestPayload GetInvoiceByNumberRequest request) {
    GetInvoiceByNumberResponse response = new GetInvoiceByNumberResponse();
    Optional<Invoice> invoice = Optional.empty();
    try {
      invoice = invoiceService.getInvoiceByNumber(request.getNumber());
      if (invoice.isPresent()) {
        response.setInvoice(InvoiceMapper.mapModelInvoiceToSoapInvoice(invoice.get()));
        response.setStatus(ResponseStatus.SUCCESS);
        return response;
      } else {
        response.setStatus(ResponseStatus.FAILURE);
        response.setMessage(String.format("Invoice with %s id does not exist.", request.getNumber()));
        return response;
      }
    } catch (ServiceOperationException | DatatypeConfigurationException e) {
      response.setStatus(ResponseStatus.FAILURE);
      response.setMessage(String.format("An error occurred during getting invoice by number. Number: %s", request.getNumber()));
      return response;
    }
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllInvoices")
  @ResponsePayload
  public GetAllInvoicesResponse getAllInvoices(@RequestPayload GetAllInvoicesRequest request) {
    GetAllInvoicesResponse response = new GetAllInvoicesResponse();
    try {
      InvoicesList invoices = InvoiceMapper.mapModelInvoicesToSoapInvoices(invoiceService.getAllInvoices());
      response.setStatus(ResponseStatus.SUCCESS);
      return response;
    } catch (DatatypeConfigurationException | ServiceOperationException e) {
      response.setStatus(ResponseStatus.FAILURE);
      response.setMessage("An error occurred during getting all invoices");
      return response;
    }
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteInvoice")
  @ResponsePayload
  public DeleteInvoiceResponse deleteInvoice(@RequestPayload DeleteInvoiceRequest request) {
    DeleteInvoiceResponse response = new DeleteInvoiceResponse();
    try {
      invoiceService.deleteInvoice(request.getId());
    } catch (ServiceOperationException e) {
      response.setStatus(ResponseStatus.FAILURE);
      response.setMessage("An error occurred during deleting Invoice");
      return response;
    }
    response.setStatus(ResponseStatus.SUCCESS);
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteAllInvoices")
  @ResponsePayload
  public DeleteAllInvoicesResponse deleteAllInvoices(@RequestPayload DeleteAllInvoicesRequest request) {
    DeleteAllInvoicesResponse response = new DeleteAllInvoicesResponse();
    try {
      invoiceService.deleteAllInvoices();
    } catch (ServiceOperationException e) {
      response.setStatus(ResponseStatus.FAILURE);
      response.setMessage("An error occurred during deleting all invoices");
    }
    response.setStatus(ResponseStatus.SUCCESS);
    return response;
  }
}
