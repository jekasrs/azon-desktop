package seller.group.azon.entity;

import java.time.LocalDate;

public class User {
    public static final String admin = "Admin";
    public static final String saleManager = "Sales Manager";
    public static final String purchaseManager = "Purchasing Manager";

    private Integer id;
    private Boolean isValidAccount;
    private String userName;
    private String password;
    private String fullName;
    private String role;
    private LocalDate dateOfBirth;
    private String phone;

    private Integer amount;
    private Double totalSum;

    public User(Integer id, Boolean isValidAccount, String userName, String password, String fullName, String role, LocalDate dateOfBirth, String phone) {
        this.id = id;
        this.isValidAccount = isValidAccount;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
    }

    // ONLY FOR ANALYSE
    public User(String fullName, String phone, int amount, Double totalSum) {
        this.fullName = fullName;
        this.phone = phone;
        this.amount = amount;
        this.totalSum = Math.round(100.00*totalSum)/100.00;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = Math.round(100.00*totalSum)/100.00;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getValidAccount() {
        return isValidAccount;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setValidAccount(Boolean validAccount) {
        isValidAccount = validAccount;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}