package org.apitest.scenarios.rules.codegen;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.codegen.CodegenProperty;

public class IntegerParameterGenerator extends ParameterGenerator<Integer> {

    private final Faker faker;

    public IntegerParameterGenerator(CodegenProperty property) {
        super(property);
        this.faker = new Faker();
    }

    @Override
    public List<Object> generateValidExamples() {
        List<Object> validExamples = new ArrayList<>();

        // Valid examples for different properties
        validExamples.addAll(generateValidRequired());
        validExamples.addAll(generateValidMaximum());
        validExamples.addAll(generateValidMinimum());
        validExamples.addAll(generateValidEnum());
        // Add more valid scenarios as needed...

        return validExamples;
    }

    @Override
    public List<Object> generateInvalidExamples() {
        List<Object> invalidExamples = new ArrayList<>();

        // Invalid examples for different properties
        invalidExamples.addAll(generateInvalidRequired());
        invalidExamples.addAll(generateInvalidMaximum());
        invalidExamples.addAll(generateInvalidMinimum());
        invalidExamples.addAll(generateInvalidEnum());
        invalidExamples.addAll(generateNegativeNumber());
        // Add more invalid scenarios as needed...

        return invalidExamples;
    }

    private List<Object> generateNegativeNumber() {
		return Arrays.asList(-1 * faker.random().nextInt(1, Integer.MAX_VALUE));
	}

	// Valid scenarios

    private List<Object> generateValidRequired() {
        String minLength = property.minimum;
        String maxLength = property.maximum;
        Integer max = null;
        Integer min = null;
        if(!isNullOrEmpty(maxLength)) {
        	max = Integer.parseInt(maxLength);
        }

        if(!isNullOrEmpty(minLength)) {
        	min = Integer.parseInt(minLength);
        }
        if (min != null && max != null) {
            return Arrays.asList(faker.number().numberBetween(min, max), faker.number().numberBetween(min,max));
        } else {
            return Arrays.asList(faker.number().randomDigit());
        }
    }

    private List<Object> generateValidMaximum() {
        if (property.maximum != null) {
            int maximum = Integer.parseInt(property.maximum);
            return Arrays.asList(maximum);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateValidMinimum() {
        if (property.minimum != null) {
            int minimum = Integer.parseInt(property.minimum);
            return Arrays.asList(minimum);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateValidEnum() {
        List<String> enumValues = property._enum;
        if (enumValues != null && !enumValues.isEmpty()) {
            return Arrays.asList(Integer.parseInt(enumValues.get(0)), Integer.parseInt(enumValues.get(1)));
        } else {
            return new ArrayList<>();
        }
    }

    // Invalid scenarios

    private List<Object> generateInvalidRequired() {
        if (property.required) {
            return Arrays.asList(null);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateInvalidMaximum() {
        if (property.maximum != null) {
            int maximum = Integer.parseInt(property.maximum);
            return Arrays.asList(maximum + 1, maximum + 2);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateInvalidMinimum() {
        if (property.minimum != null) {
            int minimum = Integer.parseInt(property.minimum);
            return Arrays.asList(minimum - 1, minimum - 2);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateInvalidEnum() {
        List<String> enumValues = property._enum;
        if (enumValues != null && !enumValues.isEmpty()) {
            return Arrays.asList(Integer.parseInt("99999"));
        } else {
            return new ArrayList<>();
        }
    }
    
    @Override
	public Object generateValidExample() {
    	return generateValidRequired().get(0);
	}

	@Override
	public Object generateInvalidExample() {
		
        if(property.minLength != null) {
        	return generateInvalidMinimum().get(0);
        }else if(property.maxLength != null) {
        	return generateInvalidMaximum().get(0);
        }else if(property.required) {
        	return generateInvalidRequired().get(0);
        }else if(generateInvalidEnum().size() > 0){
        	return generateInvalidEnum().get(0);
        } else {	        
        	// TODO need to check further
        	return generateNegativeNumber().get(0);
        }
	}
	
	@Override
	public PrimitiveValidExample getPrimitiveValidExample() {
		PrimitiveValidExample example = new PrimitiveValidExample();
        example.required = generateValidRequired();
        example.enumValues = generateValidEnum();
        example.minimum = generateValidMinimum();
        example.maximum = generateValidMaximum();
        return example;
	}
	
	@Override
	public PrimitiveInvalidExample getPrimitiveInvalidExample() {
		PrimitiveInvalidExample example = new PrimitiveInvalidExample();
		example.invalidRequired = generateInvalidRequired();
		example.invalidEnum = generateInvalidEnum();
		example.invalidMinimum = generateInvalidMinimum();
		example.invalidMaximum = generateInvalidMaximum();
		return example;
	}
}
