package org.apitest.scenarios.rules.codegen;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.openapitools.codegen.CodegenProperty;

public class BinaryParameterGenerator extends ParameterGenerator<String> {


    public BinaryParameterGenerator(CodegenProperty property) {
        super(property);
    }

    @Override
    public List<Object> generateValidExamples() {
        List<Object> validExamples = new ArrayList<>();

        // Valid examples for different properties
        validExamples.addAll(generateValidRequired());
        validExamples.addAll(generateValidMaxLength());
        // Add more valid scenarios as needed...

        return validExamples;
    }

    @Override
    public List<Object> generateInvalidExamples() {
        List<Object> invalidExamples = new ArrayList<>();

        // Invalid examples for different properties
        invalidExamples.addAll(generateInvalidRequired());
        invalidExamples.addAll(generateInvalidMaxLength());
        // Add more invalid scenarios as needed...

        return invalidExamples;
    }

    // Valid scenarios

    private List<Object> generateValidRequired() {
            try {
				return Arrays.asList(
				    Base64.getEncoder().encodeToString(faker.lorem().characters(10, 20).getBytes("utf-8")),
				    Base64.getEncoder().encodeToString(faker.lorem().characters(15, 25).getBytes("utf-8"))
				);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            return new ArrayList<>();
    }

    private List<Object> generateValidMaxLength() {
        if (property.maxLength != null && property.maxLength > 0) {
            int maxLength = property.maxLength;
            try {
				return Arrays.asList(
				    Base64.getEncoder().encodeToString(faker.lorem().characters(maxLength).getBytes("utf-8")),
				    Base64.getEncoder().encodeToString(faker.lorem().characters(maxLength - 5).getBytes("utf-8"))
				);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
            return new ArrayList<>();
    }

    // Invalid scenarios

    private List<Object> generateInvalidRequired() {
        if (property.required) {
            return Arrays.asList("", null);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateInvalidMaxLength() {
        if (property.maxLength != null && property.maxLength > 0) {
            int maxLength = property.maxLength;
            try {
				return Arrays.asList(
				    Base64.getEncoder().encodeToString(faker.lorem().characters(maxLength + 5).getBytes("utf-8")),
				    Base64.getEncoder().encodeToString(faker.lorem().characters(maxLength + 10).getBytes("utf-8"))
				);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
        return new ArrayList<>();
    }

	@Override
	public Object generateValidExample() {
		return this.generateValidRequired().get(0);
	}

	@Override
	public Object generateInvalidExample() {
		return faker.lorem().word();
	}

	@Override
	public PrimitiveValidExample getPrimitiveValidExample() {
		PrimitiveValidExample example = new PrimitiveValidExample();
		example.required = this.generateValidRequired();
		return example;
	}

	@Override
	public PrimitiveInvalidExample getPrimitiveInvalidExample() {
		PrimitiveInvalidExample example = new PrimitiveInvalidExample();
		example.invalidRequired = this.generateInvalidRequired();
		return example;
	}
}

