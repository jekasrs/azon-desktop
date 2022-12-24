package seller.group.azon.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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

public class MyselfController implements Controller {

    @FXML
    BorderPane ap;

    @FXML
    Text request_message;

    @FXML
    Text on_success;

    @FXML
    TextField username_field;

    @FXML
    TextField password_field;

    @FXML
    TextField fio_field;

    @FXML
    TextField phone_field;

    @FXML
    DatePicker date_birth_field;

    @FXML
    ComboBox role_box;

    @FXML
    Label role_text;

    User editorUser;

    private UserRepository userRepository;
    private Map<String, String> accounts;
    private Map<String, Integer> roles;
    private List<String> phones;

    @FXML
    public void initialize() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        DBconnection dBconnection = null;
        try {
            dBconnection = DBconnection.getInstance();
        } catch (IOException e) {
            System.err.println("No JDBC connector was found");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
            return;
        }
        try {
            Connection con = dBconnection.getConnection();
            userRepository = new UserRepository(con);
            roles = userRepository.getAllRoles();
            accounts = userRepository.getAllAccounts();
            phones = userRepository.getAllPhones();

            ObservableList<String> nameRoles = FXCollections.observableArrayList(roles.keySet());
            role_box.setItems(nameRoles);

        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }
    }

    public void handleUpdate() throws IOException {
        try {
            accounts = userRepository.getAllAccounts();
            phones = userRepository.getAllPhones();

            String newUserName = username_field.getText();
            String newPassword = password_field.getText();
            if (!verifyUsernameAndPassword(newUserName, newPassword)) return;
            newPassword = Password.doHash(newPassword);
            if (newPassword.equals("")) {
                request_message.setText("Unknown error.\nTry later or again now.");
                return;
            }

            String newFio = fio_field.getText();
            if (!verifyFio(newFio)) return;

            String newPhone = phone_field.getText();
            if (!verifyPhone(newPhone)) return;

            LocalDate newDateOfBirth = date_birth_field.getValue();
            if (!verifyDate(newDateOfBirth)) return;

            String roleName;
            if (role_box.getValue() == null) roleName = editorUser.getRole();
            else roleName = role_box.getValue().toString();

            if (!verifyRole(roleName)) return;

            try {
                userRepository.updateAccountByUserName(editorUser.getUserName(), newUserName, newPassword, editorUser.getId());

                editorUser.setUserName(newUserName);
                editorUser.setPassword(newPassword);
                editorUser.setFullName(newFio);
                editorUser.setRole(roleName);
                editorUser.setPhone(newPhone);
                editorUser.setDateOfBirth(newDateOfBirth);

                userRepository.updateUser(this.editorUser);
            } catch (SQLException e) {
                request_message.setText("Неизвестная ошибка.\nПовторите операцию.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }

        on_success.setFill(Color.GREEN);
        on_success.setText("Данные успешно обновлены");
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
        phones.remove(this.editorUser.getPhone());
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
        accounts.remove(this.editorUser.getUserName());
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

    public void updateManager(Integer idManager) throws SQLException {
        setUser(userRepository.getUserById(idManager));
        role_box.setVisible(true);
        role_text.setVisible(true);
        role_box.setValue(editorUser.getRole());
    }

    public void setUser(User user) {
        this.editorUser = user;
        username_field.setText(user.getUserName());
        password_field.setText(("new password"));
        fio_field.setText(user.getFullName());
        phone_field.setText(user.getPhone());
        date_birth_field.setValue(user.getDateOfBirth());
        role_box.setVisible(false);
        role_text.setVisible(true);
        role_text.setText("Роль: " + editorUser.getRole());
    }

    public void handleClear() {
        request_message.setText("");
        on_success.setText("");
    }
}
