package pl.coderstrust.generators;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;

public class InvoiceEntryGenerator {

  public static InvoiceEntry getRandomEntry() {
    Long id = IdGenerator.getNextId();
    String productName = WordGenerator.getRandomWord();
    long quantity = ThreadLocalRandom.current().nextLong(1, 999);
    BigDecimal price = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(1, 50) * quantity);
    Vat vatRate = VatRateGenerator.getRandomVatRate(Vat.class);
    BigDecimal vatValue = price.multiply(BigDecimal.valueOf(vatRate.getValue())).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    BigDecimal grossValue = price.add(vatValue);

    return new InvoiceEntry(id,
            productName,
            quantity,
            price,
            vatValue,
            grossValue,
            vatRate);
  }
}
