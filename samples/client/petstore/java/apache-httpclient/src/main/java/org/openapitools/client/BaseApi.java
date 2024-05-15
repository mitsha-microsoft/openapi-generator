/*
 * OpenAPI Petstore
 * This spec is mainly for testing Petstore server and contains fake endpoints, models. Please do not use this for any other purpose. Special characters: \" \\
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.openapitools.client;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Collections;
import java.util.Map;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", comments = "Generator version: 7.5.0-SNAPSHOT")
public abstract class BaseApi {

  protected ApiClient apiClient;

  public BaseApi() {
    this(Configuration.getDefaultApiClient());
  }

  public BaseApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Directly invoke the API for the given URL. Useful if the API returns direct links/URLs for subsequent requests.
   * @param url The URL for the request, either full URL or only the path.
   * @param method The HTTP method for the request.
   * @throws ApiException if fails to make API call.
   */
  public void invokeAPI(String url, String method) throws ApiException {
    invokeAPI(url, method, null, null, Collections.emptyMap());
  }

  /**
   * Directly invoke the API for the given URL. Useful if the API returns direct links/URLs for subsequent requests.
   * @param url The URL for the request, either full URL or only the path.
   * @param method The HTTP method for the request.
   * @param additionalHeaders Additional headers for the request.
   * @throws ApiException if fails to make API call.
   */
  public void invokeAPI(String url, String method, Map<String, String> additionalHeaders) throws ApiException {
    invokeAPI(url, method, null, null, additionalHeaders);
  }

  /**
   * Directly invoke the API for the given URL. Useful if the API returns direct links/URLs for subsequent requests.
   * @param url The URL for the request, either full URL or only the path.
   * @param method The HTTP method for the request.
   * @param request The request object.
   * @throws ApiException if fails to make API call.
   */
  public void invokeAPI(String url, String method, Object request) throws ApiException {
    invokeAPI(url, method, request, null, Collections.emptyMap());
  }

  /**
   * Directly invoke the API for the given URL. Useful if the API returns direct links/URLs for subsequent requests.
   * @param url The URL for the request, either full URL or only the path.
   * @param method The HTTP method for the request.
   * @param request The request object.
   * @param additionalHeaders Additional headers for the request.
   * @throws ApiException if fails to make API call.
   */
  public void invokeAPI(String url, String method, Object request, Map<String, String> additionalHeaders) throws ApiException {
    invokeAPI(url, method, request, null, additionalHeaders);
  }

  /**
   * Directly invoke the API for the given URL. Useful if the API returns direct links/URLs for subsequent requests.
   * @param url The URL for the request, either full URL or only the path.
   * @param method The HTTP method for the request.
   * @param returnType The return type.
   * @return The API response in the specified type.
   * @throws ApiException if fails to make API call.
   */
  public <T> T invokeAPI(String url, String method, TypeReference<T> returnType) throws ApiException {
    return invokeAPI(url, method, null, returnType, Collections.emptyMap());
  }

  /**
   * Directly invoke the API for the given URL. Useful if the API returns direct links/URLs for subsequent requests.
   * @param url The URL for the request, either full URL or only the path.
   * @param method The HTTP method for the request.
   * @param request The request object.
   * @param returnType The return type.
   * @return The API response in the specified type.
   * @throws ApiException if fails to make API call.
   */
  public <T> T invokeAPI(String url, String method, Object request, TypeReference<T> returnType) throws ApiException {
    return invokeAPI(url, method, request, returnType, Collections.emptyMap());
  }

  /**
   * Directly invoke the API for the given URL. Useful if the API returns direct links/URLs for subsequent requests.
   * @param url The URL for the request, either full URL or only the path.
   * @param method The HTTP method for the request.
   * @param request The request object.
   * @param returnType The return type.
   * @param additionalHeaders Additional headers for the request.
   * @return The API response in the specified type.
   * @throws ApiException if fails to make API call.
   */
  public abstract <T> T invokeAPI(String url, String method, Object request, TypeReference<T> returnType, Map<String, String> additionalHeaders) throws ApiException;
}
