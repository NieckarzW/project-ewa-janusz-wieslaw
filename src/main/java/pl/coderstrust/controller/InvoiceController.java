package pl.coderstrust.controller;


import java.util.Collection;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.ServiceOperationException;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

  private InvoiceService invoiceService;

  public InvoiceController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @GetMapping(value = "/all", produces = "application/json")
  @ResponseBody
  public ResponseEntity<Collection<Invoice>> getAll() {
    try {
      return new ResponseEntity<>(invoiceService.getAllInvoices(), HttpStatus.OK);
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/{id}", produces = "application/json")
  @ResponseBody
  public ResponseEntity<Invoice> getbyId(@PathVariable long id) {
    try {
      Optional<Invoice> invoiceToGet = invoiceService.getInvoice(id);
      if (invoiceToGet.isPresent()) {
        return new ResponseEntity<>(invoiceToGet.get(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/{number}", produces = "application/json")
  @ResponseBody
  public ResponseEntity<Invoice> getByNumber(@PathVariable String number) {
    try {
      Optional<Invoice> invoiceToGet = invoiceService.getInvoiceByNumber(number);
      if (invoiceToGet.isPresent()) {
        return new ResponseEntity<>(invoiceToGet.get(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  public ResponseEntity add(@RequestBody Invoice invoice) {
    try {
      invoiceService.addInvoice(invoice);
      return new ResponseEntity(HttpStatus.OK);
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping
  public ResponseEntity update(@RequestBody Invoice invoice) {
    try {
      invoiceService.updateInvoice(invoice);
      return new ResponseEntity(HttpStatus.OK);
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity remove(@PathVariable long id) {
    try {
      invoiceService.deleteInvoice(id);
      return new ResponseEntity(HttpStatus.OK);
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping
  public ResponseEntity removeAll() {
    try {
      invoiceService.deleteAllInvoices();
      return new ResponseEntity(HttpStatus.OK);
    } catch (ServiceOperationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
