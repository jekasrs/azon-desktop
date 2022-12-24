package seller.group.azon.controller.Invoice;

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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import seller.group.azon.backend.DBconnection;
import seller.group.azon.controller.Controller;
import seller.group.azon.entity.Goods;
import seller.group.azon.entity.User;
import seller.group.azon.repos.GoodsRepository;
import seller.group.azon.repos.PurchaseRepository;
import seller.group.azon.repos.SellerRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceAddController implements Controller {
    @FXML
    private BorderPane ap;

    @FXML
    TextField invoice_number_field;

    @FXML
    ComboBox seller_combobox;

    @FXML
    Text request_message;

    ComboBox goods;
    PurchaseRepository purchaseRepository;
    GoodsRepository goodsRepository;
    SellerRepository sellerRepository;

    @FXML
    VBox all_purchases_box;

    Integer currentIdOfInvoiceContent;

    User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SQLException {
        DBconnection dBconnection = DBconnection.getInstance();
        Connection con = dBconnection.getConnection();
        purchaseRepository = new PurchaseRepository(con);
        goodsRepository = new GoodsRepository(con);
        sellerRepository = new SellerRepository(con);

        seller_combobox.setMinWidth(100.0);
        seller_combobox.setPrefWidth(100.0);
        List<String> allSellers = sellerRepository.getAllSellers()
                .stream()
                .map(o -> o.getName() + " " + o.getTaxPayerId() )
                .collect(Collectors.toList());
        ObservableList<String> nameOfSellers = FXCollections.observableArrayList(allSellers);
        seller_combobox.setItems(nameOfSellers);

        all_purchases_box.setAlignment(Pos.CENTER);
        currentIdOfInvoiceContent = 0;
    }

    // оформить накладную
    public void makeOffer(ActionEvent actionEvent) throws SQLException {
        request_message.setFill(Color.RED);
        List<Goods> invoiceContent = new LinkedList<>();
        ObservableList<Node> hboxes = all_purchases_box.getChildren();
        for (int i = 0; i < hboxes.size(); i++) {
            HBox box = (HBox) hboxes.get(i);
            ObservableList<Node> values = box.getChildren();

            String v = (String) (((ComboBox) values.get(1)).getValue());
            if (v == null || v.equals("")) {
                ((Text) values.get(0)).setFill(Color.RED);
                return;
            }
            Integer vendor = Integer.parseInt(v);
            if (!verifyVendorCode(vendor)) {
                ((Text) values.get(0)).setFill(Color.RED);
                return;
            }
            String a = ((TextField) values.get(3)).getText();
            if (a == null || a.equals("")) {
                ((Text) values.get(0)).setFill(Color.RED);
                return;
            }
            Integer amount = Integer.parseInt(a);
            if (!verifyAmount(amount)) {
                ((Text) values.get(0)).setFill(Color.RED);
                return;
            }
            String p = ((TextField) values.get(5)).getText();
            if (p == null || p.equals("")) {
                ((Text) values.get(0)).setFill(Color.RED);
                return;
            }
            Double purchase = Double.parseDouble(p);
            if (!verifyPurchase(purchase)) {
                ((Text) values.get(0)).setFill(Color.RED);
                return;
            }
            invoiceContent.add(new Goods(vendor, amount, purchase));

        }

        StringBuilder sellerName = new StringBuilder((String) seller_combobox.getValue());
        if (sellerName == null || sellerName.toString().equals("")) {
            request_message.setText("Недействительное имя поставщика товаров");
            return;
        }
        String[] arr = sellerName.toString().split("\\s");
        sellerName = new StringBuilder();
        for (int i = 0; i<arr.length-1; i++){
            sellerName.append(arr[i]).append(" ");
        }

        String in = invoice_number_field.getText();
        if (in == null || in.equals("")){
            request_message.setText("Недействительный номер накладной");
            return;
        }

        Integer invoiceNumber = Integer.parseInt(in);
        if (!verifyInvoiceNumber(invoiceNumber)) return;

        if (invoiceContent.size() < 1)
            request_message.setText("Нужно добавить хотя бы одну под-закупку");
        else {
            purchaseRepository.addInvoice(invoiceContent, invoiceNumber, sellerName.toString(), user.getId());
            request_message.setFill(Color.GREEN);
            request_message.setText("Закупка оформлена");
            int size = hboxes.size();
            for (int i = 0; i < size; i++) {
                HBox box = (HBox) hboxes.get(0);
                hboxes.remove(box);
            }
        }
    }

    // добавить под-закупку
    public void addInvoiceContent(ActionEvent actionEvent) throws SQLException {
        request_message.setFill(Color.RED);
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

        goods = new ComboBox();
        goods.setId("vendor");
        goods.setMinWidth(100.0);
        goods.setPrefWidth(100.0);
        List<String> vendorCodes = goodsRepository.getAllGoods().stream()
                .map(Goods::getVendorCode)
                .map(Object::toString)
                .collect(Collectors.toList());
        ObservableList<String> allGoods = FXCollections.observableArrayList(vendorCodes);
        goods.setItems(allGoods);
        goods.setOnMouseClicked(event -> {
            vendor.setFill(Color.BLACK);
        });

        Text amount = new Text();
        amount.setText("Кол-во:");
        amount.setFont(font);
        amount.setTextAlignment(TextAlignment.JUSTIFY);
        amount.setLineSpacing(1.0);

        Text purchase = new Text();
        purchase.setText("Цена закупки:");
        purchase.setFont(font);
        purchase.setTextAlignment(TextAlignment.JUSTIFY);
        purchase.setLineSpacing(1.0);

        TextField amountValue = new TextField();
        amountValue.setId("amount");
        TextField purchaseValue = new TextField();
        purchaseValue.setId("purchase");

        hbox.getChildren().add(vendor);
        hbox.getChildren().add(goods);
        hbox.getChildren().add(amount);
        hbox.getChildren().add(amountValue);
        hbox.getChildren().add(purchase);
        hbox.getChildren().add(purchaseValue);
        hbox.setMargin(vendor, new Insets(0, 0, 0, 10));
        hbox.setMargin(goods, new Insets(0, 0, 0, 5));
        hbox.setMargin(amount, new Insets(0, 0, 0, 10));
        hbox.setMargin(amountValue, new Insets(0, 0, 0, 5));
        hbox.setMargin(purchase, new Insets(0, 0, 0, 10));
        hbox.setMargin(purchaseValue, new Insets(0, 0, 0, 5));
        all_purchases_box.getChildren().add(hbox);
        all_purchases_box.setMargin(hbox, new Insets(10, 0, 0, 10));

        sp.setContent(all_purchases_box);
        sp.setStyle("-fx-background: #78a4c2; -fx-border-color: #78a4c2;");

        ap.setMargin(sp, new Insets(50, 0, 10, 0));
        ap.setCenter(sp);

        currentIdOfInvoiceContent++;
    }

    public boolean verifyVendorCode(Integer vendorCode) throws SQLException {
        request_message.setFill(Color.RED);
        if (vendorCode == null) {
            request_message.setText("Недействительный артикул");
            return false;
        }

        List<Integer> goods = goodsRepository.getAllGoods().stream()
                .map(Goods::getVendorCode)
                .collect(Collectors.toList());

        if (!goods.contains(vendorCode)) {
            request_message.setText("Артикула не существует");
            return false;
        }
        return true;
    }

    public boolean verifyPurchase(Double price) {
        request_message.setFill(Color.RED);
        if (price == null || price <= 0.00) {
            request_message.setText("Недействительная цена закупки");
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

    public boolean verifyInvoiceNumber(Integer invoiceNumber) throws SQLException {
        request_message.setFill(Color.RED);
        if (invoiceNumber == null || invoiceNumber <= 0) {
            request_message.setText("Недействительный номер накладной");
            return false;
        }
        List<Integer> allInvoicesNumber = purchaseRepository.getAllInvoiceNumbers();
        if (allInvoicesNumber.contains(invoiceNumber)) {
            request_message.setText("Номер накладной уже занят");
            return false;
        }
        return true;
    }

}