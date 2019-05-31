package pl.coderstrust.mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.soap.bindingclasses.EntriesList;
import pl.coderstrust.soap.bindingclasses.Invoice;

public class InvoiceMapper {

  public static Invoice mapModelInvoiceToSoapInvoice(pl.coderstrust.model.Invoice modelInvoice) throws DatatypeConfigurationException {
    Invoice soapInvoice = new Invoice();
    soapInvoice.setId(modelInvoice.getId());
    soapInvoice.setNumber(modelInvoice.getNumber());
    soapInvoice.setIssueDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(modelInvoice.getIssueDate().toString()));
    soapInvoice.setDueDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(modelInvoice.getDueDate().toString()));
    soapInvoice.setSeller(CompanyMapper.mapModelCompanyToSoapCompany(modelInvoice.getSeller()));
    soapInvoice.setBuyer(CompanyMapper.mapModelCompanyToSoapCompany(modelInvoice.getBuyer()));
    soapInvoice.setEntries(mapModelEntriesToSoapEntries(modelInvoice.getEntries()));

    return soapInvoice;
  }

  public static pl.coderstrust.model.Invoice mapSoapInvoiceToModelInvoice(Invoice soapInvoice) {
    pl.coderstrust.model.Invoice modelInvoice = new pl.coderstrust.model.Invoice(
        soapInvoice.getId(),
        soapInvoice.getNumber(),
        soapInvoice.getIssueDate().toGregorianCalendar().toZonedDateTime().toLocalDate(),
        soapInvoice.getDueDate().toGregorianCalendar().toZonedDateTime().toLocalDate(),
        CompanyMapper.mapSoapCompanyToModelCompany(soapInvoice.getSeller()),
        CompanyMapper.mapSoapCompanyToModelCompany(soapInvoice.getBuyer()),
        mapSoapEntriesToModelEntries(soapInvoice.getEntries())
    );
    return modelInvoice;
  }


  private static EntriesList mapModelEntriesToSoapEntries(List<InvoiceEntry> modelEntries) {
    List<pl.coderstrust.soap.bindingclasses.InvoiceEntry> soapEntriesFromModelEntries = modelEntries.stream().map(InvoiceEntryMapper::mapModelEntryToSoapEntry).collect(Collectors.toList());
    EntriesList soapEntries = new EntriesList();
    soapEntries.getInvoiceEntries().addAll(soapEntriesFromModelEntries);
    return soapEntries;
  }

  private static List<InvoiceEntry> mapSoapEntriesToModelEntries(EntriesList soapEntries) {
    List<pl.coderstrust.soap.bindingclasses.InvoiceEntry> listOfSoapEntries = soapEntries.getInvoiceEntries();
    return listOfSoapEntries.stream().map(InvoiceEntryMapper::mapSoapEntryToModelEntry).collect(Collectors.toList());
  }

  public static List<Invoice> mapModelInvoicesToSoapInvoices(Collection<pl.coderstrust.model.Invoice> modelInvoices) throws DatatypeConfigurationException {
    List<Invoice> soapInvoices = new ArrayList<>();
    for (pl.coderstrust.model.Invoice invoice : modelInvoices) {
      soapInvoices.add(mapModelInvoiceToSoapInvoice(invoice));
    }
    return soapInvoices;
  }
}
