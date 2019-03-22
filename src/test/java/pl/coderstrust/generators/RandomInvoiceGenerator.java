package pl.coderstrust.generators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

public class RandomInvoiceGenerator {
  public static Invoice generateInvoiceWithId() {
    return new Invoice(generateRandomLong(), String.valueOf(generateRandomLong()), LocalDate.now(), RandomCompanyGenerator.generateCompany(), RandomCompanyGenerator.generateCompany(), generateListOfInvoiceEntries());
  }

  public static Invoice generateInvoiceWithNullId() {
    return new Invoice(null, String.valueOf(generateRandomLong()), LocalDate.now(), RandomCompanyGenerator.generateCompany(), RandomCompanyGenerator.generateCompany(), generateListOfInvoiceEntries());
  }

  private static List<InvoiceEntry> generateListOfInvoiceEntries() {
    List<InvoiceEntry> result = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      result.add(RandomInvoiceEntryGenerator.generateInvoiceEntry());
    }
    return result;
  }


  private static long generateRandomLong() {
    return (long) ThreadLocalRandom.current().nextInt(0, 1000);
  }
}
