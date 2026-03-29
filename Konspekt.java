import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Konspekt {
    private String failinimi;
    private String sisu;
    private List<String> loigud;

    public Konspekt(String failinimi, String sisu, List<String> loigud) {
        this.failinimi = failinimi;
        this.sisu = sisu;
        this.loigud = loigud;
    }

    public String getFailinimi() {
        return failinimi;
    }

    public String getSisu() {
        return sisu;
    }

    public List<String> getLoigud() {
        return loigud;
    }

    public void setLoigud(List<String> loigud) {
        this.loigud = loigud;
    }
}

