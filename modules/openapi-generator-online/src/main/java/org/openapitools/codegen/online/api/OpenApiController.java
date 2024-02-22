package org.openapitools.codegen.online.api;

import javax.validation.Valid;

import org.openapitools.codegen.online.model.APIRequest;
import org.openapitools.codegen.online.model.APIRequestInsight;
import org.openapitools.codegen.online.model.ResponseCode;
import org.openapitools.codegen.online.service.OpenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping("/openapi")
public class OpenApiController {

	@Autowired
	private OpenApiService openApiService;
	
	@ApiOperation(value = "Generate API Test Report Insight", nickname = "apiReportInsight", notes = "Accept reqeust config", response = ResponseCode.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = ResponseCode.class) })
    @RequestMapping(value = "/insights",
            method = RequestMethod.POST)
    public ResponseEntity<APIRequestInsight> getInsight(@ApiParam(value = "Request configuration" ,required=true )  @Valid @RequestBody APIRequest apiRequest) {
        return openApiService.getApiTestInsight(apiRequest);
    }

	@ApiOperation(value = "Generate API Test Report Stream Insight", nickname = "apiReportStreamInsight", notes = "Accept reqeust config", response = ResponseCode.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = ResponseCode.class) })
    @RequestMapping(value = "/stream-insights",
            method = RequestMethod.POST, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<String> streamInsight(@ApiParam(value = "Request configuration" ,required=true )  @Valid @RequestBody APIRequest apiRequest) {
        return openApiService.streamApiTestInsight(apiRequest);
    }
}
