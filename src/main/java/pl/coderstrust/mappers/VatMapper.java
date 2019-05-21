package pl.coderstrust.mappers;

import pl.coderstrust.model.Vat;

public class VatMapper {

  public static Vat mapSoapVatToModelVat(pl.coderstrust.soap.models.Vat soapVat) {
    switch (soapVat) {
      case VAT_0:
        return Vat.VAT_0;
      case VAT_5:
        return Vat.VAT_5;
      case VAT_8:
        return Vat.VAT_8;
      case VAT_23:
        return Vat.VAT_23;
    }
    return null;
  }

  public static pl.coderstrust.soap.models.Vat mapModelVattoSoapVat(Vat modelVat) {
    switch (modelVat) {
      case VAT_0:
        return pl.coderstrust.soap.models.Vat.VAT_0;
      case VAT_5:
        return pl.coderstrust.soap.models.Vat.VAT_5;
      case VAT_8:
        return pl.coderstrust.soap.models.Vat.VAT_8;
      case VAT_23:
        return pl.coderstrust.soap.models.Vat.VAT_23;
    }
    return null;
  }
}
