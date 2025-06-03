package br.com.edu.infnet.TestesApi.simpleapi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Exercicio12 {

    private static String generatedIsbn = null;

    public static void main(String[] args) {
        System.out.println("--- Executando Exercício 12: Experimentos com a Simple API ---");

        getAllItems();

        generatedIsbn = generateRandomIsbn();
        if (generatedIsbn == null || generatedIsbn.isEmpty()) {
            System.err.println("Não foi possível gerar um ISBN. Encerrando Exercício 12.");
            return;
        }
        System.out.println("-> ISBN Gerado para uso: " + generatedIsbn);

        createItem(generatedIsbn);

        System.out.println("\n--- Verificando todos os itens após POST ---");
        getAllItems();

        updateItem(generatedIsbn);

        System.out.println("\n--- Verificando todos os itens após PUT ---");
        getAllItems();

        deleteItem(generatedIsbn);

        System.out.println("\n--- Verificando todos os itens após DELETE ---");
        getAllItems();
    }

    private static String performRequest(String method, String endpoint, String jsonBody) {
        String fullResponseBody = "";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(endpoint);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            if (("POST".equals(method) || "PUT".equals(method)) && jsonBody != null && !jsonBody.isEmpty()) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                    byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(input);
                }
            }

            int statusCode = connection.getResponseCode();
            System.out.println("    " + method + " [" + endpoint + "] - Status: " + statusCode);

            InputStream inputStream = null;

            if (statusCode >= 200 && statusCode < 300) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                StringBuffer responseBuffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    responseBuffer.append(line).append("\n");
                }
                reader.close();
                fullResponseBody = responseBuffer.toString();
                System.out.println("    Corpo da resposta: \n" + fullResponseBody.trim());
            } else {
                System.out.println("    Nenhum corpo de resposta disponível.");
            }

        } catch (Exception e) {
            System.err.println("    ERRO na requisição " + method + " para " + endpoint + ": " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return fullResponseBody.trim();
    }

    private static void getAllItems() {
        System.out.println("\n--- GET todos os itens ---");
        String endpoint = "https://apichallenges.eviltester.com/simpleapi/items";
        performRequest("GET", endpoint, null);
    }

    private static String generateRandomIsbn() {
        System.out.println("\n--- Gerar ISBN aleatório ---");
        String endpoint = "https://apichallenges.eviltester.com/simpleapi/randomisbn";
        String response = performRequest("GET", endpoint, null);
        return response;
    }

    private static void createItem(String isbn) {
        System.out.println("\n--- Criar item com POST ---");
        String endpoint = "https://apichallenges.eviltester.com/simpleapi/items";
        String jsonBody = String.format("{\"type\": \"book\", \"isbn13\": \"%s\", \"price\": 5.99, \"numberinstock\": 5}", isbn);
        performRequest("POST", endpoint, jsonBody);
    }

    private static void updateItem(String isbn) {
        System.out.println("\n--- Atualizar item com PUT ---");
        String endpoint = "https://apichallenges.eviltester.com/simpleapi/items";

        String jsonBody = String.format("{\"type\": \"book\", \"isbn13\": \"%s\", \"price\": 12.50, \"numberinstock\": 15}", isbn);
        performRequest("PUT", endpoint, jsonBody);
    }

    private static void deleteItem(String isbn) {
        System.out.println("\n--- Remover item com DELETE ---");
        String endpoint = "https://apichallenges.eviltester.com/simpleapi/items/" + isbn;
        performRequest("DELETE", endpoint, null);
    }
}