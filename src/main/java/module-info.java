module MedicineStore.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    opens AmaliaV05 to javafx.fxml;
    opens domain to com.google.gson;
    exports AmaliaV05;
    exports service;
    exports domain;
    exports repository;
}