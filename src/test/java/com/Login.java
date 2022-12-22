package com;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import commons.BaseTest;
import utilities.DataHelper;

public class Login extends BaseTest {
    private WebDriver driver;
    DataHelper fakeData = DataHelper.getData();

    @Parameters({ "serverName", "envName", "browser", "ipAddress", "portNumber", "osName", "osVersion", "urlAdmin", "urlUser" })
    @BeforeClass
    public void beforeClass(@Optional("dev") String serverName, @Optional("local") String envName, @Optional("chrome") String browserName, @Optional("localhost") String ipAddress, @Optional("4444") String portNumber,
                            @Optional("Windows") String osName, @Optional("10") String osVersion, String adminUrl, String endUserUrl) {

        driver = getBrowserDriver(serverName, envName, browserName, ipAddress, portNumber, osName, osVersion);
    }

    @Test
    public void TC_01_TestcaseName() {

    }

    @Test
    public void TC_02_TestcaseName() {

    }

    @Test
    public void TC_03_TestcaseName() {

    }

    @Test
    public void TC_04_TestcaseName() {

    }

    @AfterClass(alwaysRun = true)
    public void quit() {
        closeBrowserDriver();
    }
}
