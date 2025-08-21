package com.asinha.llm.wrapper.client.impl;

import com.asinha.llm.wrapper.client.LlmClient;
import com.asinha.llm.wrapper.dto.LlmRequest;
import com.asinha.llm.wrapper.dto.LlmResponse;
import com.asinha.llm.wrapper.dto.ModelSpec;
import com.asinha.llm.wrapper.exception.LlmException;
import com.asinha.llm.wrapper.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;


public class AnthropicClient implements LlmClient {

    private final ModelSpec modelSpec;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public AnthropicClient(ModelSpec modelSpec) {
        this.modelSpec = modelSpec;
        this.apiKey = modelSpec.getProviderConfig("apiKey");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalStateException("Anthropic API key missing in ModelSpec");
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
                    .put("max_tokens_to_sample", request.getMaxTokens() > 0 ? request.getMaxTokens() : 512)
                    .put("temperature", request.getTemperature() > 0 ? request.getTemperature() : 0.7);

            RequestBody body = RequestBody.create(
                    objectMapper.writeValueAsString(payload),
                    MediaType.parse("application/json")
            );

            Request httpRequest = new Request.Builder()
                    .url(Constants.ANTHROPIC_API_URL)
                    .post(body)
                    .addHeader("x-api-key", apiKey)
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
            throw  new LlmException(e.getMessage());
        }
    }

    private LlmResponse parseResponse(String responseBody) throws IOException {
        JsonNode root = objectMapper.readTree(responseBody);
        LlmResponse response = new LlmResponse();

        // Anthropic returns "completion"
        String text = root.path("completion").asText();
        response.setOutputText(text);
        response.setRawResponse(responseBody);

        // Usage info is optional; can be added later if available
        return response;
    }
}
