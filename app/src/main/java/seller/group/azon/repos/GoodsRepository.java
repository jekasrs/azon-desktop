package seller.group.azon.repos;

import seller.group.azon.entity.Goods;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class GoodsRepository {

    Connection con;

    public GoodsRepository(Connection con) {
        this.con = con;
    }

    public List<Goods> getAllGoods() throws SQLException {
        List<Goods> goods = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT * FROM Goods";

        ResultSet res = statement.executeQuery(query);
        while (res.next())
            goods.add(new Goods(res.getInt(1), res.getString(2), res.getString(3), res.getDouble(4), res.getDouble(5), res.getInt(6)));

        return goods;
    }

    public List<Goods> searchWithFilter(String filterChoice, Number value) throws SQLException {
        String query = "SELECT * FROM Goods";
        switch (filterChoice) {
            case "Кол-во меньше" -> query = "SELECT * FROM Goods WHERE Amount < " + value + " ORDER BY Amount ASC";
            case "Кол-во больше" -> query = "SELECT * FROM Goods WHERE Amount > " + value + " ORDER BY Amount DESC";
            case "Цена ниже" -> query = "SELECT * FROM Goods WHERE Price <= " + value + " ORDER BY Price ASC";
            case "Цена больше" -> query = "SELECT * FROM Goods WHERE Price >= " + value + " ORDER BY Price DESC";
            case "По артикулу" -> query = "SELECT * FROM Goods WHERE VendorCode = " + value;
        }
        List<Goods> goods = new LinkedList<>();
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(query);
        while (res.next())
            goods.add(new Goods(res.getInt(1), res.getString(2), res.getString(3), res.getDouble(4), res.getDouble(5), res.getInt(6)));
        return goods;
    }

    // DONE
    public Goods getGoodsByVendorCode(Integer vendorCode) throws SQLException {
        Statement statement = con.createStatement();
        String query = "SELECT * FROM Goods WHERE VendorCode = '" + vendorCode + "'";

        ResultSet res = statement.executeQuery(query);
        if (res.next())
            return new Goods(res.getInt(1), res.getString(2), res.getString(3), res.getDouble(5), res.getInt(6));
        else
            return null;
    }

    // DONE
    public void updateGoods(Integer oldVendorCode, Goods good) throws SQLException {
        Statement statement = con.createStatement();
        String query = "UPDATE Goods SET VendorCode=" + good.getVendorCode() + ", GoodsTitle= '" + good.getTitle() + "', Description= '" + good.getDescription() + "', Price=" + good.getPrice() + " " +
                "WHERE VendorCode=" + oldVendorCode;
        statement.execute(query);
    }

    // DONE
    public Goods addGoods(Integer vendorCode, String title, String description, Double price) throws SQLException {
        Statement statement = con.createStatement();
        String query = "INSERT Goods(VendorCode, GoodsTitle, Description, Price) VALUES (" + vendorCode + ", '" + title + "', '" + description + "', " + price + ")";
        System.out.println(query);
        statement.execute(query);

        return new Goods(vendorCode, title, description, price);
    }

    public List<Goods> getAllAvailableGoods() throws SQLException {
        List<Goods> goods = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT * FROM Goods WHERE Amount > 0";

        ResultSet res = statement.executeQuery(query);
        while (res.next())
            goods.add(new Goods(res.getInt(1), res.getString(2), res.getString(3), res.getDouble(4), res.getDouble(5), res.getInt(6)));

        return goods;
    }

    public List<Goods> getAnalyze(LocalDate dateBeg, LocalDate dateEnd) throws SQLException {
        List<Goods> goods = new LinkedList<>();
        Statement statement = con.createStatement();

        List<Integer> vendorCodes = getAllVendorCodes();
        for (Integer vendor : vendorCodes) {
            Integer qS = getQuantityOfSales(vendor, dateBeg, dateEnd);
            Integer qP = getQuantityOfPurchase(vendor, dateBeg, dateEnd);
            Double sS = getSumOfSales(vendor, dateBeg, dateEnd);
            Double sP = getSumOfPurchase(vendor, dateBeg, dateEnd);

            // 1. "vendorCode"
            // 2. "averagePurchase"
            // 3. "averagePrice"
            // 4. "totalAmountPurchase"
            // 5. "totalAmountPrice"
            // 6. "profit"
            Goods good = new Goods(vendor, sP / qP.doubleValue(), sS / qS.doubleValue(), qP, qS, sS - sP);
            goods.add(good);
        }
        return goods;
    }

    public List<Integer> getAllVendorCodes() throws SQLException {
        List<Integer> vendorCodes = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT VendorCode FROM Goods";

        ResultSet res = statement.executeQuery(query);
        while (res.next())
            vendorCodes.add(res.getInt(1));

        return vendorCodes;
    }


    // DONE
    public Integer getQuantityOfSales(Integer vendorCode, LocalDate dateBeg, LocalDate dateEnd) throws SQLException {
        Statement statement = con.createStatement();
        String queryAmountPrices = "SELECT SUM(Quantity) AS SumQuantity FROM OrderContent " +
                "JOIN Orders ON Orders.OrderNumber = OrderContent.OrderNumber " +
                "WHERE OrderContent.VendorCode =" + vendorCode + " AND" +
                " Orders.[Date] >= ' " + dateBeg + "' AND" +
                " Orders.[Date] <= ' " + dateEnd + "'";
        ResultSet res = statement.executeQuery(queryAmountPrices);
        if (res.next())
            return res.getInt(1);
        else
            return 0;
    }

    // DONE
    public Integer getQuantityOfPurchase(Integer vendorCode, LocalDate dateBeg, LocalDate dateEnd) throws SQLException {
        Statement statement = con.createStatement();
        String queryAmountPurchase = "SELECT SUM(Quantity) AS SumQuantity FROM InvoiceContent " +
                "JOIN Invoice ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber " +
                "WHERE InvoiceContent.VendorCode =" + vendorCode + " AND" +
                " Invoice.[Date] >= ' " + dateBeg + "' AND" +
                " Invoice.[Date] <= ' " + dateEnd + "'";
        ResultSet res = statement.executeQuery(queryAmountPurchase);
        if (res.next())
            return res.getInt(1);
        else
            return 0;
    }

    // DONE
    public Double getSumOfSales(Integer vendorCode, LocalDate dateBeg, LocalDate dateEnd) throws SQLException {
        Statement statement = con.createStatement();
        String querySumPrices = "SELECT SUM(SumPriceOneItem) FROM ( " +
                "SELECT (Price)*(Quantity) AS SumPriceOneItem FROM OrderContent " +
                "JOIN Orders ON Orders.OrderNumber = OrderContent.OrderNumber " +
                "WHERE OrderContent.VendorCode =" + vendorCode + " AND" +
                " Orders.[Date] >= ' " + dateBeg + "' AND" +
                " Orders.[Date] <= ' " + dateEnd + "' ) as PriceQuantity";
        ResultSet res = statement.executeQuery(querySumPrices);
        if (res.next())
            return res.getDouble(1);
        else
            return 0.00;
    }

    // DONE
    public Double getSumOfPurchase(Integer vendorCode, LocalDate dateBeg, LocalDate dateEnd) throws SQLException {
        Statement statement = con.createStatement();
        String querySumPurchase = "SELECT SUM(SumPurchaseOneItem) FROM ( " +
                "SELECT (Purchase)*(Quantity) AS SumPurchaseOneItem FROM InvoiceContent " +
                "JOIN Invoice ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber " +
                "WHERE InvoiceContent.VendorCode =" + vendorCode + " AND" +
                " Invoice.[Date] >= ' " + dateBeg + "' AND" +
                " Invoice.[Date] <= ' " + dateEnd + "' ) as PurchaseQuantity";
        ResultSet res = statement.executeQuery(querySumPurchase);
        if (res.next())
            return res.getDouble(1);
        else
            return 0.00;
    }

    public Goods getAnalyze(LocalDate dateBeg, LocalDate dateEnd, Integer filterValue) throws SQLException {
        Integer qS = getQuantityOfSales(filterValue, dateBeg, dateEnd);
        Integer qP = getQuantityOfPurchase(filterValue, dateBeg, dateEnd);
        Double sS = getSumOfSales(filterValue, dateBeg, dateEnd);
        Double sP = getSumOfPurchase(filterValue, dateBeg, dateEnd);

        // 1. "vendorCode"
        // 2. "averagePurchase"
        // 3. "averagePrice"
        // 4. "totalAmountPurchase"
        // 5. "totalAmountPrice"
        // 6. "profit"
        return new Goods(filterValue, sP / qP.doubleValue(), sS / qS.doubleValue(), qP, qS, sS - sP);
    }

}
