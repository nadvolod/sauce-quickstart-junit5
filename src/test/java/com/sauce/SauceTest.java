package com.sauce;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class SauceTest {
    private RemoteWebDriver driver;
    @AfterEach
    void tearDown() {
        if(driver != null) {
            driver.quit();
        }
    }

    @Test
    void chromeTest() throws MalformedURLException {
        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 11");
        browserOptions.setBrowserVersion("latest");
        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.put("build", generateBuildId());
        sauceOptions.put("name", "chromeTest");
        browserOptions.setCapability("sauce:options", sauceOptions);

        // start the session
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
        driver = new RemoteWebDriver(url, browserOptions);

        // run commands and assertions
        driver.get("https://saucedemo.com");
        String title = driver.getTitle();
        boolean titleIsCorrect = title.contains("Swag Labs");
        String jobStatus = titleIsCorrect ? "passed" : "failed";

        // end the session
        driver.executeScript("sauce:job-result=" + jobStatus);
    }

    private String generateBuildId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        return "Build-" + now.format(formatter);
    }


}
