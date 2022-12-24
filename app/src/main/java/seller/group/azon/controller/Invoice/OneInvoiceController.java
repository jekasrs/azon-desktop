package seller.group.azon.controller.Invoice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import seller.group.azon.backend.ControlStage;
import seller.group.azon.backend.DBconnection;
import seller.group.azon.controller.Controller;
import seller.group.azon.entity.Goods;
import seller.group.azon.entity.Invoice;
import seller.group.azon.entity.User;
import seller.group.azon.repos.PurchaseRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OneInvoiceController implements Controller {
    @FXML
    BorderPane ap;

    @FXML
    Label invoice_number_field;
    @FXML
    Label date_field;
    @FXML
    Label name_of_seller_field;
    @FXML
    Label phone_of_seller_field;
    @FXML
    Label tax_of_seller_field;
    @FXML
    Label total_sum_field;
    @FXML
    Label manager_phone_field;
    @FXML
    Label manager_fio_field;

    @FXML
    TableView goods_table;

    PurchaseRepository purchaseRepository;
    private User user;

    public void setInvoice(Invoice invoice) throws SQLException {
        invoice_number_field .setText(invoice_number_field .getText() + invoice.getInvoiceNumber().toString());
        date_field           .setText(date_field           .getText() + invoice.getDate().toString());
        name_of_seller_field .setText(name_of_seller_field .getText() + invoice.getSeller().getName());
        phone_of_seller_field.setText(phone_of_seller_field.getText() + invoice.getSeller().getPhone());
        tax_of_seller_field  .setText(tax_of_seller_field  .getText() + invoice.getSeller().getTaxPayerId().toString());
        total_sum_field      .setText(total_sum_field      .getText() + invoice.getTotalSum().toString());
        manager_phone_field  .setText(manager_phone_field  .getText() + invoice.getManager().getPhone());
        manager_fio_field    .setText(manager_fio_field    .getText() + invoice.getManager().getFullName());
        List<Goods> goodsAndAmount = purchaseRepository.getInfoByInvoiceNumber(invoice.getInvoiceNumber());
        fillTable(goodsAndAmount);
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
            purchaseRepository = new PurchaseRepository(con);
        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }
    }

    public void fillTable(List<Goods> goodsAndAmount){
        goods_table = new TableView();
        TableColumn vendorCol = new TableColumn("Артикул");
        TableColumn purchaseCol = new TableColumn("Цена закупки");
        TableColumn amountCol = new TableColumn("Кол-во");
        TableColumn totalSumCol = new TableColumn("Итого");

        vendorCol  .setMinWidth(100);
        purchaseCol.setMinWidth(100);
        amountCol  .setMinWidth(100);
        totalSumCol.setMinWidth(100);

        goods_table.setColumnResizePolicy((param) -> true );
        goods_table.setMaxHeight(300);
        goods_table.setMaxWidth(400);
        goods_table.getColumns().addAll(vendorCol, amountCol, purchaseCol, totalSumCol);

        final ObservableList<Goods> data = FXCollections.observableArrayList(goodsAndAmount);
        vendorCol.setCellValueFactory(
                new PropertyValueFactory<Goods,Integer>("vendorCode")
        );
        purchaseCol.setCellValueFactory(
                new PropertyValueFactory<Goods,Integer>("purchase")
        );
        amountCol.setCellValueFactory(
                new PropertyValueFactory<Goods,Double>("amount")
        );
        totalSumCol.setCellValueFactory(
                new PropertyValueFactory<Goods,Double>("sumAndAmount")
        );

        goods_table.setItems(data);
        ap.setCenter(goods_table);
    }

    @Override
    public void setUser(User user) {
        this.user= user;
    }
}
