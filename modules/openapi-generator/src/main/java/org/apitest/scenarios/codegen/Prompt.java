package org.apitest.scenarios.codegen;
import java.util.ArrayList;
import java.util.List;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.jknack.handlebars.internal.lang3.StringUtils;

import io.swagger.v3.core.util.Json;

public class Prompt {

	public static List<ChatRequestMessage> getChatExamples(String spec, String exampleJSON)
    {
		List<ChatRequestMessage> chatMessages = new ArrayList<>();
	    	String assistantMessage = "You are an AI assistant which helps in generating the sample request, response data. You are provided with below swagger spec and you need to generate data based on user provided operation id and given parameters.\n" 
	    			+ "SWAGGER API SPEC:\n"
	    			+ "```\n"
	    			+ spec;
	    	
		    	if(StringUtils.isNoneBlank(exampleJSON)) {
		    		Object example;
					try {
						example = Json.mapper().readValue(exampleJSON, Object.class);
						String exampleString = Json.pretty(example);
			    		assistantMessage += "Use these values for the following parameter names if applicable, otherwise use your best judgement:\n"
					    			+ "```\n"
					    			+ exampleString
					    			+ "```";
			    		chatMessages.add(new ChatRequestUserMessage(assistantMessage));
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    	
		    	chatMessages.add(new ChatRequestSystemMessage(assistantMessage));
		    	String userPrompt = "Create a sampler data for the operation `LoadTestAdministration_CreateOrUpdateTest` which includes all the required parameters, the only optional parameter `loadTestConfiguration` and any of other dependent optional parameter based on description of it in swagger spec. Response should be given as only json output without any extra description.\n";
		    	chatMessages.add(new ChatRequestUserMessage(userPrompt));
	    		chatMessages.add(new ChatRequestSystemMessage("{\n"
		    			+ "    \"summary\" : \"should succeed with optional field loadTestConfiguration\",\n"
		    			+ "    \"isValid\" : true,\n"
		    			+ "    \"request\" : {\n"
		    			+ "        \"body\" : {\n"
		    			+ "            \"loadTestConfiguration\": {\n"
		    			+ "                \"engineInstances\": 1,\n"
		    			+ "                \"splitAllCSVs\": false,\n"
		    			+ "                \"quickStartTest\": true,\n"
		    			+ "                \"optionalLoadTestConfig\": {\n"
		    			+ "                    \"endpointUrl\": \"https://azure.microsoft.com\",\n"
		    			+ "                    \"virtualUsers\": 50,\n"
		    			+ "                    \"requestsPerSecond\": null,\n"
		    			+ "                    \"maxResponseTimeInMs\": null,\n"
		    			+ "                    \"rampUpTime\": 60,\n"
		    			+ "                    \"duration\": 1200\n"
		    			+ "                }\n"
		    			+ "            }\n"
		    			+ "        },\n"
		    			+ "        \"testId\": \"7628dc9d-594f-475c-83a4-19079e6c8fb4\",\n"
		    			+ "        \"api-version\":\"2022-11-01\"\n"
		    			+ "    },\n"
		    			+ "    \"response\" : {\n"
		    			+ "        \"statusCode\" : 201\n"
		    			+ "    }\n"
		    			+ "}"));
	    	
	    return chatMessages;
	   
    }
}

