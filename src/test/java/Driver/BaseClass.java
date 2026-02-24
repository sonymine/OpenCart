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
//import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

	public static WebDriver driver;
	public Logger logger;// Log4j
	public Properties p;

	@BeforeClass(groups = { "Sanity", "Regression", "Master" })
	@Parameters({ "os", "browser" })
	public void setup(String os, String br) throws IOException {
		// loading config file
		FileReader file = new FileReader("./src//test//resources//config.properties");
		p = new Properties();
		p.load(file);

		logger = LogManager.getLogger(this.getClass());

		/*
		 * if (p.getProperty("execution_env").equalsIgnoreCase("remote")) {
		 * 
		 * DesiredCapabilities capabilities = new DesiredCapabilities();
		 * 
		 * // OS (SAFE CHECK) if (os.toLowerCase().contains("win")) {
		 * capabilities.setPlatform(Platform.WIN11); } else if
		 * (os.toLowerCase().contains("mac")) { capabilities.setPlatform(Platform.MAC);
		 * } else { throw new RuntimeException("Invalid OS value in testng.xml : " +
		 * os); }
		 * 
		 * // BROWSER switch (br.toLowerCase()) { case "chrome":
		 * capabilities.setBrowserName("chrome"); break;
		 * 
		 * case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
		 * 
		 * default: System.out.println("No matching browser"); return; }
		 * 
		 * //driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),
		 * capabilities);
		 * 
		 * 
		 * if (p.getProperty("execution_env").equalsIgnoreCase("local")) {
		 * 
		 * switch (br.toLowerCase()) { case "chrome": driver = new ChromeDriver();
		 * break; case "edge":driver = new EdgeDriver();break; case "firefox": driver =
		 * new FirefoxDriver(); break; default:
		 * System.out.println("Invalid browser name.."); return; } }
		 */
		 if (br.equalsIgnoreCase("chrome")) {

	            WebDriverManager.chromedriver().setup();   // ✅ REQUIRED
	            driver = new ChromeDriver();               // ✅ REQUIRED

	        } else {
	            throw new RuntimeException("Only Chrome is supported now");
	        }
		

		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		driver.get(p.getProperty("appURL1"));// read url from properties file
		driver.manage().window().maximize();
	}

	

	@AfterClass(groups = { "Sanity", "Regression", "Master" })
	public void tearDown() {

		if (driver != null) {
			driver.quit();
		}
	}

	public String randomString() {
		return RandomStringUtils.randomAlphabetic(5);
	}

	public String randomNumber() {
		return RandomStringUtils.randomNumeric(10);
	}

	public String randomAlphaNumeric() {
		return RandomStringUtils.randomAlphanumeric(8);
	}

	public String captureScreen(String tname) throws IOException {

		if (driver == null) {
			return "Driver is null. Screenshot not captured.";
		}

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

		String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";

		File targetFile = new File(targetFilePath);
		sourceFile.renameTo(targetFile);

		return targetFilePath;
	}

}
