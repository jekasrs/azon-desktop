package seller.group.azon.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import seller.group.azon.backend.ControlStage;
import seller.group.azon.controller.Goods.GoodsController;
import seller.group.azon.controller.Invoice.InvoiceAddController;
import seller.group.azon.controller.Invoice.InvoiceController;
import seller.group.azon.controller.Manager.ManagerController;
import seller.group.azon.controller.Order.OrderAddController;
import seller.group.azon.controller.Order.OrderController;
import seller.group.azon.controller.Report.ChooseReport;
import seller.group.azon.controller.Seller.SellerController;
import seller.group.azon.entity.User;

import java.io.IOException;
import java.util.Objects;

public class AdminController implements Controller {
    @FXML
    private BorderPane ap;

    @FXML
    private VBox menu_box;

    @FXML
    private VBox top_box;

    @FXML
    private Text work_station;

    private User user;

    Button myself = new Button();           // редактирование личных данных
    Button reg = new Button();             // регистрация пользователя
    Button goods = new Button();          // просмотр и редактирование товаров
    Button managers = new Button();      // просмотр и редактирование менеджеров
    Button sellers = new Button();      // просмотр и редактирование закупщиков
    Button report = new Button();      // отчетность компании
    Button purchase = new Button();   // закупка
    Button sale = new Button();      // продажа
    Button invoices = new Button(); // накладные
    Button orders = new Button();  // заказы

    @FXML
    public void initialize() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // установка параметров root
        ap.getTop().setStyle("-fx-background-color: #1382CEFF");
        ap.getTop().setOnMouseClicked(event -> {
            reg.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            myself.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
        });
        ap.getLeft().setStyle("-fx-background-color: #024693");

        // конфигурация сцены редактирования личных данных
        myself.setText("Учетная\nзапись");
        myself.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
        myself.setTextFill(Color.color(0.9, 0.9, 0.9));
        myself.setMinWidth(ap.getLeft().maxWidth(0.0)-5);
        myself.setMinHeight(50.0);
        myself.setOnAction(event -> {
            myself.setStyle("-fx-font-size: 14; -fx-background-color: #78a4c2" );
            reg.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            goods.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            managers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sellers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            report.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            purchase.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sale.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            invoices.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            orders.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/myself-view.fxml"));
                Parent root = fxmlLoader.load();

                MyselfController myselfController = fxmlLoader.getController();
                myselfController.setUser(user);

                ap.setCenter(root);
            } catch (IOException e) {
                try {
                    System.err.println("Bad happened with connection. It is closed");
                    ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // конфигурация сцены регистрации
        reg.setText("Добавить\nсотрудника");
        reg.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
        reg.setTextFill(Color.color(0.9, 0.9, 0.9));
        reg.setMinWidth(ap.getLeft().maxWidth(0.0));
        reg.setMinHeight(50);
        reg.setOnAction(event -> {
            myself.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            reg.setStyle("-fx-font-size: 14; -fx-background-color: #78a4c2" );
            goods.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            managers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sellers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            report.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            purchase.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sale.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            invoices.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            orders.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/register-view.fxml"));
                Parent root = fxmlLoader.load();
                ap.setCenter(root);
            } catch (IOException e) {
                try {
                    System.err.println("Bad happened with connection. It is closed");
                    ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // конфигурация сцены товаров
        goods.setText("Товары");
        goods.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
        goods.setTextFill(Color.color(0.9, 0.9, 0.9));
        goods.setMinWidth(ap.getLeft().maxWidth(0.0));
        goods.setMinHeight(50);
        goods.setOnAction(event -> {
            myself.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            reg.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            goods.setStyle("-fx-font-size: 14; -fx-background-color: #78a4c2" );
            managers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sellers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            report.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            purchase.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sale.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            invoices.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            orders.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/goods/goods-view.fxml"));
                Parent root = fxmlLoader.load();
                ap.setCenter(root);
                GoodsController goodsController = fxmlLoader.getController();
                goodsController.setParentAp(ap);
                goodsController.setUser(user);
            } catch (IOException e) {
                try {
                    System.err.println("Bad happened with connection. It is closed");
                    ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // конфигурация сцены менеджеров
        managers.setText("Сотрудники");
        managers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
        managers.setTextFill(Color.color(0.9, 0.9, 0.9));
        managers.setMinWidth(ap.getLeft().maxWidth(0.0));
        managers.setMinHeight(50);
        managers.setOnAction(event -> {
            myself.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            reg.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            goods.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            managers.setStyle("-fx-font-size: 14; -fx-background-color: #78a4c2" );
            sellers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            report.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            purchase.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sale.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            invoices.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            orders.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/manager-view.fxml"));
                Parent root = fxmlLoader.load();
                ap.setCenter(root);
                ManagerController managerController = fxmlLoader.getController();
                managerController.setParentAp(ap);
            } catch (IOException e) {
                try {
                    System.err.println("Bad happened with connection. It is closed");
                    ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // конфигурация сцены поставщиков
        sellers.setText("Поставщики");
        sellers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
        sellers.setTextFill(Color.color(0.9, 0.9, 0.9));
        sellers.setMinWidth(ap.getLeft().maxWidth(0.0));
        sellers.setMinHeight(50);
        sellers.setOnAction(event -> {
            myself.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            reg.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            goods.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            managers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sellers.setStyle("-fx-font-size: 14; -fx-background-color: #78a4c2" );
            report.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            purchase.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sale.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            invoices.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            orders.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sellers/seller-view.fxml"));
                Parent root = fxmlLoader.load();
                ap.setCenter(root);
                SellerController sellerController = fxmlLoader.getController();
                sellerController.setParentAp(ap);
                sellerController.setUser(user);
            } catch (IOException e) {
                try {
                    System.err.println("Bad happened with connection. It is closed");
                    ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // конфигурация сцены для отчетов
        report.setText("Отчеты");
        report.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
        report.setTextFill(Color.color(0.9, 0.9, 0.9));
        report.setMinWidth(ap.getLeft().maxWidth(0.0));
        report.setMinHeight(50);
        report.setOnAction(event -> {
            myself.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            reg.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            goods.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            managers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sellers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            report.setStyle("-fx-font-size: 14; -fx-background-color: #78a4c2" );
            purchase.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sale.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            invoices.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            orders.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/reports/choose-report.fxml"));
                Parent root = fxmlLoader.load();
                ap.setCenter(root);
                ChooseReport chooseReport = fxmlLoader.getController();
                chooseReport.setParentAp(ap);
            } catch (IOException e) {
                try {
                    System.err.println("Bad happened with connection. It is closed");
                    ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        purchase.setText("Закупка");
        purchase.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
        purchase.setTextFill(Color.color(0.9, 0.9, 0.9));
        purchase.setMinWidth(ap.getLeft().maxWidth(0.0));
        purchase.setMinHeight(50);
        purchase.setOnAction(event -> {
            myself.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            reg.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            goods.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            managers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sellers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            report.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            purchase.setStyle("-fx-font-size: 14; -fx-background-color: #78a4c2" );
            sale.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            invoices.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            orders.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/invoices/add-invoice-view.fxml"));
                Parent root = fxmlLoader.load();
                InvoiceAddController invoiceAddController = fxmlLoader.getController();
                invoiceAddController.setUser(user);
                ap.setCenter(root);
            } catch (IOException e) {
                try {
                    System.err.println("Bad happened with connection. It is closed");
                    ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        sale.setText("Продажи");
        sale.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
        sale.setTextFill(Color.color(0.9, 0.9, 0.9));
        sale.setMinWidth(ap.getLeft().maxWidth(0.0));
        sale.setMinHeight(50);
        sale.setOnAction(event -> {
            myself.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            reg.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            goods.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            managers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sellers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            report.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            purchase.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sale.setStyle("-fx-font-size: 14; -fx-background-color: #78a4c2" );
            invoices.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            orders.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/orders/add-order-view.fxml"));
                Parent root = fxmlLoader.load();
                OrderAddController orderAddController = fxmlLoader.getController();
                orderAddController.setUser(user);
                ap.setCenter(root);
            } catch (IOException e) {
                try {
                    System.out.println(e.getMessage());
                    System.err.println("Bad happened with connection. It is closed");
                    ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        invoices.setText("Накладные");
        invoices.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
        invoices.setTextFill(Color.color(0.9, 0.9, 0.9));
        invoices.setMinWidth(ap.getLeft().maxWidth(0.0));
        invoices.setMinHeight(50);
        invoices.setOnAction(event -> {
            myself.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            reg.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            goods.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            managers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sellers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            report.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            purchase.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sale.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            invoices.setStyle("-fx-font-size: 14; -fx-background-color: #78a4c2" );
            orders.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/invoices/invoice-view.fxml"));
                Parent root = fxmlLoader.load();
                InvoiceController invoiceController = fxmlLoader.getController();
                invoiceController.setParentAp(ap);
                ap.setCenter(root);
            } catch (IOException e) {
                try {
                    System.err.println("Bad happened with connection. It is closed");
                    ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        orders.setText("Заказы");
        orders.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
        orders.setTextFill(Color.color(0.9, 0.9, 0.9));
        orders.setMinWidth(ap.getLeft().maxWidth(0.0));
        orders.setMinHeight(50);
        orders.setOnAction(event -> {
            myself.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            reg.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            goods.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            managers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sellers.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            report.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            purchase.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            sale.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            invoices.setStyle("-fx-font-size: 14; -fx-background-color: #1382CEFF" );
            orders.setStyle("-fx-font-size: 14; -fx-background-color: #78a4c2" );
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/orders/order-view.fxml"));
                Parent root = fxmlLoader.load();
                OrderController orderController = fxmlLoader.getController();
                orderController.setParentAp(ap);
                orderController.setUser(this.user);
                ap.setCenter(root);
            } catch (IOException e) {
                try {
                    System.err.println("Bad happened with connection. It is closed");
                    ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
        if (Objects.equals(user.getRole(), User.admin)){
            work_station.setText("Рабочий стол администратора");
            menu_box.getChildren().add(myself);
            menu_box.getChildren().add(report);
            menu_box.getChildren().add(goods);
            menu_box.getChildren().add(reg);
            menu_box.getChildren().add(managers);
            menu_box.getChildren().add(sellers);
            menu_box.getChildren().add(purchase);
            menu_box.getChildren().add(orders);
            menu_box.getChildren().add(invoices);
            menu_box.getChildren().add(sale);

            menu_box.setMargin(myself, new Insets(5, 0,0,0));
            menu_box.setMargin(reg, new Insets(5, 0,0,0));
            menu_box.setMargin(goods, new Insets(5, 0,0,0));
            menu_box.setMargin(managers, new Insets(5, 0,0,0));
            menu_box.setMargin(sellers, new Insets(5, 0,0,0));
            menu_box.setMargin(purchase, new Insets(5, 0,0,0));
            menu_box.setMargin(report, new Insets(5, 0,0,0));
            menu_box.setMargin(sale, new Insets(5, 0,0,0));
            menu_box.setMargin(invoices, new Insets(5, 0,0,0));
            menu_box.setMargin(orders, new Insets(5, 0,0,0));
        }
        else if (Objects.equals(user.getRole(), User.saleManager)){
            work_station.setText("Рабочий стол менеджера продаж");
            menu_box.getChildren().add(myself);
            menu_box.getChildren().add(goods);
            menu_box.getChildren().add(orders);
            menu_box.getChildren().add(sale);

            menu_box.setMargin(myself, new Insets(5, 0,0,0));
            menu_box.setMargin(goods, new Insets(5, 0,0,0));
            menu_box.setMargin(sale, new Insets(5, 0,0,0));
            menu_box.setMargin(orders, new Insets(5, 0,0,0));
        }
        else if (Objects.equals(user.getRole(), User.purchaseManager)){
            work_station.setText("Рабочий стол менеджера закупок");

            menu_box.getChildren().add(myself);
            menu_box.getChildren().add(sellers);
            menu_box.getChildren().add(purchase);
            menu_box.getChildren().add(invoices);
            menu_box.getChildren().add(goods);

            menu_box.setMargin(myself, new Insets(5, 0,0,0));
            menu_box.setMargin(sellers, new Insets(5, 0,0,0));
            menu_box.setMargin(purchase, new Insets(5, 0,0,0));
            menu_box.setMargin(invoices, new Insets(5, 0,0,0));
            menu_box.setMargin(goods, new Insets(5, 0,0,0));

        }
    }
}