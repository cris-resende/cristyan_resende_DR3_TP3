package br.com.edu.infnet.TestesApi.entidade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Exercicio1 {
    public static void main(String[] args) {
        System.out.println("--- Executando Exercício 1: GET simples de todas as entidades ---");
        String endpoint = "https://apichallenges.eviltester.com/sim/entities";

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
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer responseBody = new StringBuffer();

                while ((line = reader.readLine()) != null) {
                    responseBody.append(line).append("\n");
                }
                reader.close();

                System.out.println(responseTypeMessage);
                System.out.println(responseBody.toString());
            } else {
                System.out.println("Nenhum stream de resposta disponível.");
            }

            connection.disconnect();

        } catch (Exception e) {
            System.err.println("Ocorreu um erro no Exercício 1: " + e.getMessage());
            e.printStackTrace();
        }
    }
}