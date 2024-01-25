package org.apitest.scenarios.rules.codegen;

import org.apitest.scenarios.rules.codegen.ParameterRules.ModelValidParameterTypes;
import org.apitest.scenarios.rules.codegen.ParameterRules.PrimitiveValidParameterRuleTypes;

public class Rule {
	private PrimitiveValidParameterRuleTypes primitiveType;
	private ModelValidParameterTypes modelType;
	
	public Rule() {

	}
	
	public Rule(PrimitiveValidParameterRuleTypes primitiveType, ModelValidParameterTypes modelType) {
		this.primitiveType = primitiveType;
		this.modelType = modelType;
	}
	
	public ModelValidParameterTypes getModelType() {
		return modelType;
	}
	
	public PrimitiveValidParameterRuleTypes getPrimitiveType() {
		return primitiveType;
	}
	
	public void setModelType(ModelValidParameterTypes modelType) {
		this.modelType = modelType;
	}
	
	public void setPrimitiveType(PrimitiveValidParameterRuleTypes primitiveType) {
		this.primitiveType = primitiveType;
	}
}	