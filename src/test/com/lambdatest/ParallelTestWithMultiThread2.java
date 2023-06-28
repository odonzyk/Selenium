package com.lambdatest;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

import java.util.Arrays;
import org.openqa.selenium.*;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ParallelTestWithMultiThread2 {
    public static String hubURL = "https://hub.lambdatest.com/wd/hub";
    private WebDriver driver;

    @BeforeMethod
    public void beforeMethos() throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("PlatformName", "macOS Sierra");
        capabilities.setCapability("browserName", "Microsoft Edge");
        capabilities.setCapability("browserVersion", "87.0");
        capabilities.setCapability("commandLog", true);
        capabilities.setCapability("performance", true);
        capabilities.setCapability("console",true);
        capabilities.setCapability("network",true);
        capabilities.setCapability("video", true);
        capabilities.setCapability("visual",true);
        HashMap<String, Object> ltOptions = new HashMap<String, Object>();
        ltOptions.put("user", "donzyk"); // System.getenv("LT_USERNAME"));
        ltOptions.put("accessKey", "mtCoR3rHCVlngYUCQ3Jt3XpAHvJUeW76LGVAqvAtdGprMUrDCk"); // System.getenv("LT_ACCESS_KEY"));
        ltOptions.put("project", "Donzyk");
        ltOptions.put("build", "Selenium 4");
        ltOptions.put("selenium_version", "4.1.0");
        ltOptions.put("name", this.getClass().getName());
        ltOptions.put("platformName", "macOS Sierra");
        ltOptions.put("seCdp", true);
        ltOptions.put("visual", true);
        ltOptions.put("video", true);
        ltOptions.put("console", "true");
        ltOptions.put("w3c", true);
        ltOptions.put("idleTimeout", "20");
        ltOptions.put("plugin", "java-java");
        capabilities.setCapability("LT:Options", ltOptions);



        driver = new RemoteWebDriver(new URL(hubURL), capabilities);
        // System.out.println(driver);
    }

    @Test
    public void Testcases() {
        WebDriverWait wait;
        Augmenter augmenter = new Augmenter();
        driver = augmenter.augment(driver);

        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // access Url
        driver.get("https://www.lambdatest.com");
        JavascriptExecutor j = (JavascriptExecutor) driver;
        if (j.executeScript("return document.readyState").toString().equals("complete")) {
            System.out.println("Page has loaded");
        }
        wait.until(driver -> driver.getTitle().contentEquals("Next-Generation Mobile Apps and Cross Browser Testing Cloud | LambdaTest"));
        System.out.println(("Title of page is: " + driver.getTitle()));

        WebElement element = driver.findElement(By.linkText("SEE ALL INTEGRATIONS"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("window.open('about:blank','_blank');", element);

        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1)); //switches to new tab
        System.out.println("Windoww handles:"+tabs);

        driver.get("https://www.lambdatest.com/integrations");
        String URL = driver.getCurrentUrl();
        assertEquals("https://www.lambdatest.com/integrations",URL);

    }


    @AfterMethod
    public void afterMethod() {
        try {
            driver.close();
            driver.quit();
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

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        ParallelTestWithMultiThread test = new ParallelTestWithMultiThread();
        test.Testcases();
    }

}
