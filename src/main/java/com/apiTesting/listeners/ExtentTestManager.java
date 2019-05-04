package com.apiTesting.listeners;

import java.util.Map;
import java.util.HashMap;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentTestManager {
	
	static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
	static ExtentReports extent = ExtentManager.getReporter();
	
	public static synchronized ExtentTest getTest() {
		
		return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
	}
	
	public static synchronized void endTest() {
		extent.endTest((ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId())));
	}
	
	public static synchronized ExtentTest startTest(String testName) {
		return startTest(testName, "");
	}
	
	public static synchronized ExtentTest startTest(String testName, String desc) {
		
		ExtentTest test = extent.startTest(testName, desc);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
		
		return test;
	}
	
	
	
}
