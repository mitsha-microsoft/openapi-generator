package org.apitest.scenarios.rules.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.openapitools.codegen.CodegenProperty;

public class StringParameterGenerator extends ParameterGenerator<String> {

    private StringFormat format;
    
    public enum StringFormat {
    	NUMBER,
    	UUID,
    	EMAIL
    }
    public StringParameterGenerator(CodegenProperty property) {
        super(property);
    	if(property.isNumber) {
    		this.format = StringFormat.NUMBER;
    	}else if(property.isUuid) {
    		this.format = StringFormat.UUID;
    	}else if(property.isEmail) {
    		this.format = StringFormat.EMAIL;
    	}
    }

    @Override
    public List<Object> generateValidExamples() {
        List<Object> validExamples = new ArrayList<>();

        // Valid examples for different properties
        validExamples.addAll(generateValidRequired());
        
        if(StringFormat.NUMBER.equals(format)) {
        	validExamples.add(generateRandomNumberString());
        	validExamples.add(generateRandomNumberString());
        }else if(StringFormat.UUID.equals(format)){
        	validExamples.add(generateValidUuidString());
        	validExamples.add(generateValidUuidString());
        }else {
        	validExamples.addAll(generateValidMaxLength());
            validExamples.addAll(generateValidMinLength());
            validExamples.addAll(generateValidPattern());
            validExamples.addAll(generateValidEnum());
        }
        
        // Add more valid scenarios as needed...

        return validExamples;
    }

    @Override
    public List<Object> generateInvalidExamples() {
        List<Object> invalidExamples = new ArrayList<>();

        // Invalid examples for different properties
        invalidExamples.addAll(generateInvalidRequired());
        
        if(StringFormat.NUMBER.equals(this.format)) {
        	invalidExamples.add(generateInvalidNumberString());
        	invalidExamples.add(generateInvalidNumberString());
        }else if(StringFormat.UUID.equals(format)){
        	invalidExamples.add(generateInvalidUuidString());
        	invalidExamples.add(generateInvalidUuidString());
        }else {
        	invalidExamples.addAll(generateInvalidMaxLength());
            invalidExamples.addAll(generateInvalidMinLength());
            invalidExamples.addAll(generateInvalidPattern());
            invalidExamples.addAll(generateInvalidEnum());
        }
        
        // Add more invalid scenarios as needed...

        return invalidExamples;
    }

    // Valid scenarios

    private List<Object> generateValidRequired() {
		Integer minLength = property.minLength;
        Integer maxLength = property.maxLength;
        
        if (minLength != null && maxLength != null) {
            return Arrays.asList(
            		ensureQuotes(faker.lorem().characters(minLength, maxLength)), 
            		ensureQuotes(faker.lorem().characters(minLength, maxLength)));
        } else if(maxLength != null){
            return Arrays.asList(
            		ensureQuotes(faker.lorem().characters(maxLength)), 
            		ensureQuotes(faker.lorem().characters(maxLength)));
        } else {
            return Arrays.asList(
            		ensureQuotes(faker.lorem().word()));
        }
    }

    private List<Object> generateValidMaxLength() {
        if (property.maxLength != null && property.maxLength > 0) {
            int maxLength = property.maxLength;
            return Arrays.asList(
            		ensureQuotes(faker.lorem().characters(maxLength)), 
            		ensureQuotes(faker.lorem().characters(maxLength)));
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateValidMinLength() {
        if (property.minLength != null && property.minLength >= 0) {
            int minLength = property.minLength;
            return Arrays.asList(
            		ensureQuotes(faker.lorem().characters(minLength)), 
            		ensureQuotes(faker.lorem().characters(minLength)));
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateValidPattern() {
        if (!isNullOrEmpty(property.pattern)) {
        	String pattern = property.pattern;
            return Arrays.asList(
            		ensureQuotes(faker.regexify(pattern)), 
            		ensureQuotes(faker.regexify(pattern))
            	);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateValidEnum() {
        List<String> enumValues = property._enum;
        if (enumValues != null && !enumValues.isEmpty()) {
            return Arrays.asList(
            		ensureQuotes(enumValues.get(0))
            		);
        } else {
            return new ArrayList<>();
        }
    }

    // Invalid scenarios

    private List<Object> generateInvalidRequired() {
        if (property.required) {
            return Arrays.asList("\"\"", null);
        } else {
            return new ArrayList<>();
        }
    }


    private List<Object> generateInvalidMaxLength() {
        if (property.maxLength != null && property.maxLength > 0) {
            int maxLength = property.maxLength;
            return Arrays.asList(
            		ensureQuotes(faker.lorem().characters(maxLength + 1)), 
            		ensureQuotes(faker.lorem().characters(maxLength + 1))
            	);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateInvalidMinLength() {
        if (property.minLength != null && property.minLength > 0) {
            int minLength = property.minLength;
            return Arrays.asList(
            		ensureQuotes(faker.lorem().characters(minLength - 1)), 
            		ensureQuotes(faker.lorem().characters(minLength - 1))
            		);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateInvalidPattern() {
        if (!isNullOrEmpty(property.pattern)) {
        	// Generate a random string using Faker
            String randomString = faker.lorem().characters(property.pattern.length());

            // Modify the string to intentionally break the pattern
            // For example, add a character at a specific position
            int positionToBreakPattern = 3;
            char breakingCharacter = 'X';
            StringBuilder nonPatternString = new StringBuilder(randomString);
            nonPatternString.setCharAt(positionToBreakPattern, breakingCharacter);
            return Arrays.asList(
            		ensureQuotes(nonPatternString.toString())
            		);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateInvalidEnum() {
        List<String> enumValues = property._enum;
        if (enumValues != null && !enumValues.isEmpty()) {
            return Arrays.asList("undefined");
        } else {
            return new ArrayList<>();
        }
    }
    
    private String generateRandomNumberString() {
    	int maxLength = 5;
    	if(property.maxLength != null) {
    		maxLength = property.maxLength;
    	}
        // Generate a random number within a specified range
        int randomNumber = faker.number().numberBetween(1, (int) Math.pow(10, maxLength));

        // Convert the number to a string and truncate or pad to the desired length
        String numberString = String.valueOf(randomNumber);
        if (numberString.length() > maxLength) {
            // Truncate if too long
            numberString = numberString.substring(0, maxLength);
        } else if (numberString.length() < maxLength) {
            // Pad with zeros if too short
            numberString = String.format("%0" + maxLength + "d", randomNumber);
        }

        return ensureQuotes(numberString);
    }

    private String generateInvalidNumberString() {
    	int maxLength = 5;

    	if(property.maxLength != null) {
    		maxLength = property.maxLength;
    	}
        // Generate an invalid number string (e.g., contains non-numeric characters)
        String invalidNumberString = faker.regexify("[a-zA-Z]+");

        // Ensure the length is within the specified range
        if (invalidNumberString.length() > maxLength) {
            invalidNumberString = invalidNumberString.substring(0, maxLength);
        }

        return ensureQuotes(invalidNumberString);
    }
    
    public String generateValidUuidString() {
        // Generate a valid UUID string
        UUID validUuid = UUID.randomUUID();
        return ensureQuotes(validUuid.toString());
    }

    public String generateInvalidUuidString() {
        // Generate an invalid UUID string (e.g., non-UUID format)
        return ensureQuotes(faker.lorem().characters(36)); // Generate a random string of length 36
    }
    
    @Override
	public Object generateValidExample() {
    	if(StringFormat.NUMBER.equals(format)) {
        	return generateRandomNumberString();
        }else if(StringFormat.UUID.equals(format)){
        	return generateValidUuidString();
        }else {
        	if(generateValidEnum().size() > 0){
	        	return generateValidEnum().get(0);
	        }
        	return generateValidRequired().get(0);
        }
	}

	@Override
	public Object generateInvalidExample() {
		if(StringFormat.NUMBER.equals(format)) {
        	return generateInvalidNumberString();
        }else if(StringFormat.UUID.equals(format)){
        	return generateInvalidUuidString();
        }else {
	        if(property.minLength != null) {
	        	return (String) generateInvalidMinLength().get(0);
	        }else if(property.maxLength != null) {
	        	return (String) generateInvalidMaxLength().get(0);
	        }else if (property.pattern != null) {
	        	return (String) generateInvalidPattern().get(0);
	        } else if(property.required) {
	        	return (String) generateInvalidRequired().get(0);
	        }else if(generateInvalidEnum().size() > 0){
	        	return generateInvalidEnum().get(0);
	        } else {	        
	        	// TODO need to check further
	        	return generateRandomNumberString();
	        }
	        
        }
	}

	@Override
	public PrimitiveValidExample getPrimitiveValidExample() {
		PrimitiveValidExample example = new PrimitiveValidExample();
        example.minLength = generateValidMinLength();
        example.maxLength = generateValidMaxLength();
        example.pattern = generateValidPattern();
        example.required = generateValidRequired();
        example.enumValues = generateValidEnum();
        return example;
	}

	@Override
	public PrimitiveInvalidExample getPrimitiveInvalidExample() {
		PrimitiveInvalidExample example = new PrimitiveInvalidExample();
        example.invalidMinLength = generateInvalidMinLength();
        example.invalidMaxLength = generateInvalidMaxLength();
        example.invalidPattern = generateInvalidPattern();
        example.invalidRequired = generateInvalidRequired();
        example.invalidEnum = generateInvalidEnum();
        return example;
	}
}
