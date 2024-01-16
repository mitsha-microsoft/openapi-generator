package org.apitest.scenarios.codegen;
import java.util.List;

import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

public class TestScenario {

	public String scenario;
	public String description;
	public List<CodegenParameter> parameters;
	public ScenarioType type;
	public boolean valid; 
	public CodegenOperation operation; 
	public ExpectedOutcome expectedOutcome;
	
	public enum ScenarioType{
		AI_GENERATED,
		MANUAL
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ExpectedOutcome getExpectedOutcome() {
		return expectedOutcome;
	}

	public void setExpectedOutcome(ExpectedOutcome expectedOutcome) {
		this.expectedOutcome = expectedOutcome;
	}

	public List<CodegenParameter> getParameter() {
		return parameters;
	}

	public void setParameter(List<CodegenParameter> parameters) {
		this.parameters = parameters;
	}

	public ScenarioType getType() {
		return type;
	}

	public void setType(ScenarioType type) {
		this.type = type;
	}

	public List<CodegenParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<CodegenParameter> parameters) {
		this.parameters = parameters;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public CodegenOperation getOperation() {
		return operation;
	}

	public void setOperation(CodegenOperation operation) {
		this.operation = operation;
	}
	
	
}
