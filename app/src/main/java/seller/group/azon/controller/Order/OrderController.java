package seller.group.azon.controller.Order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import seller.group.azon.backend.ControlStage;
import seller.group.azon.backend.DBconnection;
import seller.group.azon.controller.Controller;
import seller.group.azon.entity.Order;
import seller.group.azon.entity.User;
import seller.group.azon.repos.SalesRepository;
import seller.group.azon.repos.UserRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class OrderController implements Controller {
    @FXML
    private BorderPane ap;

    @FXML
    private TextField filter_value_field;

    @FXML
    private ComboBox filter;

    private BorderPane parentAp;
    private SalesRepository salesRepository;
    private UserRepository userRepository;
    private User user;

    public void setParentAp(BorderPane parentAp) {
        this.parentAp = parentAp;
    }

    public void watchOrderContent(Integer orderNumber) throws IOException, SQLException {
        Order order = salesRepository.getOrderByOrderNumber(orderNumber);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/orders/one-order-view.fxml"));
        Parent root = fxmlLoader.load();
        parentAp.setCenter(root);
        OneOrderController controller = fxmlLoader.getController();
        controller.setOrder(order);
    }

    @FXML
    public void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        ObservableList<String> range = FXCollections.observableArrayList(
                "по номеру заказа",
                "по тел. покупателя",
                "по тел. менеджера"
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
            userRepository = new UserRepository(con);
        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            System.out.println(e.getMessage());
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }
    }

    private void setOrders(List<Order> allOrders) throws SQLException {
        ScrollPane sp = new ScrollPane();
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        VBox.setVgrow(sp, Priority.ALWAYS);
        Font font = new Font(14);
        for (Order o : allOrders){
            if (o == null) return;
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.BASELINE_LEFT);
            vbox.setMaxWidth(600);

            Text orderNumber = new Text();
            orderNumber.setText("Номер закаказа: " + o.getOrderNumber());
            orderNumber.setFont(font);
            orderNumber.setFill(Color.color(0.02, 0.7, 0.9));
            orderNumber.setTextAlignment(TextAlignment.JUSTIFY);
            orderNumber.setLineSpacing(1.0);
            orderNumber.setOnMouseClicked(event -> {
                try {
                    watchOrderContent(o.getOrderNumber());
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            });

            Text dataText = new Text();
            dataText.setText(o.getDate().toString());
            dataText.setFont(font);
            dataText.setTextAlignment(TextAlignment.JUSTIFY);
            dataText.setLineSpacing(1.0);

            Text totalSum = new Text();
            totalSum.setText("Сумма заказа: " + o.getTotalSum());
            totalSum.setFont(font);
            totalSum.setTextAlignment(TextAlignment.JUSTIFY);
            totalSum.setLineSpacing(1.0);

            Text nameCustomer = new Text();
            nameCustomer.setText("ФИО покупателя: " + o.getFio());
            nameCustomer.setFont(font);
            nameCustomer.setTextAlignment(TextAlignment.JUSTIFY);
            nameCustomer.setLineSpacing(1.0);

            Text phoneCustomer= new Text();
            phoneCustomer.setText("Тел. покупателя: " + o.getPhone());
            phoneCustomer.setFont(font);
            phoneCustomer.setTextAlignment(TextAlignment.JUSTIFY);
            phoneCustomer.setLineSpacing(1.0);

            vbox.getChildren().add(orderNumber);
            vbox.getChildren().add(dataText);
            vbox.getChildren().add(totalSum);
            vbox.getChildren().add(nameCustomer);
            vbox.getChildren().add(phoneCustomer);

            vbox.setMargin(orderNumber, new Insets(10, 0,0,0));
            vbox.setMargin(dataText, new Insets(2, 0,0,0));
            vbox.setMargin(totalSum, new Insets(2, 0,0,0));
            vbox.setMargin(nameCustomer, new Insets(2, 0,0,0));
            vbox.setMargin(phoneCustomer, new Insets(2, 0,2,0));

            box.getChildren().add(vbox);
            box.setMargin(vbox, new Insets(5, 0, 0, 20));
        }
        sp.setContent(box);
        ap.setCenter(sp);
    }
    public void searchWithFilter(ActionEvent mouseEvent) throws SQLException {
        try {
            String filterChoice = (String) this.filter.getValue();
            if (filterChoice == null) return;

            List<Order> orders;
            if (filterChoice.equals("по номеру заказа")){
                Integer orderNumber = Integer.parseInt(filter_value_field.getText());
                orders = new LinkedList<>();
                if (user.getRole().equals("Admin"))
                    orders.add(salesRepository.getOrderByOrderNumber(orderNumber));
                else
                    orders.add(salesRepository.getOrderByOrderNumberAndIdManager(orderNumber, user.getId()));
            }
            else if (filterChoice.equals("по тел. покупателя")){
                String phone = filter_value_field.getText();
                if (!salesRepository.getAllPhonesOfCustomers().contains(phone))
                    return;

                if (user.getRole().equals("Admin"))
                    orders = salesRepository.getOrdersByPhoneCustomer(phone);
                else
                    orders = salesRepository.getOrdersByPhoneCustomerAndIdManager(phone, user.getId());
            }
            else if (filterChoice.equals("по тел. менеджера")){
                String phone = filter_value_field.getText();
                if (!userRepository.getAllPhones().contains(phone))
                    return;
                if (user.getRole().equals("Admin"))
                    orders = salesRepository.getOrdersByPhoneOfManager(phone);
                else {
                    if (!user.getPhone().equals(phone)) return;
                    orders = salesRepository.getAllOrdersByManager(user.getId());
                }
            }
            else {
                if (user.getRole().equals("Admin"))
                    orders = salesRepository.getAllOrders();
                else
                    orders = salesRepository.getAllOrdersByManager(user.getId());
            }

            setOrders(orders);
        }
        catch (NumberFormatException e ){
            return;
        }

    }

    @Override
    public void setUser(User user) {
        this.user = user;
        if (user.getRole().equals("Admin")){
            try {
                setOrders(salesRepository.getAllOrders());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                setOrders(salesRepository.getAllOrdersByManager(user.getId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}