package pl.coderstrust.service;

import static com.itextpdf.text.Rectangle.NO_BORDER;

import javax.persistence.Table;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.coderstrust.model.Invoice;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {

  private static Logger logger = LoggerFactory.getLogger(PdfService.class);
  public final static Font NORMAL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 10,
      Font.NORMAL);
  public byte[] getInvoiceAsPdf(Invoice invoice) throws ServiceOperationException, DocumentException {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null.");
    }
    logger.debug("Getting invoice as pdf document.");
    try {
      Document document = new Document();
      ByteArrayOutputStream stream = new ByteArrayOutputStream();

      PdfWriter.getInstance(document, stream);
      document.open();

      document.add(new Paragraph("Test Ewa"));

//      private static void addSellerDetails(Document document, List<DataObject> sellerDetails) {
//
//        List sellerDetails = new List();
//        Paragraph paragraph = new Paragraph();
//        paragraph.setFont(NORMAL_FONT);
//        document.add(paragraph);
//      }

      private PdfPTable getProductDetails() throw DocumentException {
        final PdfPTable productDetails = new PdfPTable(7);

        PdfPCell c1 = new PdfPCell(new Phrase("Product name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        productDetails.addCell(c1);

        c1 = new PdfPCell(new Phrase("Quantity"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        productDetails.addCell(c1);

        c1 = new PdfPCell(new Phrase("Unit"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        productDetails.addCell(c1);

        c1 = new PdfPCell(new Phrase("Price"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        productDetails.addCell(c1);

        c1 = new PdfPCell(new Phrase("VAT"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        productDetails.addCell(c1);

        c1 = new PdfPCell(new Phrase("Net value"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        productDetails.addCell(c1);

        c1 = new PdfPCell(new Phrase("Gross value"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        productDetails.addCell(c1);

        document.add(productDetails);
      }

      document.close();
      return stream.toByteArray();
    } catch (DocumentException e) {
      throw new ServiceOperationException("Errorrrrr", e);
    }
  }
}
