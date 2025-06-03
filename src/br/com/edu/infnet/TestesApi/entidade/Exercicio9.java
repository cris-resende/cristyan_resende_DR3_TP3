package br.com.edu.infnet.TestesApi.entidade;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Exercicio9 {

    public static void main(String[] args) {
        System.out.println("--- Executando Exercício 9: DELETE de entidade válida ---");
        int entityIdToDelete = 9;
        String deleteEndpoint = "https://apichallenges.eviltester.com/sim/entities/" + entityIdToDelete;
        String getEndpoint = "https://apichallenges.eviltester.com/sim/entities/" + entityIdToDelete;

        try {
            System.out.println("\n--- Realizando DELETE da entidade com ID: " + entityIdToDelete + " ---");
            URL deleteUrl = new URL(deleteEndpoint);
            HttpURLConnection deleteConnection = (HttpURLConnection) deleteUrl.openConnection();

            deleteConnection.setRequestMethod("DELETE");

            int deleteStatusCode = deleteConnection.getResponseCode();
            System.out.println("Código de status do DELETE: " + deleteStatusCode);

            InputStream deleteInputStream = (deleteStatusCode >= 200 && deleteStatusCode < 300) ?
                    deleteConnection.getInputStream() :
                    deleteConnection.getErrorStream();
            if (deleteInputStream != null) {
                BufferedReader deleteReader = new BufferedReader(new InputStreamReader(deleteInputStream, StandardCharsets.UTF_8));
                String deleteLine;
                StringBuffer deleteResponseBody = new StringBuffer();
                while ((deleteLine = deleteReader.readLine()) != null) {
                    deleteResponseBody.append(deleteLine).append("\n");
                }
                deleteReader.close();
            }
            deleteConnection.disconnect();

            System.out.println("\n--- Realizando GET para confirmar DELETE ---");
            URL getUrl = new URL(getEndpoint);
            HttpURLConnection getConnection = (HttpURLConnection) getUrl.openConnection();
            getConnection.setRequestMethod("GET");

            int getStatusCode = getConnection.getResponseCode();
            System.out.println("Código de status do GET de verificação: " + getStatusCode);

            InputStream getInputStream = (getStatusCode >= 200 && getStatusCode < 300) ?
                    getConnection.getInputStream() :
                    getConnection.getErrorStream();

            if (getInputStream != null) {
                BufferedReader getReader = new BufferedReader(new InputStreamReader(getInputStream, StandardCharsets.UTF_8));
                String getLine;
                StringBuffer getResponseBody = new StringBuffer();
                while ((getLine = getReader.readLine()) != null) {
                    getResponseBody.append(getLine).append("\n");
                }
                getReader.close();

                String fullGetResponseBody = getResponseBody.toString();
                System.out.println("Corpo da resposta do GET de verificação: ");
                System.out.println(fullGetResponseBody);

                if (getStatusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    System.out.println("Verificação: Entidade ID " + entityIdToDelete + " não encontrada (Status 404) conforme esperado após DELETE.");
                } else {
                    System.out.println("Verificação: O DELETE NÃO foi bem-sucedido ou a entidade foi encontrada inesperadamente com status " + getStatusCode + ".");
                }

            } else {
                System.out.println("Nenhum stream de resposta disponível para o GET de verificação.");
            }
            getConnection.disconnect();

        } catch (Exception e) {
            System.err.println("Ocorreu um erro no Exercício 9: " + e.getMessage());
            e.printStackTrace();
        }
    }
}