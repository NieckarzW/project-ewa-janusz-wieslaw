package pl.coderstrust.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.ServiceOperationException;

@RestController
@RequestMapping("/invoices")
@Api(value = "/invoices", description = "Operations on invoices")
public class InvoiceController {

  private static Logger logger = LoggerFactory.getLogger(InvoiceController.class);
  private InvoiceService invoiceService;

  @Autowired
  public InvoiceController(InvoiceService invoiceService) {
    if (invoiceService == null) {
      throw new IllegalArgumentException("Invoice service cannot be null");
    }
    this.invoiceService = invoiceService;
  }

  @ApiOperation(value = "gets all invoices from database",
      response = Invoice.class,
      responseContainer = "List")
  @GetMapping(produces = "application/json")
  @ResponseBody
  public ResponseEntity<?> getAll() {
    try {
      logger.debug("Getting all invoices");
      return new ResponseEntity<>(invoiceService.getAllInvoices(), HttpStatus.OK);
    } catch (ServiceOperationException e) {
      String message = String.format("An error occurred during getting all invoices.");
      logger.error(message, e);
      return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @ApiOperation(value = "Gets Invoice from database by ID",
      response = Invoice.class)
  @GetMapping(path = "/{id}", produces = "application/json")
  @ResponseBody
  public ResponseEntity<?> getById(@PathVariable long id) {
    try {
      logger.debug("Getting invoice with following id: {}", id);
      Optional<Invoice> invoice = invoiceService.getInvoice(id);
      if (invoice.isPresent()) {
        return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
      }
      return new ResponseEntity<>(String.format("Invoice with %d id does not exist.", id), HttpStatus.NOT_FOUND);
    } catch (ServiceOperationException e) {
      String message = String.format("An error occurred during getting invoice by id. Id: %d", id);
      logger.error(message, e);
      return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @ApiOperation(value = "Gets Invoice from database by Number",
      response = Invoice.class)
  @GetMapping(path = "/byNumber", produces = "application/json")
  @ResponseBody
  public ResponseEntity<?> getByNumber(@RequestParam String number) {
    if (number == null) {
      return new ResponseEntity<>("Number cannot be null.", HttpStatus.BAD_REQUEST);
    }
    try {
      logger.debug("Getting invoice with following number: {}", number);
      Optional<Invoice> invoice = invoiceService.getInvoiceByNumber(number);
      if (invoice.isPresent()) {
        return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
      }
      return new ResponseEntity<>(String.format("Invoice with %s number does not exist.", number), HttpStatus.NOT_FOUND);
    } catch (ServiceOperationException e) {
      String message = String.format("An error occurred during getting invoice by number. Number: %s", number);
      logger.error(message, e);
      return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @ApiOperation(value = "Adds Invoice to database",
      notes = "Invoice should have null ID. Use update to change existing Invoice",
      response = Invoice.class)
  @PostMapping(produces = "application/json")
  public ResponseEntity<?> add(@RequestBody(required = false) Invoice invoice) {
    if (invoice == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invoice cannot be null.");
    }
    try {
      logger.debug("Adding invoice: {}", invoice);
      if (invoice.getId() != null && invoiceService.invoiceExists(invoice.getId())) {
        return new ResponseEntity<>("Invoice already exist.", HttpStatus.CONFLICT);
      }
      return new ResponseEntity(invoiceService.addInvoice(invoice), HttpStatus.OK);
    } catch (ServiceOperationException e) {
      String message = String.format("An error occurred during adding invoice. Invoice: %s", invoice);
      logger.error(message, e);
      return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @ApiOperation(value = "Updates Invoice in database",
      notes = "Invoice should have existing ID. Use add to create new Invoice",
      response = Invoice.class)
  @PutMapping(path = "/{id}", produces = "application/json")
  public ResponseEntity update(@PathVariable("id") Long id, @RequestBody(required = false) Invoice invoice) {
    if (invoice == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invoice cannot be null");
    }
    if (!id.equals(invoice.getId())) {
      return new ResponseEntity<>(String.format("Invoice to update has different id than %d.", id), HttpStatus.BAD_REQUEST);
    }
    try {
      logger.debug("Updating invoice: {}", invoice);
      if (!invoiceService.invoiceExists(id)) {
        return new ResponseEntity<>(String.format("Invoice with %d id does not exist.", id), HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity(invoiceService.updateInvoice(invoice), HttpStatus.OK);
    } catch (ServiceOperationException e) {
      String message = String.format("An error occurred during updating invoice. Invoice id: %d, invoice: %s.", id, invoice);
      logger.error(message, e);
      return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @ApiOperation(value = "Deletes Invoice from database",
      notes = "Provide ID of invoice to remove")
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> remove(@PathVariable long id) {
    try {
      logger.debug("Removing invoice with following id: {}", id);
      Optional<Invoice> invoice = invoiceService.getInvoice(id);
      if (!invoice.isPresent()) {
        return new ResponseEntity<>(String.format("Invoice with %d id does not exist.", id), HttpStatus.NOT_FOUND);
      }
      invoiceService.deleteInvoice(id);
      return new ResponseEntity(invoice, HttpStatus.OK);
    } catch (ServiceOperationException e) {
      String message = String.format("An error occurred during removing invoice. Invoice id: %d", id);
      logger.error(message, e);
      return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @ApiOperation(value = "Deletes all invoices in database")
  @DeleteMapping
  public ResponseEntity<?> removeAll() {
    try {
      logger.debug("Deleting all invoices");
      invoiceService.deleteAllInvoices();
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    } catch (ServiceOperationException e) {
      String message = String.format("An error occurred during removing all invoices.");
      logger.error(message, e);
      return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
