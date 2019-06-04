package pl.coderstrust.soap;

import java.util.Collection;
import java.util.Optional;
import javax.xml.datatype.DatatypeConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.coderstrust.controller.InvoiceController;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.ServiceOperationException;
import pl.coderstrust.soap.bindingclasses.DeleteAllInvoicesRequest;
import pl.coderstrust.soap.bindingclasses.DeleteInvoiceRequest;
import pl.coderstrust.soap.bindingclasses.GetAllInvoicesRequest;
import pl.coderstrust.soap.bindingclasses.GetInvoiceByNumberRequest;
import pl.coderstrust.soap.bindingclasses.GetInvoiceRequest;
import pl.coderstrust.soap.bindingclasses.InvoiceResponse;
import pl.coderstrust.soap.bindingclasses.InvoicesResponse;
import pl.coderstrust.soap.bindingclasses.Response;
import pl.coderstrust.soap.bindingclasses.ResponseStatus;
import pl.coderstrust.soap.bindingclasses.SaveInvoiceRequest;
import pl.coderstrust.soap.bindingclasses.UpdateInvoiceRequest;
import pl.coderstrust.soap.mappers.InvoiceMapper;

@Endpoint
public class InvoiceEndpoint {
  private static Logger log = LoggerFactory.getLogger(InvoiceEndpoint.class);
  private static final String NAMESPACE_URI = "http://project-10-ewa-januss-wieslaw";

  private InvoiceService invoiceService;

  @Autowired
  public InvoiceEndpoint(InvoiceService invoiceService) {
    if (invoiceService == null) {
      throw new IllegalArgumentException("Invoice service cannot be null.");
    }
    this.invoiceService = invoiceService;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "saveInvoiceRequest")
  @ResponsePayload
  public InvoiceResponse addInvoice(@RequestPayload SaveInvoiceRequest request) {
    try {
      log.debug("Adding invoice: {}", request.getInvoice());
      Invoice invoice = InvoiceMapper.mapInvoice(request.getInvoice());
      if (invoice.getId() != null && invoiceService.invoiceExists(invoice.getId())) {
        return createErrorInvoiceResponse("Invoice already exists");
      }
      Invoice addedInvoice = invoiceService.addInvoice(invoice);
      return createSuccessInvoiceResponse(InvoiceMapper.mapInvoice(addedInvoice));
    } catch (ServiceOperationException | DatatypeConfigurationException e) {
      String message = "An error occurred during adding invoice.";
      log.error(message);
      return createErrorInvoiceResponse(message);
    }
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateInvoiceRequest")
  @ResponsePayload
  public InvoiceResponse updateInvoice(@RequestPayload UpdateInvoiceRequest request) {
    try {
      log.debug("Updating invoice: {}", request.getInvoice());
      Invoice invoice = InvoiceMapper.mapInvoice(request.getInvoice());
      if (!(request.getId() == (invoice.getId()))) {
        String message = String.format("Invoice with %d id does not exist.", request.getId());
        log.error(message);
        return createErrorInvoiceResponse(message);
      }
      Invoice updatedInvoice = invoiceService.updateInvoice(invoice);
      return createSuccessInvoiceResponse(InvoiceMapper.mapInvoice(updatedInvoice));
    } catch (ServiceOperationException | DatatypeConfigurationException e) {
      String message = "An error occurred during updating invoice.";
      log.error(message);
      return createErrorInvoiceResponse(message);
    }
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoiceRequest")
  @ResponsePayload
  public InvoiceResponse getInvoice(@RequestPayload GetInvoiceRequest request) {
    try {
      log.debug("Getting an invoice by id: {}", request.getId());
      Optional<Invoice> invoiceOptional = invoiceService.getInvoice(request.getId());
      if (invoiceOptional.isPresent()) {
        return createSuccessInvoiceResponse(InvoiceMapper.mapInvoice(invoiceOptional.get()));
      }
      String message = String.format("Invoice with %d id does not exist.", request.getId());
      log.error(message);
      return createErrorInvoiceResponse(message);
    } catch (ServiceOperationException | DatatypeConfigurationException e) {
      String message = "An error occurred during getting invoice by id.";
      log.error(message);
      return createErrorInvoiceResponse(message);
    }
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoiceByNumberRequest")
  @ResponsePayload
  public InvoiceResponse getInvoiceByNumber(@RequestPayload GetInvoiceByNumberRequest request) {
    try {
      log.debug("Getting an invoice by number: {}", request.getNumber());
      Optional<Invoice> invoiceOptional = invoiceService.getInvoiceByNumber(request.getNumber());
      if (invoiceOptional.isPresent()) {
        return createSuccessInvoiceResponse(InvoiceMapper.mapInvoice(invoiceOptional.get()));
      }
      String message = String.format("Invoice with %s number does not exist.", request.getNumber());
      log.error(message);
      return createErrorInvoiceResponse(message);
    } catch (ServiceOperationException | DatatypeConfigurationException e) {
      String message = "An error occurred during getting invoice by number.";
      log.error(message);
      return createErrorInvoiceResponse(message);
    }
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllInvoicesRequest")
  @ResponsePayload
  public InvoicesResponse getAllInvoices(@RequestPayload GetAllInvoicesRequest request) {
    try {
      log.debug("Getting all invoices");
      Collection<Invoice> invoices = invoiceService.getAllInvoices();
      return createSuccessInvoicesResponse(InvoiceMapper.mapInvoices(invoices));
    } catch (DatatypeConfigurationException | ServiceOperationException e) {
      String message = "An error occurred during getting all invoices.";
      log.error(message);
      return createErrorInvoicesResponse(message);
    }
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteInvoiceRequest")
  @ResponsePayload
  public Response deleteInvoice(@RequestPayload DeleteInvoiceRequest request) {
    try {
      log.debug("Deleting invoice by id: {}", request.getId());
      invoiceService.deleteInvoice(request.getId());
      return createSuccessResponse();
    } catch (ServiceOperationException e) {
      String message = "An error occurred during deleting invoice.";
      log.error(message);
      return createErrorResponse(message);
    }
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteAllInvoicesRequest")
  @ResponsePayload
  public Response deleteAllInvoices(@RequestPayload DeleteAllInvoicesRequest request) {
    try {
      log.debug("Deleting all invoices");
      invoiceService.deleteAllInvoices();
      return createSuccessResponse();
    } catch (ServiceOperationException e) {
      String message = "An error occurred during deleting all invoices.";
      log.error(message);
      return createErrorResponse(message);
    }
  }

  private InvoiceResponse createSuccessInvoiceResponse(pl.coderstrust.soap.bindingclasses.Invoice invoice) {
    InvoiceResponse invoiceResponse = new InvoiceResponse();
    invoiceResponse.setStatus(ResponseStatus.SUCCESS);
    invoiceResponse.setInvoice(invoice);
    invoiceResponse.setMessage("");
    return invoiceResponse;
  }

  private InvoiceResponse createErrorInvoiceResponse(String errorMessage) {
    InvoiceResponse invoiceResponse = new InvoiceResponse();
    invoiceResponse.setStatus(ResponseStatus.FAILURE);
    invoiceResponse.setMessage(errorMessage);
    return invoiceResponse;
  }

  private InvoicesResponse createSuccessInvoicesResponse(Collection<pl.coderstrust.soap.bindingclasses.Invoice> invoices) {
    InvoicesResponse invoicesResponse = new InvoicesResponse();
    invoicesResponse.setStatus(ResponseStatus.SUCCESS);
    invoicesResponse.getInvoices();
    for (pl.coderstrust.soap.bindingclasses.Invoice invoice : invoices) {
      invoicesResponse.getInvoices().add(invoice);
    }
    invoicesResponse.setMessage("");
    return invoicesResponse;
  }

  private InvoicesResponse createErrorInvoicesResponse(String errorMessage) {
    InvoicesResponse invoicesResponse = new InvoicesResponse();
    invoicesResponse.setStatus(ResponseStatus.FAILURE);
    invoicesResponse.setMessage(errorMessage);
    return invoicesResponse;
  }

  private Response createSuccessResponse() {
    Response response = new Response();
    response.setStatus(ResponseStatus.SUCCESS);
    response.setMessage("");
    return response;
  }

  private Response createErrorResponse(String errorMessage) {
    Response response = new Response();
    response.setStatus(ResponseStatus.FAILURE);
    response.setMessage(errorMessage);
    return response;
  }
}
