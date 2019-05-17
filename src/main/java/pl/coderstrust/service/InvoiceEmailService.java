package pl.coderstrust.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.coderstrust.model.Invoice;

@Service
public class InvoiceEmailService {
  private static Logger logger = LoggerFactory.getLogger(InvoiceEmailService.class);

  private JavaMailSender javaMailSender;

  private MailProperties mailProperties;

  @Autowired
  public InvoiceEmailService(JavaMailSender javaMailSender, MailProperties mailProperties) {
    if (javaMailSender == null) {
      throw new IllegalArgumentException("Java mail sender cannot be null");
    }
    if (mailProperties == null) {
      throw new IllegalArgumentException("Mail properties cannot be null");
    }
    this.javaMailSender = javaMailSender;
    this.mailProperties = mailProperties;
  }

  @Async
  public void sendEmailWithInvoice(Invoice invoice) {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    logger.debug("Sending an email with invoice: {}", invoice);
    try {
      MimeMessage mail = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mail, true);
      helper.setTo(mailProperties.getProperties().get("receiver"));
      helper.setReplyTo(mailProperties.getUsername());
      helper.setFrom(mailProperties.getUsername());
      helper.setSubject(mailProperties.getProperties().get("title"));
      helper.setText(mailProperties.getProperties().get("content"));
      javaMailSender.send(mail);
    } catch (MessagingException e) {
      logger.error("An error occurred during sending an email with invoice.", e);
    }
  }
}
