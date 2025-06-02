package br.com.edu.infnet.TestesApi.entidade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Exercicio2 {

    public static void main(String[] args) {
        int[] entityIds = {1, 2, 3, 4, 5, 6, 7, 8};

        for (int id : entityIds) {
            System.out.println("--- Buscando entidade com ID: " + id + " ---");
            try {
                URL url = new URL("https://apichallenges.eviltester.com/sim/entities/" + id);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int statusCode = connection.getResponseCode();
                System.out.println("Código de status: " + statusCode);

                if (statusCode == HttpURLConnection.HTTP_OK) { // HTTP_OK é 200
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuffer responseBody = new StringBuffer();

                    while ((line = reader.readLine()) != null) {
                        responseBody.append(line).append("\n");
                    }
                    reader.close();
                    System.out.println("Corpo da resposta: ");
                    System.out.println(responseBody.toString());
                } else {

                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    String errorLine;
                    StringBuffer errorBody = new StringBuffer();
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorBody.append(errorLine).append("\n");
                    }
                    errorReader.close();
                    System.out.println("Corpo do erro: ");
                    System.out.println(errorBody.toString());
                }

                connection.disconnect();
                System.out.println("\n");

            } catch (Exception e) {
                System.err.println("Ocorreu um erro ao buscar a entidade com ID " + id + ": " + e.getMessage());
                e.printStackTrace();
                System.out.println("\n");
            }
        }
    }
}