/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.5.0-SNAPSHOT).
 * https://openapi-generator.tech
 * Do not edit the class manually.
*/
package org.openapitools.api

import org.openapitools.model.Client
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.enums.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.security.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest

import org.springframework.web.bind.annotation.*
import org.springframework.validation.annotation.Validated
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.beans.factory.annotation.Autowired

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid

import kotlin.collections.List
import kotlin.collections.Map

@Validated
@RequestMapping("\${api.base-path:/v2}")
interface FakeClassnameTestApi {

    @Operation(
        tags = ["fake_classname_tags 123#$%^",],
        summary = "To test class name in snake case",
        operationId = "testClassname",
        description = """To test class name in snake case""",
        responses = [
            ApiResponse(responseCode = "200", description = "successful operation", content = [Content(schema = Schema(implementation = Client::class))])
        ],
        security = [ SecurityRequirement(name = "api_key_query") ]
    )
    @RequestMapping(
            method = [RequestMethod.PATCH],
            value = ["/fake_classname_test"],
            produces = ["application/json"],
            consumes = ["application/json"]
    )
    fun testClassname(@Parameter(description = "client model", required = true) @Valid @RequestBody client: Client,serverHttpRequest: ServerHttpRequest): ResponseEntity<Client> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
