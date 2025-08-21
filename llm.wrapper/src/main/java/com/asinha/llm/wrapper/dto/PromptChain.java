package com.asinha.llm.wrapper.dto;

import java.util.List;

public class PromptChain {
    private List<ChainStep> steps;

    public List<ChainStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ChainStep> steps) {
        this.steps = steps;
    }
}
