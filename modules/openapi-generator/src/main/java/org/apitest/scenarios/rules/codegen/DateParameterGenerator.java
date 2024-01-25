package org.apitest.scenarios.rules.codegen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.openapitools.codegen.CodegenProperty;

public class DateParameterGenerator extends ParameterGenerator<Date> {


    public DateParameterGenerator(CodegenProperty property) {
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

    private Collection<Object> generateInvalidDataType() {
        return Arrays.asList(faker.lorem().word());
    }

    // Valid scenarios

    private List<Object> generateValidRequired() {
        if (property.required) {
            return Arrays.asList(
                formatDateAsString(new Date()), 
                formatDateAsString(faker.date().future(1, TimeUnit.DAYS))
            );
        } else {
            return new ArrayList<>();
        }
    }

    public List<Object> generateValidFormat() {
        String dateFormat = "yyyy-MM-dd";
        if (!isNullOrEmpty(property.getFormat())) {
            dateFormat = property.getFormat();
        }

        return Arrays.asList(
            formatDateAsString(faker.date().birthday(), dateFormat),
            formatDateAsString(faker.date().past(1, TimeUnit.DAYS), dateFormat)
        );
    }

    @Override
	public Object generateValidExample() {
		return this.generateValidFormat().get(0);
	}

	@Override
	public Object generateInvalidExample() {
		return ensureQuotes(faker.lorem().word());
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
            faker.lorem().characters(10), // Invalid date string
            faker.lorem().word() // Invalid date string
        );
    }

    private String formatDateAsString(Date date) {
        return formatDateAsString(date, "yyyy-MM-dd");
    }

    private String formatDateAsString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ROOT);
        return dateFormat.format(date);
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
