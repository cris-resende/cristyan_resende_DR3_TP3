package br.com.edu.infnet.TestesApi.entidade;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

// Este exercício utiliza o método PUT para tentar atualizar a mesma entidade com o mesmo JSON do Exercício 7.
// Observa-se que, assim como no Exercício 7 usando POST, a API 'https://apichallenges.eviltester.com/sim/entities'
// não persiste a alteração do nome. Isso reforça a compreensão de que esta API de simulação é 'stateless'
// e não foi projetada para manter o estado das modificações em suas entidades fixas.
// A requisição PUT é aceita, mas a verificação posterior com GET confirma que o recurso não foi realmente modificado no "servidor" simulado.

public class Exercicio8 {

    public static void main(String[] args) {
        System.out.println("--- Executando Exercício 8: PUT para atualizar uma entidade ---");
        int entityIdToUpdate = 10;
        String updateJson = "{\"name\": \"atualizado\"}";
        String putEndpoint = "https://apichallenges.eviltester.com/sim/entities/" + entityIdToUpdate;
        String getEndpoint = "https://apichallenges.eviltester.com/sim/entities/" + entityIdToUpdate;

        try {
            System.out.println("\n--- Realizando PUT de atualização ---");
            URL putUrl = new URL(putEndpoint);
            HttpURLConnection putConnection = (HttpURLConnection) putUrl.openConnection();

            putConnection.setRequestMethod("PUT");

            putConnection.setDoOutput(true);

            putConnection.setRequestProperty("Content-Type", "application/json");
            putConnection.setRequestProperty("Accept", "application/json");

            try (DataOutputStream outputStream = new DataOutputStream(putConnection.getOutputStream())) {
                byte[] input = updateJson.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input);
                outputStream.flush();
            }

            int putStatusCode = putConnection.getResponseCode();
            System.out.println("Código de status do PUT: " + putStatusCode);

            InputStream putInputStream = (putStatusCode >= 200 && putStatusCode < 300) ?
                    putConnection.getInputStream() :
                    putConnection.getErrorStream();
            if (putInputStream != null) {
                BufferedReader putReader = new BufferedReader(new InputStreamReader(putInputStream, StandardCharsets.UTF_8));
                String putLine;
                StringBuffer putResponseBody = new StringBuffer();
                while ((putLine = putReader.readLine()) != null) {
                    putResponseBody.append(putLine).append("\n");
                }
                putReader.close();
                System.out.println("Corpo da resposta do PUT: ");
                System.out.println(putResponseBody.toString());
            }
            putConnection.disconnect();

            System.out.println("\n--- Realizando GET para verificação ---");
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

                if (getStatusCode == HttpURLConnection.HTTP_OK) {
                    if (fullGetResponseBody.contains("\"name\":\"atualizado\"")) {
                        System.out.println("Verificação: O nome da entidade ID " + entityIdToUpdate + " foi atualizado para 'atualizado' com sucesso!");
                    } else {
                        System.out.println("Verificação: O nome da entidade ID " + entityIdToUpdate + " NÃO foi atualizado para 'atualizado' ou não foi encontrado.");
                    }
                } else {
                    System.out.println("Verificação: Não foi possível verificar a atualização devido a um erro no GET.");
                }

            } else {
                System.out.println("Nenhum stream de resposta disponível para o GET de verificação.");
            }
            getConnection.disconnect();

        } catch (Exception e) {
            System.err.println("Ocorreu um erro no Exercício 8: " + e.getMessage());
            e.printStackTrace();
        }
    }
}