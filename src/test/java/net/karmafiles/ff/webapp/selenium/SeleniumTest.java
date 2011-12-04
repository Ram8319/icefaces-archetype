package net.karmafiles.ff.webapp.selenium;

import com.thoughtworks.selenium.Selenium;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.Capabilities;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.CommandExecutor;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.SeleneseCommandExecutor;

import java.net.MalformedURLException;
import java.util.Set;
//import java.net.URL;
//import java.util.concurrent.TimeUnit;

@RunWith(BlockJUnit4ClassRunner.class)
public class SeleniumTest extends TestCase {

    private WebDriver driver;
    private Selenium selenium;

    @Before
    public void createDriver() throws MalformedURLException {
//        final DesiredCapabilities capabilities = DesiredCapabilities.firefox();
//        capabilities.setJavascriptEnabled(true);
//        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), capabilities);
//        selenium = new WebDriverBackedSelenium(driver, "http://localhost:38080/");

//        CommandExecutor executor = new SeleneseCommandExecutor(
//                new URL("http://127.0.0.1:4444/wd/hub"),
//                new URL("http://localhost:38080/faces/"),
//                capabilities);
//        driver = new RemoteWebDriver(executor, capabilities);
//        selenium = new WebDriverBackedSelenium(driver, "http://localhost:38080/faces/");
//        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), DesiredCapabilities.firefox());
//        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver = new FirefoxDriver();
        selenium = new WebDriverBackedSelenium(driver, "http://localhost:8080/faces/");

    }

    @After
    public void quitDriver() {
        driver.quit();
    }

    @Test
    public void testNoTest() throws Exception {
        // to check that selenium has been initialized properly
//        Thread.sleep(60000);
    }

    @Test
    public void testLogonHomeAndLogoff() throws Exception {
        selenium.open("/faces/");
        verifyTrue(selenium.isTextPresent("Please log in with your credentials"));
        selenium.type("id=form_login:input_user", "");
        selenium.type("id=form_login:input_password", "");
        selenium.click("id=form_login:submit_login");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("css=span.iceMsgsError")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        verifyTrue(selenium.isTextPresent(""));
        verifyTrue(selenium.isTextPresent(""));
        selenium.type("id=form_login:input_user", "dd");
        selenium.click("id=form_login:submit_login");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("css=span.iceMsgsError")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        Thread.sleep(250);
        verifyFalse(selenium.isTextPresent("css=span.iceMsgsError"));
        selenium.type("id=form_login:input_password", "sdfsdf");
        selenium.click("id=form_login:submit_login");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("css=span.iceMsgsError")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        Thread.sleep(250);
        selenium.type("id=form_login:input_user", "dd");
        selenium.type("id=form_login:input_password", "sdfsdf");
        selenium.click("id=form_login:submit_login");
        waitForText("Authentication failed");

        Thread.sleep(250);
        verifyTrue(selenium.isTextPresent("Authentication failed"));
        selenium.type("id=form_login:input_user", "admin");
        selenium.type("id=form_login:input_password", "admin");
        selenium.click("id=form_login:submit_login");
        waitForText("You are logged in");

        Thread.sleep(500);

        selenium.click("id=form_home:link_logout");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isVisible("id=form_home:button_cancel")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        Thread.sleep(500);
        selenium.click("id=form_home:button_cancel");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isVisible("id=form_home:link_logout")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        Thread.sleep(500);

        selenium.click("id=form_home:link_logout");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isVisible("id=form_home:button_confirm")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        selenium.click("id=form_home:button_confirm");
        selenium.waitForPageToLoad("30000");
        waitForText("Please log in with your credentials");

    }

    private void waitForText(String pattern) throws InterruptedException {
        for (int second = 0; ; second++) {
            if (second >= 120) fail("timeout");
            try {
                if (selenium.isTextPresent(pattern)) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }

    private void verifyTrue(boolean value) {
        assertTrue(value);
    }

    private void verifyFalse(boolean value) {
        assertFalse(value);
    }


    public void maximize() {

        Set<String> handles = driver.getWindowHandles();
        String script = "if (window.screen){var win = window.open(window.location); win.moveTo(0,0);win.resizeTo(window.screen.availWidth,window.screen.availHeight);};";
        ((JavascriptExecutor) driver).executeScript(script);
        Set<String> newHandles = driver.getWindowHandles();
        newHandles.removeAll(handles);
        driver.switchTo().window(newHandles.iterator().next());

    }


    private void waitForAjax() throws InterruptedException {
        for (int second = 0; ; second++) {
            if (second >= 600) fail("timeout");
            try {
                if ("visibility: visible;".equals(selenium.getAttribute("form_logout:connectStat:connection-idle@style")))
                    break;
            } catch (Exception e) {
            }
            Thread.sleep(100);
        }
    }


    private void verifyEquals(String s, String value) {
        assertEquals(s, value);
    }

}

