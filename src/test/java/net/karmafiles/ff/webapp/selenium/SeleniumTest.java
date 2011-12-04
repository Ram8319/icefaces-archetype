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

import java.net.MalformedURLException;
import java.util.Set;

@RunWith(BlockJUnit4ClassRunner.class)
public class SeleniumTest extends TestCase {

    private WebDriver driver;
    private Selenium selenium;

    @Before
    public void createDriver() throws MalformedURLException {
        driver = new FirefoxDriver();
        selenium = new WebDriverBackedSelenium(driver, "http://localhost:8080/faces/");

    }

    @After
    public void quitDriver() {
        driver.quit();
    }

    @Test
    public void testNoTest() throws Exception {
        // just to check that selenium has been initialized properly
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


    /**
     * This method uses connectStat widget to determine whether AJAX exchange has been completed and test may continue.
     */
    protected void waitForAjax() {
        try {
            for (int second = 0; ; second++) {
                if (second >= 600) fail("timeout");
                try {
                    if ("visibility: visible;".equals(selenium.getAttribute("form_home:connectStat:connection-idle@style")))
                        break;
                } catch (Exception e) {
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            // nothing, just noise
        }
    }

    /**
     * For Selenium verifications
     * @param s
     * @param value
     */
    private void verifyEquals(String s, String value) {
        assertEquals(s, value);
    }

}

