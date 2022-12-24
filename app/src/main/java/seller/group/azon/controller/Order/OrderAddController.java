package seller.group.azon.controller.Order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import seller.group.azon.backend.DBconnection;
import seller.group.azon.controller.Controller;
import seller.group.azon.entity.Goods;
import seller.group.azon.entity.User;
import seller.group.azon.repos.GoodsRepository;
import seller.group.azon.repos.SalesRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderAddController implements Controller {
    @FXML
    private BorderPane ap;

    @FXML
    TextField order_number_field;

    @FXML
    TextField fio_field;

    @FXML
    TextField phone_field;

    SalesRepository salesRepository;
    GoodsRepository goodsRepository;

    @FXML
    VBox all_sales_box;

    @FXML
    Text request_message;

    Integer currentIdOfInvoiceContent = 0;
    User user;

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SQLException {
        DBconnection dBconnection = DBconnection.getInstance();
        Connection con = dBconnection.getConnection();
        salesRepository = new SalesRepository(con);
        goodsRepository = new GoodsRepository(con);

        all_sales_box.setAlignment(Pos.CENTER);
        currentIdOfInvoiceContent = 0;
    }

    // оформить заказ
    public void makeOrder(ActionEvent actionEvent) throws SQLException {
        Map<Integer, Integer> orderAmounts = new HashMap<>();

        request_message.setFill(Color.RED);
        List<Goods> orderContent = new LinkedList<>();
        ObservableList<Node> hboxes = all_sales_box.getChildren();
        for (int i = 0; i<hboxes.size(); i++){
            HBox box = (HBox) hboxes.get(i);
            ObservableList<Node> values = box.getChildren();

            String v = (String) (((ComboBox) values.get(1)).getValue());
            if (v == null|| v.equals("")){
                ((Text) values.get(0)).setFill(Color.RED);
                return;
            }
            Integer vendor = Integer.parseInt(v);
            if (!verifyVendorCode(vendor)) {
                ((Text) values.get(0)).setFill(Color.RED);
                return;
            }
            Goods g = goodsRepository.getGoodsByVendorCode(vendor);
            if (!orderAmounts.containsKey(g.getVendorCode()))
                orderAmounts.put(g.getVendorCode(), g.getAmount());

            String a = ((TextField)values.get(3)).getText();
            if (a == null || a.equals("")){
                ((Text) values.get(0)).setFill(Color.RED);
                return;
            }
            Integer amount = Integer.parseInt(a);
            if (!verifyAmount(amount)) {
                ((Text) values.get(0)).setFill(Color.RED);
                return;
            }

            if (orderAmounts.get(vendor) < amount){
                ((Text) values.get(0)).setFill(Color.RED);
                request_message.setText("Нет доступного числа товаров");
                return;
            }
            else{
                orderAmounts.put(vendor, orderAmounts.get(vendor) - amount);
            }


            Double price = g.getPrice();
            if (!verifyPrice(price)) {
                ((Text) values.get(0)).setFill(Color.RED);
                return;
            }
            orderContent.add(new Goods(vendor, amount, price));

        }

        String buyerName = fio_field.getText();
        if (buyerName == null || buyerName.equals("")) {
            request_message.setText("Недействительное имя покупателя");
            return;
        }

        String phoneNumber = phone_field.getText();
        if (phoneNumber == null || phoneNumber.equals("")) {
            request_message.setText("Недействительный номер покупателя");
            return;
        }
        if (!verifyPhone(phoneNumber)) return;

        String in = order_number_field.getText();
        if (in == null || in.equals("")) {
            request_message.setText("Недействительный номер заказа");
            return;
        }

        Integer orderNumber = Integer.parseInt(in);
        if (!verifyOrderNumber(orderNumber))
            return;

        if (orderContent.size() < 1)
            request_message.setText("Нужно добавить хотя бы один под-заказ");
        else {
            salesRepository.addOrder(orderContent, orderNumber, buyerName, phoneNumber, user.getId());
            request_message.setFill(Color.GREEN);
            request_message.setText("Заказ оформлена");
            int size = hboxes.size();
            for (int i = 0; i < size; i++) {
                HBox box = (HBox) hboxes.get(0);
                hboxes.remove(box);
            }
        }
    }

    // добавить под-закупку
    public void addOrderContent(ActionEvent actionEvent) throws SQLException {
        ScrollPane sp = new ScrollPane();
        VBox.setVgrow(sp, Priority.ALWAYS);

        Font font = new Font(14);

        HBox hbox = new HBox();
        hbox.setId("hbox-" + currentIdOfInvoiceContent.toString());
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.setMinWidth(600);

        Text vendor = new Text();
        vendor.setText("Артикул:");
        vendor.setFont(font);
        vendor.setTextAlignment(TextAlignment.JUSTIFY);
        vendor.setLineSpacing(1.0);

        ComboBox goods = new ComboBox();
        goods.setId("vendor");
        goods.setMinWidth(100.0);
        goods.setPrefWidth(100.0);

        Text price = new Text();
        price.setText("Цена:");
        price.setFont(font);
        price.setTextAlignment(TextAlignment.JUSTIFY);
        price.setLineSpacing(1.0);

        List<Goods> allGoods = goodsRepository.getAllAvailableGoods();
        List<String> vendorCodes = allGoods.stream()
                .map(Goods::getVendorCode)
                .map(Object::toString)
                .collect(Collectors.toList());
        ObservableList<String> allValuesGoods = FXCollections.observableArrayList(vendorCodes);
        goods.setItems(allValuesGoods);
        goods.setOnMouseClicked(event -> {
            vendor.setFill(Color.BLACK);
        });
        goods.setOnAction(event -> {
            try {
                Goods g = goodsRepository.getGoodsByVendorCode(Integer.parseInt((String) goods.getValue()));
                price.setText("Цена: " + g.getPrice());
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        Text amount = new Text();
        amount.setText("Кол-во:");
        amount.setFont(font);
        amount.setTextAlignment(TextAlignment.JUSTIFY);
        amount.setLineSpacing(1.0);

        TextField amountValue = new TextField();
        amountValue.setId("amount");

        hbox.getChildren().add(vendor);
        hbox.getChildren().add(goods);
        hbox.getChildren().add(amount);
        hbox.getChildren().add(amountValue);
        hbox.getChildren().add(price);
        hbox.setMargin(vendor, new Insets(0, 0,0,10));
        hbox.setMargin(goods, new Insets(0, 0,0,5));
        hbox.setMargin(amount, new Insets(0, 0,0,10));
        hbox.setMargin(amountValue, new Insets(0, 0,0,5));
        hbox.setMargin(price, new Insets(0, 0,0,5));

        all_sales_box.getChildren().add(hbox);
        all_sales_box.setMargin(hbox, new Insets(10, 0, 0, 10));

        sp.setContent(all_sales_box);
        sp.setStyle("-fx-background: #78a4c2; -fx-border-color: #78a4c2;");

        ap.setMargin(sp, new Insets(50, 0, 10, 0));
        ap.setCenter(sp);

        currentIdOfInvoiceContent++;
    }

    public boolean verifyVendorCode(Integer vendorCode) throws SQLException {
        if (vendorCode == null)
            return false;

        List<Integer> goods = goodsRepository.getAllGoods().stream()
                .map(Goods::getVendorCode)
                .collect(Collectors.toList());

        return goods.contains(vendorCode);
    }
    public boolean verifyPrice(Double price){
        request_message.setFill(Color.RED);
        if (price == null || price <= 0.00) {
            request_message.setText("Недействительная цена заказа");
            return false;
        }
        return true;
    }
    public boolean verifyAmount(Integer amount) {
        request_message.setFill(Color.RED);
        if (amount == null || amount <= 0) {
            request_message.setText("Недействительное кол-во товаров");
            return false;
        }
        return true;
    }
    private boolean verifyPhone(String phone) {
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
        return true;
    }

    public boolean verifyOrderNumber(Integer orderNumber) throws SQLException {
        request_message.setFill(Color.RED);
        if (orderNumber == null || orderNumber <= 0) {
            request_message.setText("Недействительный номер заказа");
            return false;
        }
        List<Integer> allOrderNumbers = salesRepository.getAllOrderNumbers();
        if (allOrderNumbers.contains(orderNumber)) {
            request_message.setText("Номер накладной уже занят");
            return false;
        }
        return true;
    }
}
