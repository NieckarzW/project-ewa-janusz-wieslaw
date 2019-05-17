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
      addSellerAndBuyerSection(document, invoice);
      addInvoiceEntryTable(document, invoice);

      document.close();
      return stream.toByteArray();
    } catch (DocumentException e) {
      throw new ServiceOperationException("Errorrrrr", e);
    }
  }

  private void addDateSection(Document invoicePdf, Invoice invoice) throws DocumentException {

    PdfPTable table = new PdfPTable(2);
    invoicePdf.add(table);
    //   table.addCell(addDateInformation(addDateInformation(LocalDate));
  }

  private PdfPCell addDateInformation(LocalDate date) {
    PdfPCell dateInformation = new PdfPCell();
//    dateInformation.addElement(new Phrase(" Issue date: " + dateInformation.dat()));
//    dateInformation.addElement(new Phrase(" Due date: " + dateInformation.getTaxId()));
    dateInformation.setBorder(Rectangle.NO_BORDER);
    return dateInformation;
  }

  private void addInvoiceNumber(Document invoicePdf, Invoice invoice) throws DocumentException {
    PdfPTable table = new PdfPTable(2);
    PdfPCell invoiceNumber = new PdfPCell(new Phrase("InvoiceNumber:"));
    invoiceNumber.setBorder(Rectangle.NO_BORDER);
    table.addCell(invoiceNumber);
    table.addCell(invoice.getNumber());
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

  private void addInvoiceEntryTable(Document invoicePdf, Invoice invoice) throws DocumentException {

    addEmptyLine(invoicePdf, 2);
    final PdfPTable invoiceEntryTable = new PdfPTable(7);

    PdfPCell headerCell = new PdfPCell(new Phrase("Product name"));
    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    invoiceEntryTable.addCell(headerCell);

    headerCell = new PdfPCell(new Phrase("Quantity"));
    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    invoiceEntryTable.addCell(headerCell);

    headerCell = new PdfPCell(new Phrase("Unit"));
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

//    invoiceEntryTable.addCell(addInvoiceEntryDetails(invoiceEntry.getProductName());
    invoicePdf.add(invoiceEntryTable);
  }

  private PdfPCell addInvoiceEntryDetails(InvoiceEntry invoiceEntry) {
    PdfPCell invoiceEntryDetails = new PdfPCell();
    invoiceEntryDetails.addElement(new Phrase(" ProductName: " + invoiceEntry.getProductName()));

    return invoiceEntryDetails;
  }
}
