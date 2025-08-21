package com.asinha.llm.wrapper.client.factory;

import com.asinha.llm.wrapper.client.LlmClient;
import com.asinha.llm.wrapper.client.impl.AnthropicClient;
import com.asinha.llm.wrapper.client.impl.LlamaClient;
import com.asinha.llm.wrapper.client.impl.OpenAIClient;
import com.asinha.llm.wrapper.dto.ModelSpec;

public class LlmFactory {

    public static LlmClient create(ModelSpec modelSpec){
        return switch (modelSpec.getProvider()) {
            case OPENAI -> new OpenAIClient(modelSpec);
            case ANTHROPIC -> new AnthropicClient(modelSpec);
            case LLAMA -> new LlamaClient(modelSpec);
            default -> throw new IllegalArgumentException("Unsupported provider: " + modelSpec.getProvider());
        };
    }
}
