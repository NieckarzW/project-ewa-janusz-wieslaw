
package pl.coderstrust.controller;


import java.util.Optional;
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
public class InvoiceController {

  private InvoiceService invoiceService;

  @Autowired
  public InvoiceController(InvoiceService invoiceService) {
    if (invoiceService == null) {
      throw new IllegalArgumentException("Invoice service cannot be null");
    }
    this.invoiceService = invoiceService;
  }

  @GetMapping(produces = "application/json")
  @ResponseBody
  public ResponseEntity<?> getAll() {
    try {
      return new ResponseEntity<>(invoiceService.getAllInvoices(), HttpStatus.OK);
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>("An error occurred during getting all invoices.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(path = "/{id}", produces = "application/json")
  @ResponseBody
  public ResponseEntity<?> getById(@PathVariable long id) {
    try {
      Optional<Invoice> invoice = invoiceService.getInvoice(id);
      if (invoice.isPresent()) {
        return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
      }
      return new ResponseEntity<>(String.format("Invoice with %d id does not exist.", id), HttpStatus.NOT_FOUND);
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>(String.format("An error occurred during getting invoice by id. Id: %d", id), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(path = "/byNumber", produces = "application/json")
  @ResponseBody
  public ResponseEntity<?> getByNumber(@RequestParam String number) {
    if (number == null) {
      return new ResponseEntity<>("Number cannot be null.", HttpStatus.BAD_REQUEST);
    }
    try {
      Optional<Invoice> invoice = invoiceService.getInvoiceByNumber(number);
      if (invoice.isPresent()) {
        return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
      }
      return new ResponseEntity<>(String.format("Invoice with %s number does not exist.", number), HttpStatus.NOT_FOUND);
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>(String.format("An error occurred during getting invoice by number. Number: %s", number), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(produces = "application/json")
  public ResponseEntity<?> add(@RequestBody(required = false) Invoice invoice) {
    if (invoice == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invoice cannot be null.");
    }
    try {
      if (invoice.getId() != null && invoiceService.invoiceExists(invoice.getId())) {
        return new ResponseEntity<>("Invoice already exist.", HttpStatus.CONFLICT);
      }
      return new ResponseEntity(invoiceService.addInvoice(invoice), HttpStatus.OK);
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>(String.format("An error occurred during adding invoice. Invoice: %s", invoice), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping(path = "/{id}", produces = "application/json")
  public ResponseEntity update(@PathVariable("id") Long id, @RequestBody(required = false) Invoice invoice) {
    if (invoice == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invoice cannot be null");
    }
    if (!id.equals(invoice.getId())) {
      return new ResponseEntity<>(String.format("Invoice to update has different id than %d.", id), HttpStatus.BAD_REQUEST);
    }
    try {
      if (!invoiceService.invoiceExists(id)) {
        return new ResponseEntity<>(String.format("Invoice with %d id does not exist.", id), HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity(invoiceService.updateInvoice(invoice), HttpStatus.OK);
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>(String.format("An error occurred during updating invoice. Invoice id: %d, invoice: %s.", id, invoice), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> remove(@PathVariable long id) {
    try {
      Optional<Invoice> invoice = invoiceService.getInvoice(id);
      if (!invoice.isPresent()) {
        return new ResponseEntity<>(String.format("Invoice with %d id does not exist.", id), HttpStatus.NOT_FOUND);
      }
      invoiceService.deleteInvoice(id);
      return new ResponseEntity(invoice, HttpStatus.OK);
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>(String.format("An error occurred during removing invoice. Invoice id: %d,", id), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping
  public ResponseEntity<?> removeAll() {
    try {
      invoiceService.deleteAllInvoices();
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>("An error occurred during removing all invoices.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
