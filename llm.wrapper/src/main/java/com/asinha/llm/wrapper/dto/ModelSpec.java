package com.asinha.llm.wrapper.dto;

import com.asinha.llm.wrapper.dto.enums.ModelName;
import com.asinha.llm.wrapper.dto.enums.ModelProvider;

import java.util.Map;

public class ModelSpec {
    private final ModelName modelName;

    /**
     * Provider-specific config:
     * For OpenAI / Anthropic: apiKey
     * For LLaMA: endpoint URL
     * Can add other config options in future
     */
    private final Map<String, String> providerConfig;

    public ModelSpec(ModelName modelName, Map<String, String> providerConfig) {
        this.modelName = modelName;
        this.providerConfig = providerConfig;
    }

    public ModelName getModelName() {
        return modelName;
    }

    public ModelProvider getProvider() {
        return modelName.getProvider();
    }

    public int getContextWindow() {
        return modelName.getContextWindow();
    }

    public Map<String, String> getProviderConfig() {
        return providerConfig;
    }

    public String getProviderConfig(String key) {
        return providerConfig != null ? providerConfig.getOrDefault(key, null) : null;
    }
}
