package seller.group.azon.repos;

import seller.group.azon.backend.Password;
import seller.group.azon.entity.User;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static seller.group.azon.entity.User.admin;

public class UserRepository {
    private Connection con;

    public UserRepository(Connection con) {
        this.con = con;
    }

    public Map<String, String> getAllAccounts() throws SQLException {
        HashMap<String, String> accounts = new HashMap<>();
        Statement statement = con.createStatement();
        String query = "SELECT * FROM LoginPassword";

        ResultSet res = statement.executeQuery(query);
        while (res.next())
            accounts.put(res.getString(1), res.getString(2));
        return accounts;
    }

    public Map<String, Integer> getAllRoles() throws SQLException {
        HashMap<String, Integer> roles = new HashMap<>();
        Statement statement = con.createStatement();
        String query = "SELECT * FROM Role";

        ResultSet res = statement.executeQuery(query);
        while (res.next())
            roles.put(res.getString(2), res.getInt(1));

        roles.remove(admin);
        return roles;
    }

    public List<String> getAllPhones() throws SQLException {
        List<String> phones = new ArrayList<>();
        Statement statement = con.createStatement();
        String query = "SELECT Phone FROM Staff";

        ResultSet res = statement.executeQuery(query);
        while (res.next())
            phones.add(res.getString(1));
        return phones;
    }

    public List<User> getAllManagers() throws SQLException {
        List<User> managers = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT IdStaff, NameRole, FIO, DateOfBirth, Phone, LoginPassword.UserName, LoginPassword.[Password], IsValidAccount FROM Manager_VIEW \n" +
                "    JOIN LoginPassword on Manager_VIEW.UserName = LoginPassword.UserName";

        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            String roleName = res.getString(2);
            if (Objects.equals(roleName, admin)) continue;
            Integer idStaff = res.getInt(1);
            String fullName = res.getString(3);
            Date date = res.getDate(4);
            LocalDate dateOfBirth = date.toLocalDate();
            String phone = res.getString(5);
            String username = res.getString(6);
            String password = res.getString(7);
            Boolean isValidAccount = res.getBoolean(8);
            managers.add(new User(idStaff, isValidAccount, username, password, fullName, roleName, dateOfBirth, phone));
        }
        return managers;
    }

    public List<User> getAllManagers(Integer isValid) throws SQLException {
        List<User> managers = new LinkedList<>();
        Statement statement = con.createStatement();
        String query = "SELECT IdStaff, NameRole, FIO, DateOfBirth, Phone, LoginPassword.UserName, LoginPassword.[Password], IsValidAccount FROM Manager_VIEW \n" +
                "    JOIN LoginPassword on Manager_VIEW.UserName = LoginPassword.UserName" +
                " WHERE IsValidAccount=" + isValid;

        ResultSet res = statement.executeQuery(query);
        while (res.next()) while (res.next()) {
            String roleName = res.getString(2);
            if (Objects.equals(roleName, admin)) continue;
            Integer idStaff = res.getInt(1);
            String fullName = res.getString(3);
            Date date = res.getDate(4);
            LocalDate dateOfBirth = date.toLocalDate();
            String phone = res.getString(5);
            String username = res.getString(6);
            String password = res.getString(7);
            Boolean isValidAccount = res.getBoolean(8);
            managers.add(new User(idStaff, isValidAccount, username, password, fullName, roleName, dateOfBirth, phone));
        }
        return managers;
    }

    public List<User> getAllManagersWithFilter(String filterChoice, String value) throws SQLException {
        String query = "SELECT * FROM Staff";
        switch (filterChoice) {
            case "по ФИО" -> query = "SELECT IdStaff, NameRole, FIO, DateOfBirth, Phone, LoginPassword.UserName, LoginPassword.[Password], IsValidAccount FROM Manager_VIEW \n" +
                    "    JOIN LoginPassword on Manager_VIEW.UserName = LoginPassword.UserName " +
                    " WHERE FIO = '" + value + "'";
            case "по телефону" -> query = "SELECT IdStaff, NameRole, FIO, DateOfBirth, Phone, LoginPassword.UserName, LoginPassword.[Password], IsValidAccount FROM Manager_VIEW \n" +
                    "    JOIN LoginPassword on Manager_VIEW.UserName = LoginPassword.UserName " +
                    "WHERE Phone = '" + value + "'";
            case "по роли" -> query = "SELECT IdStaff, NameRole, FIO, DateOfBirth, Phone, LoginPassword.UserName, LoginPassword.[Password], IsValidAccount FROM Manager_VIEW \n" +
                    "    JOIN LoginPassword on Manager_VIEW.UserName = LoginPassword.UserName " +
                    "WHERE NameRole = '" + value + "'";
            case "заблокированные" -> query = "SELECT IdStaff, NameRole, FIO, DateOfBirth, Phone, LoginPassword.UserName, LoginPassword.[Password], IsValidAccount FROM Manager_VIEW \n" +
                    "    JOIN LoginPassword on Manager_VIEW.UserName = LoginPassword.UserName " +
                    "WHERE IsValidAccount = 0";
            case "активированные" -> query = "SELECT IdStaff, NameRole, FIO, DateOfBirth, Phone, LoginPassword.UserName, LoginPassword.[Password], IsValidAccount FROM Manager_VIEW \n" +
                    "    JOIN LoginPassword on Manager_VIEW.UserName = LoginPassword.UserName " +
                    "WHERE IsValidAccount = 1";
        }

        List<User> managers = new LinkedList<>();
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            String roleName = res.getString(2);
            if (Objects.equals(roleName, admin)) continue;
            Integer idStaff = res.getInt(1);
            String fullName = res.getString(3);
            Date date = res.getDate(4);
            LocalDate dateOfBirth = date.toLocalDate();
            String phone = res.getString(5);
            String username = res.getString(6);
            String password = res.getString(7);
            Boolean isValidAccount = res.getBoolean(8);

            managers.add(new User(idStaff, isValidAccount, username, password, fullName, roleName, dateOfBirth, phone));
        }

        return managers;
    }

    public void blockAccountByUserName(String username) throws SQLException {
        Statement statement = con.createStatement();
        String query = "UPDATE LoginPassword SET IsValidAccount=0 WHERE (LoginPassword.UserName= '" + username + "')";
        statement.execute(query);
    }

    public void activateAccountByUserName(String username) throws SQLException {
        Statement statement = con.createStatement();
        String query = "UPDATE LoginPassword SET IsValidAccount=1 WHERE (LoginPassword.UserName= '" + username + "')";
        statement.execute(query);
    }

    public void updateAccountByUserName(String oldUserName, String newUserName, String newPassword, Integer idManager) throws SQLException {
        Statement statement = con.createStatement();

        String query = "UPDATE LoginPassword SET UserName='" + newUserName + "', Password='" + newPassword + "' WHERE (UserName = '" + oldUserName + "' " +
                "AND " + idManager + " = (SELECT Staff.IdStaff FROM Staff WHERE Staff.UserName = '" + oldUserName + "'))";

        statement.execute(query);
    }

    public User getUserByLogin(String login) throws SQLException {
        Statement statement = con.createStatement();
        String query = "SELECT IdStaff, NameRole, FIO, DateOfBirth, Phone, LoginPassword.UserName, LoginPassword.[Password], IsValidAccount FROM Manager_VIEW \n" +
                "    JOIN LoginPassword on Manager_VIEW.UserName = LoginPassword.UserName" +
                " WHERE Manager_VIEW.UserName= '" + login + "'";

        ResultSet res = statement.executeQuery(query);

        if (res.next()) {
            Integer idStaff = res.getInt(1);
            String roleName = res.getString(2);
            String fullName = res.getString(3);
            Date date = res.getDate(4);
            LocalDate dateOfBirth = date.toLocalDate();
            String phone = res.getString(5);
            String username = res.getString(6);
            String password = res.getString(7);
            Boolean isValidAccount = res.getBoolean(8);
            return new User(idStaff, isValidAccount, username, password, fullName, roleName, dateOfBirth, phone);
        } else
            return null;
    }

    // DONE
    public User getUserById(Integer idStaff) throws SQLException {
        Statement statement = con.createStatement();
        String query = "SELECT IdStaff, Role.Name as RoleName, FIO, DateOfBirth, Phone, LoginPassword.UserName, LoginPassword.[Password], IsValidAccount FROM Staff \n" +
                "    JOIN Role ON Role.IdRole = Staff.IdRole" +
                "    JOIN LoginPassword on Staff.UserName = LoginPassword.UserName" +
                "    WHERE IdStaff = " + idStaff;

        ResultSet res = statement.executeQuery(query);

        if (res.next()) {
            String roleName = res.getString(2);
            String fullName = res.getString(3);
            Date date = res.getDate(4);
            LocalDate dateOfBirth = date.toLocalDate();
            String phone = res.getString(5);
            String username = res.getString(6);
            String password = res.getString(7);
            Boolean isValidAccount = res.getBoolean(8);
            return new User(idStaff, isValidAccount, username, password, fullName, roleName, dateOfBirth, phone);
        } else
            return null;
    }

    // DONE
    public void updateUser(User newUser) throws SQLException {
        Statement statement = con.createStatement();
        String query = "UPDATE Staff SET FIO='" + newUser.getFullName() + "', IdRole= (SELECT IdRole FROM Role WHERE Role.Name = '" + newUser.getRole() + "'), DateOfBirth='" + newUser.getDateOfBirth() + "', Phone='" + newUser.getPhone() + "' WHERE UserName = '" + newUser.getUserName() + "'";
        statement.execute(query);
    }

    public Boolean verifyUser(String userName, String password) throws SQLException {
        Statement statement = con.createStatement();
        password = Password.doHash(password);
        String query = "SELECT count(1) FROM LoginPassword " +
                "WHERE username = '" + userName + "' AND password = '" + password + "'" +
                " AND IsValidAccount = 1";
        ResultSet res = statement.executeQuery(query);
        if (res.next())
            return res.getInt(1) == 1;
        return false;
    }

    // DONE
    public Boolean registerUser(String userName, String password, String roleName, String fio, LocalDate dateOfBirth, String phone) throws SQLException {
        Map<String, Integer> roles = getAllRoles();

        Statement statement = con.createStatement();
        String query = "EXEC registerManager '" + userName + "', '" + password + "', " + roles.get(roleName) + ", '" + fio + "', '" + dateOfBirth + "', '" + phone + "'";
        statement.execute(query);
        return true;
    }

    // DONE
    public List<User> getAnalyzeOfSalesManager(LocalDate dateBeg, LocalDate dateEnd) throws SQLException {
        List<User> managersAnalyze = new LinkedList<>();
        String query = "SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPrice, SUM(Quantity) as SumQuantity FROM Orders " +
                " JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber" +
                " JOIN Staff ON Orders.IdManager = Staff.IdStaff" +
                " WHERE Orders.[Date] >= ' " + dateBeg + "' AND" +
                " Orders.[Date] <= ' " + dateEnd + "'" +
                " GROUP BY Staff.Phone, Staff.FIO";
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            managersAnalyze.add(new User(res.getString(2), res.getString(1), res.getInt(4), res.getDouble(3)));
        }
        return managersAnalyze;
    }

    // DONE
    public User getAnalyzeOfSalesManager(LocalDate dateBeg, LocalDate dateEnd, String filterValue) throws SQLException {
        String query = "SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPrice, SUM(Quantity) as SumQuantity FROM Orders " +
                " JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber" +
                " JOIN Staff ON Orders.IdManager = Staff.IdStaff" +
                " WHERE Orders.[Date] >= ' " + dateBeg + "' AND" +
                " Orders.[Date] <= ' " + dateEnd + "' AND " +
                " Staff.UserName = '" + filterValue + "' " +
                " GROUP BY Staff.Phone, Staff.FIO";
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(query);
        if (res.next()) {
            return new User(res.getString(2), res.getString(1), res.getInt(4), res.getDouble(3));
        }
        return null;
    }

    public List<String> getFIOofSalesManagers() throws SQLException {
        List<String> managersFio = new LinkedList<>();
        String query = "SELECT Staff.FIO FROM Staff " +
                "JOIN Role on Staff.IdRole = Role.IdRole " +
                "WHERE Role.Name = " + "'Sales Manager' OR Role.Name =" + " 'Admin'";

        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            managersFio.add(res.getString(1));
        }
        return managersFio;
    }

    public List<String> getFIOofPurchaseManagers() throws SQLException {
        List<String> managersFio = new LinkedList<>();
        String query = "SELECT Staff.FIO FROM Staff " +
                "JOIN Role on Staff.IdRole = Role.IdRole " +
                "WHERE Role.Name = " + "'Purchasing Manager' OR Role.Name =" + " 'Admin'";

        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            managersFio.add(res.getString(1));
        }
        return managersFio;
    }

    // DONE
    public List<User> getAnalyzeOfPurchaseManager(LocalDate dateBeg, LocalDate dateEnd) throws SQLException {
        List<User> managersAnalyze = new LinkedList<>();
        String query = "SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPrice, SUM(Quantity) as SumQuantity FROM Invoice " +
                " JOIN InvoiceContent ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber" +
                " JOIN Staff ON Invoice.IdManger = Staff.IdStaff" +
                " WHERE Invoice.[Date] >= ' " + dateBeg + "' AND" +
                " Invoice.[Date] <= ' " + dateEnd + "'" +
                " GROUP BY Staff.Phone, Staff.FIO";

        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(query);
        while (res.next()) {
            managersAnalyze.add(new User(res.getString(2), res.getString(1), res.getInt(4), res.getDouble(3)));
        }
        return managersAnalyze;
    }

    // DONE
    public User getAnalyzeOfPurchaseManager(LocalDate dateBeg, LocalDate dateEnd, String filterValue) throws SQLException {
        String query = "SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPrice, SUM(Quantity) as SumQuantity FROM Invoice " +
                " JOIN InvoiceContent ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber" +
                " JOIN Staff ON Invoice.IdManger = Staff.IdStaff" +
                " WHERE Invoice.[Date] >= ' " + dateBeg + "' AND" +
                " Invoice.[Date] <= ' " + dateEnd + "' AND " +
                " Staff.UserName = '" + filterValue + "' " +
                " GROUP BY Staff.Phone, Staff.FIO";
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(query);
        if (res.next()) {
            return new User(res.getString(2), res.getString(1), res.getInt(4), res.getDouble(3));
        }
        return null;
    }
}