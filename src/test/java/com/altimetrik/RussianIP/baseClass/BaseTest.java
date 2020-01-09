package com.altimetrik.RussianIP.baseClass;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.altimetrik.RussianIP.apiUtils.PropertiesManager;
import com.jayway.restassured.RestAssured;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class BaseTest{


	public Response res = null; //Response
	public JsonPath jp = null; //JsonPath
	public static ExtentReports extent;
	public static ExtentTest logger;
	//Setting up the baseURL from config.property file
	@BeforeSuite
	public void setupBaseUrl() throws IOException {

		PropertiesManager.initializeProperties();

		RestAssured.baseURI=PropertiesManager.getProperty("baseURI");

		System.out.println("This is URL:"+RestAssured.baseURI);

	}

	@BeforeTest
	public void startReport(){
		//ExtentReports(String filePath,Boolean replaceExisting) 
		//filepath - path of the file, in .htm or .html format - path where your report needs to generate. 
		//replaceExisting - Setting to overwrite (TRUE) the existing file or append to it
		//True (default): the file will be replaced with brand new markup, and all existing data will be lost. Use this option to create a brand new report
		//False: existing data will remain, new tests will be appended to the existing report. If the the supplied path does not exist, a new file will be created.
		extent = new ExtentReports("D:\\AjitAltimetrik\\IPS\\RussianIP\\src\\test\\java\\com\\altimetrik\\RussianIP\\reports\\APIAutomationResult.html");
		//extent.addSystemInfo("Environment","Environment Name")
		extent
		.addSystemInfo("Host Name", "API Automation")
		.addSystemInfo("Environment", "API Automation Testing")
		.addSystemInfo("User Name", "Ajit Bhavle");
		//loading the external xml file (i.e., extent-config.xml) which was placed under the base directory
		//You could find the xml file below. Create xml file in your project and copy past the code mentioned below

		extent.loadConfig(new File("D:\\AjitAltimetrik\\IPS\\RussianIP\\src\\test\\java\\com\\altimetrik\\RussianIP\\reports\\extent-config.xml"));

	}
	@BeforeMethod
	public void testLogger(Method method)
	{
		logger=extent.startTest(this.getClass().getSimpleName()+ " :: " +method.getName());
		logger.assignAuthor("Ajit Bhavle");
		logger.assignCategory("API TestCases");

	}
	@AfterMethod
	public void getResult(ITestResult result) throws IOException{
		if(result.getStatus() == ITestResult.FAILURE){
			logger.log(LogStatus.FAIL, "Test Case Failed is "+result.getName());
			logger.log(LogStatus.FAIL, "Message is: "+result.getThrowable());

		}else if(result.getStatus() == ITestResult.SKIP){
			logger.log(LogStatus.SKIP, "Test Case Skipped is "+result.getName());
			throw new SkipException("Skipping this test-method with exception");
		}else{
			logger.log(LogStatus.PASS, "Test Case Passed is "+result.getName());
		}
		// ending test
		//endTest(logger) : It ends the current test and prepares to create HTML report
		extent.endTest(logger);

	}

	@AfterTest
	public void endReport(){
		// writing everything to document
		//flush() - to write or update test information to your report. 
		extent.flush();
		//Call close() at the very end of your session to clear all resources. 
		//If any of your test ended abruptly causing any side-affects (not all logs sent to ExtentReports, information missing), this method will ensure that the test is still appended to the report with a warning message.
		//You should call close() only once, at the very end (in @AfterSuite for example) as it closes the underlying stream. 
		//Once this method is called, calling any Extent method will throw an error.
		//close() - To close all the operation
		extent.close();
	}

	@AfterSuite
	public void afterClass() {

		RestAssured.baseURI=null;
	}


}
