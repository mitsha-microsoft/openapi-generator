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
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

/**
 * OuterComposite
 */
@JsonPropertyOrder({
  OuterComposite.JSON_PROPERTY_MY_NUMBER,
  OuterComposite.JSON_PROPERTY_MY_STRING,
  OuterComposite.JSON_PROPERTY_MY_BOOLEAN
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", comments = "Generator version: 7.5.0-SNAPSHOT")
public class OuterComposite {
  public static final String JSON_PROPERTY_MY_NUMBER = "my_number";
  private BigDecimal myNumber;

  public static final String JSON_PROPERTY_MY_STRING = "my_string";
  private String myString;

  public static final String JSON_PROPERTY_MY_BOOLEAN = "my_boolean";
  private Boolean myBoolean;

  public OuterComposite() {
  }

  public OuterComposite myNumber(BigDecimal myNumber) {
    
    this.myNumber = myNumber;
    return this;
  }

   /**
   * Get myNumber
   * @return myNumber
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MY_NUMBER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getMyNumber() {
    return myNumber;
  }


  @JsonProperty(JSON_PROPERTY_MY_NUMBER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMyNumber(BigDecimal myNumber) {
    this.myNumber = myNumber;
  }


  public OuterComposite myString(String myString) {
    
    this.myString = myString;
    return this;
  }

   /**
   * Get myString
   * @return myString
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MY_STRING)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getMyString() {
    return myString;
  }


  @JsonProperty(JSON_PROPERTY_MY_STRING)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMyString(String myString) {
    this.myString = myString;
  }


  public OuterComposite myBoolean(Boolean myBoolean) {
    
    this.myBoolean = myBoolean;
    return this;
  }

   /**
   * Get myBoolean
   * @return myBoolean
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MY_BOOLEAN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getMyBoolean() {
    return myBoolean;
  }


  @JsonProperty(JSON_PROPERTY_MY_BOOLEAN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMyBoolean(Boolean myBoolean) {
    this.myBoolean = myBoolean;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OuterComposite outerComposite = (OuterComposite) o;
    return Objects.equals(this.myNumber, outerComposite.myNumber) &&
        Objects.equals(this.myString, outerComposite.myString) &&
        Objects.equals(this.myBoolean, outerComposite.myBoolean);
  }

  @Override
  public int hashCode() {
    return Objects.hash(myNumber, myString, myBoolean);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OuterComposite {\n");
    sb.append("    myNumber: ").append(toIndentedString(myNumber)).append("\n");
    sb.append("    myString: ").append(toIndentedString(myString)).append("\n");
    sb.append("    myBoolean: ").append(toIndentedString(myBoolean)).append("\n");
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

  /**
   * Convert the instance into URL query string.
   *
   * @return URL query string
   */
  public String toUrlQueryString() {
    return toUrlQueryString(null);
  }

  /**
   * Convert the instance into URL query string.
   *
   * @param prefix prefix of the query string
   * @return URL query string
   */
  public String toUrlQueryString(String prefix) {
    String suffix = "";
    String containerSuffix = "";
    String containerPrefix = "";
    if (prefix == null) {
      // style=form, explode=true, e.g. /pet?name=cat&type=manx
      prefix = "";
    } else {
      // deepObject style e.g. /pet?id[name]=cat&id[type]=manx
      prefix = prefix + "[";
      suffix = "]";
      containerSuffix = "]";
      containerPrefix = "[";
    }

    StringJoiner joiner = new StringJoiner("&");

    // add `my_number` to the URL query string
    if (getMyNumber() != null) {
      try {
        joiner.add(String.format("%smy_number%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getMyNumber()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `my_string` to the URL query string
    if (getMyString() != null) {
      try {
        joiner.add(String.format("%smy_string%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getMyString()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `my_boolean` to the URL query string
    if (getMyBoolean() != null) {
      try {
        joiner.add(String.format("%smy_boolean%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getMyBoolean()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    return joiner.toString();
  }

}

