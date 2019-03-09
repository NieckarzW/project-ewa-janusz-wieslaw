package invoice;

import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceEntry {

    private String id;
    private String description;
    private BigDecimal value;
    private BigDecimal vatValue;
    private VAT vatRate;

    public InvoiceEntry(String id, String description, BigDecimal value, double vatValue, double vatRate) {
        this.id = id;
        this.description = description;
        this.value = value;
        this.vatValue = vatValue;
        this.vatRate = vatRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getVatValue() {
        return vatValue;
    }

    public void setVatValue(BigDecimal vatValue) {
        this.vatValue = vatValue;
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceEntry that = (InvoiceEntry) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(value, that.value) &&
                Objects.equals(vatValue, that.vatValue) &&
                Objects.equals(vatRate, that.vatRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, value, vatValue, vatRate);
    }
}
