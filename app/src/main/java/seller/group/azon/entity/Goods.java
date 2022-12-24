package seller.group.azon.entity;

public class Goods {
    private Integer idOfOrderContent;
    private Integer idOfOrder;
    private String status;
    private Integer vendorCode;
    private String title;
    private String description;
    private Double price;
    private Double purchase = 0.00;
    private Integer amount = 0;

    private Double sumAndAmount;

    private Double averagePurchase;
    private Double averagePrice;
    private Integer totalAmountPurchase;
    private Integer totalAmountPrice;
    private Double profit;

    public Goods(Integer vendorCode, String title, String description, Double price) {
        this.vendorCode = vendorCode;
        this.title = (title);
        this.description = (description);
        this.price = Math.round(price*100.00)/100.00;
    }

    public Goods(Integer vendorCode, String title, String description, Double price, Integer amount) {
        this.vendorCode = vendorCode;
        this.title = (title);
        this.description = (description);
        this.price = Math.round(price*100.00)/100.00;
        this.amount = amount;
    }

    public Goods(Integer vendorCode, String title, String description, Double purchase, Double price, Integer amount) {
        this.vendorCode = (vendorCode);
        this.title = (title);
        this.description = (description);
        this.price = Math.round(price*100.00)/100.00;
        this.purchase = Math.round(purchase*100.00)/100.00;
        this.amount = (amount);
    }

    // ONLY FOR PURCHASE
    public Goods(Integer vendorCode, Integer amount, Double purchase) {
        this.vendorCode = (vendorCode);
        this.purchase = Math.round(purchase*100.00)/100.00;
        this.amount = (amount);
        this.sumAndAmount = Math.round(amount*purchase*100.00)/100.00;
    }

    // ONLY FOR SALES
    public Goods(Integer vendorCode, Integer amount, Double price, String status, Integer idOfOrderContent, Integer idOfOrder, String title) {
        this.vendorCode = (vendorCode);
        this.price = Math.round(price*100.00)/100.00;
        this.amount = (amount);
        this.sumAndAmount = Math.round(amount*price*100.00)/100.00;
        this.status = status;
        this.idOfOrderContent = idOfOrderContent;
        this.idOfOrder = idOfOrder;
        this.title = title;
    }


    // ONLY FOR ANALYSE
    public Goods(Integer vendor, Double averagePurchase, Double averagePrice, Integer totalAmountPurchase, Integer totalAmountPrice, Double profit) {
        this.vendorCode = (vendor);
        this.averagePurchase = Math.round(averagePurchase*100.00)/100.00 ;
        this.averagePrice = Math.round(averagePrice*100.00)/100.00;
        this.totalAmountPurchase = totalAmountPurchase;
        this.totalAmountPrice = totalAmountPrice;
        this.profit = Math.round(profit*100.00)/100.00 ;
    }

    public Double getAveragePurchase() {
        return averagePurchase;
    }

    public void setAveragePurchase(Double averagePurchase) {
        this.averagePurchase = averagePurchase;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public Integer getTotalAmountPurchase() {
        return totalAmountPurchase;
    }

    public void setTotalAmountPurchase(Integer totalAmountPurchase) {
        this.totalAmountPurchase = totalAmountPurchase;
    }

    public Integer getTotalAmountPrice() {
        return totalAmountPrice;
    }

    public void setTotalAmountPrice(Integer totalAmountPrice) {
        this.totalAmountPrice = totalAmountPrice;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Integer getIdOfOrder() {
        return idOfOrder;
    }

    public void setIdOfOrder(Integer idOfOrder) {
        this.idOfOrder = idOfOrder;
    }

    public Integer getIdOfOrderContent() {
        return idOfOrderContent;
    }

    public void setIdOfOrderContent(Integer idOfOrderContent) {
        this.idOfOrderContent = idOfOrderContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getSumAndAmount() {
        return sumAndAmount;
    }

    public void setSumAndAmount(Double sumAndAmount) {
        this.sumAndAmount = sumAndAmount;
    }

    public Integer getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(Integer vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = Math.round(price*100.00)/100.00;
    }

    public Double getPurchase() {
        return purchase;
    }

    public void setPurchase(Double purchase) {
        this.purchase = Math.round(purchase*100.00)/100.00;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
