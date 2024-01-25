package org.apitest.scenarios.codegen;

public class ExpectedOutcome{
	public int statusCode;
	public String responseBody;
	
	public ExpectedOutcome() {
	}
	
	public ExpectedOutcome(int statusCode, String responseBody) {
		this.statusCode = statusCode;
		this.responseBody = responseBody;
	}
}
