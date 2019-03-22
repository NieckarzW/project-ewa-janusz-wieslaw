package pl.coderstrust.generators;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;

public class RandomInvoiceEntryGenerator {
  public static InvoiceEntry generateInvoiceEntry() {
    return new InvoiceEntry(generateRandomLong(), generateRandomWord(), generateRandomLong(), BigDecimal.valueOf(generateRandomLong()), BigDecimal.valueOf(generateRandomLong()), BigDecimal.valueOf(generateRandomLong()), RandomVatRateGenerator.returnRandomVatRate(Vat.class));
  }

  private static String generateRandomWord() {
    int targetStringLength = 10;
    StringBuilder buffer = new StringBuilder(targetStringLength);
    for (int numberOfLetters = 0; numberOfLetters < 10; numberOfLetters++) {
      buffer.append((char) ThreadLocalRandom.current().nextInt(97, 122));
    }
    return buffer.toString();
  }

  private static long generateRandomLong() {
    return (long) ThreadLocalRandom.current().nextInt(0, 1000000);
  }
}
