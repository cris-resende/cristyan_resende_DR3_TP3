package br.com.edu.infnet.TestesApi.entidade;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Exercicio10 {

    public static void main(String[] args) {
        System.out.println("--- Executando Exercício 10: DELETE inválido ---");
        int entityIdToDelete = 2;
        String deleteEndpoint = "https://apichallenges.eviltester.com/sim/entities/" + entityIdToDelete;

        try {
            System.out.println("\n--- Tentando DELETE da entidade com ID: " + entityIdToDelete + " ---");
            URL deleteUrl = new URL(deleteEndpoint);
            HttpURLConnection deleteConnection = (HttpURLConnection) deleteUrl.openConnection();
            deleteConnection.setRequestMethod("DELETE");

            int deleteStatusCode = deleteConnection.getResponseCode();
            System.out.println("Código de status do DELETE: " + deleteStatusCode);


            InputStream deleteInputStream = null;
            String responseTypeMessage = "Corpo da resposta: ";

            if (deleteStatusCode == HttpURLConnection.HTTP_FORBIDDEN) {
                System.out.println("Mensagem: Exclusão negada (Status 403 Forbidden) conforme esperado.");
                deleteInputStream = deleteConnection.getErrorStream();
                responseTypeMessage = "Corpo da resposta (erro 403): ";
            } else if (deleteStatusCode == HttpURLConnection.HTTP_BAD_METHOD) {
                System.out.println("Mensagem: Método DELETE não permitido (Status 405 Method Not Allowed) conforme esperado.");
                deleteInputStream = deleteConnection.getErrorStream();
                responseTypeMessage = "Corpo da resposta (erro 405): ";
            } else if (deleteStatusCode == HttpURLConnection.HTTP_OK || deleteStatusCode == HttpURLConnection.HTTP_NO_CONTENT) {
                System.out.println("Mensagem: Exclusão processada, o que foi inesperado. Status: " + deleteStatusCode);
                deleteInputStream = deleteConnection.getInputStream();
            } else {
                System.out.println("Mensagem: Código de status inesperado: " + deleteStatusCode);
                deleteInputStream = deleteConnection.getErrorStream() != null ?
                        deleteConnection.getErrorStream() : deleteConnection.getInputStream();
                responseTypeMessage = "Corpo da resposta (outro status): ";
            }


            if (deleteInputStream != null) {
                BufferedReader deleteReader = new BufferedReader(new InputStreamReader(deleteInputStream, StandardCharsets.UTF_8));
                String deleteLine;
                StringBuffer deleteResponseBody = new StringBuffer();
                while ((deleteLine = deleteReader.readLine()) != null) {
                    deleteResponseBody.append(deleteLine).append("\n");
                }
                deleteReader.close();
                System.out.println(responseTypeMessage);
                System.out.println(deleteResponseBody.toString());
            } else {
                System.out.println("Nenhum stream de resposta disponível para o DELETE.");
            }

            deleteConnection.disconnect();

        } catch (Exception e) {
            System.err.println("Ocorreu um erro no Exercício 10: " + e.getMessage());
            e.printStackTrace();
        }
    }
}