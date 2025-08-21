package com.asinha.llm.wrapper.dto;

import java.util.List;

public class LlmRequest {
    private ModelSpec modelSpec;
    private List<String> messages;
    private int maxTokens;
    private double temperature;
    private boolean stream;

    public ModelSpec getModelSpec() {
        return modelSpec;
    }

    public void setModelSpec(ModelSpec modelSpec) {
        this.modelSpec = modelSpec;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}
