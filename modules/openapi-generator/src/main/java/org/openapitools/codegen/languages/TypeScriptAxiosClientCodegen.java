/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 * Copyright 2018 SmartBear Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openapitools.codegen.languages;

import com.github.curiousoddman.rgxgen.RgxGen;
import com.google.common.collect.Sets;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.util.SchemaTypeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apitest.scenarios.codegen.ParameterExampleGenerator;
import org.apitest.scenarios.rules.codegen.StringParameterGenerator;
import org.openapitools.codegen.*;
import org.openapitools.codegen.meta.features.DocumentationFeature;
import org.openapitools.codegen.meta.features.SecurityFeature;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;
import org.openapitools.codegen.templating.mustache.IndentedLambda;
import org.openapitools.codegen.templating.mustache.TrimLineBreaksLambda;
import org.openapitools.codegen.utils.ModelUtils;
import org.openapitools.codegen.CodegenDiscriminator.MappedModel;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;

import java.util.*;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openapitools.codegen.utils.CamelizeOption.LOWERCASE_FIRST_LETTER;
import static org.openapitools.codegen.utils.StringUtils.camelize;
import static org.openapitools.codegen.utils.StringUtils.underscore;

import static org.openapitools.codegen.utils.OnceLogger.once;

public class TypeScriptAxiosClientCodegen extends AbstractTypeScriptClientCodegen {

	private final Logger LOGGER = LoggerFactory.getLogger(TypeScriptAxiosClientCodegen.class);

	public static final String NPM_REPOSITORY = "npmRepository";
	public static final String MODULE_NAME = "moduleName";
	public static final String WITH_INTERFACES = "withInterfaces";
	public static final String SEPARATE_MODELS_AND_API = "withSeparateModelsAndApi";
	public static final String WITHOUT_PREFIX_ENUMS = "withoutPrefixEnums";
	public static final String USE_SINGLE_REQUEST_PARAMETER = "useSingleRequestParameter";
	public static final String WITH_NODE_IMPORTS = "withNodeImports";
	public static final String STRING_ENUMS = "stringEnums";
	public static final String STRING_ENUMS_DESC = "Generate string enums instead of objects for enum values.";
	public static final String HAS_AUTH_SCHEME = "hasAuthScheme";
	public static final String CONFIGURATION_OPTIONS = "typescriptConfiguration";
	public static final String IS_BEARER_AUTH_SCHEME = "isBearerAuthScheme";
	public static final String IS_BASIC_AUTH_SCHEME = "isBasicAuthScheme";
	public static final String AUTH_CONFIGURATION_OPTION = "auth";
	public static final String AUTH_SCHEMA_CONFIGURATION_OPTION = "scheme";
	public static final String OVERRIDE_BASE_PATH = "overrideBasepath";
	public static final String OVERIDDEN_BASE_PATH = "overriddenBasepath";
	public static final String BEARER_TOKEN = "bearerToken";
	
	private final DateTimeFormatter iso8601Date = DateTimeFormatter.ISO_DATE;
	private final DateTimeFormatter iso8601DateTime = DateTimeFormatter.ISO_DATE_TIME;
	protected String moduleName;
	protected String npmRepository = null;
	protected Boolean stringEnums = false;

	private String tsModelPackage = "";

	private String testFolder;

	public TypeScriptAxiosClientCodegen() {
		super();

		this.prependFormOrBodyParameters = false;
		modifyFeatureSet(features -> features.includeDocumentationFeatures(DocumentationFeature.Readme)
				.includeSecurityFeatures(SecurityFeature.BearerToken));

		// clear import mapping (from default generator) as TS does not use it
		// at the moment
		importMapping.clear();

		reservedWords.add("options");

		outputFolder = "generated-code/typescript-axios";
		embeddedTemplateDir = templateDir = "typescript-axios";
		testFolder = "tests";

		this.cliOptions.add(new CliOption(NPM_REPOSITORY,
				"Use this property to set an url of your private npmRepo in the package.json"));
		this.cliOptions.add(new CliOption(WITH_INTERFACES,
				"Setting this property to true will generate interfaces next to the default class implementations.",
				SchemaTypeUtil.BOOLEAN_TYPE).defaultValue(Boolean.FALSE.toString()));
		this.cliOptions.add(new CliOption(SEPARATE_MODELS_AND_API,
				"Put the model and api in separate folders and in separate classes. This requires in addition a value for 'apiPackage' and 'modelPackage'",
				SchemaTypeUtil.BOOLEAN_TYPE).defaultValue(Boolean.FALSE.toString()));
		this.cliOptions.add(new CliOption(CodegenConstants.MODEL_PACKAGE, CodegenConstants.MODEL_PACKAGE_DESC));
		this.cliOptions.add(new CliOption(CodegenConstants.API_PACKAGE, CodegenConstants.API_PACKAGE_DESC));
		this.cliOptions.add(new CliOption(WITHOUT_PREFIX_ENUMS, "Don't prefix enum names with class names",
				SchemaTypeUtil.BOOLEAN_TYPE).defaultValue(Boolean.FALSE.toString()));
		this.cliOptions.add(new CliOption(USE_SINGLE_REQUEST_PARAMETER,
				"Setting this property to true will generate functions with a single argument containing all API endpoint parameters instead of one argument per parameter.",
				SchemaTypeUtil.BOOLEAN_TYPE).defaultValue(Boolean.FALSE.toString()));
		this.cliOptions.add(new CliOption(WITH_NODE_IMPORTS, "Setting this property to true adds imports for NodeJS",
				SchemaTypeUtil.BOOLEAN_TYPE).defaultValue(Boolean.FALSE.toString()));
		this.cliOptions
				.add(new CliOption(STRING_ENUMS, STRING_ENUMS_DESC).defaultValue(String.valueOf(this.stringEnums)));
		// Templates have no mapping between formatted property names and original base
		// names so use only "original" and remove this option
		removeOption(CodegenConstants.MODEL_PROPERTY_NAMING);
	}

	@Override
	public String getName() {
		return "typescript-axios";
	}

	@Override
	public String getHelp() {
		return "Generates a TypeScript client library using axios.";
	}

	public String getNpmRepository() {
		return npmRepository;
	}

	public void setNpmRepository(String npmRepository) {
		this.npmRepository = npmRepository;
	}

	private static String getRelativeToRoot(String path) {
		StringBuilder sb = new StringBuilder();
		int slashCount = path.split("/").length;
		if (slashCount == 0) {
			sb.append("./");
		} else {
			for (int i = 0; i < slashCount; ++i) {
				sb.append("../");
			}
		}
		return sb.toString();
	}

	@Override
	public void processOpts() {
		super.processOpts();
		tsModelPackage = modelPackage.replaceAll("\\.", "/");
		String tsApiPackage = apiPackage.replaceAll("\\.", "/");

		String modelRelativeToRoot = getRelativeToRoot(tsModelPackage);
		String apiRelativeToRoot = getRelativeToRoot(tsApiPackage);

		additionalProperties.put("tsModelPackage", tsModelPackage);
		additionalProperties.put("tsApiPackage", tsApiPackage);
		additionalProperties.put("apiRelativeToRoot", apiRelativeToRoot);
		additionalProperties.put("modelRelativeToRoot", modelRelativeToRoot);
		additionalProperties.put("indent4", new IndentedLambda());
		additionalProperties.put("trimLineBreaks", new TrimLineBreaksLambda());
		

		if (additionalProperties.containsKey(MODULE_NAME)) {
			setModuleName(((String) additionalProperties.get(MODULE_NAME)));
		}
		
		if (additionalProperties.containsKey(CONFIGURATION_OPTIONS)) {
			Map<String, Object> configurationOptions = Json.mapper().convertValue(additionalProperties.get(CONFIGURATION_OPTIONS), Map.class);
			Map<String, Object> auth = (Map<String, Object>) configurationOptions.get(AUTH_CONFIGURATION_OPTION);
			if(auth != null && StringUtils.isNotEmpty((String)auth.get(AUTH_SCHEMA_CONFIGURATION_OPTION))) {
				additionalProperties.put(HAS_AUTH_SCHEME, true);
				String scheme = (String) auth.get(AUTH_SCHEMA_CONFIGURATION_OPTION);
				if("BearerToken".equalsIgnoreCase(scheme)) {
					additionalProperties.put(IS_BEARER_AUTH_SCHEME, true);
					String bearerToken = (String) auth.get("bearerToken");
					additionalProperties.put(BEARER_TOKEN, bearerToken);
				}else if("BasicAuth".equalsIgnoreCase(scheme)) {
                    additionalProperties.put(IS_BASIC_AUTH_SCHEME, true);
                    additionalProperties.put("username", auth.get("username"));
                    additionalProperties.put("password", auth.get("password"));
                } // other auth schemes can be added here
				
			}
			String basePath = configurationOptions.get("basePath") == null ? "" : configurationOptions.get("basePath").toString();
			
			if (StringUtils.isNotEmpty(basePath)) {
				additionalProperties.put(OVERRIDE_BASE_PATH, true);
				additionalProperties.put(OVERIDDEN_BASE_PATH, basePath);
			}
			
			additionalProperties.put(modelRelativeToRoot, apiRelativeToRoot);
		}

		//supportingFiles.add(new SupportingFile("index.mustache", "", "index.ts"));
		//supportingFiles.add(new SupportingFile("baseApi.mustache", "", "base.ts"));
		supportingFiles.add(new SupportingFile("common.mustache", ".lib", "common.ts"));
		supportingFiles.add(new SupportingFile("schemaValidator.mustache", ".lib", "schemaValidator.ts"));
		//supportingFiles.add(new SupportingFile("api.mustache", "", "api.ts"));
		supportingFiles.add(new SupportingFile("configuration.mustache", ".lib", "configuration.ts"));
		// supportingFiles.add(new SupportingFile("git_push.sh.mustache", "",
		// "git_push.sh"));
		// supportingFiles.add(new SupportingFile("gitignore", "", ".gitignore"));
		// supportingFiles.add(new SupportingFile("npmignore", "", ".npmignore"));
		supportingFiles.add(new SupportingFile("playwright.config.mustache", "", "playwright.config.ts"));
		supportingFiles.add(new SupportingFile("package.mustache", "", "package.json"));

		if (additionalProperties.containsKey(SEPARATE_MODELS_AND_API)) {
			boolean separateModelsAndApi = Boolean
					.parseBoolean(additionalProperties.get(SEPARATE_MODELS_AND_API).toString());
			if (separateModelsAndApi) {
				if (StringUtils.isAnyBlank(modelPackage, apiPackage)) {
					throw new RuntimeException("apiPackage and modelPackage must be defined");
				}
				modelTemplateFiles.put("model.mustache", ".ts");
				apiTemplateFiles.put("apiInner.mustache", ".ts");
				supportingFiles.add(new SupportingFile("modelIndex.mustache", tsModelPackage, "index.ts"));
			}
		}
		
		apiTestTemplateFiles().put("apirequest.mustache", ".ts");


		if (additionalProperties.containsKey(STRING_ENUMS)) {
			this.stringEnums = Boolean.parseBoolean(additionalProperties.get(STRING_ENUMS).toString());
			additionalProperties.put("stringEnums", this.stringEnums);
		}

		if (additionalProperties.containsKey(NPM_NAME)) {
			addNpmPackageGeneration();
		}

	}

	@Override
	public String apiTestFileFolder() {
		return outputFolder + File.separatorChar + testFolder;
	}

	@Override
	public String modelTestFileFolder() {
		return outputFolder + File.separatorChar + testFolder;
	}

	@Override
	public OperationsMap postProcessOperationsWithModels(OperationsMap objs, List<ModelMap> allModels) {
		objs = super.postProcessOperationsWithModels(objs, allModels);
		this.updateOperationParameterForEnum(objs);
		OperationMap vals = objs.getOperations();
		List<CodegenOperation> operations = vals.getOperation();
		/*
		 * Filter all the operations that are multipart/form-data operations and set the
		 * vendor extension flag 'multipartFormData' for the template to work with.
		 */
		operations.stream().filter(op -> op.hasConsumes)
				.filter(op -> op.consumes.stream()
						.anyMatch(opc -> opc.values().stream().anyMatch("multipart/form-data"::equals)))
				.forEach(op -> op.vendorExtensions.putIfAbsent("multipartFormData", true));

		return objs;
	}

	private void updateOperationParameterForEnum(OperationsMap operations) {
		// This method will add extra information as to whether or not we have enums and
		// update their names with the operation.id prefixed.
		// It will also set the uniqueId status if provided.
		for (CodegenOperation op : operations.getOperations().getOperation()) {
			for (CodegenParameter param : op.allParams) {
				if (Boolean.TRUE.equals(param.isEnum)) {
					param.datatypeWithEnum = param.datatypeWithEnum.replace(param.enumName,
							op.operationIdCamelCase + param.enumName);
				}
			}
		}
	}

	@Override
	public void postProcessParameter(CodegenParameter parameter) {
		super.postProcessParameter(parameter);
		if (parameter.isFormParam && parameter.isArray && "binary".equals(parameter.dataFormat)) {
			parameter.isCollectionFormatMulti = true;
		}
	}

	@Override
	public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> objs) {
		Map<String, ModelsMap> result = super.postProcessAllModels(objs);
		for (ModelsMap entry : result.values()) {
			for (ModelMap model : entry.getModels()) {
				CodegenModel codegenModel = model.getModel();
				model.put("hasAllOf", codegenModel.allOf.size() > 0);
				model.put("hasOneOf", codegenModel.oneOf.size() > 0);
			}
		}
		return result;
	}

	@Override
	protected void addAdditionPropertiesToCodeGenModel(CodegenModel codegenModel, Schema schema) {
		codegenModel.additionalPropertiesType = getTypeDeclaration(ModelUtils.getAdditionalProperties(schema));
		addImport(codegenModel, codegenModel.additionalPropertiesType);
	}

	@Override
	public String toApiTestFilename(String name) {
		return toApiName(name) + ".spec";
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Override
	public ModelsMap postProcessModels(ModelsMap objs) {
		List<ModelMap> models = postProcessModelsEnum(objs).getModels();

		boolean withoutPrefixEnums = false;
		if (additionalProperties.containsKey(WITHOUT_PREFIX_ENUMS)) {
			withoutPrefixEnums = Boolean.parseBoolean(additionalProperties.get(WITHOUT_PREFIX_ENUMS).toString());
		}

		for (ModelMap mo : models) {
			CodegenModel cm = mo.getModel();

			// Deduce the model file name in kebab case
			cm.classFilename = cm.classname.replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase(Locale.ROOT);

			// processed enum names
			if (!withoutPrefixEnums) {
				cm.imports = new TreeSet<>(cm.imports);
				// name enum with model name, e.g. StatusEnum => PetStatusEnum
				for (CodegenProperty var : cm.vars) {
					if (Boolean.TRUE.equals(var.isEnum)) {
						var.datatypeWithEnum = var.datatypeWithEnum.replace(var.enumName, cm.classname + var.enumName);
						var.enumName = var.enumName.replace(var.enumName, cm.classname + var.enumName);
					}
				}
				if (cm.parent != null) {
					for (CodegenProperty var : cm.allVars) {
						if (Boolean.TRUE.equals(var.isEnum)) {
							var.datatypeWithEnum = var.datatypeWithEnum.replace(var.enumName,
									cm.classname + var.enumName);
							var.enumName = var.enumName.replace(var.enumName, cm.classname + var.enumName);
						}
					}
				}
			}
		}

		// Apply the model file name to the imports as well
		for (Map<String, String> m : objs.getImports()) {
			String javaImport = m.get("import").substring(modelPackage.length() + 1);
			String tsImport = tsModelPackage + "/" + javaImport;
			m.put("tsImport", tsImport);
			m.put("class", javaImport);
			m.put("filename", javaImport.replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase(Locale.ROOT));
		}
		return objs;
	}

	/**
	 * Overriding toRegularExpression() to avoid escapeText() being called, as it
	 * would return a broken regular expression if any escaped character /
	 * metacharacter were present.
	 */
	@Override
	public String toRegularExpression(String pattern) {
		return addRegularExpressionDelimiter(pattern);
	}

	@Override
	public String toModelFilename(String name) {
		return super.toModelFilename(name).replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase(Locale.ROOT);
	}

	@Override
	public String toApiFilename(String name) {
		return super.toApiFilename(name).replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase(Locale.ROOT);
	}

	private void addNpmPackageGeneration() {

		if (additionalProperties.containsKey(NPM_REPOSITORY)) {
			this.setNpmRepository(additionalProperties.get(NPM_REPOSITORY).toString());
		}

		// Files for building our lib
		supportingFiles.add(new SupportingFile("README.mustache", "", "README.md"));
		supportingFiles.add(new SupportingFile("package.mustache", "", "package.json"));
		supportingFiles.add(new SupportingFile("tsconfig.mustache", "", "tsconfig.json"));
		// in case ECMAScript 6 is supported add another tsconfig for an ESM (ECMAScript
		// Module)
		if (supportsES6) {
			supportingFiles.add(new SupportingFile("tsconfig.esm.mustache", "", "tsconfig.esm.json"));
		}
	}

	@Override
	protected void updatePropertyForAnyType(CodegenProperty property, Schema p) {
		// The 'null' value is allowed when the OAS schema is 'any type'.
		// See https://github.com/OAI/OpenAPI-Specification/issues/1389
		// custom line here, do not set property.isNullable = true
		if (languageSpecificPrimitives.contains(property.dataType)) {
			property.isPrimitiveType = true;
		}
		if (ModelUtils.isMapSchema(p)) {
			// an object or anyType composed schema that has additionalProperties set
			// some of our code assumes that any type schema with properties defined will be
			// a map
			// even though it should allow in any type and have map constraints for
			// properties
			updatePropertyForMap(property, p);
		}
	}

	@Override
	protected void addImport(Schema composed, Schema childSchema, CodegenModel model, String modelName) {
		// import everything (including child schema of a composed schema)
		addImport(model, modelName);
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

	public String getModelName(Schema sc) {
		if (sc.get$ref() != null) {
			Schema unaliasedSchema = unaliasSchema(sc);
			if (unaliasedSchema.get$ref() != null) {
				return toModelName(ModelUtils.getSimpleRef(sc.get$ref()));
			}
		}
		return null;
	}

	/**
	 * Gets an example if it exists
	 *
	 * @param sc input schema
	 * @return the example value
	 */
	protected Object getObjectExample(Schema sc) {
		Schema schema = sc;
		String ref = sc.get$ref();
		if (ref != null) {
			schema = ModelUtils.getSchema(this.openAPI, ModelUtils.getSimpleRef(ref));
		}
		// TODO handle examples in object models in the future
		Boolean objectModel = (ModelUtils.isObjectSchema(schema) || ModelUtils.isMapSchema(schema)
				|| ModelUtils.isComposedSchema(schema));
		if (objectModel) {
			return null;
		}
		if (schema.getExample() != null) {
			return schema.getExample();
		}
		if (schema.getDefault() != null) {
			return schema.getDefault();
		} else if (schema.getEnum() != null && !schema.getEnum().isEmpty()) {
			return schema.getEnum().get(0);
		}
		return null;
	}

	/***
	 * Ensures that the string has a leading and trailing quote
	 *
	 * @param in input string
	 * @return quoted string
	 */
	private String ensureQuotes(String in) {
		Pattern pattern = Pattern.compile("\r\n|\r|\n");
		Matcher matcher = pattern.matcher(in);
		if (matcher.find()) {
			// if a string has a new line in it add backticks to make it a typescript
			// multiline string
			return "`" + in + "`";
		}
		String strPattern = "^['\"].*?['\"]$";
		if (in.matches(strPattern)) {
			return in;
		}
		return "\"" + in + "\"";
	}

	@Override
	public String toExampleValue(Schema schema) {
		Object objExample = getObjectExample(schema);
		return toExampleValue(schema, objExample);
	}

	public String toExampleValue(Schema schema, Object objExample) {
		String modelName = getModelName(schema);
		return toExampleValueRecursive(modelName, schema, objExample, 1, "", 0, Sets.newHashSet());
	}

	private Boolean simpleStringSchema(Schema schema) {
		Schema sc = schema;
		String ref = schema.get$ref();
		if (ref != null) {
			sc = ModelUtils.getSchema(this.openAPI, ModelUtils.getSimpleRef(ref));
		}
		return ModelUtils.isStringSchema(sc) && !ModelUtils.isDateSchema(sc) && !ModelUtils.isDateTimeSchema(sc)
				&& !"Number".equalsIgnoreCase(sc.getFormat()) && !ModelUtils.isByteArraySchema(sc)
				&& !ModelUtils.isBinarySchema(sc) && schema.getPattern() == null;
	}

	public MappedModel getDiscriminatorMappedModel(CodegenDiscriminator disc) {
		for (MappedModel mm : disc.getMappedModels()) {
			String modelName = mm.getModelName();
			Schema modelSchema = getModelNameToSchemaCache().get(modelName);
			if (ModelUtils.isObjectSchema(modelSchema)) {
				return mm;
			}
		}
		return null;
	}

	/***
	 * Recursively generates string examples for schemas
	 *
	 * @param modelName        the string name of the refed model that will be
	 *                         generated for the schema or null
	 * @param schema           the schema that we need an example for
	 * @param objExample       the example that applies to this schema, for now only
	 *                         string example are used
	 * @param indentationLevel integer indentation level that we are currently at we
	 *                         assume the indentation amount is 2 spaces times this
	 *                         integer
	 * @param prefix           the string prefix that we will use when assigning an
	 *                         example for this line this is used when setting key:
	 *                         value, pairs "key: " is the prefix and this is used
	 *                         when setting properties like
	 *                         some_property='some_property_example'
	 * @param exampleLine      this is the current line that we are generating an
	 *                         example for, starts at 0 we don't indent the 0th line
	 *                         because using the example value looks like: prop =
	 *                         ModelName( line 0
	 *                         some_property='some_property_example' line 1 ) line 2
	 *                         and our example value is: ModelName( line 0
	 *                         some_property='some_property_example' line 1 ) line 2
	 * @param seenSchemas      This set contains all the schemas passed into the
	 *                         recursive function. It is used to check if a schema
	 *                         was already passed into the function and breaks the
	 *                         infinite recursive loop. The only schemas that are
	 *                         not added are ones that contain $ref != null
	 * @return the string example
	 */
	private String toExampleValueRecursive(String modelName, Schema schema, Object objExample, int indentationLevel,
			String prefix, Integer exampleLine, Set<Schema> seenSchemas) {
		final String indentionConst = "  ";
		String currentIndentation = "";
		String closingIndentation = "";
		for (int i = 0; i < indentationLevel; i++)
			currentIndentation += indentionConst;
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
		if (objExample != null) {
			example = objExample.toString();
		}
		// checks if the current schema has already been passed in. If so, breaks the
		// current recursive pass
		if (seenSchemas.contains(schema)) {
			if (modelName != null) {
				return fullPrefix + closeChars;
			} else {
				// this is a recursive schema
				// need to add a reasonable example to avoid
				// infinite recursion
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
			}
		}

		if (null != schema.get$ref()) {
			Map<String, Schema> allDefinitions = ModelUtils.getSchemas(this.openAPI);
			String ref = ModelUtils.getSimpleRef(schema.get$ref());
			Schema refSchema = allDefinitions.get(ref);
			if (null == refSchema) {
				LOGGER.warn("Unable to find referenced schema " + schema.get$ref() + "\n");
				return fullPrefix + "null" + closeChars;
			}
			String refModelName = getModelName(schema);
			return toExampleValueRecursive(refModelName, refSchema, objExample, indentationLevel, prefix, exampleLine,
					seenSchemas);
		} else if (ModelUtils.isNullType(schema) || ModelUtils.isAnyType(schema)) {
			// The 'null' type is allowed in OAS 3.1 and above. It is not supported by OAS
			// 3.0.x,
			// though this tooling supports it.
			return fullPrefix + "null" + closeChars;
		} else if (ModelUtils.isBooleanSchema(schema)) {
			if (objExample == null) {
				example = "true";
			} else {
				if ("false".equalsIgnoreCase(objExample.toString())) {
					example = "false";
				} else {
					example = "true";
				}
			}
			return fullPrefix + example + closeChars;
		} else if (ModelUtils.isDateSchema(schema)) {
			if (objExample == null) {
				example = typescriptDate("1970-01-01");
			} else {
				example = typescriptDate(objExample);
			}
			return fullPrefix + example + closeChars;
		} else if (ModelUtils.isDateTimeSchema(schema)) {
			if (objExample == null) {
				example = typescriptDateTime("1970-01-01T00:00:00.00Z");
			} else {
				example = typescriptDateTime(objExample);
			}
			return fullPrefix + example + closeChars;
		} else if (ModelUtils.isBinarySchema(schema)) {
			String fileName = "swagger.json";
			if (objExample == null) {
				example = "C:\\\\Users\\\\krchanda\\\\Downloads\\\\apitestingpoc\\\\swagger.json";
			}
			example = "new File([fs.readFileSync('" + example + "')], '" + fileName + "')";
			return fullPrefix + example + closeChars;
		} else if (ModelUtils.isByteArraySchema(schema)) {
			if (objExample == null) {
				example = "'YQ=='";
			}
			return fullPrefix + example + closeChars;
		} else if (ModelUtils.isStringSchema(schema)) {
			if (objExample == null) {
				// a BigDecimal:
				if ("Number".equalsIgnoreCase(schema.getFormat())) {
					example = "2";
					return fullPrefix + example + closeChars;
				} else if (StringUtils.isNotBlank(schema.getPattern())) {
					String pattern = schema.getPattern();
					RgxGen rgxGen = new RgxGen(pattern);

					// this seed makes it so if we have [a-z] we pick a
					Random random = new Random(18);
					example = rgxGen.generate(random);
				} else if (schema.getMinLength() != null) {
					example = "";
					int len = schema.getMinLength().intValue();
					for (int i = 0; i < len; i++)
						example += "a";
				} else if (ModelUtils.isUUIDSchema(schema)) {
					example = "046b6c7f-0b8a-43b9-b35d-6489e6daee91";
				} else {
					example = "string_example";
				}
			}
			return fullPrefix + ensureQuotes(example) + closeChars;
		} else if (ModelUtils.isIntegerSchema(schema)) {
			if (objExample == null) {
				if (schema.getMinimum() != null) {
					example = schema.getMinimum().toString();
				} else {
					example = "1";
				}
			}
			return fullPrefix + example + closeChars;
		} else if (ModelUtils.isNumberSchema(schema)) {
			if (objExample == null) {
				if (schema.getMinimum() != null) {
					example = schema.getMinimum().toString();
				} else {
					example = "3.14";
				}
			}
			return fullPrefix + example + closeChars;
		} else if (ModelUtils.isArraySchema(schema)) {
			ArraySchema arrayschema = (ArraySchema) schema;
			Schema itemSchema = arrayschema.getItems();
			String itemModelName = getModelName(itemSchema);
			if (objExample instanceof Iterable && itemModelName == null) {
				// If the example is already a list, return it directly instead of wrongly wrap
				// it in another list
				return fullPrefix + objExample + closeChars;
			}
			Set<Schema> newSeenSchemas = new HashSet<>(seenSchemas);
			newSeenSchemas.add(schema);
			example = fullPrefix + "[" + "\n" + toExampleValueRecursive(itemModelName, itemSchema, objExample,
					indentationLevel + 1, "", exampleLine + 1, newSeenSchemas) + ",\n" + closingIndentation + "]"
					+ closeChars;
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
				Object addPropsExample = getObjectExample(addPropsSchema);
				if (addPropsSchema.getEnum() != null && !addPropsSchema.getEnum().isEmpty()) {
					key = addPropsSchema.getEnum().get(0).toString();
				}
				addPropsExample = exampleFromStringOrArraySchema(addPropsSchema, addPropsExample, key);
				String addPropPrefix = key + ": ";
				if (modelName == null) {
					addPropPrefix = ensureQuotes(key) + ": ";
				}
				String addPropsModelName = "\"" + getModelName(addPropsSchema) + "\"";
				Set<Schema> newSeenSchemas = new HashSet<>(seenSchemas);
				newSeenSchemas.add(schema);
				example = fullPrefix + "\n"
						+ toExampleValueRecursive(addPropsModelName, addPropsSchema, addPropsExample,
								indentationLevel + 1, addPropPrefix, exampleLine + 1, newSeenSchemas)
						+ ",\n" + closingIndentation + closeChars;
			} else {
				example = fullPrefix + closeChars;
			}
			return example;
		} else if (ModelUtils.isComposedSchema(schema)) {
			ComposedSchema cm = (ComposedSchema) schema;
			List<Schema> ls = cm.getOneOf();
			if (ls != null && !ls.isEmpty()) {
				return fullPrefix + toExampleValue(ls.get(0)) + closeChars;
			}
			return fullPrefix + closeChars;
		} else if (ModelUtils.isObjectSchema(schema)) {
			fullPrefix += "{";
			closeChars = "}";
			CodegenDiscriminator disc = createDiscriminator(modelName, schema);
			if (disc != null) {
				MappedModel mm = getDiscriminatorMappedModel(disc);
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
			String exampleForObjectModel = exampleForObjectModel(schema, fullPrefix, closeChars, null, indentationLevel,
					exampleLine, closingIndentation, newSeenSchemas);
			return exampleForObjectModel;
		} else {
			LOGGER.warn("Type " + schema.getType() + " not handled properly in toExampleValue");
		}

		return example;
	}

	private String exampleForObjectModel(Schema schema, String fullPrefix, String closeChars, CodegenProperty discProp,
			int indentationLevel, int exampleLine, String closingIndentation, Set<Schema> seenSchemas) {
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
				Schema refSchema = ModelUtils.getSchema(this.openAPI, ModelUtils.getSimpleRef(ref));
				if (refSchema.getReadOnly() != null) {
					readOnly = refSchema.getReadOnly();
				}
				if (readOnly) {
					continue;
				}
			}
			propName = toVarName(propName);
			String propModelName = null;
			Object propExample = null;
			if (discProp != null && propName.equals(discProp.name)) {
				propModelName = null;
				propExample = discProp.example;
			} else {
				propModelName = getModelName(propSchema);
				propExample = exampleFromStringOrArraySchema(propSchema, null, propName);
			}
			example += toExampleValueRecursive(propModelName, propSchema, propExample, indentationLevel + 1,
					propName + ": ", exampleLine + 1, seenSchemas) + ",\n";
		}
		// TODO handle additionalProperties also
		example += closingIndentation + closeChars;
		return example;
	}

	private Object exampleFromStringOrArraySchema(Schema sc, Object currentExample, String propName) {
		if (currentExample != null) {
			return currentExample;
		}
		Schema schema = sc;
		String ref = sc.get$ref();
		if (ref != null) {
			schema = ModelUtils.getSchema(this.openAPI, ModelUtils.getSimpleRef(ref));
		}
		Object example = getObjectExample(schema);
		if (example != null) {
			return example;
		} else if (simpleStringSchema(schema)) {
			return propName + "_example";
		} else if (ModelUtils.isArraySchema(schema)) {
			ArraySchema arraySchema = (ArraySchema) schema;
			Schema itemSchema = arraySchema.getItems();
			example = getObjectExample(itemSchema);
			if (example != null) {
				return example;
			} else if (simpleStringSchema(itemSchema)) {
				return propName + "_example";
			}
		}
		return null;
	}

	/***
	 * Set the codegenParameter example value We have a custom version of this
	 * function so we can invoke toExampleValue
	 *
	 * @param codegenParameter the item we are setting the example on
	 * @param parameter        the base parameter that came from the spec
	 */
	@Override
	public void setParameterExampleValue(CodegenParameter codegenParameter, Parameter parameter) {
		Schema schema = parameter.getSchema();
		// LOGGER.info("Generating example value for param name {} schema {} ",
		// parameter.getName(), parameter.getSchema().toString());
		if (schema == null) {
			LOGGER.warn("CodegenParameter.example defaulting to null because parameter lacks a schema");
			return;
		}

		Object example = null;
		if (codegenParameter.vendorExtensions != null && codegenParameter.vendorExtensions.containsKey("x-example")) {
			example = codegenParameter.vendorExtensions.get("x-example");
		} else if (parameter.getExample() != null) {
			example = parameter.getExample();
		} else if (parameter.getExamples() != null && !parameter.getExamples().isEmpty()
				&& parameter.getExamples().values().iterator().next().getValue() != null) {
			example = parameter.getExamples().values().iterator().next().getValue();
		} else {
			example = getObjectExample(schema);
		}
		example = exampleFromStringOrArraySchema(schema, example, parameter.getName());
		String finalExample = toExampleValue(schema, example);
		codegenParameter.example = finalExample;
		// LOGGER.info("Generated example value for param name {} example {} ",
		// parameter.getName(), codegenParameter.example);
	}

	public void generateExamplesForCP(CodegenParameter codegenParameter, Parameter parameter) {
		Schema schema = parameter.getSchema();
		// LOGGER.info("Generating example value for param name {} schema {} ",
		// parameter.getName(), parameter.getSchema().toString());
		if (schema == null) {
			LOGGER.warn("CodegenParameter.example defaulting to null because parameter lacks a schema");
			return;
		}
		boolean isStringSchema = false;
		Object example = null;
		/*
		 * if (codegenParameter.vendorExtensions != null &&
		 * codegenParameter.vendorExtensions.containsKey("x-example")) { example =
		 * codegenParameter.vendorExtensions.get("x-example"); } else if
		 * (parameter.getExample() != null) { example = parameter.getExample(); } else
		 * if (parameter.getExamples() != null && !parameter.getExamples().isEmpty() &&
		 * parameter.getExamples().values().iterator().next().getValue() != null) {
		 * example = parameter.getExamples().values().iterator().next().getValue(); }
		 * else { example = getObjectExample(schema); }
		 */

		String ref = schema.get$ref();
		if (ref != null) {
			schema = ModelUtils.getSchema(this.openAPI, ModelUtils.getSimpleRef(ref));
		}
		if (simpleStringSchema(schema)) {
			isStringSchema = true;
		} else if (ModelUtils.isArraySchema(schema)) {
			ArraySchema arraySchema = (ArraySchema) schema;
			Schema itemSchema = arraySchema.getItems();
			if (simpleStringSchema(itemSchema)) {
				isStringSchema = true;
			} else {

			}
		}

		String finalExample = toExampleValue(schema, example);
		codegenParameter.example = finalExample;
		// LOGGER.info("Generated example value for param name {} example {} ",
		// parameter.getName(), codegenParameter.example);

	}

	/**
	 * Return the example value of the parameter.
	 *
	 * @param codegenParameter Codegen parameter
	 * @param requestBody      Request body
	 */
	@Override
	public void setParameterExampleValue(CodegenParameter codegenParameter, RequestBody requestBody) {
		if (codegenParameter.vendorExtensions != null && codegenParameter.vendorExtensions.containsKey("x-example")) {
			codegenParameter.example = Json.pretty(codegenParameter.vendorExtensions.get("x-example"));
		}

		Content content = requestBody.getContent();

		if (content.size() > 1) {
			// @see ModelUtils.getSchemaFromContent()
			once(LOGGER).debug("Multiple MediaTypes found, using only the first one");
		}

		MediaType mediaType = content.values().iterator().next();
		Schema schema = mediaType.getSchema();
		if (schema == null) {
			LOGGER.warn("CodegenParameter.example defaulting to null because requestBody content lacks a schema");
			return;
		}

		Object example = null;
		if (mediaType.getExample() != null) {
			example = mediaType.getExample();
		} else if (mediaType.getExamples() != null && !mediaType.getExamples().isEmpty()
				&& mediaType.getExamples().values().iterator().next().getValue() != null) {
			example = mediaType.getExamples().values().iterator().next().getValue();
		} else {
			example = getObjectExample(schema);
		}
		example = exampleFromStringOrArraySchema(schema, example, codegenParameter.paramName);
		codegenParameter.example = toExampleValue(schema, example);
	}

	/**
	 * Create a CodegenParameter for a Form Property We have a custom version of
	 * this method so we can invoke setParameterExampleValue(codegenParameter,
	 * parameter) rather than setParameterExampleValue(codegenParameter) This
	 * ensures that all of our samples are generated in toExampleValueRecursive
	 *
	 * @param name           the property name
	 * @param propertySchema the property schema
	 * @param imports        our import set
	 * @return the resultant CodegenParameter
	 */
	@Override
	public CodegenParameter fromFormProperty(String name, Schema propertySchema, Set<String> imports) {
		CodegenParameter cp = super.fromFormProperty(name, propertySchema, imports);
		Parameter p = new Parameter();
		p.setSchema(propertySchema);
		p.setName(cp.paramName);
		setParameterExampleValue(cp, p);
		ParameterExampleGenerator pg = new ParameterExampleGenerator(this);
		pg.GenerateExample(cp, propertySchema);
		return cp;
	}

	@Override
	protected void addImport(Set<String> importsToBeAddedTo, String type) {
		if (type == null) {
			return;
		}

		String[] parts = splitComposedTypes(type);
		for (String s : parts) {
			super.addImport(importsToBeAddedTo, s);
		}
	}

	/**
	 * Split composed types e.g. TheFirstType | TheSecondType to TheFirstType and
	 * TheSecondType
	 *
	 * @param type String with composed types
	 * @return list of types
	 */
	protected String[] splitComposedTypes(String type) {
		return type.replace(" ", "").split("[|&<>]");
	}

}
