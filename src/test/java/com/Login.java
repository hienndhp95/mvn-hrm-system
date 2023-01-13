package com;

import commons.GlobalConstants;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import commons.BaseTest;
import utilities.DataHelper;
import utilities.ExcelHelper;
import utilities.ScreenRecorderHelper;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Login extends BaseTest {
    private WebDriver driver;
    private ExcelHelper excel = ExcelHelper.getData();
    private static String date, contractCD, contractPW;
    private int count = 0;

    @Parameters({ "serverName", "envName", "browser", "ipAddress", "portNumber", "osName", "osVersion" })
    @BeforeClass
    public void beforeClass(@Optional("dev") String serverName, @Optional("local") String envName, @Optional("chrome") String browserName, @Optional("localhost") String ipAddress, @Optional("4444") String portNumber,
                            @Optional("Windows") String osName, @Optional("10") String osVersion) throws Exception {
        ScreenRecorderHelper.startRecord("Test Recording Screen");
        driver = getBrowserDriver(serverName, envName, browserName, ipAddress, portNumber, osName, osVersion);
    }
    @BeforeMethod
    public void beforeMethod() {
      Map<String, List<String>> mapDataList;
      String excelFilePath = GlobalConstants.getGlobalConstants().getProjectPath() + File.separator + "dataTest" + File.separator + "DataTest.xlsx";
      mapDataList = excel.getExcelDataAsMap(excelFilePath, "Data Test");
      date = mapDataList.get("Application Date").get(count);
      contractCD = mapDataList.get("Contract Code").get(count);
      contractPW = mapDataList.get("Contract Password").get(count);
      count++;

    }
    @Test
    public static void Test1() {
    System.out.println("Date 1 = " + date);
    System.out.println("Contract Code 1 = " + contractCD);
    System.out.println("Contract Password 1 = " + contractPW);
    }
    @Test
    public static void Test2() {
       System.out.println("Date 2 = " + date);
       System.out.println("Contract Code 2 = " + contractCD);
       System.out.println("Contract Password 2 = " + contractPW);
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
    public void quit() throws Exception{
        ScreenRecorderHelper.stopRecord();
        closeBrowserDriver();
    }
}
