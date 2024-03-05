package org.apitest.scenarios.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apitest.scenarios.codegen.TestScenario.ScenarioType;
import org.apitest.scenarios.rules.codegen.ParameterRules;
import org.apitest.scenarios.rules.codegen.ParameterRules.ModelValidParameterTypes;
import org.apitest.scenarios.rules.codegen.ParameterRules.PrimitiveInvalidParameterRuleTypes;
import org.apitest.scenarios.rules.codegen.ParameterRules.PrimitiveValidParameterRuleTypes;
import org.apitest.scenarios.rules.codegen.PrimitiveValidExample;
import org.apitest.scenarios.rules.codegen.Rule;
import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.DefaultCodegen;
import org.openapitools.codegen.serializer.SerializerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.core.util.Json;

@SuppressWarnings("unchecked")
public class APITestScenario {
	private final Logger LOGGER = LoggerFactory.getLogger(APITestScenario.class);

	private DefaultCodegen defaultCodegen;

	public APITestScenario(DefaultCodegen defaultCodegen) {
		this.defaultCodegen = defaultCodegen;
	}

	public List<TestScenario> scenarios;

	public List<TestScenario> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<TestScenario> scenarios) {
		this.scenarios = scenarios;
	}

	public void generateTestScenariosBasedOnAllParams(CodegenOperation operation) {
		this.scenarios = new ArrayList<>();

		List<CodegenParameter> parameters = operation.allParams;

		for (int i = 0; i < ParameterExampleGenerator.NO_OF_EXAMPLES; i++) {
			TestScenario ts = new TestScenario();
			ts.scenario = "Valid test case - " + String.valueOf(i + 1);
			ts.description = "Valid test case";
			ts.expectedOutcome = new ExpectedOutcome();
			ts.expectedOutcome.responseBody = "Valid response";
			ts.expectedOutcome.statusCode = 200;
			ts.valid = true;
			ts.type = ScenarioType.MANUAL;
			ts.parameters = new ArrayList<>();
			for (CodegenParameter parameter : parameters) {
				CodegenParameter copy = parameter.copy();
				copy.example = parameter.validValues.get(i).toString();
				ts.parameters.add(copy);
			}
			scenarios.add(ts);
		}

		for (int i = 0; i < ParameterExampleGenerator.NO_OF_EXAMPLES; i++) {
			TestScenario ts = new TestScenario();
			ts.scenario = "Invalid test case - " + String.valueOf(i + 1);
			ts.description = "Invalid test case";
			ts.expectedOutcome = new ExpectedOutcome();
			ts.expectedOutcome.responseBody = "Invalid response";
			ts.expectedOutcome.statusCode = 400;
			ts.parameters = new ArrayList<>();
			ts.valid = false;
			ts.type = ScenarioType.MANUAL;
			for (CodegenParameter parameter : parameters) {
				CodegenParameter copy = parameter.copy();
				copy.example = parameter.invalidValues.get(i).toString();
				ts.parameters.add(copy);
			}
			scenarios.add(ts);
		}

		//operation.scenarios = scenarios;
	}

	public void generateTestScenariosForSingleOp(CodegenOperation operation) {
		this.scenarios = new ArrayList<>();

	
//		if(!operation.operationId.equals("addPet")) {
//			operation.scenarios = scenarios; 
//			return; 
//		}
		 
		List<CodegenParameter> allParams = operation.allParams;

		ParameterRules rules = new ParameterRules(operation);
		rules.addParametersRules();

		Map<String, Object> additionalProperties = defaultCodegen.additionalProperties();
		Map<String, Object> configurationOptions = additionalProperties == null ? null
				: (Map<String, Object>) additionalProperties.get("configuration");
		Map<String, Object> overrideParameters = configurationOptions == null ? null
				: (Map<String, Object>) configurationOptions.get("overrideParameters");
		OperationHeirarchies heirarchies = (OperationHeirarchies) additionalProperties.get("operationsHierarcy");
		Map<String, List<String>> operationHierarchyMap = new HashMap<String, List<String>>();

		if (heirarchies != null) {
			for (OperationHeirarchy heirarchy : heirarchies.hirearchies) {
				operationHierarchyMap.put(heirarchy.operationId, heirarchy.dependencies);
			}
		}

		if (allParams.size() == 0) {
			TestScenario ts = new TestScenario();
			ts.scenario = "Valid test scenario without any parameters";
			ts.expectedOutcome = new ExpectedOutcome();
			ts.expectedOutcome.responseBody = "Valid response";
			ts.expectedOutcome.statusCode = 200;
			ts.valid = true;
			ts.type = ScenarioType.MANUAL;
			ts.parameters = new ArrayList<>();
			scenarios.add(ts);
		} else {
			for (PrimitiveValidParameterRuleTypes type : PrimitiveValidParameterRuleTypes.values()) {
				LOGGER.info("#### generating test scenarios based on primitive type - {} #######", type.getDesc());
				TestScenario ts = new TestScenario();
				ts.scenario = type.getDesc();
				ts.expectedOutcome = new ExpectedOutcome();
				ts.expectedOutcome.responseBody = "Valid response";
				ts.expectedOutcome.statusCode = 200;
				ts.valid = true;
				ts.type = ScenarioType.MANUAL;
				ts.parameters = new ArrayList<>();

				boolean atLeastOneParameter = false;

				for (CodegenParameter parameter : operation.allParams) {
					CodegenParameter copy = parameter.copy();
					copy.example = "";
					if (parameter.primitiveValidExample != null) {
						Object example = parameter.primitiveValidExample.getExampleBasedOnType(type);
						if (example != null) {
							atLeastOneParameter = true;
							copy.example = example.toString();
						}
					}

					if (StringUtils.isBlank(copy.example)) {
						if (parameter.validValues.size() > 0) {
							copy.example = parameter.validValues.get(0).toString();
						} else if (parameter.example != null) {
							copy.example = parameter.example;
						} else {
							copy.example = "undefined";
						}
					}

					if (overrideParameters != null && overrideParameters.containsKey(parameter.baseName)) {
						try {
							copy.example = Json.mapper().writeValueAsString(overrideParameters.get(parameter.baseName));
						} catch (JsonProcessingException e) {
							LOGGER.error(
									"Error while parsing override parameter value for parameter {} in operation {}",
									parameter.baseName, operation.operationId);
							e.printStackTrace();
						}
					}

					ts.parameters.add(copy);
				}

				if (atLeastOneParameter) {
					scenarios.add(ts);
				}

			}

			for (PrimitiveInvalidParameterRuleTypes type : PrimitiveInvalidParameterRuleTypes.values()) {
				LOGGER.info("#### generating test scenarios based on invalid primitive type - {} #######",
						type.getDesc());
				TestScenario ts = new TestScenario();
				ts.scenario = type.getDesc();
				ts.expectedOutcome = new ExpectedOutcome();
				ts.expectedOutcome.responseBody = "Invalid response";
				ts.expectedOutcome.statusCode = 400;
				ts.valid = true;
				ts.type = ScenarioType.MANUAL;
				ts.parameters = new ArrayList<>();

				boolean atLeastOneParameter = false;

				for (CodegenParameter parameter : operation.allParams) {
					CodegenParameter copy = parameter.copy();
					copy.example = "";
					if (parameter.primitiveInvalidExample != null) {
						Object example = parameter.primitiveInvalidExample.getInvalidPrimitiveValueBasedOnType(type);
						if (example != null) {
							atLeastOneParameter = true;
							copy.example = example.toString();
						}
					}

					if (StringUtils.isBlank(copy.example)) {
						if (parameter.invalidValues.size() > 0) {
							copy.example = parameter.invalidValues.get(0).toString();
						} else if (parameter.example != null) {
							copy.example = parameter.example;
						} else {
							copy.example = "undefined";
						}
					}
					if (overrideParameters != null && overrideParameters.containsKey(parameter.paramName)) {
						try {
							copy.example = Json.mapper()
									.writeValueAsString(overrideParameters.get(parameter.paramName));
						} catch (JsonProcessingException e) {
							LOGGER.error(
									"Error while parsing override parameter value for parameter {} in operation {}",
									parameter.paramName, operation.operationId);
							e.printStackTrace();
						}
					}
					ts.parameters.add(copy);
				}

				if (atLeastOneParameter) {
					scenarios.add(ts);
				}

			}
		}

		//operation.scenarios = scenarios;
	}

	public void generateTestScenarionsForAllOps(List<CodegenOperation> operations) {

	}
	
	public void generateTestScenariosBasedOnRules(CodegenOperation operation) throws JsonProcessingException {
		
		GenerateAIBasedAPITestsScenario aiBasedAPITestsScenario = new GenerateAIBasedAPITestsScenario();
		String exampleJSON = defaultCodegen.additionalProperties().get("exampleJSON").toString();
		List<Object> examples1 = aiBasedAPITestsScenario.GenerateExampleBasedOnAI(operation, SerializerUtils.toJsonString(this.defaultCodegen.getOpenAPI()), exampleJSON);
		operation.scenarios = examples1;
		
		
		this.scenarios = new ArrayList<>();
		
		Map<String, Object> additionalProperties = defaultCodegen.additionalProperties();
		Map<String, Object> configurationOptions = additionalProperties == null ? null
				: (Map<String, Object>) additionalProperties.get("configuration");
		Map<String, Object> overrideParameters = configurationOptions == null ? null
				: (Map<String, Object>) configurationOptions.get("overrideParameters");
		
		ParameterRules parameterRules = new ParameterRules(operation);
		List<TestScenario> scenarios = new ArrayList<>();
		List<Rule> rules = parameterRules.getAllRules();
		
		List<CodegenParameter> primitiveParameters = new ArrayList<>();
		List<CodegenParameter> modelParameters = new ArrayList<>();
		ModelExamplerGenerator modelExampleGenerator = new ModelExamplerGenerator(this.defaultCodegen);
		
		for (CodegenParameter p : operation.allParams) {
			if (!p.isModel) {
				primitiveParameters.add(p);
			}else {
				modelParameters.add(p);
			}
		}
		
		for(Rule rule : rules) {
			PrimitiveValidParameterRuleTypes type = rule.getPrimitiveType();
			ModelValidParameterTypes modelType = rule.getModelType();
			List<CodegenParameter> primitiveExamples = new ArrayList<>();		
			
			
			boolean atLeastOneParameter = false;
			
			if(primitiveParameters.size() > 0) {
				
				for (CodegenParameter parameter : primitiveParameters) {
					CodegenParameter copy = parameter.copy();
					copy.example = "";
					
					if (parameter.primitiveValidExample != null) {
						Object example = parameter.primitiveValidExample.getExampleBasedOnType(type);
						if (example != null) {
							atLeastOneParameter = true;
							copy.example = example.toString();
						}
					}

					if (StringUtils.isBlank(copy.example)) {
						if (parameter.validValues != null && parameter.validValues.size() > 0) {
							copy.example = parameter.validValues.get(0).toString();
						} else if (parameter.example != null) {
							copy.example = parameter.example;
						} else {
							copy.example = "undefined";
						}
					}

					if (overrideParameters != null && overrideParameters.containsKey(parameter.baseName)) {
						try {
							copy.example = Json.mapper().writeValueAsString(overrideParameters.get(parameter.baseName));
						} catch (JsonProcessingException e) {
							LOGGER.error(
									"Error while parsing override parameter value for parameter {} in operation {}",
									parameter.baseName, operation.operationId);
							e.printStackTrace();
						}
					}

					primitiveExamples.add(copy);
				}
			}
			
			Map<String, List<TestScenario>> modelParametersValues = new HashMap<>();
			
			int maxModelExamples = 0;
			for(CodegenParameter p : modelParameters) {
				CodegenParameter copy = p.copy();
                copy.example = "";
                
                Map<ModelValidParameterTypes, List<TestScenario>> examples = modelExampleGenerator.generateTestScenarios(p);
                
                List<TestScenario> exampleScenario = examples.get(modelType);
                
                // if there is examples based on model rule type
                if(exampleScenario != null && exampleScenario.size() > 0) {
                	modelParametersValues.put(p.baseName, exampleScenario);
                	maxModelExamples = maxModelExamples < exampleScenario.size() ? exampleScenario.size() : maxModelExamples;
                } 
			}
			
			if(maxModelExamples > 0) {
				for(int i=0; i< maxModelExamples; i++) {
					List<CodegenParameter> allParameters = new ArrayList<>();
					allParameters.addAll(primitiveExamples);
					String test_desc = "";
					for(CodegenParameter param : modelParameters) {
						List<TestScenario> exampleValues = modelParametersValues.get(param.baseName);
						if(exampleValues != null && exampleValues.size() > i) {
							allParameters.add(exampleValues.get(i).parameters.get(0));
							if (StringUtils.isNotBlank(exampleValues.get(i).getDescription())) {
								test_desc += exampleValues.get(i).getDescription() + " ";
							}
						}else {
							allParameters.add(exampleValues.get(0).parameters.get(0));
						}
					}
					
					TestScenario ts = new TestScenario();
					ts.scenario = test_desc;
					ts.expectedOutcome = new ExpectedOutcome();
					ts.expectedOutcome.responseBody = "Valid response";
					ts.expectedOutcome.statusCode = 200;
					ts.valid = true;
					ts.type = ScenarioType.MANUAL;
					ts.parameters = allParameters;

					scenarios.add(ts);
				}
				
				
			}else if(primitiveExamples.size() > 0){
				
				TestScenario ts = new TestScenario();
				ts.scenario = type.getDesc();
				ts.expectedOutcome = new ExpectedOutcome();
				ts.expectedOutcome.responseBody = "Valid response";
				ts.expectedOutcome.statusCode = 200;
				ts.valid = true;
				ts.type = ScenarioType.MANUAL;
				ts.parameters = primitiveExamples;

				scenarios.add(ts);
			}			
		}
		//operation.scenarios = scenarios;
	}
}
