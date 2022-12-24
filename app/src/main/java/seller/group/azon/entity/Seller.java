package seller.group.azon.entity;

public class Seller {
    Integer idSeller;
    String name;
    Long taxPayerId;
    String phone;

    public Seller(Integer idSeller, String name, Long taxPayerId, String phone) {
        this.idSeller = idSeller;
        this.name = name;
        this.taxPayerId = taxPayerId;
        this.phone = phone;
    }

    public Integer getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(Integer idSeller) {
        this.idSeller = idSeller;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTaxPayerId() {
        return taxPayerId;
    }

    public void setTaxPayerId(Long taxPayerId) {
        this.taxPayerId = taxPayerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
