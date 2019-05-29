package pl.coderstrust.mappers;

import pl.coderstrust.soap.bindingclasses.InvoiceEntry;

public class InvoiceEntryMapper {

  public static InvoiceEntry mapModelEntryToSoapEntry(pl.coderstrust.model.InvoiceEntry modelnvoiceEntry) {
    InvoiceEntry soapInvoiceEntry = new InvoiceEntry();
    soapInvoiceEntry.setProductName(modelnvoiceEntry.getProductName());
    soapInvoiceEntry.setQuantity(modelnvoiceEntry.getQuantity());
    soapInvoiceEntry.setPrice(modelnvoiceEntry.getPrice());
    soapInvoiceEntry.setVatValue(modelnvoiceEntry.getVatValue());
    soapInvoiceEntry.setGrossValue(modelnvoiceEntry.getGrossValue());
    soapInvoiceEntry.setVatRate(VatMapper.mapModelVattoSoapVat(modelnvoiceEntry.getVatRate()));

    return soapInvoiceEntry;
  }

  public static pl.coderstrust.model.InvoiceEntry mapSoapEntryToModelEntry(InvoiceEntry soapInvoiceEntry) {
    pl.coderstrust.model.InvoiceEntry modelInvoiceEntry = new pl.coderstrust.model.InvoiceEntry(
        Long.valueOf(soapInvoiceEntry.getId()),
        soapInvoiceEntry.getProductName(),
        soapInvoiceEntry.getQuantity(),
        soapInvoiceEntry.getPrice(),
        soapInvoiceEntry.getVatValue(),
        soapInvoiceEntry.getGrossValue(),
        VatMapper.mapSoapVatToModelVat(soapInvoiceEntry.getVatRate()));

    return modelInvoiceEntry;
  }
}