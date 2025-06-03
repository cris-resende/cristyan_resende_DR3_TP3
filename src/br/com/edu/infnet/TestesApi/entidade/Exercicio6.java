package br.com.edu.infnet.TestesApi.entidade;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Exercicio6 {

    public static void main(String[] args) {
        System.out.println("--- Executando Exercício 6: GET da entidade recém-criada ---");
        int createdId = 11;
        String endpoint = "https://apichallenges.eviltester.com/sim/entities/" + createdId;

        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int statusCode = connection.getResponseCode();
            System.out.println("Código de status: " + statusCode);

            InputStream inputStream = null;
            String responseTypeMessage = "Corpo da resposta: ";

            if (statusCode >= 200 && statusCode < 300) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
                responseTypeMessage = "Corpo da resposta (erro): ";
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

                if (statusCode == HttpURLConnection.HTTP_OK) {
                    if (fullResponseBody.contains("\"id\":" + createdId) && fullResponseBody.contains("\"name\":\"aluno\"")) {
                        System.out.println("Verificação: Entidade com ID " + createdId + " e nome 'aluno' encontrada e conteúdo correto.");
                    } else {
                        System.out.println("Verificação: Conteúdo da entidade com ID " + createdId + " não corresponde ao esperado.");
                    }
                }

            } else {
                System.out.println("Nenhum stream de resposta disponível.");
            }

            connection.disconnect();

        } catch (Exception e) {
            System.err.println("Ocorreu um erro no Exercício 6: " + e.getMessage());
            e.printStackTrace();
        }
    }
}