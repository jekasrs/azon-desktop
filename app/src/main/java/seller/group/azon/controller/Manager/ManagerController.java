package seller.group.azon.controller.Manager;

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
import seller.group.azon.controller.ErrorController;
import seller.group.azon.controller.MyselfController;
import seller.group.azon.entity.User;
import seller.group.azon.repos.UserRepository;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class ManagerController implements Controller {
    @FXML
    private BorderPane ap;
    @FXML
    private TextField filter_value_field;
    @FXML
    private ComboBox filter;

    private BorderPane parentAp;
    private UserRepository userRepository;
    private User user;

    @FXML
    public void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        ObservableList<String> range = FXCollections.observableArrayList(
                "по телефону",
                "по ФИО",
                "по роли",
                "заблокированные",
                "активированные"
        );

        filter.setItems(range);
        DBconnection dBconnection;
        try {
            dBconnection = DBconnection.getInstance();
        } catch (IOException e) {
            System.err.println("No JDBC connector was found");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
            return;
        }
        try {
            Connection con = dBconnection.getConnection();
            userRepository = new UserRepository(con);
            setManagers(userRepository.getAllManagers());
        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }
    }

    private void setManagers(List<User> allManagers) throws SQLException {
        ScrollPane sp = new ScrollPane();
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        VBox.setVgrow(sp, Priority.ALWAYS);
        Font font = new Font(14);
        for (User m : allManagers){
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.BASELINE_LEFT);
            vbox.setMaxWidth(600);

            Text fio = new Text();
            fio.setText(m.getFullName());
            fio.setFont(font);
            fio.setTextAlignment(TextAlignment.JUSTIFY);
            fio.setFill(Color.color(0.02, 0.7, 0.9));
            fio.setLineSpacing(2.0);
            fio.setOnMouseClicked(event -> {
                try {
                    updateManager(m.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Text phone = new Text();
            phone.setText("Телефон: " + m.getPhone());
            phone.setFont(font);
            phone.setTextAlignment(TextAlignment.JUSTIFY);
            phone.setLineSpacing(2.0);

            Text dateOfBirth = new Text();
            dateOfBirth.setText("Дата рождения: " + m.getDateOfBirth().toString());
            dateOfBirth.setFont(font);
            dateOfBirth.setTextAlignment(TextAlignment.JUSTIFY);
            dateOfBirth.setLineSpacing(2.0);

            Text role = new Text();
            role.setText(m.getRole());
            role.setFont(font);
            role.setTextAlignment(TextAlignment.JUSTIFY);
            role.setLineSpacing(2.0);

            Text validness = new Text();
            if (!m.getValidAccount()) {
                validness.setText("Заблокирован");
                validness.setFill(Color.RED);
            } else {
                validness.setText("Активирован");
                validness.setFill(Color.BLACK);
            }
            validness.setFont(font);
            validness.setTextAlignment(TextAlignment.JUSTIFY);
            validness.setLineSpacing(2.0);
            validness.setOnMouseClicked(event -> {
                try {
                    if (m.getValidAccount()){
                        userRepository.blockAccountByUserName(m.getUserName());
                        validness.setText("Заблокирован");
                        validness.setFill(Color.RED);
                        m.setValidAccount(false);
                    }
                    else {
                        userRepository.activateAccountByUserName(m.getUserName());
                        validness.setText("Активирован");
                        validness.setFill(Color.BLACK);
                        m.setValidAccount(true);

                    }

                } catch (SQLException e) {
                    return;
                }
            });

            vbox.getChildren().add(fio);
            vbox.getChildren().add(role);
            vbox.getChildren().add(phone);
            vbox.getChildren().add(dateOfBirth);
            vbox.getChildren().add(validness);

            vbox.setMargin(fio, new Insets(10, 0,0,0));
            vbox.setMargin(role, new Insets(2, 0,0,0));
            vbox.setMargin(phone, new Insets(2, 0,0,0));
            vbox.setMargin(dateOfBirth, new Insets(2, 0,2,0));

            box.getChildren().add(vbox);
            box.setMargin(vbox, new Insets(5, 0, 0, 20));
        }
        sp.setContent(box);
        ap.setCenter(sp);
    }
    public void setParentAp(BorderPane parentAp) {
        this.parentAp = parentAp;
    }

    public void searchWithFilter(ActionEvent mouseEvent) {
        String filterChoice = (String) this.filter.getValue();
        if (filterChoice == null) return;
        String value = filter_value_field.getText();
        try {
            setManagers(userRepository.getAllManagersWithFilter(filterChoice, value));
        } catch (SQLException e) {
            return;
        }
    }
    public void updateManager(Integer idManager) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/myself-view.fxml"));
        Parent root = fxmlLoader.load();
        parentAp.setCenter(root);
        MyselfController controller = fxmlLoader.getController();
        try {
            controller.updateManager(idManager);
        } catch (SQLException e) {
            return;
        }
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
