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
import java.util.stream.Collectors;


public class OpenAIClient implements LlmClient {
    private final ModelSpec modelSpec;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public OpenAIClient(ModelSpec modelSpec){
        this.modelSpec = modelSpec;
        this.apiKey = modelSpec.getProviderConfig("apiKey");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalStateException("OpenAI API key missing in ModelSpec");
        }
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public LlmResponse generate(LlmRequest request) throws LlmException {
        try{
            JsonNode payload = buildPayload(request);

            RequestBody body = RequestBody.create(
                    objectMapper.writeValueAsString(payload),
                    MediaType.parse("application/json")
            );

            Request httpRequest = new Request.Builder()
                    .url(Constants.OPENAI_API_URL)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + apiKey)
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
        }
        catch(Exception e){
            throw new LlmException(e.getMessage());
        }
    }

    private JsonNode buildPayload(LlmRequest request) {
        // Convert messages to OpenAI format
        var messagesArray = request.getMessages().stream()
                .map(msg -> objectMapper.createObjectNode()
                        .put("role", "user") // for now, all messages are 'user'
                        .put("content", msg))
                .collect(Collectors.toList());

        var payload = objectMapper.createObjectNode();
        payload.put("model", modelSpec.getModelName().getModelId());
        payload.put("max_tokens", request.getMaxTokens() > 0 ? request.getMaxTokens() : 512);
        payload.put("temperature", request.getTemperature() > 0 ? request.getTemperature() : 0.7);
        payload.putArray("messages").addAll(messagesArray);

        return payload;
    }

    private LlmResponse parseResponse(String responseBody) throws IOException {
        JsonNode root = objectMapper.readTree(responseBody);
        LlmResponse response = new LlmResponse();

        // OpenAI Chat API returns: choices[0].message.content
        String text = root.path("choices").get(0).path("message").path("content").asText();
        response.setOutputText(text);

        // Optional: token usage
        JsonNode usage = root.path("usage");
        if (!usage.isMissingNode()) {
            response.setPromptTokens(usage.path("prompt_tokens").asInt());
            response.setCompletionTokens(usage.path("completion_tokens").asInt());
            response.setTotalTokens(usage.path("total_tokens").asInt());
        }

        response.setRawResponse(responseBody);
        return response;
    }
}
