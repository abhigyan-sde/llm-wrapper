package com.asinha.llm.wrapper.client;

import com.asinha.llm.wrapper.dto.LlmRequest;
import com.asinha.llm.wrapper.dto.LlmResponse;
import com.asinha.llm.wrapper.exception.LlmException;

public interface LlmClient {
    /**
     * Sends a request to the LLM and returns the response.
     *
     * @param request the LLM request
     * @return the LLM response
     * @throws LlmException in case of API or processing errors
     */
    LlmResponse generate(LlmRequest request) throws LlmException;
}
