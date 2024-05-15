package org.apitest.scenarios.codegen;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apitest.scenarios.rules.codegen.BinaryParameterGenerator;
import org.apitest.scenarios.rules.codegen.DateParameterGenerator;
import org.apitest.scenarios.rules.codegen.DateTimeParameterGenerator;
import org.apitest.scenarios.rules.codegen.FileParameterGenerator;
import org.apitest.scenarios.rules.codegen.IntegerParameterGenerator;
import org.apitest.scenarios.rules.codegen.NumberParameterGenerator;
import org.apitest.scenarios.rules.codegen.ParameterRules.PrimitiveValidParameterRuleTypes;
import org.apitest.scenarios.rules.codegen.PrimitiveInvalidExample;
import org.apitest.scenarios.rules.codegen.PrimitiveValidExample;
import org.apitest.scenarios.rules.codegen.StringParameterGenerator;
import org.openapitools.codegen.CodegenDiscriminator;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.DefaultCodegen;
import org.openapitools.codegen.CodegenDiscriminator.MappedModel;
import org.openapitools.codegen.utils.ModelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;

@SuppressWarnings("rawtypes")
public class ParameterExampleGenerator {

    private final Logger LOGGER = LoggerFactory.getLogger(ParameterExampleGenerator.class);
    private DefaultCodegen codegen;
    public final static int NO_OF_EXAMPLES = 2;
    private final DateTimeFormatter iso8601Date = DateTimeFormatter.ISO_DATE;
    private final DateTimeFormatter iso8601DateTime = DateTimeFormatter.ISO_DATE_TIME;
    
    public ParameterExampleGenerator(DefaultCodegen codegen) {
    	this.codegen = codegen;
    }

    public String typescriptDate(Object dateValue) {
        String strValue = null;
        if (dateValue instanceof OffsetDateTime) {
            OffsetDateTime date = null;
            try {
                date = (OffsetDateTime) dateValue;
            } catch (ClassCastException e) {
                LOGGER.warn("Invalid `date` format for value {}", dateValue);
                date = ((Date) dateValue).toInstant().atOffset(ZoneOffset.UTC);
            }
            strValue = date.format(iso8601Date);
        } else {
            strValue = dateValue.toString();
        }
        return "new Date('" + strValue + "').toISOString().split('T')[0];";
    }

    public String typescriptDateTime(Object dateTimeValue) {
        String strValue = null;
        if (dateTimeValue instanceof OffsetDateTime) {
            OffsetDateTime dateTime = null;
            try {
                dateTime = (OffsetDateTime) dateTimeValue;
            } catch (ClassCastException e) {
                LOGGER.warn("Invalid `date-time` format for value {}", dateTimeValue);
                dateTime = ((Date) dateTimeValue).toInstant().atOffset(ZoneOffset.UTC);
            }
            strValue = dateTime.format(iso8601DateTime);
        } else {
            strValue = dateTimeValue.toString();
        }
        return "new Date('" + strValue + "')";
    }
    
	public void GenerateExample(CodegenParameter codegenParameter, Schema schema) {
        
        codegenParameter.validValues = new ArrayList<Object>();
        codegenParameter.invalidValues = new ArrayList<Object>();

        for(int i = 0; i < NO_OF_EXAMPLES; i++) {
        	Object validExamples = generateExamples(codegenParameter, schema, true);
            Object invalidExamples = generateExamples(codegenParameter, schema, false);
            if(validExamples != null) {
                codegenParameter.validValues.add(validExamples);
            }else {
            	// TODO need to check more on the scenario
            	break;
            }
            if(invalidExamples != null) {
                codegenParameter.invalidValues.add(invalidExamples);
            }
        }
    }
	
	public Object generateExamples(CodegenParameter codegenParameter, Schema schema, boolean isValid) {
		Object example = exampleFromStringOrArraySchema(codegenParameter, schema, codegenParameter.baseName, isValid);
		if (example == null) {
			example = toExampleValue(codegenParameter, schema, isValid);
			if( (codegenParameter.isModel || codegenParameter.isArray || codegenParameter.isMap) 
					&& codegenParameter.primitiveValidExample == null && isValid) {
				codegenParameter.primitiveValidExample = new PrimitiveValidExample();
				codegenParameter.primitiveValidExample.required.add(example);
			} else if( (codegenParameter.isModel || codegenParameter.isArray || codegenParameter.isMap) 
					&& codegenParameter.primitiveInvalidExample == null && !isValid ) {
				if(codegenParameter.required) {
					codegenParameter.primitiveInvalidExample = new PrimitiveInvalidExample();
					codegenParameter.primitiveInvalidExample.invalidRequired.add("");
				}else {
					codegenParameter.primitiveInvalidExample = new PrimitiveInvalidExample();
					codegenParameter.primitiveInvalidExample.invalidRequired.add("");
				}
			}
		}
        return example;
	}
	
	private String toExampleValue(CodegenParameter codegenParameter, Schema schema, boolean isValid) {
        String modelName = this.getModelName(schema);
        return toExampleValueRecursive(codegenParameter, modelName, schema, 1, "", 0, Sets.newHashSet(), isValid);
    }
	
	private Object exampleFromStringOrArraySchema(CodegenParameter codegenParameter, Schema sc, String propName, boolean isValid) {
        
		Schema schema = sc;
        
        String ref = sc.get$ref();
        
        if (ref != null) {
            schema = ModelUtils.getSchema(this.codegen.getOpenAPI(), ModelUtils.getSimpleRef(ref));
        }
                
        if (this.simpleStringSchema(schema)) {
        	StringParameterGenerator pg = new StringParameterGenerator(this.codegen.fromProperty(propName, schema));
        	if(codegenParameter.primitiveValidExample == null && codegenParameter.isString) {
        		codegenParameter.primitiveValidExample = pg.getPrimitiveValidExample();
        		codegenParameter.primitiveInvalidExample = pg.getPrimitiveInvalidExample();
        	}
            if(!isValid) {
            	return ensureQuotes((String) pg.generateInvalidExample());
            }else {
            	return pg.generateValidExample();
            }
        }
        return null;
    }
	
    private Boolean simpleStringSchema(Schema schema) {
        Schema sc = schema;
        String ref = schema.get$ref();
        if (ref != null) {
            sc = ModelUtils.getSchema(this.codegen.getOpenAPI(), ModelUtils.getSimpleRef(ref));
        }
        return ModelUtils.isStringSchema(sc) && !ModelUtils.isDateSchema(sc) && !ModelUtils.isDateTimeSchema(sc) && !"Number".equalsIgnoreCase(sc.getFormat()) && !ModelUtils.isByteArraySchema(sc) && !ModelUtils.isBinarySchema(sc) && schema.getPattern() == null;
    }
    
    private String toExampleValueRecursive(CodegenParameter root ,String modelName, Schema schema, int indentationLevel, String prefix, Integer exampleLine, Set<Schema> seenSchemas, boolean isValid) {
        final String indentionConst = "  ";
        String currentIndentation = "";
        String closingIndentation = "";
        for (int i = 0; i < indentationLevel; i++) currentIndentation += indentionConst;
        if (exampleLine.equals(0)) {
            closingIndentation = currentIndentation;
            currentIndentation = "";
        } else {
            closingIndentation = currentIndentation;
        }
        String openChars = "";
        String closeChars = "";
        String fullPrefix = currentIndentation + prefix + openChars;

        String example = null;
        
        // checks if the current schema has already been passed in. If so, breaks the current recursive pass
        if (seenSchemas.contains(schema)) {
            if (modelName != null) {
                return fullPrefix + closeChars;
            } else {
                // this is a recursive schema
                // need to add a reasonable example to avoid
                // infinite recursion
                if(isValid) {
                	if (ModelUtils.isNullable(schema)) {
                        // if the schema is nullable, then 'null' is a valid value
                        return fullPrefix + "null" + closeChars;
                    } else if (ModelUtils.isArraySchema(schema)) {
                        // the schema is an array, add an empty array
                        return fullPrefix + "[]" + closeChars;
                    } else {
                        // the schema is an object, make an empty object
                        return fullPrefix + "{}" + closeChars;
                    }
                }else {
                	
                	// TODO need to check this later for invalid scenario
                	return fullPrefix + "undefined" + closeChars;
                }
            }
        }

        if (null != schema.get$ref()) {
            Map<String, Schema> allDefinitions = ModelUtils.getSchemas(this.codegen.getOpenAPI());
            String ref = ModelUtils.getSimpleRef(schema.get$ref());
            Schema refSchema = allDefinitions.get(ref);
            if (null == refSchema) {
                LOGGER.warn("Unable to find referenced schema " + schema.get$ref() + "\n");
                return fullPrefix + "null" + closeChars;
            }
            String refModelName = getModelName(schema);
            return toExampleValueRecursive(root, refModelName, refSchema, indentationLevel, prefix, exampleLine, seenSchemas, isValid);
        } else if (ModelUtils.isNullType(schema) || ModelUtils.isAnyType(schema)) {
            // The 'null' type is allowed in OAS 3.1 and above. It is not supported by OAS 3.0.x,
            // though this tooling supports it.
            if(isValid) {
            	return fullPrefix + "null" + closeChars;
            }else {
            	return fullPrefix + "undefined" + closeChars;
            }
        } else if (ModelUtils.isBooleanSchema(schema)) {
        	if(isValid){
        		example = "true";
                return fullPrefix + example + closeChars;
        	}else {
        		example = "undefined";
        		return fullPrefix + example + closeChars;
        	}
            
        } else if (ModelUtils.isDateSchema(schema)) {
        	CodegenProperty property = this.codegen.fromProperty("inner", schema);  
			DateParameterGenerator pg = new DateParameterGenerator(property);
			if(root.primitiveValidExample == null && root.isDate) {
				root.primitiveValidExample = pg.getPrimitiveValidExample();
        		root.primitiveInvalidExample = pg.getPrimitiveInvalidExample();
        	}
            if(isValid) {
            	return fullPrefix + typescriptDate(pg.generateValidExample()) + closeChars;
            }else {
            	return fullPrefix + pg.generateInvalidExample() + closeChars;
            }
            
        } else if (ModelUtils.isDateTimeSchema(schema)) {
        	CodegenProperty property = this.codegen.fromProperty("inner", schema);  
        	DateTimeParameterGenerator pg = new DateTimeParameterGenerator(property);
        	if(root.primitiveValidExample == null && root.isDateTime) {
				root.primitiveValidExample = pg.getPrimitiveValidExample();
        		root.primitiveInvalidExample = pg.getPrimitiveInvalidExample();

        	}
            if(isValid) {
            	return fullPrefix + typescriptDateTime(pg.generateValidExample()) + closeChars;
            }else {
            	return fullPrefix + pg.generateInvalidExample() + closeChars;
            }
        }else if (ModelUtils.isBinarySchema(schema)) {
        	CodegenProperty property = this.codegen.fromProperty("inner", schema);  
        	FileParameterGenerator pg = new FileParameterGenerator(property);
        	if(root.primitiveValidExample == null && root.isBinary) {
				root.primitiveValidExample = pg.getPrimitiveValidExample();
        		root.primitiveInvalidExample = pg.getPrimitiveInvalidExample();
        	}
            if(isValid) {
            	return fullPrefix + pg.generateValidExample() + closeChars;
            }else {
            	return fullPrefix + pg.generateInvalidExample() + closeChars;
            }
        } else if (ModelUtils.isByteArraySchema(schema)) {
        	CodegenProperty property = this.codegen.fromProperty("inner", schema);  
        	BinaryParameterGenerator pg = new BinaryParameterGenerator(property);
        	if(root.primitiveValidExample == null  && root.isByteArray) {
				root.primitiveValidExample = pg.getPrimitiveValidExample();
        		root.primitiveInvalidExample = pg.getPrimitiveInvalidExample();
        	}
            if(isValid) {
            	return fullPrefix + pg.generateValidExample() + closeChars;
            }else {
            	return fullPrefix + pg.generateInvalidExample() + closeChars;
            }        	
        } else if (ModelUtils.isStringSchema(schema)) {
        	CodegenProperty property = this.codegen.fromProperty("inner", schema);  
        	StringParameterGenerator pg = new StringParameterGenerator(property);
        	if(root.primitiveValidExample == null && root.isString) {
				root.primitiveValidExample = pg.getPrimitiveValidExample();
        		root.primitiveInvalidExample = pg.getPrimitiveInvalidExample();
        	}
            if(isValid) {
            	return fullPrefix + ensureQuotes((String) pg.generateValidExample()) + closeChars;
            }else {
            	return fullPrefix + pg.generateInvalidExample() + closeChars;
            }
        } else if (ModelUtils.isIntegerSchema(schema) ) {
        	CodegenProperty property = this.codegen.fromProperty("inner", schema);  
        	IntegerParameterGenerator pg = new IntegerParameterGenerator(property);
        	if(root.primitiveValidExample == null  && (root.isInteger || root.isNumber || root.isLong || root.isFloat || root.isDouble)	) {
				root.primitiveValidExample = pg.getPrimitiveValidExample();
        		root.primitiveInvalidExample = pg.getPrimitiveInvalidExample();
        	}
            if(isValid) {
            	return fullPrefix + pg.generateValidExample() + closeChars;
            }else {
            	return fullPrefix + pg.generateInvalidExample() + closeChars;
            }
        }else if (ModelUtils.isNumberSchema(schema) || ModelUtils.isLongSchema(schema) || ModelUtils.isFloatSchema(schema) || ModelUtils.isDoubleSchema(schema)) {
        	CodegenProperty property = this.codegen.fromProperty("inner", schema);  
        	NumberParameterGenerator pg = new NumberParameterGenerator(property);
        	if(root.primitiveValidExample == null  && (root.isInteger || root.isNumber || root.isLong || root.isFloat || root.isDouble)	) {
				root.primitiveValidExample = pg.getPrimitiveValidExample();
        		root.primitiveInvalidExample = pg.getPrimitiveInvalidExample();
        	}
            if(isValid) {
            	return fullPrefix + pg.generateValidExample() + closeChars;
            }else {
            	return fullPrefix + pg.generateInvalidExample() + closeChars;
            }
        } 
        else if (ModelUtils.isArraySchema(schema)) {
            ArraySchema arrayschema = (ArraySchema) schema;
            Schema itemSchema = arrayschema.getItems();
            String itemModelName = getModelName(itemSchema);
            Set<Schema> newSeenSchemas = new HashSet<>(seenSchemas);
            newSeenSchemas.add(schema);
            example = fullPrefix + "[" + "\n" + toExampleValueRecursive(root, itemModelName, itemSchema, indentationLevel + 1, "", exampleLine + 1, newSeenSchemas, isValid) + ",\n" + closingIndentation + "]" + closeChars;
            return example;
        } else if (ModelUtils.isMapSchema(schema)) {
            if (modelName == null) {
                fullPrefix += "{";
                closeChars = "}";
            }
            Object addPropsObj = schema.getAdditionalProperties();
            // TODO handle true case for additionalProperties
            if (addPropsObj instanceof Schema) {
                Schema addPropsSchema = (Schema) addPropsObj;
                String key = "key";
                String addPropPrefix = key + ": ";
                if (modelName == null) {
                    addPropPrefix = ensureQuotes(key) + ": ";
                }
                String addPropsModelName = "\"" + getModelName(addPropsSchema) + "\"";
                Set<Schema> newSeenSchemas = new HashSet<>(seenSchemas);
                newSeenSchemas.add(schema);
                example = fullPrefix + "\n" + toExampleValueRecursive(root, addPropsModelName, addPropsSchema, indentationLevel + 1, addPropPrefix, exampleLine + 1, newSeenSchemas, isValid) + ",\n" + closingIndentation + closeChars;
            } else {
                example = fullPrefix + closeChars;
            }
            return example;
        } else if (ModelUtils.isComposedSchema(schema)) {
            ComposedSchema cm = (ComposedSchema) schema;
            List<Schema> ls = cm.getOneOf();
            if (ls != null && !ls.isEmpty()) {
                return fullPrefix + toExampleValue(root, ls.get(0), isValid) + closeChars;
            }
            return fullPrefix + closeChars;
        } else if (ModelUtils.isObjectSchema(schema)) {
            fullPrefix += "{";
            closeChars = "}";
            CodegenDiscriminator disc = this.codegen.createDiscriminator(modelName, schema);
            if (disc != null) {
                MappedModel mm = this.getDiscriminatorMappedModel(disc);
                if (mm != null) {
                    String discPropNameValue = mm.getMappingName();
                    String chosenModelName = mm.getModelName();
                    // TODO handle this case in the future, this is when the discriminated
                    // schema allOf includes this schema, like Cat allOf includes Pet
                    // so this is the composed schema use case
                } else {
                    return fullPrefix + closeChars;
                }
            }

            Set<Schema> newSeenSchemas = new HashSet<>(seenSchemas);
            newSeenSchemas.add(schema);
            String exampleForObjectModel = exampleForObjectModel(root, schema, fullPrefix, closeChars, null, indentationLevel, exampleLine, closingIndentation, newSeenSchemas, isValid);
            return exampleForObjectModel;
        } else {
            LOGGER.warn("Type " + schema.getType() + " not handled properly in toExampleValue");
        }

        return example;
    }
    
    private String getModelName(Schema sc) {
        if (sc.get$ref() != null) {
            Schema unaliasedSchema = this.codegen.unaliasSchema(sc);
            if (unaliasedSchema.get$ref() != null) {
                return this.codegen.toModelName(ModelUtils.getSimpleRef(sc.get$ref()));
            }
        }
        return null;
    }
    
    private String ensureQuotes(String in) {
        Pattern pattern = Pattern.compile("\r\n|\r|\n");
        Matcher matcher = pattern.matcher(in);
        if (matcher.find()) {
            // if a string has a new line in it add backticks to make it a typescript multiline string
            return "`" + in + "`";
        }
        String strPattern = "^['\"].*?['\"]$";
        if (in.matches(strPattern)) {
            return in;
        }
        return "\"" + in + "\"";
    }
    
    private String exampleForObjectModel(CodegenParameter root, Schema schema, String fullPrefix, String closeChars, CodegenProperty discProp, int indentationLevel, int exampleLine, String closingIndentation, Set<Schema> seenSchemas, boolean isValid) {
        Map<String, Schema> requiredAndOptionalProps = schema.getProperties();
        if (requiredAndOptionalProps == null || requiredAndOptionalProps.isEmpty()) {
            return fullPrefix + closeChars;
        }

        String example = fullPrefix + "\n";
        for (Map.Entry<String, Schema> entry : requiredAndOptionalProps.entrySet()) {
            String propName = entry.getKey();
            Schema propSchema = entry.getValue();
            boolean readOnly = false;
            if (propSchema.getReadOnly() != null) {
                readOnly = propSchema.getReadOnly();
            }
            if (readOnly) {
                continue;
            }
            String ref = propSchema.get$ref();
            if (ref != null) {
                Schema refSchema = ModelUtils.getSchema(this.codegen.getOpenAPI(), ModelUtils.getSimpleRef(ref));
                if (refSchema.getReadOnly() != null) {
                    readOnly = refSchema.getReadOnly();
                }
                if (readOnly) {
                    continue;
                }
            }
            propName = this.codegen.toVarName(propName);
            String propModelName = null;
            Object propExample = null;
            if (discProp != null && propName.equals(discProp.name)) {
                propModelName = null;
                propExample = discProp.example;
            } else {
                propModelName = getModelName(propSchema);
                propExample = exampleFromStringOrArraySchema(root, propSchema, propName, isValid);
            }
            example += toExampleValueRecursive(root, propModelName, propSchema, indentationLevel + 1, propName + ": ", exampleLine + 1, seenSchemas, isValid) + ",\n";
        }
        // TODO handle additionalProperties also
        example += closingIndentation + closeChars;
        return example;
    }
    
    public MappedModel getDiscriminatorMappedModel(CodegenDiscriminator disc) {
        for (MappedModel mm : disc.getMappedModels()) {
            String modelName = mm.getModelName();
            Schema modelSchema = this.codegen.getModelNameToSchemaCache().get(modelName);
            if (ModelUtils.isObjectSchema(modelSchema)) {
                return mm;
            }
        }
        return null;
    }
    
    public void GenerateExampleForPrimitiveParameter(CodegenParameter parameter, PrimitiveValidParameterRuleTypes ruleType) {
    	
    }
}
