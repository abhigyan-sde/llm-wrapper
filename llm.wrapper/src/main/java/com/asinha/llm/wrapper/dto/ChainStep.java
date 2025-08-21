package com.asinha.llm.wrapper.dto;

public class ChainStep {
    private String promptTemplate;
    private String role;   // "system", "user", "assistant"
    private boolean requiresPreviousOutput;

    public String getPromptTemplate() {
        return promptTemplate;
    }

    public void setPromptTemplate(String promptTemplate) {
        this.promptTemplate = promptTemplate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isRequiresPreviousOutput() {
        return requiresPreviousOutput;
    }

    public void setRequiresPreviousOutput(boolean requiresPreviousOutput) {
        this.requiresPreviousOutput = requiresPreviousOutput;
    }
}
