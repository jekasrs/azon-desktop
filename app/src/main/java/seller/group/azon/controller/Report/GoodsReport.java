package seller.group.azon.controller.Report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import seller.group.azon.backend.ControlStage;
import seller.group.azon.backend.DBconnection;
import seller.group.azon.controller.Controller;
import seller.group.azon.entity.Goods;
import seller.group.azon.entity.Invoice;
import seller.group.azon.entity.User;
import seller.group.azon.repos.GoodsRepository;
import seller.group.azon.repos.PurchaseRepository;
import seller.group.azon.repos.SalesRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class GoodsReport implements Controller {
    @FXML
    BorderPane ap;

    @FXML
    DatePicker date_beg_field;
    @FXML
    DatePicker date_end_field;
    @FXML
    TableView goods_table;
    @FXML
    Text request_message;
    @FXML
    TextField filter;
    GoodsRepository goodsRepository;

    private User user;

    @Override
    public void setUser(User user) {
        this.user = user;
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

    public void fillTable(List<Goods> goods) {
        TableColumn vendorCol = new TableColumn("Артикул");
        TableColumn purchaseCol = new TableColumn("Средняя цена закупки");
        TableColumn priceCol = new TableColumn("Средняя цена продаж");
        TableColumn amountPurchaseCol = new TableColumn("Кол-во закупок");
        TableColumn amountSalesCol = new TableColumn("Кол-во продаж");
        TableColumn profitCol = new TableColumn("Прибыль");

        vendorCol.setMinWidth(100);
        purchaseCol.setMinWidth(200);
        priceCol.setMinWidth(200);
        amountPurchaseCol.setMinWidth(100);
        amountSalesCol.setMinWidth(100);
        profitCol.setMinWidth(100);

        goods_table.setColumnResizePolicy((param) -> true);
        goods_table.setMaxHeight(300);
        goods_table.setMaxWidth(800);
        goods_table.getColumns().addAll(vendorCol, purchaseCol, priceCol, amountPurchaseCol, amountSalesCol, profitCol);

        final ObservableList<Goods> data = FXCollections.observableArrayList(goods);

        vendorCol.setCellValueFactory(
                new PropertyValueFactory<Goods, Integer>("vendorCode")
        );
        purchaseCol.setCellValueFactory(
                new PropertyValueFactory<Goods, Double>("averagePurchase")
        );
        priceCol.setCellValueFactory(
                new PropertyValueFactory<Goods, Double>("averagePrice")
        );
        amountPurchaseCol.setCellValueFactory(
                new PropertyValueFactory<Goods, Integer>("totalAmountPurchase")
        );
        amountSalesCol.setCellValueFactory(
                new PropertyValueFactory<Goods, Integer>("totalAmountPrice")
        );
        profitCol.setCellValueFactory(
                new PropertyValueFactory<Goods, Double>("profit")
        );

        goods_table.setItems(data);
    }

    public void handleClear(MouseEvent mouseEvent) {
        request_message.setText("");
    }

    public void handleAnalyze(ActionEvent actionEvent) {
        request_message.setText("");

        LocalDate dateBeg = date_beg_field.getValue();
        LocalDate dateEnd = date_end_field.getValue();

        if (!verifyDates(dateBeg, dateEnd)) return;

        try {
            if (filter.getText() == null || filter.getText().equals("")) {
                List<Goods> analyzeOfGoods = goodsRepository.getAnalyze(dateBeg, dateEnd);
                fillTable(analyzeOfGoods);
            } else {
                Integer filterValue = Integer.parseInt(filter.getText());
                if (!verifyVendor(filterValue))
                    return;
                Goods analyzeOfOneGoods = goodsRepository.getAnalyze(dateBeg, dateEnd, filterValue);
                List<Goods> analyzeOfGoods = new LinkedList<>();
                analyzeOfGoods.add(analyzeOfOneGoods);
                fillTable(analyzeOfGoods);
            }
        } catch (SQLException e) {
            request_message.setText("Неизвестная ошибка.\nПовторите операцию.");
            return;
        }
        date_beg_field.setValue(null);
        date_end_field.setValue(null);

    }

    private boolean verifyDates(LocalDate dateBeg, LocalDate dateEnd) {
        if (dateBeg == null) {
            request_message.setText("Недействительная дата начала");
            return false;
        }
        if (dateEnd == null) {
            request_message.setText("Недействительная дата конца");
            return false;
        }

        if (dateBeg.isAfter(LocalDate.now()) || dateEnd.isAfter(LocalDate.now())) {
            request_message.setText("Периоды не могут быть в будущем");
            return false;
        }
        if (dateBeg.isAfter(dateEnd)) {
            request_message.setText("Начало периода должно быть раньше");
            return false;
        }
        return true;
    }

    private boolean verifyVendor(Integer vendorCode) throws SQLException {
        List<Integer> codes = goodsRepository.getAllVendorCodes();
        if (!codes.contains(vendorCode)) {
            request_message.setText("Недействительный артикул");
            return false;
        }
        return true;
    }
}
