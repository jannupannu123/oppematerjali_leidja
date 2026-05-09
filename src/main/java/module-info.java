module org.example.oppematerjalide_leidjajavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.apache.pdfbox;
    requires org.apache.commons.logging;
    requires openai.java.core;
    requires openai.java.client.okhttp;

    opens org.example.oppematerjalide_leidjajavafx to javafx.fxml;
    exports org.example.oppematerjalide_leidjajavafx;
}