package br.com.edu.infnet.TestesApi.entidade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Exercicio4 {

    public static void main(String[] args) {
        System.out.println("--- Realizando GET com parâmetros na URL ---");

        try {
            String baseUrl = "https://apichallenges.eviltester.com/sim/entities";
            String categoria = "teste";
            int limite = 5;

            String queryParams = String.format("categoria=%s&limite=%d",
                    URLEncoder.encode(categoria, StandardCharsets.UTF_8.toString()),
                    limite);

            String finalUrlString = baseUrl + "?" + queryParams;
            System.out.println("URL final montada: " + finalUrlString);

            URL url = new URL(finalUrlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int statusCode = connection.getResponseCode();
            System.out.println("Código de status: " + statusCode);

            BufferedReader reader;
            InputStreamReader inputStreamReader;
            if (statusCode >= 200 && statusCode < 300) {
                inputStreamReader = new InputStreamReader(connection.getInputStream());
            } else {
                inputStreamReader = new InputStreamReader(connection.getErrorStream());
            }

            reader = new BufferedReader(inputStreamReader);
            String line;
            StringBuffer responseBody = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                responseBody.append(line).append("\n");
            }
            reader.close();

            System.out.println("Corpo da resposta: ");
            System.out.println(responseBody.toString());


            connection.disconnect();

        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao realizar a requisição com parâmetros: " + e.getMessage());
            e.printStackTrace();
        }
    }
}