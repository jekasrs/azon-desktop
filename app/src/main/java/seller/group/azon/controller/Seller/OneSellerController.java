package seller.group.azon.controller.Seller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import seller.group.azon.backend.ControlStage;
import seller.group.azon.backend.DBconnection;
import seller.group.azon.controller.Controller;
import seller.group.azon.controller.ErrorController;
import seller.group.azon.entity.Seller;
import seller.group.azon.entity.User;
import seller.group.azon.repos.SellerRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OneSellerController implements Controller {
    @FXML
    BorderPane ap;

    @FXML
    TextField tax_field;
    @FXML
    TextField phone_field;
    @FXML
    TextField name_field;
    @FXML
    Button button;

    @FXML
    Text request_message;

    private Boolean isUpdate;
    private Seller seller;
    private SellerRepository sellerRepository;
    private User user;

    // false if add    поставщика
    // true  if update поставщика
    public void setTypeOfProcedure(Integer idSeller, Boolean isUpdate) throws SQLException {
        this.isUpdate = isUpdate;
        if (!isUpdate) {
            button.setText("Добавить");
            tax_field.setText("");
            phone_field.setText("");
            name_field.setText("");
            this.seller = null;
        } else {
            this.seller = sellerRepository.getSellerById(idSeller);
            button.setText("Изменить");
            tax_field.setText(seller.getTaxPayerId().toString());
            phone_field.setText(seller.getPhone());
            name_field.setText(seller.getName());
        }
    }

    @FXML
    public void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
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
            sellerRepository = new SellerRepository(con);
        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }
    }

    public void clearHandler() {
        request_message.setFill(Color.RED);
        request_message.setText("");
    }

    @FXML
    public void buttonAction(ActionEvent actionEvent) throws IOException {
        try {
            request_message.setFill(Color.RED);
            String name = name_field.getText();
            Long taxPayer = Long.parseLong(tax_field.getText());
            String phone = phone_field.getText();

            if (!verifyName(name)) return;
            if (!verifyTaxPayerId(taxPayer)) return;
            if (!verifyPhone(phone)) return;

            if (isUpdate) {
                sellerRepository.updateSeller(this.seller.getIdSeller(), name, taxPayer, phone);
                request_message.setFill(Color.GREEN);
                request_message.setText("Поставщик успешно изменен");
            } else {
                sellerRepository.addSeller(name, taxPayer, phone);
                request_message.setFill(Color.GREEN);
                request_message.setText("Поставщик успешно добавлен");
            }

            this.seller = sellerRepository.getSellerByTaxPayer(taxPayer);
            name_field.setText("");
            tax_field.setText("");
            phone_field.setText("");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.err.println("Bad happened with connection. It is closed");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }
        catch (NumberFormatException e){
            request_message.setFill(Color.RED);
            request_message.setText("Неправильный формат данных");
            return;
        }

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
        List<String> phones = sellerRepository.getAllPhones();
        if (isUpdate) {
            phones.remove(phone);
        }
        if (phones.contains(phone)) {
            request_message.setText("Телефон уже используется");
            return false;
        }
        return true;
    }

    private boolean verifyName(String fio) {
        if (fio == null || fio.isBlank()) {
            request_message.setText("Недействительное Название");
            return false;
        }
        if (fio.length() > 100) {
            request_message.setText("Слишком длинное имя: максимальная длина 100.");
            return false;
        }
        return true;
    }

    private boolean verifyTaxPayerId(Long taxPayerId) throws SQLException {
        if (taxPayerId <= 0) {
            request_message.setText("Недействительный ИНН");
            return false;
        }
        List<Long> taxPayers = sellerRepository.getAllTaxPayerId();
        if (isUpdate) {
            taxPayers.remove(taxPayerId);
        }
        if (taxPayers.contains(taxPayerId)) {
            request_message.setText("ИНН уже используется");
            return false;
        }

        return true;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}