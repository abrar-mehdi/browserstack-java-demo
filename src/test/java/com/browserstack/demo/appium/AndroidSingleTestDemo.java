package com.browserstack.demo.appium;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AndroidSingleTestDemo {

    private static final String APP_ID_PROPERTY = "appId";
    private static final String USERNAME_PROPERTY = "username";
    private static final String PASSWORD_PROPERTY = "password";

    @BeforeAll
    public static void setupEnvironment() {
        System.setProperty(APP_ID_PROPERTY, "********");
        System.setProperty(USERNAME_PROPERTY, "********");
        System.setProperty(PASSWORD_PROPERTY, "********");
    }

    @Test
    public void startSession() {

        AndroidDriver driver = null;

        try {
            final String appId = System.getProperty(APP_ID_PROPERTY);
            final String username = System.getProperty(USERNAME_PROPERTY);
            final String password = System.getProperty(PASSWORD_PROPERTY);

            if (Objects.isNull(appId) || appId.isBlank()) {
                throw new AssertionError("Invalid appId");
            }

            if (Objects.isNull(username) || username.isBlank()) {
                throw new AssertionError("Invalid username");
            }

            if (Objects.isNull(password) || password.isBlank()) {
                throw new AssertionError("Invalid password");
            }

            System.out.println("App id = " + appId);
            DesiredCapabilities capabilities = new DesiredCapabilities();
            HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
            browserstackOptions.put("projectName", "Certification Demo");
            browserstackOptions.put("buildName", "Certification Demo Android Build");
            browserstackOptions.put("sessionName", "Android Test");
            browserstackOptions.put("appiumVersion", "1.22.0");
            browserstackOptions.put("networkLogs", "true");
            browserstackOptions.put("deviceOrientation", "landscape");
            capabilities.setCapability("bstack:options", browserstackOptions);
            capabilities.setCapability("platformName", "android");
            capabilities.setCapability("platformVersion", "11.0");
            capabilities.setCapability("deviceName", "OnePlus 9");
            capabilities.setCapability("app", appId);

            driver = new AndroidDriver(
                    new URL("https://" + username + ":" + password + "@hub-cloud.browserstack" + ".com/wd/hub"),
                    capabilities);

            WebElement searchElement = (WebElement) new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                    ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Search Wikipedia")));
            searchElement.click();
            WebElement insertTextElement = (WebElement) new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                    ExpectedConditions.elementToBeClickable(AppiumBy.id("org.wikipedia.alpha:id/search_src_text")));
            insertTextElement.sendKeys("BrowserStack");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.TextView")));

            List<WebElement> allProductsName = driver.findElements(AppiumBy.className("android.widget.TextView"));
            assert (allProductsName.size() > 0);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(driver)) {
                driver.quit();
            }
        }
    }
}
