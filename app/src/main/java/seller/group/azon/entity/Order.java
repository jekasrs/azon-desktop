package seller.group.azon.entity;

import java.time.LocalDate;

public class Order {
    Integer orderNumber;
    User manager;
    String fio;
    String phone;
    LocalDate date;
    Double totalSum;

    public Order(Integer orderNumber, User manager, String fio, String phone, LocalDate date, Double totalSum) {
        this.orderNumber = orderNumber;
        this.manager = manager;
        this.fio = fio;
        this.phone = phone;
        this.date = date;
        this.totalSum = totalSum;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        this.totalSum = Math.round(totalSum*100.00)/100.00;
    }
}
