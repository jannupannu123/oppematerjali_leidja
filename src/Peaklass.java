import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Peaklass {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Tere! Programm loeb sisse konspekti ja leiab sealt sinu küsimusele" +
                " vatsueks kõige asjakohasemad lõigud konspektist");
        System.out.print("Sisesta failitee: ");
        String failitee = sc.nextLine();

        Faililugeja faililugeja = new Faililugeja();
        String sisu = faililugeja.loeFail(failitee);

        Tekstitootleja tekstitootleja = new Tekstitootleja();
        List<String> loigud = tekstitootleja.looLõigud(sisu);
        System.out.println("Lõikude arv: " + loigud.size());

        Konspekt konspekt = new Konspekt(failitee, sisu, loigud);

        System.out.println("Sisestage küsimus: ");
        String kusimus = sc.nextLine();
        List<String> olulisedSonad = tekstitootleja.eraldaOlulisedSonad(kusimus);



        SonadeOtsija otsija = new SonadeOtsija();
        List<String> parimad = otsija.leiaParimadLoigud(konspekt.getLoigud(), olulisedSonad, 3);
        System.out.println("Olulised sõnad: " + olulisedSonad);

        // prindib teksti sedasi ekraanile et oleks mõnusam lugeda
        for (String loik : parimad) {
            //System.out.println(loik);
            String[] sonad = loik.split("\\s+");
            for (int i = 0; i < sonad.length; i++) {
                System.out.print(sonad[i] + " ");
                if ((i + 1) % 10 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
            System.out.println("------------------------");
        }

    }
}
