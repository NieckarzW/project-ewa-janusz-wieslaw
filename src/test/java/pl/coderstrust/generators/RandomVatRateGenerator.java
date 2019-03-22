package pl.coderstrust.generators;

import java.util.concurrent.ThreadLocalRandom;

public class RandomVatRateGenerator {

  public static <T extends Enum<?>> T returnRandomVatRate(Class<T> vat) {
    int x = ThreadLocalRandom.current().nextInt(vat.getEnumConstants().length);
    return vat.getEnumConstants()[x];
  }
}
