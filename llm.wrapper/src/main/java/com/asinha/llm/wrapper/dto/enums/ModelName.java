package com.asinha.llm.wrapper.dto.enums;

public enum ModelName {
    // --- OpenAI Models ---
    GPT_4O("gpt-4o", ModelProvider.OPENAI, 128000),
    GPT_4O_MINI("gpt-4o-mini", ModelProvider.OPENAI, 128000),
    GPT_35_TURBO("gpt-3.5-turbo", ModelProvider.OPENAI, 16000),

    // --- Anthropic Models ---
    CLAUDE_3_OPUS("claude-3-opus-20240229", ModelProvider.ANTHROPIC, 200000),
    CLAUDE_3_SONNET("claude-3-sonnet-20240229", ModelProvider.ANTHROPIC, 200000),
    CLAUDE_3_HAIKU("claude-3-haiku-20240307", ModelProvider.ANTHROPIC, 200000),

    // --- LLaMA Models ---
    LLAMA_3_70B("llama-3-70b", ModelProvider.LLAMA, 8000),
    LLAMA_3_8B("llama-3-8b", ModelProvider.LLAMA, 8000);

    private final String modelId;
    private final ModelProvider provider;
    private final int contextWindow;

    ModelName(String modelId, ModelProvider provider, int contextWindow) {
        this.modelId = modelId;
        this.provider = provider;
        this.contextWindow = contextWindow;
    }

    public String getModelId() {
        return modelId;
    }

    public ModelProvider getProvider() {
        return provider;
    }

    public int getContextWindow() {
        return contextWindow;
    }
}
