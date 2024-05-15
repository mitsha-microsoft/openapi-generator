package org.apitest.scenarios.codegen;

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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public class GenerateAIBasedAPITestsScenario {
    
	private static final String OPENAI_API_KEY = "f62a2e9e9989487384827d169152f737";
    private static final String ENDPOINT = "https://vens-openai.openai.azure.com/";
    private static final String API_TESTING_ENDPOINT = "https://apitesting.openai.azure.com/";
    private static final String API_TESTING_KEY = "92eb2e08ed17450b80de7f7d24a94388";
    
    private OpenAIClient client; 
    
    public GenerateAIBasedAPITestsScenario() {
    	
    	this.client = new OpenAIClientBuilder()
        	    .credential(new AzureKeyCredential(API_TESTING_KEY))
        	    .endpoint(API_TESTING_ENDPOINT)
        	    .buildClient();
    	
    }
    
    public String Run(String userPrompt) {
    	List<ChatRequestMessage> chatMessages = new ArrayList<>();
    	chatMessages.add(new ChatRequestSystemMessage("I am an AI assistant which helps in providing API testing scenarios for a given swagger operation schema in JSON. I given API testing scenarios in the JSON format."));
    	chatMessages.add(new ChatRequestUserMessage("{\"tags\":[\"user\"],\"summary\":\"Logs user into the system\",\"operationId\":\"loginUser\",\"parameters\":[{\"name\":\"username\",\"in\":\"query\",\"description\":\"The user name for login\",\"required\":true,\"schema\":{\"type\":\"string\",\"extensions\":{},\"exampleSetFlag\":false,\"types\":[\"string\"]},\"extensions\":{}},{\"name\":\"password\",\"in\":\"query\",\"description\":\"The password for login in clear text\",\"required\":true,\"schema\":{\"type\":\"string\",\"extensions\":{},\"exampleSetFlag\":false,\"types\":[\"string\"]},\"extensions\":{}}],\"responses\":{\"200\":{\"description\":\"successful operation\",\"headers\":{\"X-Rate-Limit\":{\"description\":\"calls per hour allowed by the user\",\"schema\":{\"type\":\"integer\",\"format\":\"int32\",\"extensions\":{},\"exampleSetFlag\":false,\"types\":[\"integer\"]}},\"X-Expires-After\":{\"description\":\"date in UTC when token expires\",\"schema\":{\"type\":\"string\",\"format\":\"date-time\",\"extensions\":{},\"exampleSetFlag\":false,\"types\":[\"string\"]}}},\"content\":{\"application/json\":{\"schema\":{\"type\":\"string\",\"exampleSetFlag\":false,\"types\":[\"string\"]},\"exampleSetFlag\":false},\"application/xml\":{\"schema\":{\"type\":\"string\",\"exampleSetFlag\":false,\"types\":[\"string\"]},\"exampleSetFlag\":false}},\"extensions\":{}},\"400\":{\"description\":\"Invalid username/password supplied\",\"content\":{},\"extensions\":{}}},\"extensions\":{}}\r\n"
    			+ ""));
    	chatMessages.add(new ChatRequestAssistantMessage("[\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Valid Login\",\r\n"
    			+ "    \"description\": \"Test the API with valid credentials.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"username\": \"valid_user\",\r\n"
    			+ "      \"password\": \"valid_password\"\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 200,\r\n"
    			+ "      \"responseBody\": \"A string representing a token\",\r\n"
    			+ "      \"headers\": {\r\n"
    			+ "        \"X-Rate-Limit\": \"Numeric value indicating the calls per hour allowed.\",\r\n"
    			+ "        \"X-Expires-After\": \"Date in UTC when the token expires.\"\r\n"
    			+ "      }\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Missing Username\",\r\n"
    			+ "    \"description\": \"Test the API when the username is not provided.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"password\": \"valid_password\"\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 400,\r\n"
    			+ "      \"responseBody\": \"An error message indicating invalid username/password supplied.\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Missing Password\",\r\n"
    			+ "    \"description\": \"Test the API when the password is not provided.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"username\": \"valid_user\"\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 400,\r\n"
    			+ "      \"responseBody\": \"An error message indicating invalid username/password supplied.\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Invalid Credentials\",\r\n"
    			+ "    \"description\": \"Test the API with incorrect username and password.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"username\": \"invalid_user\",\r\n"
    			+ "      \"password\": \"invalid_password\"\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 400,\r\n"
    			+ "      \"responseBody\": \"An error message indicating invalid username/password supplied.\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Username with Special Characters\",\r\n"
    			+ "    \"description\": \"Test the API with a username containing special characters.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"username\": \"user@name\",\r\n"
    			+ "      \"password\": \"valid_password\"\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 200,\r\n"
    			+ "      \"responseBody\": \"A string representing a token\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Password with Special Characters\",\r\n"
    			+ "    \"description\": \"Test the API with a password containing special characters.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"username\": \"valid_user\",\r\n"
    			+ "      \"password\": \"pass!@#word\"\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 200,\r\n"
    			+ "      \"responseBody\": \"A string representing a token\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Username and Password with Leading/Trailing Spaces\",\r\n"
    			+ "    \"description\": \"Test the API with a username and password containing leading/trailing spaces.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"username\": \"  valid_user  \",\r\n"
    			+ "      \"password\": \"  valid_password  \"\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 200,\r\n"
    			+ "      \"responseBody\": \"A string representing a token\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Username and Password with XSS Attempt\",\r\n"
    			+ "    \"description\": \"Test the API with a username and password containing XSS attempt.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"username\": \"<script>alert('XSS')</script>\",\r\n"
    			+ "      \"password\": \"<script>alert('XSS')</script>\"\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 400,\r\n"
    			+ "      \"responseBody\": \"An error message indicating invalid username/password supplied.\"\r\n"
    			+ "    }\r\n"
    			+ "  }\r\n"
    			+ "]"));
    	
    	chatMessages.add(new ChatRequestUserMessage("{\"tags\":[\"pet\"],\"summary\":\"Deletes a pet\",\"operationId\":\"deletePet\",\"parameters\":[{\"name\":\"api_key\",\"in\":\"header\",\"schema\":{\"type\":\"string\",\"extensions\":{},\"exampleSetFlag\":false,\"types\":[\"string\"]},\"extensions\":{}},{\"name\":\"petId\",\"in\":\"path\",\"description\":\"Pet id to delete\",\"required\":true,\"schema\":{\"type\":\"integer\",\"format\":\"int64\",\"extensions\":{},\"exampleSetFlag\":false,\"types\":[\"integer\"]},\"extensions\":{}}],\"responses\":{\"400\":{\"description\":\"Invalid ID supplied\",\"content\":{},\"extensions\":{}},\"404\":{\"description\":\"Pet not found\",\"content\":{},\"extensions\":{}}},\"security\":[{\"petstore_auth\":[\"write:pets\",\"read:pets\"]}],\"extensions\":{}}\r\n"
    			+ ""));
    	
    	chatMessages.add(new ChatRequestAssistantMessage("[\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Valid Pet Deletion\",\r\n"
    			+ "    \"description\": \"Test the API with a valid pet ID and a valid API key.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"api_key\": \"valid_api_key\",\r\n"
    			+ "      \"petId\": 12345\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 200,\r\n"
    			+ "      \"responseBody\": \"Empty\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Missing API Key\",\r\n"
    			+ "    \"description\": \"Test the API when the API key is not provided.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"petId\": 12345\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 400,\r\n"
    			+ "      \"responseBody\": \"An error message indicating the missing API key.\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Missing Pet ID\",\r\n"
    			+ "    \"description\": \"Test the API when the pet ID is not provided.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"api_key\": \"valid_api_key\"\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 400,\r\n"
    			+ "      \"responseBody\": \"An error message indicating invalid ID supplied.\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Invalid Pet ID Type\",\r\n"
    			+ "    \"description\": \"Test the API with a non-integer value for pet ID.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"api_key\": \"valid_api_key\",\r\n"
    			+ "      \"petId\": \"abc\"\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 400,\r\n"
    			+ "      \"responseBody\": \"An error message indicating invalid ID supplied.\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Pet Not Found\",\r\n"
    			+ "    \"description\": \"Test the API with a valid API key but an invalid pet ID that does not exist.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"api_key\": \"valid_api_key\",\r\n"
    			+ "      \"petId\": 99999\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 404,\r\n"
    			+ "      \"responseBody\": \"An error message indicating pet not found.\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Unauthorized Deletion\",\r\n"
    			+ "    \"description\": \"Test the API with a valid pet ID but an invalid or missing API key.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"petId\": 12345\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 401,\r\n"
    			+ "      \"responseBody\": \"An error message indicating unauthorized access.\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Unauthorized Operation\",\r\n"
    			+ "    \"description\": \"Test the API with a valid pet ID and API key, but without the required write permissions.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"api_key\": \"valid_api_key\",\r\n"
    			+ "      \"petId\": 12345\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 403,\r\n"
    			+ "      \"responseBody\": \"An error message indicating insufficient permissions.\"\r\n"
    			+ "    }\r\n"
    			+ "  }\r\n"
    			+ "]"));
    	
    	chatMessages.add(new ChatRequestUserMessage("{\"tags\":[\"pet\"],\"summary\":\"Add a new pet to the store\",\"operationId\":\"addPet\",\"requestBody\":{\"description\":\"Pet object that needs to be added to the store\",\"content\":{\"application/json\":{\"schema\":{\"$ref\":\"#/components/schemas/Pet\",\"exampleSetFlag\":false},\"exampleSetFlag\":false},\"application/xml\":{\"schema\":{\"$ref\":\"#/components/schemas/Pet\",\"exampleSetFlag\":false},\"exampleSetFlag\":false}},\"required\":true,\"extensions\":{}},\"responses\":{\"405\":{\"description\":\"Invalid input\",\"content\":{},\"extensions\":{}}},\"security\":[{\"petstore_auth\":[\"write:pets\",\"read:pets\"]}],\"extensions\":{\"x-codegen-request-body-name\":\"body\"}}\r\n"
    			+ ""));
    	
    	chatMessages.add(new ChatRequestAssistantMessage("[\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Valid Pet Addition\",\r\n"
    			+ "    \"description\": \"Test the API with a valid pet object.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"body\": {\r\n"
    			+ "        \"id\": 123,\r\n"
    			+ "        \"name\": \"Fluffy\",\r\n"
    			+ "        \"category\": {\r\n"
    			+ "          \"id\": 1,\r\n"
    			+ "          \"name\": \"Cats\"\r\n"
    			+ "        },\r\n"
    			+ "        \"photoUrls\": [\"https://example.com/fluffy.jpg\"],\r\n"
    			+ "        \"tags\": [\r\n"
    			+ "          {\r\n"
    			+ "            \"id\": 1,\r\n"
    			+ "            \"name\": \"cute\"\r\n"
    			+ "          }\r\n"
    			+ "        ],\r\n"
    			+ "        \"status\": \"available\"\r\n"
    			+ "      }\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 200,\r\n"
    			+ "      \"responseBody\": \"Pet added successfully.\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Missing Pet Object\",\r\n"
    			+ "    \"description\": \"Test the API without providing the pet object.\",\r\n"
    			+ "    \"inputs\": {},\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 405,\r\n"
    			+ "      \"responseBody\": \"An error message indicating invalid input.\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Invalid Pet Object\",\r\n"
    			+ "    \"description\": \"Test the API with an invalid pet object.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"body\": {\r\n"
    			+ "        \"name\": \"Fluffy\",\r\n"
    			+ "        \"photoUrls\": [\"https://example.com/fluffy.jpg\"]\r\n"
    			+ "        // Missing required fields like 'id', 'category', 'tags', 'status'\r\n"
    			+ "      }\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 405,\r\n"
    			+ "      \"responseBody\": \"An error message indicating invalid input.\"\r\n"
    			+ "    }\r\n"
    			+ "  },\r\n"
    			+ "  {\r\n"
    			+ "    \"scenario\": \"Unauthorized Operation\",\r\n"
    			+ "    \"description\": \"Test the API without the required write permissions.\",\r\n"
    			+ "    \"inputs\": {\r\n"
    			+ "      \"body\": {\r\n"
    			+ "        \"id\": 123,\r\n"
    			+ "        \"name\": \"Fluffy\",\r\n"
    			+ "        \"category\": {\r\n"
    			+ "          \"id\": 1,\r\n"
    			+ "          \"name\": \"Cats\"\r\n"
    			+ "        },\r\n"
    			+ "        \"photoUrls\": [\"https://example.com/fluffy.jpg\"],\r\n"
    			+ "        \"tags\": [\r\n"
    			+ "          {\r\n"
    			+ "            \"id\": 1,\r\n"
    			+ "            \"name\": \"cute\"\r\n"
    			+ "          }\r\n"
    			+ "        ],\r\n"
    			+ "        \"status\": \"available\"\r\n"
    			+ "      }\r\n"
    			+ "    },\r\n"
    			+ "    \"expectedOutcome\": {\r\n"
    			+ "      \"statusCode\": 403,\r\n"
    			+ "      \"responseBody\": \"An error message indicating insufficient permissions.\"\r\n"
    			+ "    }\r\n"
    			+ "  }\r\n"
    			+ "]"));
    	
    	    	
    	chatMessages.add(new ChatRequestUserMessage(userPrompt));
    	
    	ChatCompletions chatCompletions = client.getChatCompletions("vens-chatgpt-completion1",
    	    new ChatCompletionsOptions(chatMessages));

    	System.out.printf(Locale.ROOT, "Model ID=%s is created at %s.%n", chatCompletions.getId(), chatCompletions.getCreatedAt());
    	
    	String response = "";
    	for (ChatChoice choice : chatCompletions.getChoices()) {
    	    ChatResponseMessage message = choice.getMessage();
    	    System.out.printf(Locale.ROOT, "Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
    	    System.out.printf(Locale.ROOT, "Message:");
    	    response += message.getContent();
    	}
    	return response;
    }
    
    public List<Object> GenerateExampleBasedOnAI(CodegenOperation op, String spec, String exampleJSON)
    {
    	Map<String, Object> paramExampleValues = new HashMap<>();
    	List<CodegenParameter> optionalParams = op.allParams.stream().filter(p -> !p.required).toList();
    	List<CodegenParameter> requiredParams = op.allParams.stream().filter(p -> p.required).toList();
    	
    	List<String> requiredParamNames = requiredParams.stream().map(p -> p.paramName).toList();
    	List<String> optionalParamNames = optionalParams.stream().map(p -> p.paramName).toList();
    	CodegenParameter body = op.bodyParam;
    	List<CodegenProperty> requiredVas = body.requiredVars;
    	List<CodegenProperty> optionalVars = body.vars.stream().filter(v -> !v.required).toList();
    	requiredParamNames = requiredVas.stream().map(p -> p.baseName).toList();
    	
		for(int i=0;i<optionalVars.size();i++)
		{	
			CodegenProperty param = optionalVars.get(i);
			String userPrompt = "Create a sampler data for the operation `{0}` which includes all the required parameters, the only optional parameter `{1}` and any of other dependent optional parameter based on description of it in swagger spec. Response should be only json format with summary, request, response as provided in examples.";

			MessageFormat mf  = new MessageFormat(userPrompt, Locale.ROOT);
			
			userPrompt = mf.format(new Object[] {op.operationIdOriginal, param.baseName});
			
	    	List<ChatRequestMessage> chatMessages = Prompt.getChatExamples(spec, exampleJSON);
	    	
//	    	if(i == 0) {
//	    		chatMessages = Prompt.getChatExamples(exampleJSON);
//	    	}
	    	
	    	System.out.println(userPrompt);
	    	chatMessages.add(new ChatRequestUserMessage(userPrompt));
	    	
	    	ChatCompletions chatCompletions = client.getChatCompletions("gpt4-api",
	        	    new ChatCompletionsOptions(chatMessages));
	        	
	        	String response = "";
	        	for (ChatChoice choice : chatCompletions.getChoices()) {
	        	    ChatResponseMessage message = choice.getMessage();
	        	    response += message.getContent();
	        	}
	        	response = response.replaceAll("```json", "");
	        	response = response.replaceAll("```", "");
	        	
	        	System.out.printf(Locale.ROOT, "Example for param %s: %s", param.baseName, response);
	        	paramExampleValues.put(param.baseName, response);
		}
		return paramExampleValues.values().stream().toList();
    }
}
