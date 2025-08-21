package com.asinha.llm.wrapper.dto;

import java.util.HashMap;
import java.util.Map;

public class ChainContext {
    private Map<String, Object> variables;
    private String lastOutput;

    public ChainContext(){
        this.variables = new HashMap<>();
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getLastOutput() {
        return lastOutput;
    }

    public void setLastOutput(String lastOutput) {
        this.lastOutput = lastOutput;
    }

    public void putVariable(String key, Object value) {
        variables.put(key, value);
    }

    public Object getVariable(String key) {
        return variables.get(key);
    }
}
