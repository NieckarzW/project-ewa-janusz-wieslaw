package pl.coderstrust.database;

import java.util.concurrent.atomic.AtomicLong;

public class IdentifierGenerator {

  private AtomicLong identifier;

  public void initialize(long initialValue) {
    identifier = new AtomicLong(initialValue);
  }

  public long getNextId() throws IdentifierGeneratorException {
    if (identifier == null) {
      throw new IdentifierGeneratorException("Identifier has to be initialize before first use");
    }
    return identifier.incrementAndGet();
  }
}
