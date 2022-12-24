package seller.group.azon.controller.Goods;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import seller.group.azon.backend.ControlStage;
import seller.group.azon.backend.DBconnection;
import seller.group.azon.controller.Controller;
import seller.group.azon.controller.ErrorController;
import seller.group.azon.entity.Goods;
import seller.group.azon.entity.User;
import seller.group.azon.repos.GoodsRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class OneGoodsController implements Controller {
    @FXML
    BorderPane ap;

    @FXML
    TextField vendorcode_field;
    @FXML
    TextField title_field;
    @FXML
    TextField price_field;
    @FXML
    TextArea description_field;

    @FXML
    Button button;

    @FXML
    Text request_message;

    private Boolean isUpdate;
    private Goods good;
    private GoodsRepository goodsRepository;

    // false if add    товар
    // true  if update товар
    public void setTypeOfProcedure(Integer vendorCode, Boolean isUpdate) throws SQLException {
        this.isUpdate = isUpdate;
        if (!isUpdate){
            button.setText("Добавить");
            vendorcode_field.setText("");
            title_field.setText("");
            price_field.setText("");
            description_field.setText("");
            this.good = null;
        }
        else {
            this.good = goodsRepository.getGoodsByVendorCode(vendorCode);
            button.setText("Изменить");
            vendorcode_field.setText(good.getVendorCode().toString());
            title_field.setText(good.getTitle());
            price_field.setText(good.getPrice().toString());
            description_field.setText(good.getDescription());
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
            goodsRepository = new GoodsRepository(con);
        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }
    }

    @FXML
    public void buttonAction(ActionEvent actionEvent) throws IOException {
        try {
            Integer vendorCode = Integer.parseInt(vendorcode_field.getText());
            String title = title_field.getText();
            String description = description_field.getText();
            Double price = Double.parseDouble(price_field.getText());
            if (!verifyVendorCode(vendorCode)) return;
            if (!verifyTitle(title)) return;
            if (!verifyPrice(price)) return;
            if (!verifyDescription(description)) return;

            price = Math.round(price * 100.00)/100.00;

            if (isUpdate)
                goodsRepository.updateGoods(this.good.getVendorCode(), new Goods(vendorCode, title, description, price));
            else
               this.good = goodsRepository.addGoods(vendorCode, title, description, price);
            request_message.setFill(Color.GREEN);
            request_message.setText("Товар успешно добавлен");
            vendorcode_field.setText("");
            title_field.setText("");
            description_field.setText("");
            price_field.setText("");

        }
        catch (NumberFormatException e){
            verifyPrice(null);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            System.err.println("Bad happened with connection. It is closed");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }

    }
    public void clearHandler(){
        request_message.setFill(Color.RED);
        request_message.setText("");
    }
    public boolean verifyVendorCode(Integer vendorCode) throws SQLException {
        if (vendorCode == null || vendorCode <= 0) {
            request_message.setText("Недействительные артикул");
            return false;
        }
        List<Goods> goods = goodsRepository.getAllGoods();
        if (isUpdate) {
            goods.remove(this.good);
        }

        long num = goods.stream()
                .map(Goods::getVendorCode)
                .filter((g) -> g.equals(vendorCode))
                .count();
            if (num != 0){
                request_message.setText("Артикул уже занят");
                return false;
            }
        return true;
    }
    public boolean verifyTitle(String title){
        if (title == null || title.isBlank()) {
            request_message.setText("Недействительное название товара");
            return false;
        }
        if (title.length() > 100) {
            request_message.setText("Слишком длинное название: максимальная длина 100.");
            return false;
        }
        return true;
    }
    public boolean verifyDescription(String Description){
        if (Description == null || Description.isBlank()) {
            request_message.setText("Недействительное описание товара");
            return false;
        }
        return true;
    }
    public boolean verifyPrice(Double price){
        if (price == null || price <= 0.00) {
            request_message.setText("Недействительная цена товара");
            return false;
        }
        return true;
    }

    @Override
    public void setUser(User user) {}
}
