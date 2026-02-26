package Driver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

    public static WebDriver driver;
    public Logger logger;
    public Properties p;

    @BeforeClass(groups = { "Sanity", "Regression", "Master" })
    @Parameters({ "os", "browser" })
    public void setup(String os, String br) throws IOException {

        // Load config.properties
        FileReader file = new FileReader("./src/test/resources/config.properties");
        p = new Properties();
        p.load(file);

        // Initialize logger
        logger = LogManager.getLogger(this.getClass());

        // Browser setup
        if (br.equalsIgnoreCase("chrome")) {

            WebDriverManager.chromedriver().setup(); // AUTO driver management
            driver = new ChromeDriver();

        } else {
            throw new RuntimeException("Browser not supported: " + br);
        }

        // Common browser settings
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Open application URL
        driver.get(p.getProperty("appURL1"));
    }

    @AfterClass(groups = { "Sanity", "Regression", "Master" })
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }

    // ================= Utility Methods =================

    public String randomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public String randomNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public String randomAlphaNumeric() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public String captureScreen(String testName) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        String targetPath = System.getProperty("user.dir")
                + File.separator + "screenshots"
                + File.separator + testName + "_" + timeStamp + ".png";

        File target = new File(targetPath);
        source.renameTo(target);

        return targetPath;
    }
}