package com.qa.rest.tests;

import java.io.File;
import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.apiTesting.TestAPI.ExcelDataSheetValidationPOI;
import com.apiTesting.listeners.BaseClass;
import com.apiTesting.listeners.ExtentTestManager;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public class TestWeatherAPIWithDataProvider extends BaseClass {
	
	String currentDir = System.getProperty("user.dir") + File.separator + "Library\\TestData.xlsx";
	
	/******************		POSITIVE TEST CASES			******************/
	
	@DataProvider(name = "PositiveTestSenarios")
	public Object[][] fetchPositiveTestData() throws Exception
	{
		ExcelDataSheetValidationPOI excel = new ExcelDataSheetValidationPOI();
		return excel.readFileSheet(currentDir, "PositiveTestCaseData");
	}
	
	@SuppressWarnings("rawtypes")
	@Test(priority=6, dataProvider="PositiveTestSenarios")
	public void verifyWithValidData(Map mObj) {
		
		RestAssured.baseURI = "http://api.openweathermap.org/data/2.5/weather";
		String cityName = mObj.get("City Name") + "," + mObj.get("Country Code");
		ValidatableResponse response = RestAssured.given().param("q", cityName)
				.param("appid", mObj.get("App ID")).when().get().then();
		
		Reporter.log("Test case name is: " + mObj.get("Test Case Name"), true);
		Reporter.log("Response is " + response.extract().asString(), true);
		ExtentTestManager.getTest().log(LogStatus.INFO, "Test Case Name : " + mObj.get("Test Case Name"));
		ExtentTestManager.getTest().log(LogStatus.INFO, "Response is : " + response.extract().asString());
		
		String stsCode = (String)mObj.get("Status Code");
		int statusCode = Integer.parseInt(stsCode);
		response.statusCode(statusCode);
		Reporter.log("Verified the status code successfully.", true);
		ExtentTestManager.getTest().log(LogStatus.INFO, "Verified the Status Code successfully.");
		
		response.contentType(ContentType.JSON);
		
		response.body("sys.country", Matchers.notNullValue());
		response.body("sys.country", Matchers.equalToIgnoringCase((String)mObj.get("Country Code")));
		Reporter.log("Country code has verified successfully.", true);
		ExtentTestManager.getTest().log(LogStatus.INFO, "Country Code has verified successfully.");
		
		response.body("name", Matchers.notNullValue());
		response.body("name", Matchers.equalToIgnoringCase((String)mObj.get("City Name")));
		Reporter.log("City name has verified successfully.", true);
		ExtentTestManager.getTest().log(LogStatus.INFO, "City name has verified successfully.");
	}
	
	/******************		NEGATIVE TEST CASES			******************/
	
	@DataProvider(name = "NegativeTestSenarios")
	public Object[][] fetchNegitiveTestData() throws Exception
	{
		ExcelDataSheetValidationPOI excel = new ExcelDataSheetValidationPOI();
		return excel.readFileSheet(currentDir, "NegativeTestCaseData");
	}
	
	@SuppressWarnings("rawtypes")
	@Test(priority=7, dataProvider="NegativeTestSenarios")
	public void verifyWithInvalidData(Map mObj) {
		
		RestAssured.baseURI = "http://api.openweathermap.org/data/2.5/weather";
		String cityName = mObj.get("City Name") + "," + mObj.get("Country Code");
		
		ValidatableResponse res = (ValidatableResponse) RestAssured.given().param("q", cityName)
				.param("appid", mObj.get("App ID")).when().get().then();
		
		Reporter.log("Test case name is: " + mObj.get("Test Case Name"), true);
		Reporter.log("Response is : " + res.extract().asString(), true);
		ExtentTestManager.getTest().log(LogStatus.INFO, "Test Case Name : " + mObj.get("Test Case Name"));
		ExtentTestManager.getTest().log(LogStatus.INFO, "Response is : " + res.extract().asString());
		
		String stsCode = (String)mObj.get("Status Code");
		int statusCode = Integer.parseInt(stsCode);
		res.statusCode(statusCode);
		res.body("message", Matchers.notNullValue());
		res.body("message", Matchers.containsString((String)mObj.get("Error Message")));
		Reporter.log("Verified the error message successfully.", true);
		ExtentTestManager.getTest().log(LogStatus.INFO, "Verified the error message successfully.");
	}
}
