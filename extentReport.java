package com.qa.Utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import static com.qa.ProjectLibrary.mobileAndroid_ConnectionScript.driver;

public class extentReport implements ITestListener {

	static ExtentReports reports;
	static ExtentTest test;
	
	static {
		try {
			String dateformat = new SimpleDateFormat("ddMMMyyyy_hh_mm_ssaa").format(Calendar.getInstance().getTime());
			reports = new ExtentReports(System.getProperty("user.dir") + "\\test-output\\" + dateformat + ".html",true);
			reports.addSystemInfo("Host_Name", "KONE_MobileSiteSurvey").addSystemInfo("Environment", "AutomationTestingPOC");
			reports.loadConfig(new File(System.getProperty("user.dir") + "\\com.qa.supportingFiles\\extent-config.xml"));
//			 System.out.println("--Static Method executed--");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onTestStart(ITestResult result) {
		test.log(LogStatus.INFO, result.getName() + ": Test Execution is STARTED");
//		System.out.println("--onTestStart SCRIPT--"+result.getName()+"=");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		test.log(LogStatus.PASS, result.getName() + ": TestCase is PASSED");
//		System.out.println("--onTestSuccess SCRIPT--");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		test.log(LogStatus.FAIL, result.getName() + ": TestCase is FAILED");
		try {
//			System.out.println("--getName--"+result.getName()+"--getInstanceName--"+result.getInstanceName()+"--getAttributeNames--"+result.getAttributeNames());
			screenshot_capture(result.getName());
			String screenshotinReport=screenshot_capture(result.getName());
			test.log(LogStatus.FAIL, "Refer Screen Shot"+test.addScreenCapture(screenshotinReport));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		test.log(LogStatus.SKIP, result.getName() + " TestCase is SKIPPED due to the reason " + result.getThrowable());
//		System.out.println("--onTestSkipped SCRIPT--");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	@Override 
	public void onStart(ITestContext context) {
		try {
			test = reports.startTest(context.getName());
			test.log(LogStatus.INFO, context.getName() + " :Execution Started");
//			 System.out.println("--onTestStart SCRIPT executed "+context.getName()+"<>"+context.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFinish(ITestContext context) {
		try {
			reports.endTest(test);
			reports.flush();
//			System.out.println("--onFinish SCRIPT--");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String screenshot_capture(String screenshotName) throws IOException {
		RemoteWebDriver ss_driver=driver;
        String location = null;
        String dateformat = new SimpleDateFormat("ddMMMyyyy_hh_mm_ssaa").format(Calendar.getInstance().getTime());
		try {
			TakesScreenshot takescreenshot = (TakesScreenshot) ss_driver;
			File source = takescreenshot.getScreenshotAs(OutputType.FILE);
			File creatFolder=new File("C:\\Users\\con_svijay02\\eclipse-workspace\\KONE_MobileSiteSurvey_POC\\screenShots\\"+dateformat);
			creatFolder.mkdir();
			String new_folderLocation=creatFolder.getAbsolutePath();
			File destination = new File(new_folderLocation+"\\SS_"+dateformat+"_"+screenshotName+".png");
			location=destination.getAbsolutePath();
//			System.out.println(location);
			FileUtils.copyFile(source, destination);	        
		} catch (WebDriverException e) {
			e.printStackTrace();
		}
		return location;
    }
}
