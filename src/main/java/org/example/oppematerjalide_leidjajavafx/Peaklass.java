package org.example.oppematerjalide_leidjajavafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class Peaklass {
    private TextField failiTeeVali;
    private TextArea kusimuseVali;
    private TextArea vastuseAla;
    private TextArea loikudeAla;
    private File valitudFail;

    @Override
    public void start(Stage stage) {
        Label tutvustus = new Label(
                "See programm aitab leida konspektist küsimusega seotud lõigud " +
                "ja koostab nende põhjal lühikese kokkuvõtte."
        );
        tutvustus.setWrapText(true);

        Button valiFailiNupp = new Button("Vali fail");
        failiTeeVali = new TextField();
        failiTeeVali.setEditable(false);

        HBox failiRida = new HBox(10, valiFailiNupp, failiTeeVali);
        HBox.setHgrow(failiTeeVali, Priority.ALWAYS);

        kusimuseVali = new TextArea();
        kusimuseVali.setPromptText("Kirjuta küsimus...");
        kusimuseVali.setPrefRowCount(3);
        kusimuseVali.setWrapText(true);

        Button leiaVastusNupp = new Button("Leia vastus");

        vastuseAla = new TextArea();
        vastuseAla.setEditable(false);
        vastuseAla.setWrapText(true);

        loikudeAla = new TextArea();
        loikudeAla.setEditable(false);
        loikudeAla.setWrapText(true);

        VBox sisu = new VBox(
                10,
                tutvustus,
                failiRida,
                new Label("Küsimus:"),
                kusimuseVali,
                leiaVastusNupp,
                new Label("AI kokkuvõte:"),
                vastuseAla,
                new Label("Kasutatud lõigud:"),
                loikudeAla
        );

        sisu.setPadding(new Insets(15));
        VBox.setVgrow(vastuseAla, Priority.ALWAYS);
        VBox.setVgrow(loikudeAla, Priority.ALWAYS);

        BorderPane juur = new BorderPane();
        juur.setCenter(sisu);

        valiFailiNupp.setOnAction(e -> valiFail(stage));
        leiaVastusNupp.setOnAction(e -> leiaVastus());

        kusimuseVali.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && e.isControlDown()) {
                leiaVastus();
            }
        });

        Scene scene = new Scene(juur, 850, 650);
        stage.setTitle("Õppematerjalide leidja");
        stage.setScene(scene);
        stage.show();
    }

    private void valiFail(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vali .txt või .pdf fail");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("TXT ja PDF failid", "*.txt", "*.pdf")
        );

        File fail = fileChooser.showOpenDialog(stage);

        if (fail != null) {
            valitudFail = fail;
            failiTeeVali.setText(fail.getAbsolutePath());
        }
    }

    private void leiaVastus() {
        if (valitudFail == null) {
            naitaViga("Palun vali enne fail.");
            return;
        }

        String kusimus = kusimuseVali.getText();

        if (kusimus == null || kusimus.isBlank()) {
            naitaViga("Palun sisesta küsimus.");
            return;
        }

        try {
            vastuseAla.setText("Otsin vastust...");
            loikudeAla.clear();

            Faililugeja faililugeja = new Faililugeja();
            Tekstitootleja tekstitootleja = new Tekstitootleja();
            SonadeOtsija otsija = new SonadeOtsija();
            AIVastaja aiVastaja = new AIVastaja();

            String sisu = faililugeja.loeFail(valitudFail.getAbsolutePath());
            List<String> loigud = tekstitootleja.looLõigud(sisu);

            Konspekt konspekt = new Konspekt(
                    valitudFail.getAbsolutePath(),
                    sisu,
                    loigud
            );

            List<String> olulisedSonad = tekstitootleja.eraldaOlulisedSonad(kusimus);
            List<String> parimad = otsija.leiaParimadLoigud(
                    konspekt.getLoigud(),
                    olulisedSonad,
                    5
            );

            if (parimad.isEmpty()) {
                vastuseAla.setText("");
                loikudeAla.setText("");
                naitaViga("Sobivaid lõike ei leitud. Proovi teistsugust küsimust.");
                return;
            }

            String aiVastus = aiVastaja.vastaKusimusele(kusimus, parimad);
            vastuseAla.setText(aiVastus);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parimad.size(); i++) {
                sb.append(i + 1)
                        .append(". lõik:\n")
                        .append(parimad.get(i))
                        .append("\n\n");
            }

            loikudeAla.setText(sb.toString());

        } catch (Exception e) {
            vastuseAla.setText("");
            naitaViga("Tekkis viga: " + e.getMessage());
        }
    }

    private void naitaViga(String tekst) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Viga");
        alert.setHeaderText(null);
        alert.setContentText(tekst);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
