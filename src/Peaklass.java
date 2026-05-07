import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Peaklass {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Tere! Programm loeb sisse konspekti ja leiab sealt sinu küsimusele" +
                " vastavaks kõige asjakohasemad lõigud konspektist.");

        // --- Faili sisestamine ---
        System.out.print("Sisesta failitee: ");
        String failitee = sc.nextLine();

        Faililugeja faililugeja = new Faililugeja();
        Tekstitootleja tekstitootleja = new Tekstitootleja();
        SonadeOtsija otsija = new SonadeOtsija();
        String sisu;
        List<String> loigud;

        try {
            sisu = faililugeja.loeFail(failitee);
            loigud = tekstitootleja.looLõigud(sisu);
        } catch (FailiViga e) {
            System.out.println("Viga faili lugemisel: " + e.getMessage());
            return;
        }

        System.out.println("Lõikude arv: " + loigud.size());
        Konspekt konspekt = new Konspekt(failitee, sisu, loigud);

        // --- Küsimuse sisestamine ---
        System.out.print("Sisestage küsimus: ");
        String kusimus = sc.nextLine();

        List<String> olulisedSonad;
        List<String> parimad;

        try {
            olulisedSonad = tekstitootleja.eraldaOlulisedSonad(kusimus);
            parimad = otsija.leiaParimadLoigud(konspekt.getLoigud(), olulisedSonad, 3);
        } catch (KasutajaViga e) {
            System.out.println("Viga: " + e.getMessage());
            return;
        }

        System.out.println("Olulised sõnad: " + olulisedSonad);

        // --- Lõikude kuvamine ---
        for (String loik : parimad) {
            String[] sonad = loik.split("\\s+");
            for (int i = 0; i < sonad.length; i++) {
                System.out.print(sonad[i] + " ");
                if ((i + 1) % 10 == 0) System.out.println();
            }
            System.out.println();
            System.out.println("------------------------");
        }

        // --- AI vastus ---
        String aiVastus;
        try {
            AIKlient aiKlient = new AIKlient();
            aiVastus = aiKlient.kusiVastus(kusimus, parimad);
            System.out.println("\nAI vastus:\n" + aiVastus);
        } catch (AIViga e) {
            aiVastus = "AI viga: " + e.getMessage();
            System.out.println(aiVastus);
        }

        // --- Logimine ---
        try {
            kirjutaLogi(failitee, kusimus, olulisedSonad, parimad.size(), aiVastus);
        } catch (IOException e) {
            System.out.println("Hoiatus: logi kirjutamine ebaõnnestus: " + e.getMessage());
        }
    }

    private static void kirjutaLogi(String failitee, String kusimus,
            List<String> olulisedSonad, int leitudLoike, String aiVastus) throws IOException {

        String aeg = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        try (PrintWriter pw = new PrintWriter(new FileWriter("logi.txt", true))) {
            pw.println("Aeg: " + aeg);
            pw.println("Fail: " + failitee);
            pw.println("Küsimus: " + kusimus);
            pw.println("Olulised sõnad: " + olulisedSonad);
            pw.println("Leitud lõike: " + leitudLoike);
            pw.println("AI vastus: " + aiVastus);
            pw.println("----------------------------------------");
        }
    }
}
