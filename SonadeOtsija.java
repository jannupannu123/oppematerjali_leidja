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
        //võtab iga tulemuse skoori ja sorteerib need ära kasvavas järjekorras
        tulemused.sort(Comparator.comparingInt(LoiguTulemus::getSkoor).reversed());

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
        System.out.println("---- SKOORID ----");
        for (LoiguTulemus t : tulemused) {
            System.out.println("Skoor: " + t.getSkoor());
        }

        return parimad;
    }
    private int arvutaSkoor(String loik, List<String> olulisedSonad) {
        int skoor = 0;
        String vaikeloik = loik.toLowerCase();

        for (String sona : olulisedSonad) {
            if (vaikeloik.contains(sona)) {
                skoor++;
            }
        }

        return skoor;
    }


}
