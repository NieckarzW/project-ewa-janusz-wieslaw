package pl.coderstrust.soap.mappers;

import pl.coderstrust.model.Vat;

public class VatMapper {

  public static Vat mapVat(pl.coderstrust.soap.bindingclasses.Vat vat) {
    switch (vat) {
      case VAT_0:
        return Vat.VAT_0;
      case VAT_5:
        return Vat.VAT_5;
      case VAT_8:
        return Vat.VAT_8;
      case VAT_23:
        return Vat.VAT_23;
      default:
        return null;
    }
  }

  public static pl.coderstrust.soap.bindingclasses.Vat mapVat(Vat vat) {
    switch (vat) {
      case VAT_0:
        return pl.coderstrust.soap.bindingclasses.Vat.VAT_0;
      case VAT_5:
        return pl.coderstrust.soap.bindingclasses.Vat.VAT_5;
      case VAT_8:
        return pl.coderstrust.soap.bindingclasses.Vat.VAT_8;
      case VAT_23:
        return pl.coderstrust.soap.bindingclasses.Vat.VAT_23;
      default:
        return null;
    }
  }
}
