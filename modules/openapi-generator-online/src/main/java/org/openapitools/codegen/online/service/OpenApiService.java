package org.openapitools.codegen.online.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.online.model.APIRequest;
import org.openapitools.codegen.online.model.APIRequestInsight;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestAssistantMessage;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.azure.ai.openai.models.ChatResponseMessage;
import com.azure.core.credential.AzureKeyCredential;


@Service
public class OpenApiService {

	private static final String OPENAI_API_KEY = "92eb2e08ed17450b80de7f7d24a94388";
    private static final String ENDPOINT = "https://apitesting.openai.azure.com/";
    private OpenAIClient client; 
    
    public OpenApiService() {
    	
    	this.client = new OpenAIClientBuilder()
        	    .credential(new AzureKeyCredential(OPENAI_API_KEY))
        	    .endpoint(ENDPOINT)
        	    .buildClient();
    	
    }
	public ResponseEntity<APIRequestInsight> getApiTestInsight(APIRequest apiRequest) {
		// create response entity object with 200 status code
		
    	List<ChatRequestMessage> chatMessages = new ArrayList<>();
    	
    	chatMessages.add(new ChatRequestSystemMessage("I am an assistant, which helps in improving the api implentation along with given api swagger specification."));
    	chatMessages.add(new ChatRequestUserMessage("Given the below api summary, api request payload, expected api response code, response code and api response data, can you please provide any recommendation in json format which can helps either fixing the api implementation or the request payload to get the correct response. We are generating data values for the api request parameters based on given swagger parameters schema. Can you suggest to add some missing constraint or validation on the swagger schema, so that data can be generated correctly and API implementation works. Can you also modify the swagger operation and update the json and share the diff from existing swagger json in json format as well.\r\n"
    			+ "\r\n"
    			+ "Api name \r\n"
    			+ "\r\n"
    			+ "quantity default case\r\n"
    			+ "\r\n"
    			+ "Api Request \r\n"
    			+ "\r\n"
    			+ "{\"body\":{\"quantity\":1080396839},\"itemId\":\"3ZCL\",\"api-version\":\"2022-11-01\"}\r\n"
    			+ "\r\n"
    			+ "Api Response \r\n"
    			+ "\r\n"
    			+ "{\"errors\":[{\"type\":\"field\",\"value\":\"3ZCL\",\"msg\":\"Item ID must be at least 5 characters long\",\"path\":\"itemId\",\"location\":\"params\"}]}\r\n"
    			+ "\r\n"
    			+ "Expected Response Code \r\n"
    			+ "\r\n"
    			+ "200\r\n"
    			+ "\r\n"
    			+ "Response Code \r\n"
    			+ "\r\n"
    			+ "400\r\n"
    			+ "\r\n"
    			+ "Swagger parameters schema \r\n"
    			+ "\r\n"
    			+ "{\"swagger\":\"2.0\",\"info\":{\"title\":\"Cart API\",\"version\":\"1.0.0\"},\"paths\":{\"/cart/\":{\"get\":{\"produces\":[\"application/json\"],\"parameters\":[],\"responses\":{\"200\":{\"description\":\"List of items in the cart\",\"schema\":{\"properties\":{\"cartSummary\":{\"properties\":{\"totalItems\":{\"type\":\"integer\"}},\"type\":\"object\"},\"items\":{\"items\":{\"$ref\":\"#/definitions/CartItem\"},\"type\":\"array\"},\"success\":{\"type\":\"boolean\"}},\"type\":\"object\"}},\"500\":{\"description\":\"Internal Server Error\"}},\"summary\":\"Get all items in the cart\"},\"post\":{\"consumes\":[\"application/json\"],\"produces\":[\"application/json\"],\"parameters\":[{\"in\":\"body\",\"name\":\"body\",\"required\":true,\"schema\":{\"properties\":{\"itemId\":{\"type\":\"string\"},\"quantity\":{\"type\":\"integer\",\"maximum\":100,\"minimum\":1}},\"required\":[\"itemId\",\"quantity\"],\"type\":\"object\"}}],\"responses\":{\"201\":{\"description\":\"Item added to cart successfully\",\"schema\":{\"properties\":{\"cartItem\":{\"$ref\":\"#/definitions/CartItem\"},\"message\":{\"type\":\"string\"},\"success\":{\"type\":\"boolean\"}},\"type\":\"object\"}},\"400\":{\"description\":\"Invalid item ID\"},\"500\":{\"description\":\"Internal Server Error\"}},\"summary\":\"Add an item to the cart\"}},\"/cart/{itemId}\":{\"delete\":{\"parameters\":[{\"in\":\"path\",\"name\":\"itemId\",\"required\":true,\"type\":\"string\"}],\"responses\":{\"200\":{\"description\":\"Item removed from cart\"},\"400\":{\"description\":\"Invalid item ID\"},\"404\":{\"description\":\"Item not found in cart\"},\"500\":{\"description\":\"Internal Server Error\"}},\"summary\":\"Remove a specific item from the cart\"},\"get\":{\"produces\":[\"application/json\"],\"parameters\":[{\"in\":\"path\",\"maxLength\":15,\"minLength\":5,\"name\":\"itemId\",\"pattern\":\"^[a-zA-Z0-9]{5,}$\",\"required\":true,\"type\":\"string\"}],\"responses\":{\"200\":{\"description\":\"Specific cart item details\",\"schema\":{\"properties\":{\"cartItem\":{\"$ref\":\"#/definitions/CartItem\"},\"success\":{\"type\":\"boolean\"}},\"type\":\"object\"}},\"400\":{\"description\":\"Invalid item ID\"},\"404\":{\"description\":\"Item not found in cart\"},\"500\":{\"description\":\"Internal Server Error\"}},\"summary\":\"Get a specific item from the cart\"},\"put\":{\"consumes\":[\"application/json\"],\"produces\":[\"application/json\"],\"parameters\":[{\"in\":\"path\",\"name\":\"itemId\",\"required\":true,\"type\":\"string\"},{\"in\":\"body\",\"name\":\"body\",\"required\":true,\"schema\":{\"properties\":{\"quantity\":{\"type\":\"integer\"}},\"required\":[\"quantity\"],\"type\":\"object\"}}],\"responses\":{\"200\":{\"description\":\"Item updated successfully\",\"schema\":{\"properties\":{\"cartItem\":{\"$ref\":\"#/definitions/CartItem\"},\"message\":{\"type\":\"string\"},\"success\":{\"type\":\"boolean\"}},\"type\":\"object\"}},\"400\":{\"description\":\"Invalid request parameters\"},\"404\":{\"description\":\"Item not found in cart\"},\"500\":{\"description\":\"Internal Server Error\"}},\"summary\":\"Update a specific item in the cart\"}}},\"definitions\":{\"CartItem\":{\"properties\":{\"itemId\":{\"minLength\":5,\"pattern\":\"^[a-zA-Z0-9]{5,}$\",\"type\":\"string\"},\"quantity\":{\"maximum\":100,\"minimum\":0,\"type\":\"integer\"}},\"required\":[\"itemId\",\"quantity\"],\"type\":\"object\"}},\"x-components\":{}}\r\n"
    			+ "\r\n"
    			+ "I need all recommendation and modified json in a single json output"));
    	
    	chatMessages.add(new ChatRequestAssistantMessage("{\r\n"
    			+ "  \"recommendations\": [\r\n"
    			+ "    {\r\n"
    			+ "      \"message\": \"Add a minimum length constraint for 'itemId' to ensure it is at least 5 characters long.\"\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"message\": \"Ensure that the 'quantity' parameter is within the range of 1 to 100.\"\r\n"
    			+ "    }\r\n"
    			+ "  ],\r\n"
    			+ "  \"modified_swagger\": {\r\n"
    			+ "    \"swagger\": \"2.0\",\r\n"
    			+ "    \"info\": {\r\n"
    			+ "      \"title\": \"Cart API\",\r\n"
    			+ "      \"version\": \"1.0.0\"\r\n"
    			+ "    },\r\n"
    			+ "    \"paths\": {\r\n"
    			+ "      \"/cart/\": {\r\n"
    			+ "        \"post\": {\r\n"
    			+ "          \"parameters\": [\r\n"
    			+ "            {\r\n"
    			+ "              \"in\": \"body\",\r\n"
    			+ "              \"name\": \"body\",\r\n"
    			+ "              \"required\": true,\r\n"
    			+ "              \"schema\": {\r\n"
    			+ "                \"properties\": {\r\n"
    			+ "                  \"itemId\": {\r\n"
    			+ "                    \"type\": \"string\",\r\n"
    			+ "                    \"minLength\": 5,\r\n"
    			+ "                    \"pattern\": \"^[a-zA-Z0-9]{5,}$\"\r\n"
    			+ "                  },\r\n"
    			+ "                  \"quantity\": {\r\n"
    			+ "                    \"type\": \"integer\",\r\n"
    			+ "                    \"maximum\": 100,\r\n"
    			+ "                    \"minimum\": 1\r\n"
    			+ "                  }\r\n"
    			+ "                },\r\n"
    			+ "                \"required\": [\"itemId\", \"quantity\"],\r\n"
    			+ "                \"type\": \"object\"\r\n"
    			+ "              }\r\n"
    			+ "            }\r\n"
    			+ "          ],\r\n"
    			+ "          \"responses\": {\r\n"
    			+ "            \"201\": {\r\n"
    			+ "              \"description\": \"Item added to cart successfully\",\r\n"
    			+ "              \"schema\": {\r\n"
    			+ "                \"properties\": {\r\n"
    			+ "                  \"cartItem\": {\r\n"
    			+ "                    \"$ref\": \"#/definitions/CartItem\"\r\n"
    			+ "                  },\r\n"
    			+ "                  \"message\": {\r\n"
    			+ "                    \"type\": \"string\"\r\n"
    			+ "                  },\r\n"
    			+ "                  \"success\": {\r\n"
    			+ "                    \"type\": \"boolean\"\r\n"
    			+ "                  }\r\n"
    			+ "                },\r\n"
    			+ "                \"type\": \"object\"\r\n"
    			+ "              }\r\n"
    			+ "            },\r\n"
    			+ "            \"400\": {\r\n"
    			+ "              \"description\": \"Invalid request parameters\",\r\n"
    			+ "              \"schema\": {\r\n"
    			+ "                \"type\": \"object\",\r\n"
    			+ "                \"properties\": {\r\n"
    			+ "                  \"errors\": {\r\n"
    			+ "                    \"type\": \"array\",\r\n"
    			+ "                    \"items\": {\r\n"
    			+ "                      \"type\": \"object\",\r\n"
    			+ "                      \"properties\": {\r\n"
    			+ "                        \"type\": {\r\n"
    			+ "                          \"type\": \"string\"\r\n"
    			+ "                        },\r\n"
    			+ "                        \"value\": {\r\n"
    			+ "                          \"type\": \"string\"\r\n"
    			+ "                        },\r\n"
    			+ "                        \"msg\": {\r\n"
    			+ "                          \"type\": \"string\"\r\n"
    			+ "                        },\r\n"
    			+ "                        \"path\": {\r\n"
    			+ "                          \"type\": \"string\"\r\n"
    			+ "                        },\r\n"
    			+ "                        \"location\": {\r\n"
    			+ "                          \"type\": \"string\"\r\n"
    			+ "                        }\r\n"
    			+ "                      },\r\n"
    			+ "                      \"required\": [\"type\", \"value\", \"msg\", \"path\", \"location\"]\r\n"
    			+ "                    }\r\n"
    			+ "                  }\r\n"
    			+ "                }\r\n"
    			+ "              }\r\n"
    			+ "            },\r\n"
    			+ "            \"500\": {\r\n"
    			+ "              \"description\": \"Internal Server Error\"\r\n"
    			+ "            }\r\n"
    			+ "          },\r\n"
    			+ "          \"summary\": \"Add an item to the cart\"\r\n"
    			+ "        }\r\n"
    			+ "      }\r\n"
    			+ "    },\r\n"
    			+ "    \"definitions\": {\r\n"
    			+ "      \"CartItem\": {\r\n"
    			+ "        \"properties\": {\r\n"
    			+ "          \"itemId\": {\r\n"
    			+ "            \"minLength\": 5,\r\n"
    			+ "            \"pattern\": \"^[a-zA-Z0-9]{5,}$\",\r\n"
    			+ "            \"type\": \"string\"\r\n"
    			+ "          },\r\n"
    			+ "          \"quantity\": {\r\n"
    			+ "            \"maximum\": 100,\r\n"
    			+ "            \"minimum\": 0,\r\n"
    			+ "            \"type\": \"integer\"\r\n"
    			+ "          }\r\n"
    			+ "        },\r\n"
    			+ "        \"required\": [\"itemId\", \"quantity\"],\r\n"
    			+ "        \"type\": \"object\"\r\n"
    			+ "      }\r\n"
    			+ "    },\r\n"
    			+ "    \"x-components\": {}\r\n"
    			+ "  }\r\n"
    			+ "}\r\n"
    			+ ""));
    	
		String userPrompt = "Given below api summary, request payload, response from api, expected response code from swagger and response code from the backend service and with swagger schema";

    	if(StringUtils.isNotEmpty(apiRequest.prompt)) {
    		userPrompt = apiRequest.prompt;
    	}else {
    		
    		userPrompt = "I ran an API test and it failed. Provide a brief summary of what went wrong. Then, provide some recommendations in the markdown format how to fix the issue with code snippets.\r\n"
    				+ "\r\n"
    				+ "Consider the following when providing recommendations:\r\n"
    				+ "- Generally, if the API response is an error, and it contains specific error information, and the API spec lacks this detail, then the spec could be 'under-speced' and needs to be updated to bring it in line with the API impelementation.\r\n"
    				+ "- When comparing the spec with the implementation, base your recommendation on what the spec says for the specific operation that was executed by the test (POST, GET, PUT, etc).\r\n"
    				+ "- If the implementation and spec disagree about a constraint, then the options are to update the spec to align it with the implementation, or to align the implementation with the spec. In this case, provide alternative options with code snippets so the user can choose. If the option is about updating the API spec, then provide a code snippet.\r\n"
    				+ "\r\n"
    				+ "[TEST NAME]:\r\n"
    				+ "{0}\r\n"
    				+ "\r\n"
    				+ "[TEST EXPECTED RESPONSE CODE]:\r\n"
    				+ "{1}\r\n"
    				+ "\r\n"
    				+ "[TEST ACTUAL RESPONSE CODE]:\r\n"
    				+ "{2}\r\n"
    				+ "\r\n"
    				+ "[API REQUEST]:\r\n"
    				+ "{3}:\r\n"
    				+ "{4}\r\n"
    				+ "\r\n"
    				+ "[API RESPONSE]:\r\n"
    				+ "```\r\n"
    				+ "{5}\r\n"
    				+ "```\r\n"
    				+ "\r\n"
    				+ "[API SPEC]:\r\n"
    				+ "swagger.json\r\n"
    				+ "```\r\n"
    				+ "{6}\r\n"
    				+ "```\r\n"
    				+ "\r\n";
    		
    		if(!StringUtils.isEmpty(apiRequest.implementationCode))
    			userPrompt +=  "[IMPLEMENTATION CODE]:\r\n"
        				+ "```\r\n"
        				+ "{7}\r\n"
        				+ "```\r\n";
    		
    		if(!StringUtils.isEmpty(apiRequest.schemaValidationErrors))
    			userPrompt +=  "[API RESPONSE SCHEMA VALIDATION ERRORS]:\r\n"
        				+ "{8}\r\n";
    		
    		MessageFormat mf = new MessageFormat(userPrompt, Locale.ROOT);
    		userPrompt = mf.format( new Object[] {apiRequest.testSummary, apiRequest.expectedResponseCode, apiRequest.responseCode, apiRequest.apiPath, apiRequest.requestPayload, apiRequest.response, apiRequest.swagger, apiRequest.implementationCode, apiRequest.schemaValidationErrors});
//    		userPrompt += "Api name \n\n" + apiRequest.testSummary + "\n\n";
//        	userPrompt += "Api Request \n\n" + apiRequest.requestPayload + "\n\n";
//        	userPrompt += "Api Response \n\n" + apiRequest.response + "\n\n";
//        	userPrompt += "Expected Response Code \n\n" + apiRequest.expectedResponseCode + "\n\n";
//        	userPrompt += "Response Code \n\n" + apiRequest.responseCode + "\n\n";
//        	userPrompt += "Swagger schema \n\n" + apiRequest.swagger + "\n\n";
//        	userPrompt += "I need to get the recommendation and modified complete swagger as only json output";
    	}
    	
    	//if(apiRequest.isSummaryInsight) {
    		// only pass user prompts
    		chatMessages = new ArrayList<>();
    	//}
		chatMessages.add(new ChatRequestUserMessage(userPrompt));
    	
		String engine = "gpt35";
		if(StringUtils.isNotEmpty(apiRequest.engine)) {
			engine = apiRequest.engine;
		}
    	ChatCompletions chatCompletions = client.getChatCompletions(engine,
    	    new ChatCompletionsOptions(chatMessages));

    	System.out.printf(Locale.ROOT, "Model ID=%s is created at %s.%n", chatCompletions.getId(), chatCompletions.getCreatedAt());
    	
    	String response = "";
    	
    	for (ChatChoice choice : chatCompletions.getChoices()) {
    	    ChatResponseMessage message = choice.getMessage();
    	    System.out.printf(Locale.ROOT, "Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
    	    System.out.printf(Locale.ROOT, "Message:");
    	    response += message.getContent();
    	}
    	
    	APIRequestInsight insight = new APIRequestInsight();
    	insight.request = apiRequest;
    	insight.insight = response;
    	insight.prompt = userPrompt;
    	
		return new ResponseEntity<>(insight, org.springframework.http.HttpStatus.OK);
		
	}
}
