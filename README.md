AI-funktsiooni kasutamiseks peab arvutis olema määratud keskkonnamuutuja OPENAI_API_KEY.

Windows PowerShellis:
setx OPENAI_API_KEY "sinu_api_key"

Pärast seda tuleb IntelliJ uuesti avada.

Kui API key puudub, töötab programm piiratud režiimis: programm loeb konspekti sisse ja leiab küsimusele kõige asjakohasemad lõigud, kuid ei genereeri OpenAI vastust. 
