package pl.coderstrust.foobar;

public class FooBar {

  public static String fooBar(int number) {
    if (number % 15 == 0) {
      return number + " FooBar";
    }
    if (number % 5 == 0) {
      return number + " Bar";
    }
    if (number % 3 == 0) {
      return number + " Foo";
    }
    return number + "";
  }
}
