package pl.coderstrust.soap.mappers;

import pl.coderstrust.soap.bindingclasses.InvoiceEntry;

public class InvoiceEntryMapper {

  public static InvoiceEntry mapInvoiceEntries(pl.coderstrust.model.InvoiceEntry entries) {
    InvoiceEntry mappedInvoiceEntries = new InvoiceEntry();
    mappedInvoiceEntries.setProductName(entries.getProductName());
    mappedInvoiceEntries.setQuantity(entries.getQuantity());
    mappedInvoiceEntries.setPrice(entries.getPrice());
    mappedInvoiceEntries.setVatValue(entries.getVatValue());
    mappedInvoiceEntries.setGrossValue(entries.getGrossValue());
    mappedInvoiceEntries.setVatRate(VatMapper.mapVat(entries.getVatRate()));

    return mappedInvoiceEntries;
  }

  public static pl.coderstrust.model.InvoiceEntry mapInvoiceEntries(InvoiceEntry entries) {
    pl.coderstrust.model.InvoiceEntry mappedInvoiceEntries = new pl.coderstrust.model.InvoiceEntry(
        Long.valueOf(entries.getId()),
        entries.getProductName(),
        entries.getQuantity(),
        entries.getPrice(),
        entries.getVatValue(),
        entries.getGrossValue(),
        VatMapper.mapVat(entries.getVatRate()));

    return mappedInvoiceEntries;
  }
}
