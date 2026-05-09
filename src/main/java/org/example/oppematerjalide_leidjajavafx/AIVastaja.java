package org.example.oppematerjalide_leidjajavafx;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import java.util.List;

public class AIVastaja {

    private OpenAIClient klient;

    public AIVastaja() {
        String apiKey = System.getenv("OPENAI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            klient = null;
        } else {
            klient = OpenAIOkHttpClient.fromEnv();

        }
    }

    public String vastaKusimusele(String kusimus, List<String> parimadLoigud) {
        if (parimadLoigud == null || parimadLoigud.isEmpty()) {
            return "Ma ei leidnud konspektist piisavalt infot, et küsimusele vastata.";
        }

        if (klient == null) {

        }

        String kontekst = looKontekst(parimadLoigud);

        String prompt = """
                Sa oled õpetaja, kes aitab tudengil konspektist aru saada.

                Vasta ainult etteantud konspekti lõikude põhjal.
                Kui konspekti lõikudes ei ole piisavalt infot, siis ütle:
                "Konspekti põhjal ei saa sellele kindlalt vastata."

                Vasta eesti keeles.
                Vasta selgelt ja tudengile arusaadavalt.
                Ära mõtle fakte ise juurde.

                KÜSIMUS:
                %s

                KONSPEKTI LÕIGUD:
                %s
                """.formatted(kusimus, kontekst);

        try {
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_5_2)
                    .addUserMessage(prompt)
                    .build();

            ChatCompletion vastus = klient.chat().completions().create(params);

            return vastus.choices()
                    .getFirst()
                    .message()
                    .content()
                    .orElse("AI ei tagastanud vastust.");

        } catch (Exception e) {
            return "AI vastuse genereerimisel tekkis viga: " + e.getMessage();
        }
    }

    private String looKontekst(List<String> loigud) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < loigud.size(); i++) {
            sb.append("Lõik ")
                    .append(i + 1)
                    .append(":\n")
                    .append(loigud.get(i))
                    .append("\n\n");
        }

        return sb.toString();
    }

    private String looDemoVastus(String kusimus, List<String> parimadLoigud) {
        StringBuilder sb = new StringBuilder();
        sb.append("OPENAI_API_KEY puudub.\n");
        sb.append("AI kokkuvõtet ei saa hetkel genereerida. Väjastan asjakohased lõigud.\n\n");

        sb.append("Küsimus:\n");
        sb.append(kusimus).append("\n\n");

        sb.append("Programm leidis konspektist järgmised kõige asjakohasemad lõigud:\n\n");

        for (int i = 0; i < parimadLoigud.size(); i++) {
            sb.append(i + 1).append(". lõik:\n");
            sb.append(parimadLoigud.get(i)).append("\n\n");
        }

        return sb.toString();
    }
}
