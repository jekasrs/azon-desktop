package seller.group.azon.controller.Goods;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import seller.group.azon.backend.ControlStage;
import seller.group.azon.backend.DBconnection;
import seller.group.azon.controller.Controller;
import seller.group.azon.entity.Goods;
import seller.group.azon.entity.User;
import seller.group.azon.repos.GoodsRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GoodsController implements Controller {

    @FXML
    private BorderPane ap;
    @FXML
    private TextField filter_value_field;
    @FXML
    private ComboBox filter;
    @FXML
    private Button add_button;

    private BorderPane parentAp;
    private GoodsRepository goodsRepository;
    private User user;

    public void setParentAp(BorderPane parentAp) {
        this.parentAp = parentAp;
    }

    public void addGoods(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/goods/one-goods-view.fxml"));
        Parent root = fxmlLoader.load();
        parentAp.setCenter(root);
        OneGoodsController controller = fxmlLoader.getController();
        try {
            controller.setTypeOfProcedure(null, false);
        } catch (SQLException e) {
            return;
        }
    }
    public void updateGoods(Integer vendorCode) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/goods/one-goods-view.fxml"));
        Parent root = fxmlLoader.load();
        parentAp.setCenter(root);
        OneGoodsController controller = fxmlLoader.getController();
        try {
            controller.setTypeOfProcedure(vendorCode, true);
        } catch (SQLException e) {
            return;
        }
    }

    @FXML
    public void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        ObservableList<String> range = FXCollections.observableArrayList(
                "Кол-во меньше",
                "Кол-во больше",
                "Цена ниже",
                "Цена больше",
                "По артикулу"
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
            goodsRepository = new GoodsRepository(con);
            setGoods(goodsRepository.getAllGoods());
        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            System.out.println(e.getMessage());
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }
    }

    private void setGoods(List<Goods> allGoods) throws SQLException {
        ScrollPane sp = new ScrollPane();
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        VBox.setVgrow(sp, Priority.ALWAYS);
        Font font = new Font(14);
        for (Goods g : allGoods){
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.BASELINE_LEFT);
            vbox.setMaxWidth(600);

            Text title = new Text();
            title.setText(g.getTitle());
            title.setFont(font);
            title.setTextAlignment(TextAlignment.JUSTIFY);
            title.setLineSpacing(1.0);

            Text vCode = new Text();
            vCode.setText(g.getVendorCode().toString());
            vCode.setFont(font);
            vCode.setFill(Color.color(0.02, 0.7, 0.9));
            vCode.setOnMouseClicked(event -> {
                try {
                    updateGoods(Integer.parseInt(vCode.getText()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            vCode.setTextAlignment(TextAlignment.JUSTIFY);
            vCode.setLineSpacing(1.0);

            Text price = new Text();
            price.setText("Продажа: " + g.getPrice().toString());
            price.setFont(font);
            price.setTextAlignment(TextAlignment.JUSTIFY);
            price.setLineSpacing(1.0);

            Text purchase = new Text();
            purchase.setText("Закупка: " + g.getPurchase().toString());
            purchase.setFont(font);
            purchase.setTextAlignment(TextAlignment.JUSTIFY);
            purchase.setLineSpacing(1.0);

            Text amount = new Text();
            amount.setText("Остаток: " + g.getAmount().toString());
            amount.setFont(font);
            amount.setTextAlignment(TextAlignment.JUSTIFY);
            amount.setLineSpacing(1.0);

            Text description = new Text();
            description.setText(g.getDescription());
            description.setFont(font);
            description.setTextAlignment(TextAlignment.JUSTIFY);
            description.setLineSpacing(1.0);

            vbox.getChildren().add(title);
            vbox.getChildren().add(vCode);
            vbox.getChildren().add(purchase);
            vbox.getChildren().add(price);
            vbox.getChildren().add(amount);
            vbox.getChildren().add(description);

            vbox.setMargin(title, new Insets(10, 0,0,0));
            vbox.setMargin(vCode, new Insets(2, 0,0,0));
            vbox.setMargin(purchase, new Insets(2, 0,0,0));
            vbox.setMargin(price, new Insets(2, 0,0,0));
            vbox.setMargin(amount, new Insets(2, 0,0,0));
            vbox.setMargin(description, new Insets(2, 0,2,0));

            box.getChildren().add(vbox);
            box.setMargin(vbox, new Insets(5, 0, 0, 20));
        }
        sp.setContent(box);
        ap.setCenter(sp);
    }
    public void searchWithFilter(ActionEvent mouseEvent) throws SQLException {
        String filterChoice = (String) this.filter.getValue();
        if (filterChoice == null) return;
        try
        {
            Number number = Double.parseDouble(filter_value_field.getText());
            setGoods(goodsRepository.searchWithFilter(filterChoice, number));
        }
        catch (NumberFormatException e){
            return;
        }
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        if (user.getRole().equals("Purchasing Manager")){
            add_button.setVisible(false);
        }
    }
}
