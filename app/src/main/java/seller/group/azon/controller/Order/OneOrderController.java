package seller.group.azon.controller.Order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.*;
import seller.group.azon.backend.ControlStage;
import seller.group.azon.backend.DBconnection;
import seller.group.azon.controller.Controller;
import seller.group.azon.controller.Seller.OneSellerController;
import seller.group.azon.entity.Goods;
import seller.group.azon.entity.Order;
import seller.group.azon.entity.Seller;
import seller.group.azon.entity.User;
import seller.group.azon.repos.SalesRepository;
import seller.group.azon.repos.SellerRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class OneOrderController implements Controller {
    @FXML
    BorderPane ap;

    @FXML
    Label order_number_field;
    @FXML
    Label date_field;
    @FXML
    Label name_of_customer_field;
    @FXML
    Label phone_of_customer_field;
    @FXML
    Label total_sum_field;

    @FXML
    private ComboBox filter;

    private BorderPane parentAp;
    SalesRepository salesRepository;
    Order order;
    private User user;

    public void setParentAp(BorderPane parentAp) {
        this.parentAp = parentAp;
    }
    public void setOrder(Order order) throws SQLException {

        order_number_field .setText(order_number_field .getText() + order.getOrderNumber().toString());
        date_field           .setText(date_field           .getText() + order.getDate().toString());
        name_of_customer_field .setText(name_of_customer_field .getText() + order.getFio());
        phone_of_customer_field.setText(phone_of_customer_field.getText() + order.getPhone());
        total_sum_field      .setText(total_sum_field      .getText() + order.getTotalSum().toString());

        this.order = order;
        setContentOrders(salesRepository.getInfoByOrderNumber(order.getOrderNumber()));
    }

    @FXML
    public void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        ObservableList<String> range = FXCollections.observableArrayList(
                "RECEIVED",
                "PAID"
        );

        filter.setItems(range);
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
            salesRepository = new SalesRepository(con);
        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            System.out.println(e.getMessage());
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }
    }

    private void setContentOrders(List<Goods> allOrderContent) throws SQLException {
        ScrollPane sp = new ScrollPane();
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        VBox.setVgrow(sp, Priority.ALWAYS);
        Font font = new Font(14);
        for (Goods g : allOrderContent){
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.BASELINE_LEFT);
            vbox.setMaxWidth(600);

            Text vendorCode = new Text();
            vendorCode.setText("Артикул: " + g.getVendorCode().toString());
            vendorCode.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
            vendorCode.setTextAlignment(TextAlignment.JUSTIFY);

            Text title = new Text();
            title.setText(g.getTitle());
            title.setFont(font);
            title.setTextAlignment(TextAlignment.JUSTIFY);
            title.setLineSpacing(1.0);

            Text amount = new Text();
            amount.setText("Кол-во.: " + g.getAmount());
            amount.setFont(font);
            amount.setTextAlignment(TextAlignment.JUSTIFY);
            amount.setLineSpacing(1.0);

            Text price = new Text();
            price.setText("Цена: " + g.getPrice());
            price.setFont(font);
            price.setTextAlignment(TextAlignment.JUSTIFY);
            price.setLineSpacing(1.0);

            Text sum = new Text();
            sum.setText("Итого: " + g.getSumAndAmount());
            sum.setFont(font);
            sum.setTextAlignment(TextAlignment.JUSTIFY);
            sum.setLineSpacing(1.0);

            Text status = new Text();
            status.setText("Статус: " + g.getStatus());
            status.setFont(font);
            status.setTextAlignment(TextAlignment.JUSTIFY);
            status.setLineSpacing(1.0);
            status.setOnMouseClicked(event -> {
                        if (Objects.equals(g.getStatus(), "PAID")) {
                            try {
                                salesRepository.updateStatusOfOrderContent(g, "RECEIVED");
                                g.setStatus("RECEIVED");
                                status.setFill(Color.color(0.1, 0.9, 0.62));
                                status.setText("Статус: " + g.getStatus());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (Objects.equals(g.getStatus(), "RECEIVED")) {
                            try {
                                salesRepository.updateStatusOfOrderContent(g, "PAID");

                                status.setFill(Color.color(0.0, 0.0, 0.0));
                                g.setStatus("PAID");
                                status.setText("Статус: " + g.getStatus());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            if (g.getStatus().equals("RECEIVED"))
                status.setFill(Color.color(0.1, 0.9, 0.62));

            vbox.getChildren().add(vendorCode);
            vbox.getChildren().add(title);
            vbox.getChildren().add(amount);
            vbox.getChildren().add(price);
            vbox.getChildren().add(sum);
            vbox.getChildren().add(status);

            vbox.setMargin(vendorCode, new Insets(0, 0,0,0));
            vbox.setMargin(title, new Insets(2, 0,0,0));
            vbox.setMargin(amount, new Insets(2, 0,0,0));
            vbox.setMargin(price, new Insets(2, 0,0,0));
            vbox.setMargin(sum, new Insets(2, 0,0,0));
            vbox.setMargin(status, new Insets(2, 0,0,0));

            box.getChildren().add(vbox);
            box.setMargin(vbox, new Insets(10, 0, 0, 20));
        }
        sp.setContent(box);
        ap.setCenter(sp);
    }

    public void searchWithFilter(ActionEvent mouseEvent) throws SQLException {
        String filterChoice = (String) this.filter.getValue();
        if (filterChoice == null) return;
        setContentOrders(salesRepository.searchWithFilter(filterChoice, order.getOrderNumber()));
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
