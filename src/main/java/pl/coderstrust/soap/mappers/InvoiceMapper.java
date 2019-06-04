package pl.coderstrust.soap.mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.soap.bindingclasses.EntriesList;
import pl.coderstrust.soap.bindingclasses.Invoice;

public class InvoiceMapper {

  public static Invoice mapInvoice(pl.coderstrust.model.Invoice invoice) throws DatatypeConfigurationException {
    Invoice mappedInvoice = new Invoice();
    JAXBElement<Long> id = new JAXBElement<>(QName.valueOf("ns2:id"), Long.class, invoice.getId());
    mappedInvoice.setId(id);
    mappedInvoice.setNumber(invoice.getNumber());
    mappedInvoice.setIssueDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(invoice.getIssueDate().toString()));
    mappedInvoice.setDueDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(invoice.getDueDate().toString()));
    mappedInvoice.setSeller(CompanyMapper.mapCompany(invoice.getSeller()));
    mappedInvoice.setBuyer(CompanyMapper.mapCompany(invoice.getBuyer()));
    mappedInvoice.setEntries(mapInvoiceEntries(invoice.getEntries()));

    return mappedInvoice;
  }

  public static pl.coderstrust.model.Invoice mapInvoice(Invoice invoice) {
    Long id = invoice.getId() == null ? null : invoice.getId().getValue();
    pl.coderstrust.model.Invoice mappedInvoice = new pl.coderstrust.model.Invoice(
        id,
        invoice.getNumber(),
        invoice.getIssueDate().toGregorianCalendar().toZonedDateTime().toLocalDate(),
        invoice.getDueDate().toGregorianCalendar().toZonedDateTime().toLocalDate(),
        CompanyMapper.mapCompany(invoice.getSeller()),
        CompanyMapper.mapCompany(invoice.getBuyer()),
        mapInvoiceEntries(invoice.getEntries())
    );
    return mappedInvoice;
  }


  private static EntriesList mapInvoiceEntries(List<InvoiceEntry> entries) {
    List<pl.coderstrust.soap.bindingclasses.InvoiceEntry> soapEntries = entries.stream().map(InvoiceEntryMapper::mapInvoiceEntries).collect(Collectors.toList());
    EntriesList mappedEntries = new EntriesList();
    mappedEntries.getInvoiceEntries().addAll(soapEntries);
    return mappedEntries;
  }

  private static List<InvoiceEntry> mapInvoiceEntries(EntriesList entries) {
    List<pl.coderstrust.soap.bindingclasses.InvoiceEntry> soapEntries = entries.getInvoiceEntries();
    return soapEntries.stream().map(InvoiceEntryMapper::mapInvoiceEntries).collect(Collectors.toList());
  }

  public static List<Invoice> mapInvoices(Collection<pl.coderstrust.model.Invoice> invoices) throws DatatypeConfigurationException {
    List<Invoice> mappedInvoices = new ArrayList<>();
    for (pl.coderstrust.model.Invoice invoice : invoices) {
      mappedInvoices.add(mapInvoice(invoice));
    }
    return mappedInvoices;
  }
}
