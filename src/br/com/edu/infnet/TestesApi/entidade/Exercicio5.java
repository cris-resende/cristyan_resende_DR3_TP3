package br.com.edu.infnet.TestesApi.entidade;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Exercicio5 {

    public static void main(String[] args) {
        System.out.println("--- Executando Exercício 5: POST criando uma nova entidade ---");
        String endpoint = "https://apichallenges.eviltester.com/sim/entities";
        String jsonInputString = "{\"name\": \"aluno\"}";

        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");


            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input);
                outputStream.flush();
            }

            int statusCode = connection.getResponseCode();
            System.out.println("Código de status: " + statusCode);

            InputStream inputStream = null;
            String responseTypeMessage = "Corpo da resposta: ";

            if (statusCode >= 200 && statusCode < 300) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
                responseTypeMessage = "Corpo da resposta: ";
            }

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                StringBuffer responseBody = new StringBuffer();

                while ((line = reader.readLine()) != null) {
                    responseBody.append(line).append("\n");
                }
                reader.close();

                String fullResponseBody = responseBody.toString();
                System.out.println(responseTypeMessage);
                System.out.println(fullResponseBody);

                if (statusCode == HttpURLConnection.HTTP_CREATED || statusCode == HttpURLConnection.HTTP_OK) {
                    int idIndex = fullResponseBody.indexOf("\"id\":");
                    if (idIndex != -1) {
                        int startIndex = idIndex + "\"id\":".length();
                        int endIndex = fullResponseBody.indexOf(",", startIndex);
                        if (endIndex == -1) {
                            endIndex = fullResponseBody.indexOf("}", startIndex);
                        }
                        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                            String idString = fullResponseBody.substring(startIndex, endIndex).trim();
                            System.out.println("ID gerado identificado: " + idString);
                        }
                    }
                }

            } else {
                System.out.println("Nenhum stream de resposta disponível.");
            }

            connection.disconnect();

        } catch (Exception e) {
            System.err.println("Ocorreu um erro no Exercício 5: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
