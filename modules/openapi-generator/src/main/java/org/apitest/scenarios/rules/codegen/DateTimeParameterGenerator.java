package org.apitest.scenarios.rules.codegen;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openapitools.codegen.CodegenProperty;

public class DateTimeParameterGenerator extends ParameterGenerator<String> {

    public final static String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ssXXX";

    public DateTimeParameterGenerator(CodegenProperty property) {
        super(property);
    }

    @Override
    public List<Object> generateValidExamples() {
        List<Object> validExamples = new ArrayList<>();

        // Valid examples for different properties
        validExamples.addAll(generateValidRequired());
        validExamples.addAll(generateValidFormat());
        // Add more valid scenarios as needed...

        return validExamples;
    }

    @Override
    public List<Object> generateInvalidExamples() {
        List<Object> invalidExamples = new ArrayList<>();

        // Invalid examples for different properties
        invalidExamples.addAll(generateInvalidRequired());
        invalidExamples.addAll(generateInvalidFormat());
        invalidExamples.addAll(generateInvalidDataType());
        // Add more invalid scenarios as needed...

        return invalidExamples;
    }

    private Collection<String> generateInvalidDataType() {
        return Arrays.asList(ensureQuotes(faker.lorem().word()));
    }

    // Valid scenarios

    private List<Object> generateValidRequired() {
        if (property.required) {
            return Arrays.asList(
                formatDateTimeAsString(new Date()), 
                formatDateTimeAsString(faker.date().future(1, TimeUnit.DAYS))
            );
        } else {
            return new ArrayList<>();
        }
    }

    public List<Object> generateValidFormat() {
//        if (!isNullOrEmpty(property.getFormat())) {
//            dateTimeFormat = property.getFormat();
//        }

        return Arrays.asList(
            formatDateTimeAsString(faker.date().birthday(), dateTimeFormat),
            formatDateTimeAsString(faker.date().past(1, TimeUnit.DAYS), dateTimeFormat)
        );
    }

    // Invalid scenarios

    private List<Object> generateInvalidRequired() {
        if (property.required) {
            return Arrays.asList(null);
        } else {
            return new ArrayList<>();
        }
    }

    public List<Object> generateInvalidFormat() {
        return Arrays.asList(
        		ensureQuotes(faker.lorem().characters(20)), // Invalid datetime string
        		ensureQuotes(faker.lorem().word()) // Invalid datetime string
        );
    }

    private String formatDateTimeAsString(Date date) {
        return formatDateTimeAsString(date, dateTimeFormat);
    }

    private String formatDateTimeAsString(Date date, String format) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(format);
        return dateTimeFormat.format(date);
    }
    
    @Override
	public Object generateValidExample() {
		return this.generateValidFormat().get(0);
	}

	@Override
	public Object generateInvalidExample() {
		return ensureQuotes(faker.lorem().word());
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
		example.invalidFormat = this.generateInvalidFormat();
		return example;
	}
}

