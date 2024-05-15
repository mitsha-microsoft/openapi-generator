package org.apitest.scenarios.rules.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openapitools.codegen.CodegenProperty;

public class FileParameterGenerator extends ParameterGenerator<byte[]> {


    public FileParameterGenerator(CodegenProperty property) {
        super(property);
    }

    @Override
    public List<Object> generateValidExamples() {
        List<Object> validExamples = new ArrayList<>();

        // Valid examples for different properties
        validExamples.addAll(generateValid());
        // Add more valid scenarios as needed...

        return validExamples;
    }

    @Override
    public List<Object> generateInvalidExamples() {
        List<Object> invalidExamples = new ArrayList<>();

        // Invalid examples for different properties
        invalidExamples.addAll(generateInvalid());
        // Add more invalid scenarios as needed...

        return invalidExamples;
    }

    private List<Object> generateInvalid() {
		return Arrays.asList("undefined", null);
	}

	// Valid scenarios

    private List<Object> generateValid() {
	    	String fileName = "swagger.json";
            String filePath = "C:\\\\Users\\\\krchanda\\\\Downloads\\\\apitestingpoc\\\\swagger.json";
	        Object example = "new File([fs.readFileSync('" + filePath + "')], '" + fileName + "')";
            return Arrays.asList(
               example
            );
    }


	@Override
	public Object generateValidExample() {
		return this.generateValid().get(0);
	}

	@Override
	public Object generateInvalidExample() {
		return "undefined";
	}

	@Override
	public PrimitiveValidExample getPrimitiveValidExample() {
		PrimitiveValidExample example = new PrimitiveValidExample();
        example.binary = this.generateValid();
        return example;
	}

	@Override
	public PrimitiveInvalidExample getPrimitiveInvalidExample() {
		PrimitiveInvalidExample example = new PrimitiveInvalidExample();
		example.invalidFileTypes = this.generateInvalid();
		return example;
	}
}


