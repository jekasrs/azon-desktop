package seller.group.azon.repos;

import seller.group.azon.entity.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SellerRepository {
    private Connection con;

    public SellerRepository(Connection con) {
        this.con = con;
    }

    public List<Seller> getAllSellers() throws SQLException {
        List<Seller> sellers = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT * FROM Seller";

        ResultSet res = statement.executeQuery(query);
        while (res.next())
            sellers.add(new Seller(res.getInt(1), res.getString(2), res.getLong(3), res.getString(4)));

        return sellers;
    }

    // DONE
    public Seller getSellerById(Integer idSeller) throws SQLException {
        Statement statement = con.createStatement();
        String query = "SELECT * FROM Seller WHERE IdSeller = " + idSeller;

        ResultSet res = statement.executeQuery(query);
        if (res.next())
            return new Seller(res.getInt(1), res.getString(2), res.getLong(3), res.getString(4));
        else
            return null;
    }

    // DONE
    public Seller getSellerByTaxPayer(Long taxPayer) throws SQLException {
        Statement statement = con.createStatement();
        String query = "SELECT * FROM Seller WHERE TaxPayerId = " + taxPayer;

        ResultSet res = statement.executeQuery(query);
        if (res.next())
            return new Seller(res.getInt(1), res.getString(2), res.getLong(3), res.getString(4));
        else
            return null;
    }

    // DONE
    public List<Seller> getSellersByPhone(String value) throws SQLException {
        String query = "SELECT * FROM Seller WHERE Phone = '" + value + "'";

        List<Seller> sellers = new LinkedList<>();
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(query);
        while (res.next())
            sellers.add(new Seller(res.getInt(1), res.getString(2), res.getLong(3), res.getString(4)));
        return sellers;
    }

    public List<Seller> getSellersByTaxId(Number value) throws SQLException {
        String query = "SELECT * FROM Seller WHERE TaxPayerId = " + value;

        List<Seller> sellers = new LinkedList<>();
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(query);
        while (res.next())
            sellers.add(new Seller(res.getInt(1), res.getString(2), res.getLong(3), res.getString(4)));
        return sellers;
    }

    public List<String> getAllPhones() throws SQLException {
        List<String> phones = new ArrayList<>();
        Statement statement = con.createStatement();
        String query = "SELECT Phone FROM Seller";

        ResultSet res = statement.executeQuery(query);
        while (res.next())
            phones.add(res.getString(1));
        return phones;
    }

    public List<Long> getAllTaxPayerId() throws SQLException {
        List<Long> taxPayersId = new ArrayList<>();
        Statement statement = con.createStatement();
        String query = "SELECT TaxPayerId FROM Seller";

        ResultSet res = statement.executeQuery(query);
        while (res.next())
            taxPayersId.add(res.getLong(1));
        return taxPayersId;
    }

    // DONE
    public void updateSeller(Integer IdSeller, String name, Long taxPayer, String phone) throws SQLException {
        Statement statement = con.createStatement();
        String query = "UPDATE Seller SET Name='" + name + "', TaxPayerId=" + taxPayer + ", Phone= '" + phone + "' WHERE IdSeller=" + IdSeller;
        statement.execute(query);
    }

    public Seller addSeller(String name, Long taxPayer, String phone) throws SQLException {
        Statement statement = con.createStatement();
        String query = "INSERT Seller(Name, TaxPayerId, Phone) VALUES ( '" + name + "', " + taxPayer + ", '" + phone + "')";

        statement.execute(query);
        return getSellerByTaxPayer(taxPayer);
    }

    public Seller getSellerByName(String sellerName) throws SQLException {
        Statement statement = con.createStatement();
        String query = "SELECT * FROM Seller WHERE Name = '" + sellerName + "'";

        ResultSet res = statement.executeQuery(query);
        if (res.next())
            return new Seller(res.getInt(1), res.getString(2), res.getLong(3), res.getString(4));
        else
            return null;
    }
}
