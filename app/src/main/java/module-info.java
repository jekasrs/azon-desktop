module seller.group.azon {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens seller.group.azon to javafx.fxml;
    exports seller.group.azon;

    exports seller.group.azon.controller;
    opens seller.group.azon.controller to javafx.fxml;

    exports seller.group.azon.entity;
    opens seller.group.azon.entity to javafx.fxml;

    exports seller.group.azon.controller.Goods;
    opens seller.group.azon.controller.Goods to javafx.fxml;

    exports seller.group.azon.controller.Seller;
    opens seller.group.azon.controller.Seller to javafx.fxml;

    exports seller.group.azon.controller.Manager;
    opens seller.group.azon.controller.Manager to javafx.fxml;

    exports seller.group.azon.controller.Invoice;
    opens seller.group.azon.controller.Invoice to javafx.fxml;

    exports seller.group.azon.controller.Order;
    opens seller.group.azon.controller.Order to javafx.fxml;

    exports seller.group.azon.controller.Report;
    opens seller.group.azon.controller.Report to javafx.fxml;
}

