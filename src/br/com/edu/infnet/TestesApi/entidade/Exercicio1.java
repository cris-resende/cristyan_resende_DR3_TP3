package br.com.edu.infnet.TestesApi.entidade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Exercicio1 {
    public static void main(String[] args) {
        try{
            URL url = new URL("https://apichallenges.eviltester.com/sim/entities");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int statusCode = connection.getResponseCode();
            System.out.println("Código de status: " + statusCode);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuffer responseBody = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                responseBody.append(line).append("\n");
            }

            reader.close();
            connection.disconnect();

            System.out.println("Body da resposta: ");
            System.out.println(responseBody.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}