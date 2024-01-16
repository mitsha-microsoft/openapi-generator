package org.apitest.scenarios.rules.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

public class ParameterRules {
	
	private CodegenOperation operation;
	
	public HashMap<PrimitiveValidParameterRuleTypes, List<CodegenParameter>> validPrimitiveParameters;
	public HashMap<PrimitiveInvalidParameterRuleTypes, List<CodegenParameter>> invalidPrimitiveParameters;
	public HashMap<ModelValidParameterTypes, List<CodegenParameter>> validModelParameters;
	
	public ParameterRules(CodegenOperation operation) {
		this.operation = operation;
		this.validPrimitiveParameters = new HashMap<>();
		this.invalidPrimitiveParameters = new HashMap<>();
		this.validModelParameters = new HashMap<>();	
	}
	
	public void addParametersRules() {
		addRequiredPrimitiveValidParameters();
		addMinLengthPrimitiveValidParameters();
		addMaxLengthPrimitiveValidParameters();
		addMinimumPrimitiveValidParameters();
		addMaximumPrimitiveValidParameters();
		addPatternPrimitiveValidParameters();
		addEnumPrimitiveValidParameters();
		addBinaryPrimitiveValidParameters();
	}
	
	public void addRequiredPrimitiveValidParameters() {
		List<CodegenParameter> params = new ArrayList<>();
		for(CodegenParameter cp : operation.allParams) {
			if(cp.required) {
				params.add(cp);
			}
		}
		this.validPrimitiveParameters.put(PrimitiveValidParameterRuleTypes.REQUIRED, params);
	}
	
	public void addMinLengthPrimitiveValidParameters() {
		List<CodegenParameter> params = new ArrayList<>();
		for(CodegenParameter cp : operation.allParams) {
			if(cp.minLength != null) {
				params.add(cp);
			}
		}
		this.validPrimitiveParameters.put(PrimitiveValidParameterRuleTypes.MIN_LENGTH, params);
	}
	
	public void addMaxLengthPrimitiveValidParameters() {
		List<CodegenParameter> params = new ArrayList<>();
		for(CodegenParameter cp : operation.allParams) {
			if(cp.maxLength != null) {
				params.add(cp);
			}
		}
		this.validPrimitiveParameters.put(PrimitiveValidParameterRuleTypes.MAX_LENGTH, params);
	}
	
	public void addMinimumPrimitiveValidParameters() {
		List<CodegenParameter> params = new ArrayList<>();
		for(CodegenParameter cp : operation.allParams) {
			if(cp.minimum != null) {
				params.add(cp);
			}
		}
		this.validPrimitiveParameters.put(PrimitiveValidParameterRuleTypes.MINIMUM, params);
	}
	
	public void addMaximumPrimitiveValidParameters() {
		List<CodegenParameter> params = new ArrayList<>();
		for(CodegenParameter cp : operation.allParams) {
			if(cp.maximum != null) {
				params.add(cp);
			}
		}
		this.validPrimitiveParameters.put(PrimitiveValidParameterRuleTypes.MAXIMUM, params);
	}
	
	public void addPatternPrimitiveValidParameters() {
		List<CodegenParameter> params = new ArrayList<>();
		for(CodegenParameter cp : operation.allParams) {
			if(StringUtils.isNotBlank(cp.pattern)) {
				params.add(cp);
			}
		}
		this.validPrimitiveParameters.put(PrimitiveValidParameterRuleTypes.PATTERN, params);
	}

	public void addEnumPrimitiveValidParameters() {
		List<CodegenParameter> params = new ArrayList<>();
		for(CodegenParameter cp : operation.allParams) {
			List<String> enumValues = cp._enum;
	        if (enumValues != null && !enumValues.isEmpty()) {
				params.add(cp);
			}
		}
		this.validPrimitiveParameters.put(PrimitiveValidParameterRuleTypes.ENUM_VALUE, params);
	}
	
	public void addBinaryPrimitiveValidParameters() {
		List<CodegenParameter> params = new ArrayList<>();
		for(CodegenParameter cp : operation.allParams) {
			if(cp.getSchema() != null && cp.getSchema().isFile) {
				params.add(cp);
			}
		}
		this.validPrimitiveParameters.put(PrimitiveValidParameterRuleTypes.FILE_TYPE, params);
	}
	
	
	public enum PrimitiveValidParameterRuleTypes {
		REQUIRED("required value fields"),
		MIN_LENGTH("min length value fields"),
		MAX_LENGTH("max length value fields"),
		MINIMUM("minimum value fields"),
		MAXIMUM("maximum value fields"),
		PATTERN("pattern value fields"),
		VALID_FORMAT("valid format value fields"),
		NUMBER_VALUE("number value fields"),
		ENUM_VALUE("enum value fields"),
		FILE_TYPE("file type value fields");
		
		private String desc;
		
		PrimitiveValidParameterRuleTypes(String desc) {
			this.desc = desc;
		}
		
		public String getDesc() {
			return desc;
		}
	}
	
	public enum PrimitiveInvalidParameterRuleTypes {
		NOT_REQUIRED("not all required value fields"),
		INVALID_MIN_LENGTH("invalid min length value fields"),
		INVALID_MAX_LENGTH("invalid max length value fields"), 
		INVALID_MINIMUM("invalid minimum value fields"),
		INVALID_MAXIMUM("invalid maximum value fields"),
		INVALID_PATTERN("invalid pattern value fields"),
		INVALID_FORMAT("invalid format value fields"),
		INVALID_NUMBER_VALUE("invalid number value fields"),
		INVALID_ENUM_VALUE("invalid enum value fields"),
		INVALID_BINARY_DATA("invalid binary data value fields"),
		INVALID_FILE_TYPE("invalid file type value fields");
		
		private String desc;
		
		PrimitiveInvalidParameterRuleTypes(String desc) {
			this.desc = desc;
		}
		
		public String getDesc() {
			return desc;
		}
	}
	
	public enum ModelValidParameterTypes {
		ALL_REQUIRED_PASSED,
		VALID_FORMAT_PASSED,
		OPTIONAL_SKIPPED,
		OPTIONAL_PASSED
	}
	
	public enum ModelInvalidParameterTypes{
		NOT_ALL_REQUIRED_PASSED,
		NOT_VALID_FORMAT_PASSED,		
	}
}

