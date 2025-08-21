package com.asinha.llm.wrapper.client.impl;

import com.asinha.llm.wrapper.client.LlmClient;
import com.asinha.llm.wrapper.dto.LlmRequest;
import com.asinha.llm.wrapper.dto.LlmResponse;
import com.asinha.llm.wrapper.dto.ModelSpec;
import com.asinha.llm.wrapper.exception.LlmException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class LlamaClient implements LlmClient {

    private final ModelSpec modelSpec;
    private final String endpointUrl; // e.g., http://localhost:8080/generate
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public LlamaClient(ModelSpec modelSpec){
        this.modelSpec = modelSpec;
        this.endpointUrl = modelSpec.getProviderConfig("endpointUrl");
        if (this.endpointUrl == null || this.endpointUrl.isEmpty()) {
            throw new IllegalStateException("LLaMA endpoint URL missing in ModelSpec");
        }
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public LlmResponse generate(LlmRequest request) throws LlmException {
        try{
            String prompt = String.join("\n", request.getMessages());

            JsonNode payload = objectMapper.createObjectNode()
                    .put("model", modelSpec.getModelName().getModelId())
                    .put("prompt", prompt)
                    .put("max_tokens", request.getMaxTokens() > 0 ? request.getMaxTokens() : 512)
                    .put("temperature", request.getTemperature() > 0 ? request.getTemperature() : 0.7);

            RequestBody body = RequestBody.create(
                    objectMapper.writeValueAsString(payload),
                    MediaType.parse("application/json")
            );

            Request httpRequest = new Request.Builder()
                    .url(endpointUrl)
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    throw new LlmException("Unexpected code " + response);
                }

                if(response.body() == null)
                    throw new LlmException("No response received. Try again");

                String responseBody = response.body().string();
                return parseResponse(responseBody);
            }
        }catch(LlmException e){
            throw e;
        }catch(Exception e){
            throw new LlmException(e.getMessage());
        }
    }

    private LlmResponse parseResponse(String responseBody) throws IOException {
        JsonNode root = objectMapper.readTree(responseBody);
        LlmResponse response = new LlmResponse();

        // Assume endpoint returns JSON with field "output"
        String text = root.path("output").asText();
        response.setOutputText(text);
        response.setRawResponse(responseBody);

        return response;
    }
}
