import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Faililugeja {

    public String loeTxtFail(String failitee) throws FailiViga {
        File fail = new File(failitee);
        if (!fail.exists()) {
            throw new FailiViga("Faili ei leitud: " + failitee);
        }
        StringBuilder sb = new StringBuilder();
        try (java.util.Scanner failiScanner = new java.util.Scanner(fail)) {
            while (failiScanner.hasNextLine()) {
                sb.append(failiScanner.nextLine()).append("\n");
            }
        } catch (IOException e) {
            throw new FailiViga("TXT faili lugemisel tekkis viga: " + e.getMessage());
        }
        return "txt" + sb;
    }

    public String loePdfFail(String failitee) throws FailiViga {
        File fail = new File(failitee);
        if (!fail.exists()) {
            throw new FailiViga("Faili ei leitud: " + failitee);
        }
        try (PDDocument dokument = Loader.loadPDF(fail)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return "pdf" + stripper.getText(dokument);
        } catch (IOException e) {
            throw new FailiViga("PDF faili lugemisel tekkis viga: " + e.getMessage());
        }
    }

    public String loeFail(String failitee) throws FailiViga {
        if (failitee == null || failitee.isBlank()) {
            throw new FailiViga("Failitee ei tohi olla tühi.");
        }
        if (failitee.toLowerCase().endsWith(".txt")) {
            return loeTxtFail(failitee);
        } else if (failitee.toLowerCase().endsWith(".pdf")) {
            return loePdfFail(failitee);
        } else {
            throw new FailiViga("Toetatud on ainult .txt ja .pdf failid. Sisestasid: " + failitee);
        }
    }
}
