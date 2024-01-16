package org.apitest.scenarios.codegen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apitest.scenarios.codegen.TestScenario.ScenarioType;
import org.apitest.scenarios.rules.codegen.ParameterRules;
import org.apitest.scenarios.rules.codegen.ParameterRules.PrimitiveInvalidParameterRuleTypes;
import org.apitest.scenarios.rules.codegen.ParameterRules.PrimitiveValidParameterRuleTypes;
import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.DefaultCodegen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.core.util.Json;

public class APITestScenario {
    private final Logger LOGGER = LoggerFactory.getLogger(APITestScenario.class);
    
    private CodegenConfig codegenConfig;
    
    public APITestScenario(CodegenConfig codegenConfig) {
    	this.codegenConfig = codegenConfig;
    }
    
	public List<TestScenario> scenarios;

	public List<TestScenario> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<TestScenario> scenarios) {
		this.scenarios = scenarios;
	}
	
	public void generateTestScenariosBasedOnAllParams(CodegenOperation operation) {
		this.scenarios =  new ArrayList<>();
		List<CodegenParameter> parameters = operation.allParams;
		
		for(int i=0;i<ParameterExampleGenerator.NO_OF_EXAMPLES;i++) {
			TestScenario ts = new TestScenario();
			ts.scenario = "Valid test case - " + String.valueOf(i+1);
			ts.description = "Valid test case";
			ts.expectedOutcome = new ExpectedOutcome();
			ts.expectedOutcome.responseBody = "Valid response";
			ts.expectedOutcome.statusCode = 200;
			ts.valid = true;
			ts.type = ScenarioType.MANUAL;
			ts.parameters = new ArrayList<>();
			for(CodegenParameter parameter : parameters) {
				CodegenParameter copy = parameter.copy();
				copy.example =  parameter.validValues.get(i).toString();
				ts.parameters.add(copy);
			}
			scenarios.add(ts);
		}
		
		for(int i=0;i<ParameterExampleGenerator.NO_OF_EXAMPLES;i++) {
			TestScenario ts = new TestScenario();
			ts.scenario = "Invalid test case - " + String.valueOf(i+1);
			ts.description = "Invalid test case";
			ts.expectedOutcome = new ExpectedOutcome();
			ts.expectedOutcome.responseBody = "Invalid response";
			ts.expectedOutcome.statusCode = 400;
			ts.parameters = new ArrayList<>();
			ts.valid = false;
			ts.type = ScenarioType.MANUAL;
			for(CodegenParameter parameter : parameters) {
				CodegenParameter copy = parameter.copy();
				copy.example =  parameter.invalidValues.get(i).toString();
				ts.parameters.add(copy);
			}
			scenarios.add(ts);
		}
		
		operation.scenarios = scenarios;
	}
	
	public void generateAPITestScenariosBasedOnRule(CodegenOperation operation) {
		this.scenarios =  new ArrayList<>();
		List<CodegenParameter> allParams = operation.allParams;
		
		ParameterRules rules = new ParameterRules(operation);
		rules.addParametersRules();
		
		Map<String, Object> additionalProperties = codegenConfig.additionalProperties();
		Map<String, Object> configurationOptions = additionalProperties == null ? null : (Map<String, Object>) additionalProperties.get("configuration");
		Map<String, Object> overrideParameters = configurationOptions == null ? null : (Map<String, Object>) configurationOptions.get("overrideParameters");
		
		if(allParams.size() == 0) {
			TestScenario ts = new TestScenario();
			ts.scenario = "Valid test scenario without any parameters" ;
			ts.expectedOutcome = new ExpectedOutcome();
			ts.expectedOutcome.responseBody = "Valid response";
			ts.expectedOutcome.statusCode = 200;
			ts.valid = true;
			ts.type = ScenarioType.MANUAL;
			ts.parameters = new ArrayList<>();
			scenarios.add(ts);
		}else {
			for(PrimitiveValidParameterRuleTypes type : PrimitiveValidParameterRuleTypes.values()) {
				LOGGER.info("#### generating test scenarios based on primitive type - {} #######", type.getDesc());
				TestScenario ts = new TestScenario();
				ts.scenario = "Valid test scenario based on " + type.getDesc();
				ts.expectedOutcome = new ExpectedOutcome();
				ts.expectedOutcome.responseBody = "Valid response";
				ts.expectedOutcome.statusCode = 200;
				ts.valid = true;
				ts.type = ScenarioType.MANUAL;
				ts.parameters = new ArrayList<>();
				
				boolean atLeastOneParameter = false;
				
				for(CodegenParameter parameter : operation.allParams) {
					CodegenParameter copy = parameter.copy();
					copy.example = "";
					if(parameter.primitiveValidExample != null) {
						Object example = parameter.primitiveValidExample.getExampleBasedOnType(type);
						if (example != null) {
							atLeastOneParameter = true;
							copy.example = example.toString();
						}
					}
					
					if (StringUtils.isBlank(copy.example)) {
						if(parameter.validValues.size() > 0) {
							copy.example = parameter.validValues.get(0).toString();	
						}else if(parameter.example != null){
							copy.example = parameter.example;	
						}else {
							copy.example = "undefined";
						}
					}
					
					if (overrideParameters != null && overrideParameters.containsKey(parameter.baseName)) {
						try {
							copy.example = Json.mapper().writeValueAsString(overrideParameters.get(parameter.baseName));
						} catch (JsonProcessingException e) {
							LOGGER.error("Error while parsing override parameter value for parameter {} in operation {}", parameter.baseName, operation.operationId);
							e.printStackTrace();
						}
					}
					
					ts.parameters.add(copy);
				}
				
				if(atLeastOneParameter) {
					scenarios.add(ts);
				}

			}
			
			for(PrimitiveInvalidParameterRuleTypes type : PrimitiveInvalidParameterRuleTypes.values()) {
				LOGGER.info("#### generating test scenarios based on invalid primitive type - {} #######", type.getDesc());
				TestScenario ts = new TestScenario();
				ts.scenario = "Invalid test scenario based on " + type.getDesc();
				ts.expectedOutcome = new ExpectedOutcome();
				ts.expectedOutcome.responseBody = "Invalid response";
				ts.expectedOutcome.statusCode = 400;
				ts.valid = true;
				ts.type = ScenarioType.MANUAL;
				ts.parameters = new ArrayList<>();
				
				boolean atLeastOneParameter = false;
				
				for(CodegenParameter parameter : operation.allParams) {
					CodegenParameter copy = parameter.copy();
					copy.example = "";
					if(parameter.primitiveInvalidExample != null) {
						Object example = parameter.primitiveInvalidExample.getInvalidPrimitiveValueBasedOnType(type);
						if (example != null) {
							atLeastOneParameter = true;
							copy.example = example.toString();
						}
					}
					
					if (StringUtils.isBlank(copy.example)) {
						if(parameter.invalidValues.size() > 0) {
							copy.example = parameter.invalidValues.get(0).toString();	
						}else if(parameter.example != null){
							copy.example = parameter.example;	
						}else {
							copy.example = "undefined";
						}
					}
					if (overrideParameters != null && overrideParameters.containsKey(parameter.paramName)) {
						try {
							copy.example = Json.mapper().writeValueAsString(overrideParameters.get(parameter.paramName));
						} catch (JsonProcessingException e) {
							LOGGER.error("Error while parsing override parameter value for parameter {} in operation {}", parameter.paramName, operation.operationId);
							e.printStackTrace();
						}
					}
					ts.parameters.add(copy);
				}
				
				if(atLeastOneParameter) {
					scenarios.add(ts);
				}

			}
		}
		
		operation.scenarios = scenarios;
	}
}
