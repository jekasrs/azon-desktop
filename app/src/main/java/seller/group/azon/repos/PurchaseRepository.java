package seller.group.azon.repos;

import seller.group.azon.entity.Goods;
import seller.group.azon.entity.Invoice;
import seller.group.azon.entity.Seller;
import seller.group.azon.entity.User;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class PurchaseRepository {

    Connection con;
    SellerRepository sellerRepository;
    UserRepository userRepository;

    public PurchaseRepository(Connection con) {
        this.con = con;
        sellerRepository = new SellerRepository(con);
        userRepository = new UserRepository(con);
    }

    // DONE
    public List<Goods> getInfoByInvoiceNumber(Integer invoiceNumber) throws SQLException {
        List<Goods> goods = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT VendorCode, Quantity, Purchase FROM InvoiceContent WHERE InvoiceNumber = " + invoiceNumber;
        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            goods.add(new Goods(res.getInt(1), res.getInt(2), res.getDouble(3)));
        }
        return goods;
    }

    // Вывод накладных, оформленных за период, отсортированных по дате оформление
    public List<Invoice> getInvoicesByDate(LocalDate dateBeg, LocalDate dateEnd) throws SQLException {
        List<Invoice> invoices = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = " SELECT Invoice.InvoiceNumber, [Date], TotalSum, " +
                "Seller.IdSeller, Staff.UserName FROM Invoice " +
                " JOIN Staff ON Invoice.IdManger = Staff.IdStaff " +
                " JOIN Seller ON Invoice.IdSeller = Seller.IdSeller " +
                "       WHERE Invoice.[Date] >= '" + dateBeg + "' AND Invoice.[Date] <= '" + dateEnd + "'" +
                "            ORDER BY Invoice.[Date] ASC";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            // ...
        }
        return invoices;
    }

    // DONE
    public List<Invoice> getInvoicesByPhoneSeller(String phone) throws SQLException {
        List<Invoice> invoices = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = " SELECT Invoice.InvoiceNumber, [Date], TotalSum, " +
                "Seller.IdSeller, Staff.UserName FROM Invoice " +
                " JOIN Staff ON Invoice.IdManger = Staff.IdStaff  " +
                " JOIN Seller ON Invoice.IdSeller = Seller.IdSeller " +
                " WHERE Seller.Phone = '" + phone + "' ORDER BY Invoice.[Date] ASC";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(5));
            Seller seller = sellerRepository.getSellerById(res.getInt(4));
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            Integer invoiceNumber = res.getInt(1);
            invoices.add(new Invoice(invoiceNumber, user, seller, localDate, totalSum));
        }
        return invoices;
    }

    // DONE
    public List<Invoice> getInvoicesByTaxPayerIdSeller(Integer taxPayerId) throws SQLException {
        List<Invoice> invoices = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = " SELECT Invoice.InvoiceNumber, [Date], TotalSum, " +
                "Seller.IdSeller, Staff.UserName FROM Invoice " +
                " JOIN Staff ON Invoice.IdManger = Staff.IdStaff " +
                " JOIN Seller ON Invoice.IdSeller = Seller.IdSeller " +
                " WHERE Seller.TaxPayerId = " + taxPayerId + " ORDER BY Invoice.[Date] ASC";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(5));
            Seller seller = sellerRepository.getSellerById(res.getInt(4));
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            Integer invoiceNumber = res.getInt(1);
            invoices.add(new Invoice(invoiceNumber, user, seller, localDate, totalSum));
        }
        return invoices;
    }

    // Вывод всех накладных оформленных менеджером
    public List<Invoice> getAllInvoicesByIdManager(Integer IdManager) throws SQLException {
        List<Invoice> invoices = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = " SELECT Invoice.InvoiceNumber, [Date], TotalSum, " +
                "Seller.IdSeller, Staff.UserName FROM Invoice " +
                " JOIN Staff ON Invoice.IdManger = Staff.IdStaff " +
                " JOIN Seller ON Invoice.IdSeller = Seller.IdSeller " +
                " WHERE Staff.IdStaff = " + IdManager + " ORDER BY Invoice.[Date] ASC";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(5));
            Seller seller = sellerRepository.getSellerById(res.getInt(4));
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            Integer invoiceNumber = res.getInt(1);
            invoices.add(new Invoice(invoiceNumber, user, seller, localDate, totalSum));
        }
        return invoices;
    }

    public void addInvoice(List<Goods> invoiceContent, Integer invoiceNumber, String sellerName, Integer idManager) throws SQLException {
        Statement statement = con.createStatement();
        LocalDate date = LocalDate.now();
        Seller seller = sellerRepository.getSellerByName(sellerName);

        String queryInvoice = "EXEC AddInvoice " + invoiceNumber + ", " + idManager + ", " + seller.getIdSeller() + ", '" + date + "'";
        statement.execute(queryInvoice);

        Integer invoiceContentId = 0;
        for (Goods g : invoiceContent) {
            invoiceContentId++;
            String invoiceSub = "EXEC AddSubInvoice " + invoiceContentId + ", " + invoiceNumber + ", " + g.getVendorCode() + ", " + g.getAmount() + ", " + g.getPurchase();
            statement.execute(invoiceSub);
        }
    }

    public List<Integer> getAllInvoiceNumbers() throws SQLException {
        List<Integer> invoices = new LinkedList<>();
        Statement statement = con.createStatement();

        String query = "SELECT InvoiceNumber FROM Invoice";
        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            invoices.add(res.getInt(1));
        }
        return invoices;
    }

    // DONE
    public Invoice getInvoiceByInvoiceNumber(Integer invoiceNumber) throws SQLException {
        Statement statement = con.createStatement();
        String query = " SELECT Invoice.InvoiceNumber, [Date], TotalSum, " +
                "Seller.IdSeller, Staff.UserName FROM Invoice " +
                " JOIN Staff ON Invoice.IdManger = Staff.IdStaff " +
                " JOIN Seller ON Invoice.IdSeller = Seller.IdSeller " +
                " WHERE InvoiceNumber = " + invoiceNumber + " ORDER BY Invoice.[Date] ASC";

        ResultSet res = statement.executeQuery(query);
        if (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(5));
            Seller seller = sellerRepository.getSellerById(res.getInt(4));
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            return new Invoice(invoiceNumber, user, seller, localDate, totalSum);
        } else
            return null;
    }

    public List<Invoice> getAllInvoices() throws SQLException {
        List<Invoice> invoices = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = " SELECT Invoice.InvoiceNumber, [Date], TotalSum, " +
                "Seller.IdSeller, Staff.UserName FROM Invoice " +
                " JOIN Staff ON Invoice.IdManger = Staff.IdStaff " +
                " JOIN Seller ON Invoice.IdSeller = Seller.IdSeller" +
                " ORDER BY Invoice.[Date] ASC";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(5));
            Seller seller = sellerRepository.getSellerById(res.getInt(4));
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            Integer invoiceNumber = res.getInt(1);
            invoices.add(new Invoice(invoiceNumber, user, seller, localDate, totalSum));
        }
        return invoices;
    }

    // DONE
    public List<Invoice> getAllInvoicesByPhoneManager(String phone) throws SQLException {
        List<Invoice> invoices = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = " SELECT Invoice.InvoiceNumber, [Date], TotalSum, " +
                "Seller.IdSeller, Staff.UserName FROM Invoice " +
                " JOIN Staff ON Invoice.IdManger = Staff.IdStaff " +
                " JOIN Seller ON Invoice.IdSeller = Seller.IdSeller" +
                " WHERE Staff.Phone = '" + phone + "' " +
                " ORDER BY Invoice.[Date] ASC";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(5));
            Seller seller = sellerRepository.getSellerById(res.getInt(4));
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            Integer invoiceNumber = res.getInt(1);
            invoices.add(new Invoice(invoiceNumber, user, seller, localDate, totalSum));
        }
        return invoices;
    }
}
