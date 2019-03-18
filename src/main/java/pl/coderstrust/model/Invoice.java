package pl.coderstrust.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public final class Invoice {
  private final Long id;
  private final String number;
  private final LocalDate issueDate;
  private final Company seller;
  private final Company buyer;
  private final List<InvoiceEntry> entries;

  public Invoice(Long id, String number, LocalDate issueDate, Company toCompany, Company fromCompany, List<InvoiceEntry> invoiceEntries) {
    this.id = id;
    this.number = number;
    this.issueDate = issueDate;
    seller = toCompany;
    buyer = fromCompany;
    entries = invoiceEntries;
  }

  public Long getId() {
    return id;
  }

  public String getNumber() {
    return number;
  }

  public LocalDate getIssueDate() {
    return issueDate;
  }

  public Company getSeller() {
    return seller;
  }

  public Company getBuyer() {
    return buyer;
  }

  public List<InvoiceEntry> getEntries() {
    return entries;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Invoice invoice = (Invoice) o;
    return Objects.equals(id, invoice.id)
        && Objects.equals(number, invoice.number)
        && Objects.equals(issueDate, invoice.issueDate)
        && Objects.equals(seller, invoice.seller)
        && Objects.equals(buyer, invoice.buyer)
        && Objects.equals(entries, invoice.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, number, issueDate, seller, buyer, entries);
  }

  @Override
  public String toString() {
    return "Invoice{"
        + "id=" + id
        + ", number='" + number + '\''
        + ", issueDate=" + issueDate
        + ", seller=" + seller
        + ", buyer=" + buyer
        + ", entries=" + entries
        + '}';
  }
}
