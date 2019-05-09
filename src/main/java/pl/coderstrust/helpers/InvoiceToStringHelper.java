package pl.coderstrust.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import pl.coderstrust.model.Invoice;

public class InvoiceToStringHelper {

  @Autowired
  ObjectMapper objectMapper;

  public String invoiceToString(Invoice invoice) throws IOException {
    return objectMapper.writeValueAsString(invoice);
  }

  public Invoice stringToInvoice(String jsonInvoice) throws IOException {
    return objectMapper.readValue(jsonInvoice, Invoice.class);
  }

  public List<Invoice> listOfStringsToListOfInvoices(List<String> stringsList) {
    return stringsList
        .stream()
        .map(stringInvoice -> {
          try {
            return stringToInvoice(stringInvoice);
          } catch (IOException e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public List<String> listOfInvoicesToListOfStrings(List<Invoice> invoicesList) {
    return invoicesList
        .stream()
        .map(invoice -> {
          try {
            return invoiceToString(invoice);
          } catch (IOException e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
