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

package org.openapitools.codegen.online.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.parser.core.models.AuthorizationValue;

import java.util.List;
import java.util.Map;

public class GeneratorInput {
    private JsonNode spec;
    private Map<String, String> options;
    private String openAPIUrl;
    private AuthorizationValue authorizationValue;
    private Map<String, Object> configuration; // custom configuration
    private List<String> requiredOperationIds;
    private String exampleJSON;
    private String useLLM;

    public AuthorizationValue getAuthorizationValue() {
        return authorizationValue;
    }

    public void setAuthorizationValue(AuthorizationValue authorizationValue) {
        this.authorizationValue = authorizationValue;
    }

    public JsonNode getSpec() {
        return spec;
    }

    public void setSpec(JsonNode spec) {
        this.spec = spec;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    @ApiModelProperty(example = "https://raw.githubusercontent.com/OpenAPITools/openapi-generator/master/modules/openapi-generator/src/test/resources/2_0/petstore.yaml")
    public String getOpenAPIUrl() {
        return openAPIUrl;
    }

    public void setOpenAPIUrl(String url) {
        this.openAPIUrl = url;
    }
    
    // getter
    public Map<String, Object> getConfiguration() {
		return configuration;
	}

	// setter
	public void setConfiguration(Map<String, Object> configuration) {
		this.configuration = configuration;
	}
	
	public List<String> getRequiredOperationIds() {
		return requiredOperationIds;
	}
	
	public void setRequiredOperationIds(List<String> requiredOperationIds) {
		this.requiredOperationIds = requiredOperationIds;
	}
	
	public void setExampleJSON(String exampleJSON) {
		this.exampleJSON = exampleJSON;
	}
	
	public String getExampleJSON() {
		return exampleJSON;
	}
	
	public String getUseLLM() {
        return useLLM;
    }
	
	public void setUseLLM(String useLLM) {
		this.useLLM = useLLM;
	}
}
