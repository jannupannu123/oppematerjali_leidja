package org.example.oppematerjalide_leidjajavafx;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RakenduseLogija {

    public void kirjutaLogi(
            String failinimi,
            String kusimus,
            List<String> loigud,
            String vastus
    ) {

        try (PrintWriter pw =
                     new PrintWriter(new FileWriter("logi.txt", true))) {

            String aeg = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            pw.println("Aeg: " + aeg);
            pw.println("Fail: " + failinimi);
            pw.println("Küsimus: " + kusimus);
            pw.println("Leitud lõike: " + loigud.size());
            pw.println("AI vastus: " + vastus);
            pw.println("--------------------------------");

        } catch (IOException e) {
            System.out.println("Logi kirjutamine ebaõnnestus.");
        }
    }
}
