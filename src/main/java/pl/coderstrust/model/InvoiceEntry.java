package pl.coderstrust.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@ApiModel(value = "InvoiceEntry")
@Entity
public class InvoiceEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Long id;
  @ApiModelProperty(value = "productName", example = "Soda")
  private final String productName;
  @ApiModelProperty(value = "quantity", example = "5")
  private final Long quantity;
  @ApiModelProperty(value = "price", example = "2.99")
  private final BigDecimal price;
  @ApiModelProperty(value = "vat value", example = "0.72")
  private final BigDecimal vatValue;
  @ApiModelProperty(value = "Value of all items with VAT value", example = "14.95")
  private final BigDecimal grossValue;
  @ApiModelProperty(value = "Tax amount", example = "VAT_23")
  private final Vat vatRate;

  @JsonCreator
  public InvoiceEntry(@JsonProperty("id") Long id,
                      @JsonProperty("productName") String productName,
                      @JsonProperty("quantity") Long quantity,
                      @JsonProperty("price") BigDecimal price,
                      @JsonProperty("vatValue") BigDecimal vatValue,
                      @JsonProperty("grossValue") BigDecimal grossValue,
                      @JsonProperty("vatRate") Vat vatRate) {
    this.id = id;
    this.productName = productName;
    this.quantity = quantity;
    this.price = price;
    this.vatValue = vatValue;
    this.grossValue = grossValue;
    this.vatRate = vatRate;
  }

  private InvoiceEntry() {
    id = null;
    productName = null;
    quantity = null;
    price = null;
    vatValue = null;
    grossValue = null;
    vatRate = null;
  }

  public Long getId() {
    return id;
  }

  public String getProductName() {
    return productName;
  }

  public Long getQuantity() {
    return quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getVatValue() {
    return vatValue;
  }

  public BigDecimal getGrossValue() {
    return grossValue;
  }

  public Vat getVatRate() {
    return vatRate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvoiceEntry that = (InvoiceEntry) o;
    return Objects.equals(id, that.id)
        && Objects.equals(productName, that.productName)
        && Objects.equals(price, that.price)
        && Objects.equals(vatValue, that.vatValue)
        && Objects.equals(vatRate, that.vatRate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, productName, quantity, price, vatValue, grossValue, vatRate);
  }

  @Override
  public String toString() {
    return "InvoiceEntry{"
        + "id=" + id
        + ", productName='" + productName + '\''
        + ", quantity=" + quantity
        + ", price=" + price
        + ", vatValue=" + vatValue
        + ", grossValue=" + grossValue
        + ", vatRate=" + vatRate
        + '}';
  }
}
