package seller.group.azon.controller.Seller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
import seller.group.azon.entity.Seller;
import seller.group.azon.entity.User;
import seller.group.azon.repos.SellerRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SellerController implements Controller {
    @FXML
    private BorderPane ap;
    @FXML
    private TextField filter_value_field;
    @FXML
    private ComboBox filter;
    @FXML
    private Button add_button;

    private BorderPane parentAp;
    private SellerRepository sellerRepository;
    private User user;

    @FXML
    public void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        ObservableList<String> range = FXCollections.observableArrayList(
                "ИНН",
                "Телефон"
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
            sellerRepository = new SellerRepository(con);
            setSellers(sellerRepository.getAllSellers());

        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            System.out.println(e.getMessage());
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }
    }

    private void setSellers(List<Seller> allSellers) throws SQLException {
        ScrollPane sp = new ScrollPane();
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        VBox.setVgrow(sp, Priority.ALWAYS);
        Font font = new Font(14);
        for (Seller s : allSellers) {
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.BASELINE_LEFT);
            vbox.setMaxWidth(600);

            Text name = new Text();
            name.setText(s.getName());
            name.setFont(font);
            name.setFill(Color.color(0.02, 0.7, 0.9));
            name.setOnMouseClicked(event -> {
                try {
                    updateSeller(s.getIdSeller());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            name.setTextAlignment(TextAlignment.JUSTIFY);
            name.setLineSpacing(1.0);

            Text taxPayer = new Text();
            taxPayer.setText("ИНН: " + s.getTaxPayerId().toString());
            taxPayer.setFont(font);
            taxPayer.setTextAlignment(TextAlignment.JUSTIFY);
            taxPayer.setLineSpacing(1.0);

            Text phone = new Text();
            phone.setText("Тел.: " + s.getPhone());
            phone.setFont(font);
            phone.setTextAlignment(TextAlignment.JUSTIFY);
            phone.setLineSpacing(1.0);

            vbox.getChildren().add(name);
            vbox.getChildren().add(phone);
            vbox.getChildren().add(taxPayer);

            vbox.setMargin(name, new Insets(10, 0, 0, 0));
            vbox.setMargin(phone, new Insets(2, 0, 0, 0));
            vbox.setMargin(taxPayer, new Insets(2, 0, 2, 0));

            box.getChildren().add(vbox);
            box.setMargin(vbox, new Insets(5, 0, 0, 20));
        }
        sp.setContent(box);
        ap.setCenter(sp);
    }

    public void setParentAp(BorderPane parentAp) {
        this.parentAp = parentAp;
    }

    public void addSeller(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sellers/one-seller-view.fxml"));
        Parent root = fxmlLoader.load();
        parentAp.setCenter(root);
        OneSellerController controller = fxmlLoader.getController();
        try {
            controller.setTypeOfProcedure(null, false);
        } catch (SQLException e) {
            return;
        }
    }

    public void updateSeller(Integer idSeller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sellers/one-seller-view.fxml"));
        Parent root = fxmlLoader.load();
        parentAp.setCenter(root);
        OneSellerController controller = fxmlLoader.getController();
        try {
            controller.setTypeOfProcedure(idSeller, true);
        } catch (SQLException e) {
            return;
        }
    }

    public void searchWithFilter(ActionEvent mouseEvent) throws SQLException {
        String filterChoice = (String) this.filter.getValue();
        if (filterChoice == null) return;
        try {
            if (filterChoice.equals("ИНН")){
                Number number = Float.parseFloat(filter_value_field.getText());
                setSellers(sellerRepository.getSellersByTaxId(number));
            }
            else {
                List<String> phones = sellerRepository.getAllPhones();
                String number = (filter_value_field.getText());
                if (phones.contains(number))
                    setSellers(sellerRepository.getSellersByPhone(number));
            }
        } catch (NumberFormatException e) {
            return;
        }
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        if (!user.getRole().equals("Admin")){
            add_button.setVisible(false);
        }
    }
}
