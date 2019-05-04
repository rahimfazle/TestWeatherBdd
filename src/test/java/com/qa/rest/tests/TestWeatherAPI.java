package com.qa.rest.tests;

import org.hamcrest.Matchers;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;

public class TestWeatherAPI {
	
	String responseString;
	ValidatableResponse responseJson;
	
	@BeforeMethod
	public void initiateFirst() {
		// Set API URI
		RestAssured.baseURI = "http://api.openweathermap.org/data/2.5/weather";
	}
	
	@Test(priority=2)
	public void getWeatherOfBanglore() {
		// Storing response into a String.
		String responseString = RestAssured.given().param("q", "Bangalore").param("appid", "14d92fb7c2c0cd897c5ce09935897c0d").
		when().get().then().extract().asString();
		System.out.println("Response is :- " + responseString);
		Reporter.log("Response is :- " + responseString, true);
		
		JsonPath path = new JsonPath(responseString);
		
		// Store response into ValidatableResponse Json format.
		responseJson = RestAssured.given().param("q", "Bangalore").param("appid", "14d92fb7c2c0cd897c5ce09935897c0d").
		when().get().then();
		
		// Validate Status code from Json format.
		String statusCod = path.get("cod").toString();
		System.out.println("Status code is - " + statusCod);
		Reporter.log("Verified the Status code successfully. " + statusCod, true);
		
		responseJson.contentType(ContentType.JSON);
		Object countryName = responseJson.extract().response().path("sys.country");
		System.out.println("Country Name is - " + countryName);
		
		//JsonPath path = new JsonPath(responseString);
		Object countryNm = path.get("sys.country");
		System.out.println("Country name is : " + countryNm);
		
		responseJson.body("sys.country", Matchers.notNullValue());
		responseJson.body("sys.country", Matchers.equalToIgnoringCase("IN"));
		Reporter.log("Verified the Country code successfully. ", true);
		
		responseJson.body("name", Matchers.notNullValue());
		responseJson.body("name", Matchers.equalToIgnoringCase("Bangalore"));
		Reporter.log("Verified the City name successfully. ", true);
	}
	
	@Test(priority=3)
	public void getWeatherBangaloreInvalidCity() {
		
		responseJson = RestAssured.given().param("q", "Bangaloree").param("appid", "14d92fb7c2c0cd897c5ce09935897c0d").
						when().get().then();
		Reporter.log("Response is - " + responseJson.extract().asString(), true);
		
		responseJson.statusCode(404);
		responseJson.body("message", Matchers.notNullValue());
		responseJson.body("message", Matchers.equalToIgnoringCase("city not found"));
		Reporter.log("Verified the error message successfully.", true);
		
	}
	
	@Test(priority=4)
	public void getWeatherBangaloreInvalidKey() {
		
		responseJson = RestAssured.given().param("q", "Bangalore").param("appid", "14d92fb7c2c0cd897c5ce09935897c0dA").
				when().get().then();
		Reporter.log("Response is - " + responseJson.extract().asString(), true);

		responseJson.statusCode(401);
		responseJson.body("message", Matchers.notNullValue());
		responseJson.body("message", Matchers.containsString("Invalid API key."));
		Reporter.log("Verified the error message successfully.", true);
		
	}
	
	@Test(priority=5)
	public void getWeatherOfDelhi() {
		
		// Storing response into a String.
		String responseString = RestAssured.given().param("q", "Delhi").param("appid", "14d92fb7c2c0cd897c5ce09935897c0d").
		when().get().then().extract().asString();
		System.out.println("Response is :- " + responseString);
		Reporter.log("Response is :- " + responseString, true);
		
		JsonPath path = new JsonPath(responseString);
		
		// Store response into ValidatableResponse Json format.
		responseJson = RestAssured.given().param("q", "Delhi").param("appid", "14d92fb7c2c0cd897c5ce09935897c0d").
		when().get().then();
		
		// Validate Status code from Json format.
		String statusCod = path.get("cod").toString();
		System.out.println("Status code is - " + statusCod);
		Reporter.log("Verified the Status code successfully. " + statusCod, true);
		
		responseJson.contentType(ContentType.JSON);
		Object countryName = responseJson.extract().response().path("sys.country");
		System.out.println("Country Name is - " + countryName);
		
		//JsonPath path = new JsonPath(responseString);
		Object countryNm = path.get("sys.country");
		System.out.println("Country name is : " + countryNm);
		
		responseJson.body("sys.country", Matchers.notNullValue());
		responseJson.body("sys.country", Matchers.equalToIgnoringCase("IN"));
		Reporter.log("Verified the Country code successfully. ", true);
		
		responseJson.body("name", Matchers.notNullValue());
		responseJson.body("name", Matchers.equalToIgnoringCase("Delhi"));
		System.out.println("City name is : " + responseJson.extract().response().path("name")); //  ***
		
		Reporter.log("Verified the City name successfully. ", true);
	}
}
