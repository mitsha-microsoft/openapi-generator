package org.apitest.scenarios.rules.codegen;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import com.github.javafaker.Faker;

import io.swagger.v3.oas.models.media.Schema;

public abstract class ParameterGenerator<T> {
	
    protected final Faker faker;

	public CodegenProperty property;
	public Schema schema;
	
    public abstract List<Object> generateValidExamples();
    public abstract List<Object> generateInvalidExamples();
    
    public abstract Object generateValidExample();
    public abstract Object generateInvalidExample();
    public abstract PrimitiveValidExample getPrimitiveValidExample();
    public abstract PrimitiveInvalidExample getPrimitiveInvalidExample();
    
    
    public ParameterGenerator(CodegenProperty property) {
    	this.property = property;
        this.faker = new Faker();
    }
    
    // Other common utility methods...

    protected boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    protected boolean isWithinMaxLength(String value) {
        return isNullOrEmpty(value) || value.length() <= property.maxLength;
    }

    protected boolean isWithinMinLength(String value) {
        return isNullOrEmpty(value) || value.length() >= property.minLength;
    }

    protected boolean matchesPattern(String value) {
        return isNullOrEmpty(value) || value.matches(property.pattern);
    }
    
    public String ensureQuotes(String in) {
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
}
