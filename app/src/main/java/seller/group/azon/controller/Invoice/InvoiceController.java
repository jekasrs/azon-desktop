package seller.group.azon.controller.Invoice;

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
import seller.group.azon.entity.Invoice;
import seller.group.azon.entity.User;
import seller.group.azon.repos.PurchaseRepository;
import seller.group.azon.repos.UserRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class InvoiceController implements Controller {
    @FXML
    private BorderPane ap;

    @FXML
    private TextField filter_value_field;

    @FXML
    private ComboBox filter;

    private BorderPane parentAp;
    private PurchaseRepository purchaseRepository;
    private UserRepository userRepository;
    private User user;

    public void setParentAp(BorderPane parentAp) {
        this.parentAp = parentAp;
    }

    public void watchInvoiceContent(Integer invoiceNumber) throws IOException, SQLException {
        Invoice invoice = purchaseRepository.getInvoiceByInvoiceNumber(invoiceNumber);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/invoices/one-invoice-view.fxml"));
        Parent root = fxmlLoader.load();
        parentAp.setCenter(root);
        OneInvoiceController controller = fxmlLoader.getController();

        controller.setInvoice(invoice);
    }

    @FXML
    public void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        ObservableList<String> range = FXCollections.observableArrayList(
                "по номеру накладной",
                "по ИНН",
                "по тел. поставщика",
                "по тел. сотрудника"
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
            purchaseRepository = new PurchaseRepository(con);
            userRepository = new UserRepository(con);
            setInvoices(purchaseRepository.getAllInvoices());
        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            System.out.println(e.getMessage());
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }
    }

    private void setInvoices(List<Invoice> allInvoices) throws SQLException {
        ScrollPane sp = new ScrollPane();
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        VBox.setVgrow(sp, Priority.ALWAYS);
        Font font = new Font(14);
        for (Invoice i : allInvoices){
            if (i == null) return;
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.BASELINE_LEFT);
            vbox.setMaxWidth(600);

            Text invoiceNumber = new Text();
            invoiceNumber.setText("Номер накладной: " + i.getInvoiceNumber());
            invoiceNumber.setFont(font);
            invoiceNumber.setFill(Color.color(0.02, 0.7, 0.9));
            invoiceNumber.setTextAlignment(TextAlignment.JUSTIFY);
            invoiceNumber.setLineSpacing(1.0);
            invoiceNumber.setOnMouseClicked(event -> {
                try {
                    watchInvoiceContent(i.getInvoiceNumber());
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            });

            Text dataText = new Text();
            dataText.setText(i.getDate().toString());
            dataText.setFont(font);
            dataText.setTextAlignment(TextAlignment.JUSTIFY);
            dataText.setLineSpacing(1.0);

            Text totalSum = new Text();
            totalSum.setText("Сумма закупки: " + i.getTotalSum());
            totalSum.setFont(font);
            totalSum.setTextAlignment(TextAlignment.JUSTIFY);
            totalSum.setLineSpacing(1.0);

            Text nameSeller = new Text();
            nameSeller.setText("Поставщик: " + i.getSeller().getName());
            nameSeller.setFont(font);
            nameSeller.setTextAlignment(TextAlignment.JUSTIFY);
            nameSeller.setLineSpacing(1.0);

            Text phoneSeller = new Text();
            phoneSeller.setText("Тел. Поставщика: " + i.getSeller().getPhone());
            phoneSeller.setFont(font);
            phoneSeller.setTextAlignment(TextAlignment.JUSTIFY);
            phoneSeller.setLineSpacing(1.0);

            vbox.getChildren().add(invoiceNumber);
            vbox.getChildren().add(dataText);
            vbox.getChildren().add(totalSum);
            vbox.getChildren().add(nameSeller);
            vbox.getChildren().add(phoneSeller);

            vbox.setMargin(invoiceNumber, new Insets(10, 0,0,0));
            vbox.setMargin(dataText, new Insets(2, 0,0,0));
            vbox.setMargin(totalSum, new Insets(2, 0,0,0));
            vbox.setMargin(nameSeller, new Insets(2, 0,0,0));
            vbox.setMargin(phoneSeller, new Insets(2, 0,2,0));

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

            List<Invoice> invoices;
            if (filterChoice.equals("по ИНН")) {
                Integer inn = Integer.parseInt(filter_value_field.getText());
                invoices = purchaseRepository.getInvoicesByTaxPayerIdSeller(inn);
            } else if (filterChoice.equals("по тел. поставщика")) {
                String phone = filter_value_field.getText();
                invoices = purchaseRepository.getInvoicesByPhoneSeller(phone);
            } else if (filterChoice.equals("по тел. сотрудника")) {
                String phone = filter_value_field.getText();
                if (!userRepository.getAllPhones().contains(phone))
                    return;
                invoices = purchaseRepository.getAllInvoicesByPhoneManager(phone);
            }
            else if (filterChoice.equals("по номеру накладной")) {
                Integer number = Integer.parseInt(filter_value_field.getText());
                invoices = new LinkedList<>();
                invoices.add(purchaseRepository.getInvoiceByInvoiceNumber(number));
            }
            else {
                invoices = purchaseRepository.getAllInvoices();
            }

            setInvoices(invoices);
        }
        catch (NumberFormatException e){
            return;
        }
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}