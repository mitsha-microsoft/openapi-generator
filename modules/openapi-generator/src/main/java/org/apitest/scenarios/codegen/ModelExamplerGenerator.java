package org.apitest.scenarios.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apitest.scenarios.rules.codegen.ParameterRules.ModelValidParameterTypes;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.DefaultCodegen;
import org.openapitools.codegen.utils.ModelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;

public class ModelExamplerGenerator {

	private CodegenPropertyExampleGenerator codegenPropertyExampleGenerator;
	private DefaultCodegen defaultCodegen;
	private Logger LOGGER = LoggerFactory.getLogger(ModelExamplerGenerator.class);
	
	public ModelExamplerGenerator(DefaultCodegen defaultCodegen) {
		this.defaultCodegen = defaultCodegen;
		this.codegenPropertyExampleGenerator = new CodegenPropertyExampleGenerator(defaultCodegen);
	}
	
	public Map<ModelValidParameterTypes, List<TestScenario>> generateTestScenarios(CodegenParameter codegenParameter) throws JsonProcessingException {
	    
		Map<ModelValidParameterTypes, List<TestScenario>> scenarios = new HashMap<>();
		
	    List<CodegenProperty> requiredParams = codegenParameter.requiredVars;
	    List<String> requiredParamNames = requiredParams.stream().map(p -> p.baseName).toList();
	    List<CodegenProperty> allParams = new ArrayList<CodegenProperty>(codegenParameter.vars);
	    List<CodegenProperty> optionalParams =  allParams.stream().filter(p -> !requiredParamNames.contains(p.name)).toList();
	    
	    
		if(requiredParams.size() > 0) {
			scenarios.put(ModelValidParameterTypes.ONLY_REQUIRED_PARAMETERS_PASSED, generateExamplerWithAllRequiredFields(codegenParameter, requiredParams));
		}
	    
	    // Generate the test scenarios for all fields
		if(allParams.size() > 0) {
			scenarios.put(ModelValidParameterTypes.ALL_PARAMETERS_PASSED, generateExamplerWithAllFields(codegenParameter, allParams));
		}
		
		// Generate the test scenarios for all required fields and individual optional field
		if(optionalParams.size() > 0) {
			scenarios.put(ModelValidParameterTypes.INDIVIDUAL_OPTIONAL_PARAMETER_PASS,
					generateExamplesWithIndividualOptionalField(codegenParameter, requiredParams, optionalParams));
		}
		
		
	    return scenarios;
	}

	private List<TestScenario> generateExamplesWithIndividualOptionalField(CodegenParameter codegenParameter,
			List<CodegenProperty> requiredParams, List<CodegenProperty> optionalParams) throws JsonProcessingException {
		
		List<TestScenario> scenarios = new ArrayList<>();

		for (CodegenProperty p : optionalParams) {
			List<CodegenProperty> properties = new ArrayList<>(requiredParams.stream().toList());
			properties.add(p);
			String modelExample = generateModelExampleFromProperties(properties);
			TestScenario ts = new TestScenario();
			ts.parameters = new ArrayList<>();
			ts.scenario = String.format(Locale.ROOT, "Testing with all required and %s optional field", p.baseName);
			ts.description = String.format(Locale.ROOT,"Testing with all required and %s optional field", p.baseName);
			ts.valid = true;
			ts.expectedOutcome = new ExpectedOutcome(200, modelExample);
		    ts.parameters.add(cloneParameter(codegenParameter, modelExample));
		    scenarios.add(ts);
		}
	    
	    return scenarios;
	}

	private List<TestScenario> generateExamplerWithAllRequiredFields(CodegenParameter codegenParameter,
			List<CodegenProperty> requiredParams) throws JsonProcessingException {
		// Generate the test scenario for all required fields
//        Map<String, Schema> schemas = ModelUtils.getSchemas(this.defaultCodegen.getOpenAPI());
//
//		Schema parameterSchema = schemas.get(codegenParameter.baseType);
//		
//		if(codegenParameter.isBodyParam) {
//			RequestBody body = Json.mapper().convertValue(codegenParameter.jsonSchema, RequestBody.class);
//			parameterSchema = ModelUtils.getSchemaFromRequestBody(body);
//		}
		List<TestScenario> scenarios = new ArrayList<>();
	    for (CodegenProperty p : requiredParams) {
	    	//Schema propertySchema = this.defaultCodegen.propertySchemaCache.get(p);
	        //Schema propertySchema = Json.mapper().convertValue(p.jsonSchema, Schema.class);
//	    	if(p.primitiveValidExample != null && p.primitiveValidExample.required != null && p.primitiveValidExample.required.size() > 0) {
//		    	p.example = (String) p.primitiveValidExample.required.get(0);
//	        }else {
//	        	LOGGER.error("Could not find schema for property {}", p.name);
//	        }
	    	p.example = "demo_Example";
	    }
	    
	    String modelExample = generateModelExampleFromProperties(requiredParams);
	    
	    TestScenario ts1 = new TestScenario();
	    ts1.parameters =  new ArrayList<>();
	    ts1.scenario = "Testing api with all required fields";
	    ts1.description = "Testing api with all required fields";
	    ts1.valid = true;
	    ts1.expectedOutcome = new ExpectedOutcome(200, modelExample);
	    ts1.parameters.add(cloneParameter(codegenParameter, modelExample));
	    
	    scenarios.add(ts1);
		return scenarios;
	}
	
	private List<TestScenario> generateExamplerWithAllFields(CodegenParameter codegenParameter,
			List<CodegenProperty> allParams) throws JsonProcessingException {
		// Generate the test scenario for all required fields
//        Map<String, Schema> schemas = ModelUtils.getSchemas(this.defaultCodegen.getOpenAPI());
//
//		Schema parameterSchema = schemas.get(codegenParameter.baseType);
//		
//		if(codegenParameter.isBodyParam) {
//			RequestBody body = Json.mapper().convertValue(codegenParameter.jsonSchema, RequestBody.class);
//			parameterSchema = ModelUtils.getSchemaFromRequestBody(body);
//		}
		List<TestScenario> scenarios = new ArrayList<>();
	    for (CodegenProperty p : allParams) {
	    	//Schema propertySchema = this.defaultCodegen.propertySchemaCache.get(p);
	        //Schema propertySchema = Json.mapper().convertValue(p.jsonSchema, Schema.class);
//	    	if(p.primitiveValidExample != null && p.primitiveValidExample.required != null && p.primitiveValidExample.required.size() > 0) {
//		    	p.example = (String) p.primitiveValidExample.required.get(0);
//	        }else {
//	        	LOGGER.error("Could not find schema for property {}", p.name);
//	        }
	    	p.example = "demo_Example";
	    }
	    
	    String modelExample = generateModelExampleFromProperties(allParams);
	    
	    TestScenario ts1 = new TestScenario();
	    ts1.parameters =  new ArrayList<>();
	    ts1.scenario = "Testing api with all fields";
	    ts1.description = "Testing api with all fields";
	    ts1.valid = true;
	    ts1.expectedOutcome = new ExpectedOutcome(200, modelExample);
	    ts1.parameters.add(cloneParameter(codegenParameter, modelExample));
	    
	    scenarios.add(ts1);
		return scenarios;
	}

	private String generateModelExampleFromProperties(List<CodegenProperty> properties) throws JsonProcessingException{
		Map<String, Object> example = new HashMap<>();
		for (CodegenProperty p : properties) {
			example.put(p.name, p.example);
		}
		return Json.mapper().writeValueAsString(example);
	}
	
	private CodegenParameter cloneParameter(CodegenParameter parameter, String exampler) throws JsonProcessingException {
		CodegenParameter clone = parameter.copy();
		clone.example = exampler;
		return clone;
	}
}