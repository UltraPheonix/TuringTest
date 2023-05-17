module com.gonzaga.turingtest {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gonzaga.turingtest to javafx.fxml;
    exports com.gonzaga.turingtest;
}