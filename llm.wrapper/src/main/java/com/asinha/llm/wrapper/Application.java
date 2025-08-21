package com.asinha.llm.wrapper;


import com.asinha.llm.wrapper.client.LlmClient;
import com.asinha.llm.wrapper.client.factory.LlmFactory;
import com.asinha.llm.wrapper.dto.LlmRequest;
import com.asinha.llm.wrapper.dto.LlmResponse;
import com.asinha.llm.wrapper.dto.ModelSpec;
import com.asinha.llm.wrapper.dto.enums.ModelName;
import com.asinha.llm.wrapper.exception.LlmException;

import java.util.List;
import java.util.Map;

public class Application {

	public static void main(String[] args) {
		//Demo usage
		String apiKey = System.getenv("OPENAI_API_KEY");
		ModelSpec openAiSpec = new ModelSpec(
				ModelName.GPT_4O_MINI,
				Map.of("apiKey", apiKey)
		);

		LlmClient client = LlmFactory.create(openAiSpec);
		LlmRequest request = new LlmRequest();
		request.setModelSpec(openAiSpec);
		request.setMessages(List.of("Summarize the network call which is executing this API."));
		LlmResponse response;
		try{
			 response = client.generate(request);
			 System.out.println(response.getOutputText());
		}catch(LlmException e){
			System.out.println(e.getMessage());
		}
	}

}
