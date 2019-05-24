package pl.coderstrust.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

@Service
public class InvoicePdfService {

  private static Logger logger = LoggerFactory.getLogger(InvoicePdfService.class);

  public byte[] getInvoiceAsPdf(Invoice invoice) throws ServiceOperationException {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null.");
    }
    logger.debug("Getting invoice as pdf document.");
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      Document invoicePdf = new Document();
      PdfWriter.getInstance(invoicePdf, byteArrayOutputStream);
      invoicePdf.open();
      addInvoiceNumberAndDates(invoicePdf, invoice);
      addSellerAndBuyerSection(invoicePdf, invoice);
      addEntriesSection(invoicePdf, invoice);
      invoicePdf.close();
      return byteArrayOutputStream.toByteArray();
    } catch (DocumentException e) {
      throw new ServiceOperationException("An error occurred during getting an invoice", e);
    }
  }

  private void addInvoiceNumberAndDates(Document invoicePdf, Invoice invoice) throws DocumentException {
    Paragraph line;
    line = new Paragraph("Issue date: " + invoice.getIssueDate());
    line.setAlignment(Element.ALIGN_RIGHT);
    invoicePdf.add(line);
    line = new Paragraph("Due date: " + invoice.getDueDate());
    line.setAlignment(Element.ALIGN_RIGHT);
    invoicePdf.add(line);
    line = new Paragraph("Invoice number: " + invoice.getNumber());
    invoicePdf.add(line);
  }

  private void addSellerAndBuyerSection(Document invoicePdf, Invoice invoice) throws DocumentException {
    addEmptyLine(invoicePdf, 2);
    PdfPTable table = new PdfPTable(2);
    setNewTableFormat(table, 100, new int[]{5, 5});
    PdfPCell seller = new PdfPCell(new Phrase("Seller:"));
    seller.setBorder(Rectangle.NO_BORDER);
    table.addCell(seller);
    PdfPCell buyer = new PdfPCell(new Phrase("Buyer:"));
    buyer.setBorder(Rectangle.NO_BORDER);
    table.addCell(buyer);
    table.addCell(addCompanyInformation(invoice.getSeller()));
    table.addCell(addCompanyInformation(invoice.getBuyer()));
    invoicePdf.add(table);
  }

  private void addEntriesSection(Document invoicePdf, Invoice invoice) throws DocumentException {
    addEmptyLine(invoicePdf, 2);
    addEntriesTableHeader(invoicePdf);
    addEntries(invoicePdf, invoice.getEntries());
  }

  private PdfPCell addCompanyInformation(Company company) {
    PdfPCell companyInformation = new PdfPCell();
    companyInformation.addElement(new Phrase(" Name: " + company.getName()));
    companyInformation.addElement(new Phrase(" TaxId: " + company.getTaxId()));
    companyInformation.addElement(new Phrase(" Address: " + company.getAddress()));
    companyInformation.addElement(new Phrase(" Phone number: " + company.getPhoneNumber()));
    companyInformation.addElement(new Phrase(" Email: " + company.getEmail()));
    companyInformation.addElement(new Phrase(" Account number: " + company.getAccountNumber()));
    companyInformation.setBorder(Rectangle.NO_BORDER);
    return companyInformation;
  }

  private void addEntriesTableHeader(Document invoicePdf) throws DocumentException {
    PdfPTable table = new PdfPTable(6);
    setNewTableFormat(table, 100, new int[]{2, 1, 1, 1, 1, 1});
    table.addCell("Product name");
    table.addCell("Quantity");
    table.addCell("Price");
    table.addCell("VAT");
    table.addCell("Nett value");
    table.addCell("Gross value");
    invoicePdf.add(table);
  }

  private void addEntries(Document invoicePdf, List<InvoiceEntry> entries) throws DocumentException {
    BigDecimal totalNettValue = new BigDecimal(0);
    BigDecimal totalGrossValue = new BigDecimal(0);
    for (InvoiceEntry entry : entries) {
      PdfPTable table = new PdfPTable(6);
      setNewTableFormat(table, 100, new int[]{2, 1, 1, 1, 1, 1});
      BigDecimal netValue = entry.getPrice().multiply(BigDecimal.valueOf(entry.getQuantity()));
      table.addCell(new Phrase(entry.getProductName()));
      table.addCell(new Phrase(String.valueOf(entry.getQuantity())));
      table.addCell(new Phrase(entry.getPrice() + " zł"));
      table.addCell(new Phrase(entry.getVatRate().getValue() * 100 + "%"));
      table.addCell(new Phrase(netValue + " zł"));
      table.addCell(new Phrase(entry.getGrossValue() + " zł"));
      invoicePdf.add(table);
      totalNettValue = totalNettValue.add(netValue);
      totalGrossValue = totalGrossValue.add(entry.getGrossValue());
    }
    addEntriesSummary(invoicePdf, totalNettValue, totalGrossValue);
  }

  private void addEntriesSummary(Document invoicePdf, BigDecimal totalNettValue, BigDecimal totalGrossValue) throws DocumentException {
    PdfPTable entriesTable = new PdfPTable(3);
    setNewTableFormat(entriesTable, 100, new int[]{5, 1, 1});
    entriesTable.addCell(new Phrase("Total:"));
    entriesTable.addCell(new Phrase(totalNettValue + " zł"));
    entriesTable.addCell(new Phrase(totalGrossValue + " zł"));
    invoicePdf.add(entriesTable);
  }

  private void setNewTableFormat(PdfPTable table, int widthPercentage, int[] spacing) throws DocumentException {
    table.setWidths(spacing);
    table.setWidthPercentage(widthPercentage);
  }

  private void addEmptyLine(Document invoicePdf, int number) throws DocumentException {
    Paragraph emptyLine = new Paragraph();
    for (int i = 0; i < number; i++) {
      emptyLine.add(new Paragraph(" "));
    }
    invoicePdf.add(emptyLine);
  }
}
