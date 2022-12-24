package seller.group.azon.controller.Report;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ChooseReport {

    private BorderPane parentAp;

    @FXML
    BorderPane ap;

    public void startReportGoods(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/reports/report-goods-view.fxml"));
        Parent root = fxmlLoader.load();
        parentAp.setCenter(root);
    }

    public void setParentAp(BorderPane parentAp) {
        this.parentAp = parentAp;
    }

    public void startReportManagersSales(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/reports/report-smanagers-view.fxml"));
        Parent root = fxmlLoader.load();
        parentAp.setCenter(root);
    }

    public void startReportManagersPurchase(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/reports/report-pmanagers-view.fxml"));
        Parent root = fxmlLoader.load();
        parentAp.setCenter(root);
    }
}