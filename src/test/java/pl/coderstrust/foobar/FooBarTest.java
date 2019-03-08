package pl.coderstrust.foobar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FooBarTest {

  @Test
  void shouldReturnFooBarForNumber15() {
    assertEquals("15 FooBar", FooBar.fooBar(15));
  }
}
