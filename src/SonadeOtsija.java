import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SonadeOtsija {
    public List<String> leiaParimadLoigud(List<String> loigud, List<String> olulisedSonad, int mituTulemust) {
        List<LoiguTulemus> tulemused = new ArrayList<>();

        for (String loik : loigud) {
            int skoor = arvutaSkoor(loik, olulisedSonad);
            tulemused.add(new LoiguTulemus(loik, skoor));
        }
        System.out.println("---- SKOORID ----");


        //võtab iga tulemuse skoori ja sorteerib need ära kasvavas järjekorras
        tulemused.sort(Comparator.comparingInt(LoiguTulemus::getSkoor).reversed());

        // väljastab ainult nende lõikude skoorid mis on suurem kui 0 ning kui mitte üheski lõigus
        // ei ole relevantset sõna siis väljastab vastava teate ekraanile
        boolean leidusSobiv = false;
        int sobivateLoikudeArv = 0;

// loeme sobivad lõigud
        for (LoiguTulemus t : tulemused) {
            if (t.getSkoor() > 0) {
                sobivateLoikudeArv++;
            }
        }

// kui ei leitud ühtegi sobivat lõiku
        if (sobivateLoikudeArv == 0) {
            System.out.println("Ükski lõik ei sisaldanud sinu sisestatud sõnu.");
        } else {
            // prindime arvu
            System.out.println("Sobivaid lõike leiti: " + sobivateLoikudeArv);

            // prindime skoorid
            for (LoiguTulemus t : tulemused) {
                if (t.getSkoor() > 0) {
                    System.out.println("Skoor: " + t.getSkoor());
                    leidusSobiv = true;
                }
            }
        }

        //lisab nii mitu lõiku lisi kui on ette antud mituTulemust parameetriga
        List<String> parimad = new ArrayList<>();
        for (LoiguTulemus tulemus : tulemused) {
            if (tulemus.getSkoor() > 0 && parimad.size() < mituTulemust) {
                parimad.add(tulemus.getLoik());
            }
            if (parimad.size() == mituTulemust) {
                break;
            }
        }


        return parimad;
    }

    private String leiaTüvi(String sona) {
        sona = sona.toLowerCase();

        String[] lopud = {
                "ga", "s", "le", "st", "lt", "ni", "na", "ta",
                "ks", "l", "le", "ga", "te", "de",
                "id", "ud", "ed", "ad", "t", "d"
        };

        for (String lopp : lopud) {
            if (sona.endsWith(lopp) && (sona.length() > lopp.length() + 2)) {
                return sona.substring(0, sona.length() - lopp.length());
            }
        }

        return sona;
    }

    /**
     * Teeb lõigu massiiviks, ning siis tsüklitega hakkab tsükliga massivi läbi
     * käia ja kontorllima, kas sõnatüvi on võrdne olulise(otsitava) sõna tüvega.
     * Iga leitud sobiva sõna puhul tõuseb lõigu skoor ühe punkti võrra
     * @param loik
     * @param olulisedSonad
     * @return tagastab leitud lõigu skoori int arvuna
     */
    private int arvutaSkoor(String loik, List<String> olulisedSonad) {
        int skoor = 0;
        String[] sonad = loik.toLowerCase().split("\\W+");

        for (String s : sonad) {
            String sonaTüvi = leiaTüvi(s);

            for (String oluline : olulisedSonad) {
                String olulineTüvi = leiaTüvi(oluline);

                if (sonaTüvi.equals(olulineTüvi)) {
                    skoor++;
                }
            }
        }

        return skoor;
    }


}
