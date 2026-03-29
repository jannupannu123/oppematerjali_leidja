import java.util.ArrayList;
import java.util.List;

public class Tekstitootleja {

    /**
     * Jagab teksti lõikudeks eeldusega, et iga lõigu vahel on tühi rida
     * @param tekst võtab parameertiks kogu konspekti sisu sõnena
     * @return tagastab arraylisti koos lõikudega
     */
    public List<String> jagaLõikudeks(String tekst) {
        String[] osad = tekst.split("\\n\\s*\\n");//iga lõigu vahel on tühi rida
        List<String> lõigud = new ArrayList<>();

        for (String osa : osad) {
            String puhatatud = osa.trim();
            if (!puhatatud.isEmpty()) {
                lõigud.add(puhatatud);
            }
        }
        return lõigud;
    }

    /**
     * Eralab küsimusest siesõnad/teemaga mitteseonduva ja tagastab ainult "olulised sõnad"
     *
     * @param kusimus võtab parameetriks küsimuse sõnena
     * @return
     */
    public List<String> eraldaOlulisedSonad(String kusimus) {
        kusimus = kusimus.toLowerCase();
        //eemaldame kõik sümbolid, mis ei ole tähed või numbrid
        kusimus = kusimus.replaceAll("[^a-zA-ZõäöüšžÕÄÖÜŠŽ0-9 ]", "");
        //tühikute koha pealt tükeldab ära
        String[] sonad = kusimus.split("\\s+");
        List<String> olulised = new ArrayList<>();

        for (String sona : sonad) {
            if (!sona.equals("mis") &&
                    !sona.equals("on") &&
                    !sona.equals("kuidas") &&
                    !sona.equals("selgita") &&
                    !sona.equals("too") &&
                    !sona.equals("ja") &&
                    !sona.equals("miks")&&
                    !sona.isBlank()) {
                olulised.add(sona);
            }
        }
        return olulised;
    }
}
