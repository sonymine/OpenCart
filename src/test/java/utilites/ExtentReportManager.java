package utilites;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import Driver.BaseClass;

public class ExtentReportManager implements ITestListener {

    public ExtentSparkReporter sparkReporter;
    public ExtentReports extent;
    public ExtentTest test;
    String repName;

    // For parallel execution
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // ON START
    public void onStart(ITestContext testContext) {

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        repName = "Test-Report-" + timeStamp + ".html";

        sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/reports/" + repName);
        sparkReporter.config().setDocumentTitle("OpenCart Automation Report");
        sparkReporter.config().setReportName("OpenCart Functional Testing");
        sparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Application", "OpenCart");
        extent.setSystemInfo("Module", "Admin");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User Name", System.getProperty("user.name"));

        String os = testContext.getCurrentXmlTest().getParameter("os");
        extent.setSystemInfo("Operating System", os);

        String browser = testContext.getCurrentXmlTest().getParameter("browser");
        extent.setSystemInfo("Browser", browser);

        List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
        if (!includedGroups.isEmpty()) {
            extent.setSystemInfo("Groups", includedGroups.toString());
        }
    }

    // TEST SUCCESS
    public void onTestSuccess(ITestResult result) {

        test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);

        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.PASS, result.getName() + " executed successfully");
    }

    // TEST FAILURE
    public void onTestFailure(ITestResult result) {

        test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);

        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.FAIL, result.getName() + " failed");
        test.log(Status.INFO, result.getThrowable());

        try {
            BaseClass base = (BaseClass) result.getInstance();
            String imgPath = base.captureScreen(result.getMethod().getMethodName());
            test.addScreenCaptureFromPath(imgPath);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TEST SKIPPED
    public void onTestSkipped(ITestResult result) {

        test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);

        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.SKIP, result.getName() + " skipped");

        if (result.getThrowable() != null) {
            test.log(Status.INFO, result.getThrowable());
        }
    }

    // ON FINISH
    public void onFinish(ITestContext testContext) {

        extent.flush();

        String path = System.getProperty("user.dir") + "/reports/" + repName;
        File reportFile = new File(path);

        try {
            Desktop.getDesktop().browse(reportFile.toURI());
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}