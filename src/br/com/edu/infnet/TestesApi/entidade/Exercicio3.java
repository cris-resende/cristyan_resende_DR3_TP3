package br.com.edu.infnet.TestesApi.entidade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Exercicio3 {

    public static void main(String[] args) {
        int nonexistentId = 13;
        System.out.println("--- Buscando entidade com ID inexistente: " + nonexistentId + " ---");

        try {
            URL url = new URL("https://apichallenges.eviltester.com/sim/entities/" + nonexistentId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int statusCode = connection.getResponseCode();
            System.out.println("Código de status: " + statusCode);

            InputStream inputStream = null;
            String responseType = "Corpo da resposta: ";

            if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                System.out.println("Mensagem: Entidade não encontrada conforme esperado.");
                inputStream = connection.getErrorStream();
                responseType = "Corpo da resposta (erro): ";
            } else if (statusCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Mensagem: Entidade encontrada inesperadamente.");
                inputStream = connection.getInputStream();
            } else {
                System.out.println("Mensagem: Código de status inesperado: " + statusCode);
                inputStream = connection.getErrorStream() != null ? connection.getErrorStream() : connection.getInputStream();
                responseType = "Corpo da resposta (outros status): ";
            }

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer responseBody = new StringBuffer();

                while ((line = reader.readLine()) != null) {
                    responseBody.append(line).append("\n");
                }
                reader.close();
                System.out.println(responseType);
                System.out.println(responseBody.toString());
            } else {
                System.out.println("Nenhum stream de resposta disponível.");
            }

            connection.disconnect();

        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao buscar a entidade inexistente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}