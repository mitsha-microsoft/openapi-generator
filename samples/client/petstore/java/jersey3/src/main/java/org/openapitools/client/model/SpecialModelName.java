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


package org.openapitools.client.model;

import java.util.Objects;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import org.openapitools.client.JSON;


/**
 * SpecialModelName
 */
@JsonPropertyOrder({
  SpecialModelName.JSON_PROPERTY_$_SPECIAL_PROPERTY_NAME,
  SpecialModelName.JSON_PROPERTY_SPECIAL_MODEL_NAME
})
@JsonTypeName("_special_model.name_")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", comments = "Generator version: 7.5.0-SNAPSHOT")
public class SpecialModelName {
  public static final String JSON_PROPERTY_$_SPECIAL_PROPERTY_NAME = "$special[property.name]";
  private Long $specialPropertyName;

  public static final String JSON_PROPERTY_SPECIAL_MODEL_NAME = "_special_model.name_";
  private String specialModelName;

  public SpecialModelName() { 
  }

  public SpecialModelName $specialPropertyName(Long $specialPropertyName) {
    this.$specialPropertyName = $specialPropertyName;
    return this;
  }

   /**
   * Get $specialPropertyName
   * @return $specialPropertyName
  **/
  @jakarta.annotation.Nullable

  @JsonProperty(JSON_PROPERTY_$_SPECIAL_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long get$SpecialPropertyName() {
    return $specialPropertyName;
  }


  @JsonProperty(JSON_PROPERTY_$_SPECIAL_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void set$SpecialPropertyName(Long $specialPropertyName) {
    this.$specialPropertyName = $specialPropertyName;
  }


  public SpecialModelName specialModelName(String specialModelName) {
    this.specialModelName = specialModelName;
    return this;
  }

   /**
   * Get specialModelName
   * @return specialModelName
  **/
  @jakarta.annotation.Nullable

  @JsonProperty(JSON_PROPERTY_SPECIAL_MODEL_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getSpecialModelName() {
    return specialModelName;
  }


  @JsonProperty(JSON_PROPERTY_SPECIAL_MODEL_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSpecialModelName(String specialModelName) {
    this.specialModelName = specialModelName;
  }


  /**
   * Return true if this _special_model.name_ object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpecialModelName specialModelName = (SpecialModelName) o;
    return Objects.equals(this.$specialPropertyName, specialModelName.$specialPropertyName) &&
        Objects.equals(this.specialModelName, specialModelName.specialModelName);
  }

  @Override
  public int hashCode() {
    return Objects.hash($specialPropertyName, specialModelName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpecialModelName {\n");
    sb.append("    $specialPropertyName: ").append(toIndentedString($specialPropertyName)).append("\n");
    sb.append("    specialModelName: ").append(toIndentedString(specialModelName)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

