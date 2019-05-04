package com.qa.rest.tests;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TestCarRacingBDD {
	
	@Test(priority=1)
	public void test_numberOfCircuitsFor2017_Season() {
		
		baseURI = "http://ergast.com/api/f1/2017/circuits.json";
		
		given().
		when().
			get("http://ergast.com/api/f1/2017/circuits.json").
		then().
			assertThat().
			statusCode(200).
			and().
			body("MRData.total", equalTo("20")).
			and().
			body("MRData.CircuitTable.season", equalTo("2017")).
			and().
			body("MRData.CircuitTable.Circuits.circuitId", hasSize(20)).
			and().
			header("Content-Length", equalTo("4551"));
	}
	
}