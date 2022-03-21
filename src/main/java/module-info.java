module com.checkers.warcaby {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.checkers.warcaby to javafx.fxml;
    exports com.checkers.warcaby;
}