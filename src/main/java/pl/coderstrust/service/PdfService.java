package pl.coderstrust.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import pl.coderstrust.model.InvoiceEntry;

@Service
public class PdfService {

  private static Logger logger = LoggerFactory.getLogger(PdfService.class);
  public final static Font NORMAL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 10,
      Font.NORMAL);

  public byte[] getInvoiceAsPdf(Invoice invoice) throws ServiceOperationException {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null.");
    }
    logger.debug("Getting invoice as pdf document.");
    try {
      Document document = new Document();
      ByteArrayOutputStream stream = new ByteArrayOutputStream();

      PdfWriter.getInstance(document, stream);
      document.open();

      addInvoiceNumber(document, invoice);
      addDateSection(document, invoice);
      addSellerAndBuyerSection(document, invoice);
      addInvoiceEntries(document, invoice);

      document.close();
      return stream.toByteArray();
    } catch (DocumentException e) {
      throw new ServiceOperationException("An error ocured during getting an invoice", e);
    }
  }

  private void addDateSection(Document invoicePdf, Invoice invoice) throws DocumentException {
    PdfPTable table = new PdfPTable(2);
    table.setHorizontalAlignment(Element.ALIGN_RIGHT);
    PdfPCell issueDate = new PdfPCell(new Phrase(String.format("Issue date:  ", invoice.getIssueDate())));
    issueDate.setBorder(Rectangle.NO_BORDER);
    issueDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
    table.addCell(issueDate);
    addEmptyLine(invoicePdf, 1);
    PdfPCell dueDate = new PdfPCell(new Phrase(String.format("Due date: ", "   ", invoice.getDueDate())));
    dueDate.setBorder(Rectangle.NO_BORDER);
    dueDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
    table.addCell(dueDate);
    invoicePdf.add(table);
  }

  private void addInvoiceNumber(Document invoicePdf, Invoice invoice) throws DocumentException {
    addEmptyLine(invoicePdf, 2);
    PdfPTable table = new PdfPTable(2);
    table.setHorizontalAlignment(Element.ALIGN_CENTER);
    PdfPCell invoiceNumber = new PdfPCell(new Phrase("InvoiceNumber:"));
    invoiceNumber.setBorder(Rectangle.NO_BORDER);
    table.addCell(invoiceNumber);
    PdfPCell invoiceGetNumber = new PdfPCell(Phrase.getInstance(invoice.getNumber()));
    table.addCell(invoiceGetNumber);
    invoiceGetNumber.setBorder(Rectangle.NO_BORDER);
    invoicePdf.add(table);
  }

  private void addSellerAndBuyerSection(Document invoicePdf, Invoice invoice) throws DocumentException {

    addEmptyLine(invoicePdf, 2);
    PdfPTable table = new PdfPTable(2);
    setNewTableFormat(table, 100, new int[] {5, 5});
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

  private void addInvoiceEntryTable(Document invoicePdf) throws DocumentException {

    addEmptyLine(invoicePdf, 2);
    PdfPTable invoiceEntryTable = new PdfPTable(6);

    PdfPCell headerCell = new PdfPCell(new Phrase("Product name"));
    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    invoiceEntryTable.addCell(headerCell);

    headerCell = new PdfPCell(new Phrase("Quantity"));
    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    invoiceEntryTable.addCell(headerCell);

    headerCell = new PdfPCell(new Phrase("Price"));
    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    invoiceEntryTable.addCell(headerCell);

    headerCell = new PdfPCell(new Phrase("VAT"));
    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    invoiceEntryTable.addCell(headerCell);

    headerCell = new PdfPCell(new Phrase("Net value"));
    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    invoiceEntryTable.addCell(headerCell);

    headerCell = new PdfPCell(new Phrase("Gross value"));
    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    invoiceEntryTable.addCell(headerCell);

    invoicePdf.add(invoiceEntryTable);
  }

  private void addInvoiceEntries(Document invoicePdf, Invoice invoice) throws DocumentException {

    addInvoiceEntryTable(invoicePdf);

    for (InvoiceEntry entry : invoice.getEntries()) {
      PdfPTable table = new PdfPTable(6);
      table.addCell(new Phrase(entry.getProductName()));
      table.addCell(new Phrase(entry.getQuantity()));
      table.addCell(new Phrase(String.valueOf(entry.getPrice())));
      table.addCell(new Phrase(String.valueOf(entry.getVatRate())));
      table.addCell(new Phrase(String.valueOf(entry.getVatValue())));
      table.addCell(new Phrase(String.valueOf(entry.getGrossValue())));

      invoicePdf.add(table);
    }
  }
}
