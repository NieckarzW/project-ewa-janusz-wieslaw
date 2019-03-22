package pl.coderstrust.generators;

import java.util.concurrent.ThreadLocalRandom;

import pl.coderstrust.model.Company;

public class RandomCompanyGenerator {
  public static Company generateCompany() {
    return new Company(generateRandomLong(), generateRandomWord(), generateRandomWord(), String.valueOf(generateRandomLong()), String.valueOf(generateRandomLong()), String.valueOf(generateRandomLong()), generateRandomWord());
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
