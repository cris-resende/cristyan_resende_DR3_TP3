package br.com.edu.infnet.TestesApi.entidade;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

// Este exercício demonstra o envio de uma requisição POST para "atualizar" uma entidade existente
// com um novo nome. No entanto, a API 'https://apichallenges.eviltester.com/sim/entities'
// é 'stateless'. Isso significa que, embora a requisição POST seja bem-sucedida,
// a API não persiste as alterações para entidades pré-existentes.
// A verificação subsequente com um GET comprova que o nome da entidade permanece o original,
// confirmando a natureza stateless da API e a falta de persistência real das modificações.

public class Exercicio7 {

    public static void main(String[] args) {
        System.out.println("--- Executando Exercício 7: POST para atualizar uma entidade ---");
        int entityIdToUpdate = 10;
        String updateJson = "{\"name\": \"atualizado\"}";
        String postEndpoint = "https://apichallenges.eviltester.com/sim/entities/" + entityIdToUpdate;
        String getEndpoint = "https://apichallenges.eviltester.com/sim/entities/" + entityIdToUpdate;

        try {
            System.out.println("\n--- Realizando POST de atualização ---");
            URL postUrl = new URL(postEndpoint);
            HttpURLConnection postConnection = (HttpURLConnection) postUrl.openConnection();
            postConnection.setRequestMethod("POST");
            postConnection.setDoOutput(true);
            postConnection.setRequestProperty("Content-Type", "application/json");
            postConnection.setRequestProperty("Accept", "application/json");

            try (DataOutputStream outputStream = new DataOutputStream(postConnection.getOutputStream())) {
                byte[] input = updateJson.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input);
                outputStream.flush();
            }

            int postStatusCode = postConnection.getResponseCode();
            System.out.println("Código de status do POST: " + postStatusCode);

            InputStream postInputStream = (postStatusCode >= 200 && postStatusCode < 300) ?
                    postConnection.getInputStream() :
                    postConnection.getErrorStream();
            if (postInputStream != null) {
                BufferedReader postReader = new BufferedReader(new InputStreamReader(postInputStream, StandardCharsets.UTF_8));
                String postLine;
                StringBuffer postResponseBody = new StringBuffer();
                while ((postLine = postReader.readLine()) != null) {
                    postResponseBody.append(postLine).append("\n");
                }
                postReader.close();
                System.out.println("Corpo da resposta do POST: ");
                System.out.println(postResponseBody.toString());
            }
            postConnection.disconnect();

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
            System.err.println("Ocorreu um erro no Exercício 7: " + e.getMessage());
            e.printStackTrace();
        }
    }
}