package seller.group.azon.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import seller.group.azon.backend.ControlStage;
import seller.group.azon.backend.DBconnection;
import seller.group.azon.backend.Password;
import seller.group.azon.entity.User;
import seller.group.azon.repos.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class LoginController implements Controller {

    @FXML private BorderPane ap;

    @FXML
    TextField username_field;

    @FXML
    PasswordField password_field;

    @FXML
    Text request_message;

    User user;
    private UserRepository userRepository;

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
            userRepository = new UserRepository(con);
            Map<String, String> check = userRepository.getAllAccounts();
            if (check.isEmpty()){
                Properties props = new Properties();
                try(InputStream in = Files.newInputStream(Paths.get("/Users/evgenijsmirnov/Desktop/base/app/src/main/resources/database.properties"))){
                    props.load(in);
                }
                try {
                    String password = props.getProperty("adminPass");
                    password = Password.doHash(password);

                    Statement statement = con.createStatement();
                    String query = "EXEC registerManager '" + props.getProperty("adminLogin") + "', '" + password + "', " + 3 + ", '" + props.getProperty("adminFio") + "', '" + LocalDate.of(2001, 12, 10)+ "', '" + props.getProperty("adminPhone") + "'";
                    statement.execute(query);
                } catch (SQLException e) {
                    request_message.setText("Неизвестная ошибка.\nПовторите операцию.");
                    return;
                }
            }

        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }


    }

    @FXML
    protected void handleSignIn(ActionEvent event) throws IOException {
        String userName = username_field.getText();
        String password = password_field.getText();

        try {
            if (!verifyUsernameAndPassword(userName, password)) return;
            if (!userRepository.verifyUser(userName, password)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Unknown user!");
                alert.setContentText("Invalid login or password.");
                alert.show();
                return;
            }
            user = userRepository.getUserByLogin(userName);
            if (user == null) {
                request_message.setText("Пользователь не найден");
                return;
            }
            if (Objects.equals(user.getRole(), User.admin)){
                AdminController adminController = (AdminController) ControlStage.changeStage(getClass(), ap, "/fxml/admin-view.fxml");
                adminController.setUser(user);
            }
            else if (Objects.equals(user.getRole(), User.saleManager)){
                AdminController adminController = (AdminController) ControlStage.changeStage(getClass(), ap, "/fxml/admin-view.fxml");
                adminController.setUser(user);
            }
            else if (Objects.equals(user.getRole(), User.purchaseManager)){
                AdminController adminController = (AdminController) ControlStage.changeStage(getClass(), ap, "/fxml/admin-view.fxml");
                adminController.setUser(user);
            }
        }
        catch (SQLException e){
            System.err.println("Bad happened with connection. It is closed");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }

    }

    private boolean verifyUsernameAndPassword(String username, String password) {
        if (username == null || username.isBlank()){
            request_message.setText("Недействительный логин");
            return false;
        }
        if (username.length() < 3){
            request_message.setText("Слишком короткий логин: минимальная длина 3");
            return false;
        }
        if (username.length() > 20){
            request_message.setText("Слишком длинный логин: максимальная длина 20");
            return false;
        }
        if (password == null || password.isBlank()){
            request_message.setText("Недействительный пароль");
            return false;
        }
        if (password.length() < 3){
            request_message.setText("Слишком короткий пароль: минимальная длина 3");
            return false;
        }
        return true;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void clearHandler(){
        request_message.setText("");
    }
}
