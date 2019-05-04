package com.apiTesting.listeners;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.testng.ITestResult;
import java.lang.reflect.Method;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.relevantcodes.extentreports.LogStatus;
/**
 * 
 * @author Rahim Fazle
 * 
 */
public abstract class BaseClass {
	/**
	 * @param method
	 */
	@BeforeMethod
	public void beforeMethod(Method method) {
		ExtentTestManager.startTest(method.getName());
	}
	
	/**
	 * 
	 * @param result
	 */
	@AfterMethod
	protected void afterMethod(ITestResult result) {
		if(result.getStatus() == ITestResult.FAILURE) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, result.getThrowable());
		}else if(result.getStatus() == ITestResult.SKIP) {
			ExtentTestManager.getTest().log(LogStatus.SKIP, "" + result.getThrowable());
		}else {
			ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed");
		}
		
		ExtentManager.getReporter().endTest(ExtentTestManager.getTest());
		ExtentManager.getReporter().flush();
	}
	
	/**
	 * 
	 * @param t
	 * @return 
	 */
	protected String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		
		return sw.toString();
	}
}
