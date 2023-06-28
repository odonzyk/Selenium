package com.lambdatest.Tests;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ParallelTest {

	// Lambdatest Credentails can be found here at https://www.lambdatest.com/capabilities-generator
	String username = System.getenv("LT_USERNAME") == null ? "YOUR LT_USERNAME" : System.getenv("LT_USERNAME"); 
	String accessKey = System.getenv("LT_ACCESS_KEY") == null ? "YOUR LT_ACCESS_KEY" : System.getenv("LT_ACCESS_KEY");
    String buildName = System.getenv("LT_BUILD_NAME") == null ? "TestNG Parallel" : System.getenv("LT_BUILD_NAME");
	
	
	public static WebDriver driver;

	@BeforeTest(alwaysRun = true)
	@Parameters(value = { "browser", "version", "platform" })
	protected void setUp(String browser, String version, String platform) throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		// set desired capabilities to launch appropriate browser on Lambdatest
		capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
		capabilities.setCapability(CapabilityType.VERSION, version);
		capabilities.setCapability(CapabilityType.PLATFORM, platform);
		
		capabilities.setCapability("build", "TestNG Parallel");
		capabilities.setCapability("build", buildName);
		capabilities.setCapability("name", "TestNG Parallel");
		capabilities.setCapability("idleTimeout", "240");
		capabilities.setCapability("network", true);
		capabilities.setCapability("video", true);
		capabilities.setCapability("console", true);
		capabilities.setCapability("visual", true);
		// capabilities.setCapability("w3c", true);
		

		System.out.println("capabilities" + capabilities);

		// Launch remote browser and set it as the current thread
		String gridURL = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";
		try {
			driver = new RemoteWebDriver(new URL(gridURL), capabilities);
		} catch (Exception e) {
			System.out.println("driver error");
			System.out.println(e.getMessage());
		}

	}

	@Test
	public void tests() throws Exception {

		try {
			driver.wait();
			// Launch the app
			driver.get("https://lambdatest.github.io/sample-todo-app/");

			// Click on First Item
			driver.findElement(By.name("li1")).click();

			// Click on Second Item
			driver.findElement(By.name("li2")).click();

			// Add new item is list
			driver.findElement(By.id("sampletodotext")).clear();
			driver.findElement(By.id("sampletodotext")).sendKeys("Yey, Let's add it to list");
			driver.findElement(By.id("addbutton")).click();

			// Verify Added item
			String item = driver.findElement(By.xpath("/html/body/div/div/div/ul/li[6]/span")).getText();
			AssertJUnit.assertTrue(item.contains("Yey, Let's add it to list"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
	}

    @AfterMethod
    public void afterMethod() {
        try {
            driver.close();
            // driver.quit();
        } catch (

        Exception e) {
            markStatus("failed", "Got exception!", driver);
            e.printStackTrace();
            driver.quit();
        }
    }

	public static void markStatus(String status, String reason, WebDriver driver) {
		JavascriptExecutor jsExecute = (JavascriptExecutor) driver;
		jsExecute.executeScript("lambda-status=" + status);
		System.out.println(reason);
	}
	

}
