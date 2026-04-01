import java.util.ArrayList;
import java.util.List;

public class Tekstitootleja {

    /**
     * Jagab teksti lõikudeks eeldusega, et iga lõigu vahel on tühi rida
     * @param tekst võtab parameertiks kogu konspekti sisu sõnena
     * @return tagastab arraylisti koos lõikudega
     */

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
    private String normaliseeriTekst(String tekst) {
        // Ühtlustame reavahetused (windowsil on \r\n või mõnel vanal süsteem võib olla \r)
        tekst = tekst.replace("\r\n", "\n");
        tekst = tekst.replace("\r", "\n");

        // Eemaldame poolitused rea lõpus: näiteks "ettevõt-\nte" -> "ettevõte"
        tekst = tekst.replaceAll("-\\n", "");

        // Asendame üksikud reavahetused tühikuga, aga jätame alles kohad, kus on mitu tühja rida (päris lõigupiirid)
        tekst = tekst.replaceAll("(?<!\\n)\\n(?!\\n)", " ");

        // Mitu tühja rida järjest on üks lõigupiir
        tekst = tekst.replaceAll("\\n{2,}", "\n\n");

        // Mitu tühikut järjest on üks tühik
        tekst = tekst.replaceAll("[ \\t]+", " ");

        return tekst.trim();
    }
    private List<String> jagaLõikudeks(String tekst) {
        List<String> loigud = new ArrayList<>();

        String normaliseeritud = normaliseeriTekst(tekst);

        //nüüd normaliseeeritud teksti, saan õigesti lõikudeks jagada
        String[] osad = normaliseeritud.split("\\n\\n+");

        for (String osa : osad) {
            osa = osa.trim();
            loigud.add(osa);
        }

        return loigud;
    }
    /*private List<String> poolitaLiigaPikadLoigud(List<String> loigud) {
        List<String> tulemus = new ArrayList<>();

        for (String loik : loigud) {
            if (loik.length() <= 500) {
                tulemus.add(loik);
            } else {
                String[] laused = loik.split("(?<=[.!?])\\s+");

                StringBuilder uusLoik = new StringBuilder();

                for (String lause : laused) {
                    if (uusLoik.length() + lause.length() > 500) {
                        tulemus.add(uusLoik.toString().trim());
                        uusLoik = new StringBuilder();
                    }
                    uusLoik.append(lause).append(" ");
                }

                if (!uusLoik.isEmpty()) {
                    tulemus.add(uusLoik.toString().trim());
                }
            }
        }

        return tulemus;
    }*/
    /*public ArrayList<String> looLõigud(String sisu){
        List<String> lõigud = jagaLõikudeks(sisu);
        List<String> õigePikkusegaLõigud = poolitaLiigaPikadLoigud(lõigud);
        return new ArrayList<>(õigePikkusegaLõigud);
    }*/

}
