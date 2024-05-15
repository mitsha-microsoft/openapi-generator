package org.apitest.scenarios.rules.codegen;

import java.util.ArrayList;
import java.util.List;

import org.apitest.scenarios.rules.codegen.ParameterRules.PrimitiveInvalidParameterRuleTypes;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.core.util.Json;

public class PrimitiveInvalidExample {

	public PrimitiveInvalidExample() {
		this.invalidType = new ArrayList<>();
		this.invalidFormat = new ArrayList<>();
		this.invalidEnum = new ArrayList<>();
		this.invalidPattern = new ArrayList<>();
		this.invalidMinLength = new ArrayList<>();
		this.invalidMaxLength = new ArrayList<>();
		this.invalidMinimum = new ArrayList<>();
		this.invalidMaximum = new ArrayList<>();
		this.invalidBinary = new ArrayList<>();
		this.invalidRequired = new ArrayList<>();
		this.invalidNullable = new ArrayList<>();
		this.invalidReadOnly = new ArrayList<>();
		this.invalidDeprecated = new ArrayList<>();
		this.invalidAllowEmptyValue = new ArrayList<>();
		this.invalidCollectionFormat = new ArrayList<>();
		this.invalidExclusiveMaximum = new ArrayList<>();
		this.invalidExclusiveMinimum = new ArrayList<>();
		this.invalidUniqueItems = new ArrayList<>();
		this.invalidMultipleOf = new ArrayList<>();
		this.invalidMinItems = new ArrayList<>();
		this.invalidMaxItems = new ArrayList<>();
		this.invalidMinProperties = new ArrayList<>();
		this.invalidMaxProperties = new ArrayList<>();
		this.invalidAdditionalProperties = new ArrayList<>();
		this.invalidItems = new ArrayList<>();
		this.invalidProperties = new ArrayList<>();
		this.invalidAllOf = new ArrayList<>();
		this.invalidOneOf = new ArrayList<>();
		this.invalidAnyOf = new ArrayList<>();
		this.invalidNot = new ArrayList<>();
		this.invalidRequiredProperties = new ArrayList<>();
		this.invalidFileTypes = new ArrayList<>();
	}
	
	public List<Object> invalidType;
	
	public List<Object> invalidFormat;
	
	public List<Object> invalidEnum;
	
	public List<Object> invalidPattern;
	
	public List<Object> invalidMinLength;
	
	public List<Object> invalidMaxLength;
	
	public List<Object> invalidMinimum;
	
	public List<Object> invalidMaximum;
	
	public List<Object> invalidBinary;
	
	public List<Object> invalidRequired;
	    
    public List<Object> invalidNullable;
    
    public List<Object> invalidReadOnly;
        
    public List<Object> invalidDeprecated;
    
    public List<Object> invalidAllowEmptyValue;
    
    public List<Object> invalidCollectionFormat;
    
    public List<Object> invalidExclusiveMaximum;
    
    public List<Object> invalidExclusiveMinimum;
    
    public List<Object> invalidUniqueItems;
    
    public List<Object> invalidMultipleOf;
    
    public List<Object> invalidMinItems;
    
    public List<Object> invalidMaxItems;
    
    public List<Object> invalidMinProperties;
    
    public List<Object> invalidMaxProperties;
    
    public List<Object> invalidAdditionalProperties;
    
    public List<Object> invalidItems;
    
    public List<Object> invalidProperties;
    
    public List<Object> invalidAllOf;
    
    public List<Object> invalidOneOf;
    
    public List<Object> invalidAnyOf;
    
    public List<Object> invalidNot;
    
    public List<Object> invalidRequiredProperties;
    
    public List<Object> invalidFileTypes;
    
    
    public Object getInvalidPrimitiveValueBasedOnType(PrimitiveInvalidParameterRuleTypes type) {
    	if(type == PrimitiveInvalidParameterRuleTypes.NOT_REQUIRED) {
    		return this.invalidRequired.size() > 0 ? this.invalidRequired.get(0) : null;
    	}else if(type == PrimitiveInvalidParameterRuleTypes.INVALID_MIN_LENGTH) {
    		return this.invalidMaxLength.size() > 0 ? this.invalidMaxLength.get(0) : null;
    	}else if(type == PrimitiveInvalidParameterRuleTypes.INVALID_MIN_LENGTH) {
    		return this.invalidMinLength.size() > 0 ? this.invalidMinLength.get(0) : null;
		}else if(type == PrimitiveInvalidParameterRuleTypes.INVALID_MINIMUM) {
			return this.invalidMinimum.size() > 0 ? this.invalidMinimum.get(0) : null;
		} else if(type == PrimitiveInvalidParameterRuleTypes.INVALID_MAXIMUM) {
			return this.invalidMaximum.size() > 0 ? this.invalidMaximum.get(0) : null;
		}else if(type == PrimitiveInvalidParameterRuleTypes.INVALID_PATTERN) {
			return this.invalidPattern.size() > 0 ? this.invalidPattern.get(0) : null;
		}else if(type == PrimitiveInvalidParameterRuleTypes.INVALID_FORMAT) {
			return this.invalidFormat.size() > 0 ? this.invalidFormat.get(0) : null;
		} else if(type == PrimitiveInvalidParameterRuleTypes.INVALID_NUMBER_VALUE) {
			return this.invalidType.size() > 0 ? this.invalidType.get(0) : null;
		} else if(type == PrimitiveInvalidParameterRuleTypes.INVALID_ENUM_VALUE) {
			return this.invalidEnum.size() > 0 ? this.invalidEnum.get(0) : null;
		} else if (type == PrimitiveInvalidParameterRuleTypes.INVALID_FILE_TYPE) {
			return this.invalidFileTypes.size() > 0 ? this.invalidFileTypes.get(0) : null;
		}
    	
    	return null;
    }
    
    public PrimitiveInvalidExample copy() {
    	PrimitiveInvalidExample copy = new PrimitiveInvalidExample();
		try {
			String str = Json.mapper().writeValueAsString(this);
			copy = Json.mapper().readValue(str, PrimitiveInvalidExample.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();		
		}
		return copy;
	}
}
