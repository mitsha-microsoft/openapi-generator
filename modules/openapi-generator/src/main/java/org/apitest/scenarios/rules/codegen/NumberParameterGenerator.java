package org.apitest.scenarios.rules.codegen;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.codegen.CodegenProperty;

public class NumberParameterGenerator extends ParameterGenerator<Float> {

    private final Faker faker;

    public NumberParameterGenerator(CodegenProperty property) {
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
        float negativeValue = (float) (-1 * faker.number().randomDouble(2, 0, 1) * Float.MAX_VALUE);
        return Arrays.asList(negativeValue);
    }

    // Valid scenarios

    private List<Object> generateValidRequired() {
        String minLength = property.minimum;
        String maxLength = property.maximum;
        Float max = null;
        Float min = null;
        if (!isNullOrEmpty(maxLength)) {
            max = Float.parseFloat(maxLength);
        }

        if (!isNullOrEmpty(minLength)) {
            min = Float.parseFloat(minLength);
        }
        if (min != null && max != null) {
            double randomValue1 = faker.number().randomDouble(2, 0, 1);
            double randomValue2 = faker.number().randomDouble(2, 0, 1);
            
            float scaledValue1 = (float) (min + (max - min) * randomValue1);
            float scaledValue2 = (float) (min + (max - min) * randomValue2);
            
            return Arrays.asList(scaledValue1, scaledValue2);
        } else {
            return Arrays.asList((float) faker.number().randomDouble(2, 0, 1));
        }
    }

    private List<Object> generateValidMaximum() {
        if (property.maximum != null) {
            float maximum = Float.parseFloat(property.maximum);
            return Arrays.asList(maximum);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateValidMinimum() {
        if (property.minimum != null) {
            float minimum = Float.parseFloat(property.minimum);
            return Arrays.asList(minimum);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateValidEnum() {
        List<String> enumValues = property._enum;
        if (enumValues != null && !enumValues.isEmpty()) {
            return Arrays.asList(Float.parseFloat(enumValues.get(0)), Float.parseFloat(enumValues.get(1)));
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
            float maximum = Float.parseFloat(property.maximum);
            return Arrays.asList(maximum + 1, maximum + 2);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateInvalidMinimum() {
        if (property.minimum != null) {
            float minimum = Float.parseFloat(property.minimum);
            return Arrays.asList(minimum - 1, minimum - 2);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Object> generateInvalidEnum() {
        List<String> enumValues = property._enum;
        if (enumValues != null && !enumValues.isEmpty()) {
            return Arrays.asList(Float.parseFloat("99999.99"));
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

        if (property.minLength != null) {
            return generateInvalidMinimum().get(0);
        } else if (property.maxLength != null) {
            return generateInvalidMaximum().get(0);
        } else if (property.required) {
            return generateInvalidRequired().get(0);
        } else if (generateInvalidEnum().size() > 0) {
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
