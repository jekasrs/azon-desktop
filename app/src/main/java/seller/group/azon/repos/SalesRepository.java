package seller.group.azon.repos;

import seller.group.azon.entity.*;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class SalesRepository {

    Connection con;
    UserRepository userRepository;

    public SalesRepository(Connection con) {
        this.con = con;
        userRepository = new UserRepository(con);
    }

    public List<Integer> getAllOrderNumbers() throws SQLException {
        List<Integer> orders = new LinkedList<>();
        Statement statement = con.createStatement();

        String query = "SELECT OrderNumber FROM Orders";
        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            orders.add(res.getInt(1));
        }
        return orders;
    }

    public void addOrder(List<Goods> orderContent, Integer orderNumber, String buyerName, String phoneNumber, Integer idManager) throws SQLException {
        Statement statement = con.createStatement();
        LocalDate date = LocalDate.now();

        String queryOrder = "EXEC AddOrder " + orderNumber + ", " + idManager + ", '" + phoneNumber + "', '" + buyerName + "', '" + date + "'";
        statement.execute(queryOrder);

        Integer invoiceOrderId = 0;
        for (Goods g : orderContent) {
            invoiceOrderId++;
            String invoiceSub = "EXEC AddSubOrder " + invoiceOrderId + ", " + orderNumber + ", " + 1 + ", " + g.getVendorCode() + ", " + g.getAmount();
            statement.execute(invoiceSub);
        }
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.UserName FROM Orders \n" +
                " JOIN Staff ON Orders.IdManager = Staff.IdStaff " +
                " ORDER BY Orders.[Date] ASC";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(6));
            String nameCustomer = res.getString(5);
            String phoneCustomer = res.getString(4);
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            Integer orderNumber = res.getInt(1);
            orders.add(new Order(orderNumber, user, nameCustomer, phoneCustomer, localDate, totalSum));
        }
        return orders;
    }

    public Order getOrderByOrderNumber(Integer orderNumber) throws SQLException {
        Statement statement = con.createStatement();
        String query = "SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.UserName FROM Orders \n" +
                " JOIN Staff ON Orders.IdManager = Staff.IdStaff " +
                " WHERE OrderNumber =" + orderNumber;

        ResultSet res = statement.executeQuery(query);
        if (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(6));
            String nameCustomer = res.getString(5);
            String phoneCustomer = res.getString(4);
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            return new Order(orderNumber, user, nameCustomer, phoneCustomer, localDate, totalSum);
        } else return null;
    }

    // DONE
    public List<Goods> getInfoByOrderNumber(Integer orderNumber) throws SQLException {
        List<Goods> goods = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT OrderContent.VendorCode, Quantity, OrderContent.Price, StatusOrder.Title, OrderContentNumber, GoodsTitle FROM OrderContent " +
                " JOIN StatusOrder ON OrderContent.Status = StatusOrder.IdStatus " +
                " JOIN Goods G on OrderContent.VendorCode = G.VendorCode" +
                " WHERE OrderNumber = " + orderNumber;

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {

            goods.add(new Goods(res.getInt(1), res.getInt(2), res.getDouble(3), res.getString(4), res.getInt(5), orderNumber,  res.getString(6)));
        }
        return goods;
    }

    // DONE
    public List<Order> getOrdersByPhoneCustomer(String phone) throws SQLException {
        List<Order> orders = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.UserName FROM Orders \n" +
                " JOIN Staff ON Orders.IdManager = Staff.IdStaff " +
                " WHERE Orders.PnoneCustomer = '" + phone +"'"+
                " ORDER BY Orders.[Date] ASC";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(6));
            String nameCustomer = res.getString(5);
            String phoneCustomer = res.getString(4);
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            Integer orderNumber = res.getInt(1);
            orders.add(new Order(orderNumber, user, nameCustomer, phoneCustomer, localDate, totalSum));
        }
        return orders;
    }

    public void updateStatusOfOrderContent(Goods g, String newStatus) throws SQLException {
        Statement statement = con.createStatement();
        Map<String, Integer> statuses = getAllStatus();
        String query = "EXEC ChangeStatus " + g.getIdOfOrder() + ", " + g.getIdOfOrderContent() + ", " + statuses.get(newStatus);

        statement.execute(query);
    }

    public Map<String, Integer> getAllStatus() throws SQLException {
        Statement statement = con.createStatement();
        Map<String, Integer> statuses = new HashMap<>();
        String query = "SELECT * FROM StatusOrder";
        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            statuses.put(res.getString(2), res.getInt(1));
        }
        return statuses;
    }

    // DONE
    public List<Goods> searchWithFilter(String filterChoice, Integer orderNumber) throws SQLException {
        List<Goods> goods = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT OrderContent.VendorCode, Quantity, OrderContent.Price, StatusOrder.Title, OrderContentNumber, GoodsTitle FROM OrderContent " +
                " JOIN StatusOrder ON OrderContent.Status = StatusOrder.IdStatus " +
                " JOIN Goods G on OrderContent.VendorCode = G.VendorCode" +
                " WHERE OrderNumber = " + orderNumber + " AND " +
                " StatusOrder.Title = '" + filterChoice + "'";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            goods.add(new Goods(res.getInt(1), res.getInt(2), res.getDouble(3), res.getString(4), res.getInt(5), orderNumber, res.getString(6)));
        }
        return goods;
    }

    // DONE
    public List<Order> getAllOrdersByManager(Integer idManager) throws SQLException {
        List<Order> orders = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.UserName FROM Orders \n" +
                " JOIN Staff ON Orders.IdManager = Staff.IdStaff " +
                " WHERE Staff.IdStaff = " + idManager +
                " ORDER BY Orders.[Date] ASC";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(6));
            String nameCustomer = res.getString(5);
            String phoneCustomer = res.getString(4);
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            Integer orderNumber = res.getInt(1);
            orders.add(new Order(orderNumber, user, nameCustomer, phoneCustomer, localDate, totalSum));
        }
        return orders;
    }

    public List<String> getAllPhonesOfCustomers() throws SQLException {
        List<String> phones = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT PnoneCustomer FROM Orders ";

        ResultSet res = statement.executeQuery(query);
        while (res.next())
            phones.add(res.getString(1));
        return phones;
    }

    public Order getOrderByOrderNumberAndIdManager(Integer orderNumber, Integer id) throws SQLException {
        Statement statement = con.createStatement();
        String query = "SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.UserName FROM Orders \n" +
                " JOIN Staff ON Orders.IdManager = Staff.IdStaff " +
                " WHERE OrderNumber =" + orderNumber + " AND IdManager = " + id;

        ResultSet res = statement.executeQuery(query);
        if (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(6));
            String nameCustomer = res.getString(5);
            String phoneCustomer = res.getString(4);
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            return new Order(orderNumber, user, nameCustomer, phoneCustomer, localDate, totalSum);
        } else return null;
    }

    public List<Order> getOrdersByPhoneCustomerAndIdManager(String phone, Integer id) throws SQLException {
        List<Order> orders = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.UserName FROM Orders \n" +
                " JOIN Staff ON Orders.IdManager = Staff.IdStaff " +
                " WHERE Orders.PnoneCustomer ='" + phone + "' " +
                " AND Orders.IdManager = " + id +
                " ORDER BY Orders.[Date] ASC";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(6));
            String nameCustomer = res.getString(5);
            String phoneCustomer = res.getString(4);
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            Integer orderNumber = res.getInt(1);
            orders.add(new Order(orderNumber, user, nameCustomer, phoneCustomer, localDate, totalSum));
        }
        return orders;
    }

    // DONE
    public List<Order> getOrdersByPhoneOfManager(String phone) throws SQLException {
        List<Order> orders = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.UserName FROM Orders \n" +
                " JOIN Staff ON Orders.IdManager = Staff.IdStaff " +
                " WHERE Staff.Phone ='" + phone + "' " +
                " ORDER BY Orders.[Date] ASC";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            User user = userRepository.getUserByLogin(res.getString(6));
            String nameCustomer = res.getString(5);
            String phoneCustomer = res.getString(4);
            Double totalSum = res.getDouble(3);
            Date date = res.getDate(2);
            LocalDate localDate = date.toLocalDate();
            Integer orderNumber = res.getInt(1);
            orders.add(new Order(orderNumber, user, nameCustomer, phoneCustomer, localDate, totalSum));
        }
        return orders;
    }
}