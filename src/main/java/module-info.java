module org.example.diplom_fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens org.example.diplom_fx.controllers to javafx.fxml;
    exports org.example.diplom_fx.controllers;
    opens org.example.diplom_fx to javafx.fxml;
    exports org.example.diplom_fx;

}