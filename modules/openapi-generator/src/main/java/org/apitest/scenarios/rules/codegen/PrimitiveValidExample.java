package org.apitest.scenarios.rules.codegen;

import java.util.ArrayList;
import java.util.List;

import org.apitest.scenarios.rules.codegen.ParameterRules.PrimitiveValidParameterRuleTypes;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.core.util.Json;

public class PrimitiveValidExample {

	public List<Object> required;
	public List<Object> minLength;
	public List<Object> maxLength;
	public List<Object> minimum;
	public List<Object> maximum;
	public List<Object> enumValues;
	public List<Object> binary;
	public List<Object> pattern;
	
	public PrimitiveValidExample() {
		this.required = new ArrayList<>();
		this.minLength = new ArrayList<>();
		this.maxLength = new ArrayList<>();
		this.minimum = new ArrayList<>();
		this.maximum = new ArrayList<>();
		this.enumValues = new ArrayList<>();
		this.binary = new ArrayList<>();
		this.pattern = new ArrayList<>();
		
	}	
	
	public Object getExampleBasedOnType(PrimitiveValidParameterRuleTypes type) {
		if (type == PrimitiveValidParameterRuleTypes.REQUIRED) {
			return required.size() > 0 ? required.get(0) : null;
		} else if (type == PrimitiveValidParameterRuleTypes.MIN_LENGTH) {
			return minLength.size() > 0 ? minLength.get(0) : null;
		} else if (type == PrimitiveValidParameterRuleTypes.MAX_LENGTH) {
			return maxLength.size() > 0 ? maxLength.get(0) : null;
		} else if (type == PrimitiveValidParameterRuleTypes.MINIMUM) {
			return minimum.size() > 0 ? minimum.get(0) : null;
		} else if (type == PrimitiveValidParameterRuleTypes.MAXIMUM) {
			return maximum.size() > 0 ? maximum.get(0) : null;
		} else if (type == PrimitiveValidParameterRuleTypes.ENUM_VALUE) {
			return enumValues.size() > 0 ? enumValues.get(0) : null;
		} else if (type == PrimitiveValidParameterRuleTypes.FILE_TYPE) {
			return binary.size() > 0 ? binary.get(0) : null;
		} else if (type == PrimitiveValidParameterRuleTypes.PATTERN) {
			return pattern.size() > 0 ? pattern.get(0) : null;
		}
		return null;
	}

	public PrimitiveValidExample copy() {
		PrimitiveValidExample copy = new PrimitiveValidExample();
		try {
			String str = Json.mapper().writeValueAsString(this);
			copy = Json.mapper().readValue(str, PrimitiveValidExample.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();		
		}
		return copy;
	}
}
