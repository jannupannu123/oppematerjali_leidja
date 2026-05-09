package org.example.oppematerjalide_leidjajavafx;

import java.util.ArrayList;
import java.util.List;

public class Tekstitootleja {


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
                    !sona.equals("miks") &&
                    !sona.isBlank()) {
                olulised.add(sona);
            }
        }
        return olulised;
    }

    //töötab ainult txt faili puhul, sest pdfis ei ole sellised reavhaetused

    /**
     * Ühtlustab teksti (nomraliseerib), et hiljme oleks lihtam
     * lõigu piirid üles leida ja õigete kohtade pealt poolitada
     * @param tekst Kogu konspekti sisu sõnena
     * @return Puhastatud tekst sõnena
     */
    private String normaliseeriTxtTekst(String tekst) {
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

    /**
     * Jagab normaliseertud teksti lõikudeks
     * @param tekst normalisseritud tekst, kus saame eelsada, et iga kaks reavaehtust
     *              või rohkem tähendab uut lõiku
     * @return List, kus on lõigud
     */
    public List<String> jagaTxtLõikudeks(String tekst) {
        List<String> loigud = new ArrayList<>();

        String normaliseeritud = normaliseeriTxtTekst(tekst);

        //nüüd normaliseeeritud teksti, saan õigesti lõikudeks jagada
        String[] osad = normaliseeritud.split("\n\n+");
        for (String osa : osad) {
            osa = osa.trim();
            loigud.add(osa);
        }

        return loigud;
    }

    /**
     * Poolitab liiga pikad lõigud väiksemateks osadeks.
     * Üritab poolitada lausete kaupa, et mõte ei katkeks suvalise koha pealt.
     * @param loigud lõigud, mida vajadusel väiksemaks jagada
     * @return lõigud, kus ükski lõik ei ole üldjuhul üle 500 tähemärgi
     */
    private List<String> poolitaPikadLoigud(List<String> loigud) {
        List<String> tulemus = new ArrayList<>();

        for (String loik : loigud) {
            if (loik == null || loik.isBlank()) {
                continue;
            }

            loik = loik.trim();

            if (loik.length() <= 500) {
                tulemus.add(loik);
            } else {
                String[] laused = loik.split("(?<=[.!?])\\s+");

                StringBuilder uusLoik = new StringBuilder();

                for (String lause : laused) {
                    lause = lause.trim();

                    if (lause.isBlank()) {
                        continue;
                    }

                    if (uusLoik.length() + lause.length() > 500) {
                        if (!uusLoik.isEmpty()) {
                            tulemus.add(uusLoik.toString().trim());
                            uusLoik = new StringBuilder();
                        }
                    }

                    uusLoik.append(lause).append(" ");
                }

                if (!uusLoik.isEmpty()) {
                    tulemus.add(uusLoik.toString().trim());
                }
            }
        }

        return tulemus;
    }

    /**
     * Poolitab PDF-ist saadud kogu teksti lõikudeks
     * @param tekst kogu PDF-ist saadud tekst sõnena
     * @return väiksemateks lõikudeks jagatud tekst
     */
    private List<String> poolitaPDFLoigud(String tekst) {
        List<String> suurTekst = new ArrayList<>();
        suurTekst.add(tekst);
        return poolitaPikadLoigud(suurTekst);
    }

    public ArrayList<String> looLõigud(String sisu) {
        List<String> lõigud;

        if (sisu.startsWith("txt")) {
            String tekst = sisu.substring(3);//eemaldame "txt"
            lõigud = jagaTxtLõikudeks(tekst);

            lõigud = poolitaPikadLoigud(lõigud);
        } else if (sisu.startsWith("pdf")) {
            String tekst = sisu.substring(3); // eemaldame "pdf"

            lõigud = poolitaPDFLoigud(tekst);

        } else {
            throw new IllegalArgumentException("Tundmatu failitüüp");
        }

        return new ArrayList<>(lõigud);
    }
}
