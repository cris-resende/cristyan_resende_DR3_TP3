package br.com.edu.infnet.TestesApi.entidade;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Exercicio11 {

    public static void main(String[] args) {
        System.out.println("--- Executando Exercício 11: OPTIONS com verificação de métodos ---");
        String endpoint = "https://apichallenges.eviltester.com/sim/entities";

        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("OPTIONS");

            int statusCode = connection.getResponseCode();
            System.out.println("Código de status do OPTIONS: " + statusCode);

            System.out.println("\n--- Cabeçalhos da Resposta ---");
            Map<String, java.util.List<String>> headers = connection.getHeaderFields();
            boolean allowHeaderFound = false;
            for (Map.Entry<String, java.util.List<String>> entry : headers.entrySet()) {
                if (entry.getKey() != null) {
                    System.out.println(entry.getKey() + ": " + String.join(", ", entry.getValue()));
                    if (entry.getKey().equalsIgnoreCase("Allow")) {
                        System.out.println("\n--- Métodos HTTP permitidos (Cabeçalho Allow) ---");
                        for (String method : entry.getValue()) {
                            System.out.println("  - " + method);
                        }
                        allowHeaderFound = true;
                    }
                }
            }

            if (!allowHeaderFound) {
                System.out.println("Cabeçalho 'Allow' não encontrado na resposta.");
            }

            InputStream inputStream = (statusCode >= 200 && statusCode < 300) ?
                    connection.getInputStream() :
                    connection.getErrorStream();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                StringBuffer responseBody = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    responseBody.append(line).append("\n");
                }
                reader.close();
                if (responseBody.length() > 0) {
                    System.out.println("\n--- Corpo da resposta ---");
                    System.out.println(responseBody.toString());
                }
            }

            connection.disconnect();

        } catch (Exception e) {
            System.err.println("Ocorreu um erro no Exercício 11: " + e.getMessage());
            e.printStackTrace();
        }
    }
}