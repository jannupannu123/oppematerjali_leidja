import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.util.Scanner;

public class Faililugeja {
    public String loeTxtFail(String failitee) throws IOException {
        File fail = new File(failitee);
        StringBuilder sb = new StringBuilder();

        try (java.util.Scanner failiScanner = new java.util.Scanner(fail)) {
            while (failiScanner.hasNextLine()) {
                sb.append(failiScanner.nextLine()).append("\n");
            }
        }

        return sb.toString();
    }

    public String loePdfFail(String failitee) throws IOException {
        File fail = new File(failitee);

        try (PDDocument dokument = Loader.loadPDF(fail)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(dokument);
        }
    }

    public String loeFail(String failitee) throws IOException {
        if (failitee.toLowerCase().endsWith(".txt")) {
            return loeTxtFail(failitee);
        } else if (failitee.toLowerCase().endsWith(".pdf")) {
            return loePdfFail(failitee);
        } else {
            throw new IllegalArgumentException("Toetatud on ainult .txt ja .pdf failid.");
        }
    }
}
