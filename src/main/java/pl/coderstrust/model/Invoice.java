package pl.coderstrust.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public final class Invoice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Long id;
  @ApiModelProperty(value = "Invoice Number", example = "FV 2019/05/01/01")
  private final String number;
  @ApiModelProperty(value = "Issue date", example = "2019-05-01")
  private final LocalDate issueDate;
  @ApiModelProperty(value = "Due date", example = "2019-05-15")
  private final LocalDate dueDate;
  @ManyToOne(cascade = CascadeType.ALL)
  private final Company seller;
  @ManyToOne(cascade = CascadeType.ALL)
  private final Company buyer;
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private final List<InvoiceEntry> entries;

  @JsonCreator
  public Invoice(@JsonProperty("id") Long id,
                 @JsonProperty("number") String number,
                 @JsonProperty("issueDate") LocalDate issueDate,
                 @JsonProperty("dueDate") LocalDate dueDate,
                 @JsonProperty("seller") Company seller,
                 @JsonProperty("buyer") Company buyer,
                 @JsonProperty("entries") List<InvoiceEntry> entries) {
    this.id = id;
    this.number = number;
    this.issueDate = issueDate;
    this.dueDate = dueDate;
    this.seller = seller;
    this.buyer = buyer;
    this.entries = entries;
  }

  private Invoice() {
    id = null;
    number = null;
    issueDate = null;
    dueDate = null;
    seller = null;
    buyer = null;
    entries = new ArrayList<>();
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

  public LocalDate getDueDate() {
    return dueDate;
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
        && Objects.equals(dueDate, invoice.dueDate)
        && Objects.equals(seller, invoice.seller)
        && Objects.equals(buyer, invoice.buyer)
        && Objects.equals(entries, invoice.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, number, issueDate, dueDate, seller, buyer, entries);
  }

  @Override
  public String toString() {
    return "Invoice{"
        + "id=" + id
        + ", number='" + number + '\''
        + ", issueDate=" + issueDate
        + ", dueDate=" + dueDate
        + ", seller=" + seller
        + ", buyer=" + buyer
        + ", entries=" + entries
        + '}';
  }
}
