module com.checkers.warcaby {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.checkers.warcaby to javafx.fxml;
    exports com.checkers.warcaby;
}