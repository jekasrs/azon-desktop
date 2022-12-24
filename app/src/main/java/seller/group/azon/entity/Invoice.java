package seller.group.azon.entity;

import java.time.LocalDate;

public class Invoice {
    Integer invoiceNumber;
    User manager;
    Seller seller;
    LocalDate date;
    Double totalSum;

    public Invoice(Integer invoiceNumber, User manager, Seller seller, LocalDate date, Double totalSum) {
        this.invoiceNumber = invoiceNumber;
        this.manager = manager;
        this.seller = seller;
        this.date = date;
        this.totalSum = totalSum;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = Math.round(totalSum *100.00)/100.00;
    }
}
