package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Invoice {
    private Long id;
    private String number;
    private LocalDateTime date;
    private LocalDate dueDate;
    private Company toCompany;
    private Company fromCompany;
    private List<InvoiceEntry> invoiceEntries;

    public Invoice(long id, String number, LocalDateTime date, LocalDate dueDate, Company toCompany, Company fromCompany) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.dueDate = dueDate;
        this.toCompany = toCompany;
        this.fromCompany = fromCompany;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Company getToCompany() {
        return toCompany;
    }

    public void setToCompany(Company toCompany) {
        this.toCompany = toCompany;
    }

    public Company getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(Company fromCompany) {
        this.fromCompany = fromCompany;
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
        return id == invoice.id &&
                Objects.equals(number, invoice.number) &&
                Objects.equals(date, invoice.date) &&
                Objects.equals(dueDate, invoice.dueDate) &&
                Objects.equals(toCompany, invoice.toCompany) &&
                Objects.equals(fromCompany, invoice.fromCompany);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, date, dueDate, toCompany, fromCompany);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", date=" + date +
                ", dueDate=" + dueDate +
                ", toCompany=" + toCompany +
                ", fromCompany=" + fromCompany +
                ", invoiceEntries=" + invoiceEntries +
                '}';
    }
}
