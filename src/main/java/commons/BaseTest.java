package commons;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import enums.EnvironmentList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;

import factoryEnvironment.BrowserstackFactory;
import factoryEnvironment.GridFactory;
import factoryEnvironment.LocalFactory;
import factoryEnvironment.SaucelabFactory;
import utilities.PropertiesConfig;

public class BaseTest {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
    protected final Log log;

    @BeforeSuite
    public void initBeforeSuite() {
        deleteAllureReport();
    }

    protected BaseTest() {
        log = LogFactory.getLog(getClass());
    }

    protected WebDriver getBrowserDriver(String serverName, String envName, String browserName, String ipAddress, String portNumber, String osName, String osVersion) {
        switch (envName) {
            case "local":
                driver.set(new LocalFactory(browserName).createDriver());
                break;
            case "grid":
                driver.set(new GridFactory(browserName, ipAddress, portNumber).createDriver());
                break;
            case "browserStack":
                driver.set(new BrowserstackFactory(browserName, osName, osVersion).createDriver());
                break;
            case "sourceLab":
                driver.set(new SaucelabFactory(browserName, osName).createDriver());
                break;
            default:
                driver.set(new LocalFactory(browserName).createDriver());
                break;
        }
        driver.get().manage().window().maximize();
//		driver.get().manage().timeouts().implicitlyWait(GlobalConstants.getGlobalConstants().getLongTimeout(), TimeUnit.SECONDS);
        driver.get().manage().timeouts().implicitlyWait(PropertiesConfig.getFileConfigReader().getLongTimeout(), TimeUnit.SECONDS);
        driver.get().get(getEnvironmentUrl(serverName));
        return driver.get();
    }

    private String getEnvironmentUrl(String serverName) {
        String envUrl = null;
        EnvironmentList env = EnvironmentList.valueOf(serverName.toUpperCase());
        if (env == EnvironmentList.DEV) {
            envUrl = "https://www.24h.com.vn/";
        } else if (env == EnvironmentList.STAGING) {
            envUrl = "https://www.saucedemo.com/";
        } else if (env == EnvironmentList.PRODUCTION) {
            envUrl = "https://lazesoftware.com/tool/strgen/";
        } else if (env == EnvironmentList.USER) {
            envUrl = "https://admin-demo.nopcommerce.com/";
        } else if (env == EnvironmentList.ADMIN) {
            envUrl = "https://admin-demo.nopcommerce.com/";
        }
        return envUrl;
    }

    public WebDriver getDriverInstance() {
        return driver.get();
    }

    protected boolean verifyTrue(boolean condition) {
        boolean pass = true;
        try {
            Assert.assertTrue(condition);
            log.info(" -------------------------- PASSED -------------------------- ");
        } catch (Throwable e) {
            log.info(" -------------------------- FAILED -------------------------- ");
            pass = false;
            // Add lỗi vào ReportNG
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    protected boolean verifyFalse(boolean condition) {
        boolean pass = true;
        try {
            Assert.assertFalse(condition);
            log.info(" -------------------------- PASSED -------------------------- ");
        } catch (Throwable e) {
            log.info(" -------------------------- FAILED -------------------------- ");
            pass = false;
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    protected boolean verifyEquals(Object actual, Object expected) {
        boolean pass = true;
        try {
            Assert.assertEquals(actual, expected);
            log.info(" -------------------------- PASSED -------------------------- ");
        } catch (Throwable e) {
            pass = false;
            log.info(" -------------------------- FAILED -------------------------- ");
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    public void deleteAllureReport() {
        try {
            String pathFolderDownload = GlobalConstants.getGlobalConstants().getProjectPath() + "/allure-results";
            File file = new File(pathFolderDownload);
            File[] listOfFiles = file.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    new File(listOfFiles[i].toString()).delete();
                }
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    protected void closeBrowserDriver() {
        String cmd = null;
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            log.info("OS name = " + osName);

            String driverInstanceName = driver.toString().toLowerCase();
            log.info("Driver instance name = " + driverInstanceName);

            String browserDriverName = null;

            if (driverInstanceName.contains("chrome")) {
                browserDriverName = "chromedriver";
            } else if (driverInstanceName.contains("internetexplorer")) {
                browserDriverName = "IEDriverServer";
            } else if (driverInstanceName.contains("firefox")) {
                browserDriverName = "geckodriver";
            } else if (driverInstanceName.contains("edge")) {
                browserDriverName = "msedgedriver";
            } else if (driverInstanceName.contains("opera")) {
                browserDriverName = "operadriver";
            } else {
                browserDriverName = "safaridriver";
            }

            if (osName.contains("window")) {
                cmd = "taskkill /F /FI \"IMAGENAME eq " + browserDriverName + "*\"";
            } else {
                cmd = "pkill " + browserDriverName;
            }

            if (driver != null) {
                driver.get().manage().deleteAllCookies();
                driver.get().quit();
                driver.remove();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            try {
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
