package seller.group.azon.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import seller.group.azon.backend.ControlStage;
import seller.group.azon.backend.DBconnection;
import seller.group.azon.backend.Password;
import seller.group.azon.entity.User;
import seller.group.azon.repos.UserRepository;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class RegistrationController implements Controller {

    @FXML
    BorderPane ap;

    @FXML
    Text request_message;

    @FXML
    Text on_success;

    @FXML
    TextField username_field;

    @FXML
    PasswordField password_field;

    @FXML
    TextField fio_field;

    @FXML
    TextField phone_field;

    @FXML
    DatePicker date_birth_field;

    @FXML
    ComboBox role_box;

    private UserRepository userRepository;
    private Map<String, Integer> roles;
    private Map<String, String> accounts;
    private List<String> phones;
    private User user;

    @FXML
    public void initialize() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        DBconnection dBconnection = DBconnection.getInstance();
        Connection con = dBconnection.getConnection();
        userRepository = new UserRepository(con);
        roles = userRepository.getAllRoles();
        accounts = userRepository.getAllAccounts();
        phones = userRepository.getAllPhones();

        ObservableList<String> nameRoles = FXCollections.observableArrayList(roles.keySet());
        role_box.setItems(nameRoles);

    }

    public void handleRegister(ActionEvent actionEvent) throws IOException, SQLException {
        accounts = userRepository.getAllAccounts();
        phones = userRepository.getAllPhones();

        String userName = username_field.getText();
        String password = password_field.getText();
        if (!verifyUsernameAndPassword(userName, password)) return;
        password = Password.doHash(password);

        if (password.equals("")) {
            request_message.setText("Unknown error.\nTry later or again now.");
            return;
        }

        String fio = fio_field.getText();
        if (!verifyFio(fio)) return;

        String phone = phone_field.getText();
        if (!verifyPhone(phone)) return;

        LocalDate dateOfBirth = date_birth_field.getValue();
        if (!verifyDate(dateOfBirth)) return;

        String roleName;
        if (role_box.getValue() == null) {
            request_message.setText("Недействительное название должности");
            return;
        } else {
            roleName = role_box.getValue().toString();
            if (!verifyRole(roleName)) return;
        }
        try {
            userRepository.registerUser(userName, password, roleName, fio, dateOfBirth, phone);
        } catch (SQLException e) {
            request_message.setText("Неизвестная ошибка.\nПовторите операцию.");
            return;
        }
        on_success.setFill(Color.GREEN);
        on_success.setText("Новый сотрудник добавлен");
        username_field.setText("");
        username_field.setText("");
        password_field.setText("");
        fio_field.setText("");
        phone_field.setText("");
        date_birth_field.setValue(null);
        role_box.setValue(null);
    }

    private boolean verifyDate(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            request_message.setText("Недействительная дата рождения");
            return false;
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            request_message.setText("Дата рождения не может быть в будущем");
            return false;
        }
        int dif = LocalDate.now().getYear() - dateOfBirth.getYear();
        if (dif < 18) {
            request_message.setText("Сотрудник должен быть старше 18 лет.");
            return false;
        }
        return true;
    }

    private boolean verifyPhone(String phone) throws SQLException {
        if (phone == null || phone.isBlank()) {
            request_message.setText("Недействительный номер телефона");
            return false;
        }
        for (int i = 0; i < phone.length(); i++) {
            if (!(phone.charAt(i) >= '0' && phone.charAt(i) <= '9')) {
                request_message.setText("Номер телефона должен содержать только цифры");
                return false;
            }
        }
        if (phone.length() < 5) {
            request_message.setText("Слишком короткий телефон: минимальная длина 5");
            return false;
        }
        if (phone.length() > 13) {
            request_message.setText("Слишком длинный телефон: максимальная длина 13");
            return false;
        }
        phones = userRepository.getAllPhones();
        if (phones.contains(phone)) {
            request_message.setText("Телефон уже используется");
            return false;
        }
        return true;
    }

    private boolean verifyUsernameAndPassword(String username, String password) throws SQLException {
        if (username == null || username.isBlank()) {
            request_message.setText("Недействительный логин");
            return false;
        }
        if (username.length() < 3) {
            request_message.setText("Слишком короткий логин: минимальная длина 3");
            return false;
        }
        if (username.length() > 20) {
            request_message.setText("Слишком длинный логин: максимальная длина 20");
            return false;
        }
        if (password == null || password.isBlank()) {
            request_message.setText("Недействительный пароль");
            return false;
        }
        if (password.length() < 3) {
            request_message.setText("Слишком короткий пароль: минимальная длина 3");
            return false;
        }
        accounts = userRepository.getAllAccounts();
        if (accounts.containsKey(username)) {
            request_message.setText("Логин уже использован");
            return false;
        }
        return true;
    }

    private boolean verifyFio(String fio) {
        if (fio == null || fio.isBlank()) {
            request_message.setText("Недействительные ФИО");
            return false;
        }
        if (fio.length() > 100) {
            request_message.setText("Слишком длинное имя: максимальная длина 100.");
            return false;
        }
        return true;
    }

    private boolean verifyRole(String role) {
        if (role.isBlank()) {
            request_message.setText("Недействительная роль");
            return false;
        }
        return true;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void handleClear() {
        request_message.setText("");
        on_success.setText("");
    }
}
