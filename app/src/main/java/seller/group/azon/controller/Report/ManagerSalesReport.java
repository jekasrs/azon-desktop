package seller.group.azon.controller.Report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import seller.group.azon.backend.ControlStage;
import seller.group.azon.backend.DBconnection;
import seller.group.azon.controller.Controller;
import seller.group.azon.entity.Goods;
import seller.group.azon.entity.User;
import seller.group.azon.repos.UserRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class ManagerSalesReport implements Controller {
    @FXML
    BorderPane ap;

    @FXML
    DatePicker date_beg_field;
    @FXML
    DatePicker date_end_field;
    @FXML
    TableView manager_table;
    @FXML
    Text request_message;
    @FXML
    TextField filter;

    UserRepository userRepository;

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
            userRepository = new UserRepository(con);
        } catch (SQLException e) {
            System.err.println("Bad happened with connection. It is closed");
            ControlStage.changeStage(getClass(), ap, "/fxml/error-view.fxml");
        }
    }

    public void fillTable(List<User> managers) {
        TableColumn fioCol = new TableColumn("ФИО");
        TableColumn phoneCol = new TableColumn("Телефон");
        TableColumn amountCol = new TableColumn("Кол-во заказов");
        TableColumn sumCol = new TableColumn("Сумма заказов");

        fioCol.setMinWidth(200);
        phoneCol.setMinWidth(200);
        amountCol.setMinWidth(200);
        sumCol.setMinWidth(200);

        manager_table.setColumnResizePolicy((param) -> true);
        manager_table.setMaxHeight(300);
        manager_table.setMaxWidth(800);
        manager_table.getColumns().addAll(fioCol, phoneCol, amountCol, sumCol);

        final ObservableList<User> data = FXCollections.observableArrayList(managers);

        fioCol.setCellValueFactory(
                new PropertyValueFactory<Goods, String>("fullName")
        );
        phoneCol.setCellValueFactory(
                new PropertyValueFactory<Goods, String>("phone")
        );
        amountCol.setCellValueFactory(
                new PropertyValueFactory<Goods, Integer>("amount")
        );
        sumCol.setCellValueFactory(
                new PropertyValueFactory<Goods, Double>("totalSum")
        );

        manager_table.setItems(data);
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
                List<User> managersAnalyse = userRepository.getAnalyzeOfSalesManager(dateBeg, dateEnd);
                fillTable(managersAnalyse);
            } else {
                String filterValue = (filter.getText());
                if (!verifyFio(filterValue))
                    return;
                User analyzeOfOneGoods = userRepository.getAnalyzeOfSalesManager(dateBeg, dateEnd, filterValue);
                List<User> managersAnalyse = new LinkedList<>();
                managersAnalyse.add(analyzeOfOneGoods);
                fillTable(managersAnalyse);
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

    private boolean verifyFio(String fio) throws SQLException {
        List<String> fioes = userRepository.getFIOofSalesManagers();
        if (!fioes.contains(fio)) {
            request_message.setText("Недействительное ФИО менеджера");
            return false;
        }
        return true;
    }

}