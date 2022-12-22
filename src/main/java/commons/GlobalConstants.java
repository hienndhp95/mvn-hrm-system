package commons;

import java.io.File;

import lombok.Getter;

@Getter
public class GlobalConstants {
    private final String portalPageUrl = "https://demo.nopcommerce.com/";
    private final String adminPageUrl = "https://admin-demo.nopcommerce.com/";
    private final String projectPath = System.getProperty("user.dir");
    private final String PATH_TEST_DATA = projectPath + "";
    private final String javaVersion = System.getProperty("java.version");
    private final String uploadFile = projectPath + File.separator + "uploadFiles" + File.separator;
    private final String downloadFile = projectPath + File.separator + "downloadFiles";
    private final String browserLog = projectPath + File.separator + "browserLogs";
    private final String reportingScreenshot = projectPath + File.separator + "reportNGImages" + File.separator;
    private final String dbDevUrl = "192.168.100.168:8080";
    private final String dbDevUser = "sa";
    private final String dbDevPassword = "kinjirou";
    private final String dbProductionUrl = "172.58.100.168:8080";
    private final String dbProductionUser = "admin";
    private final String dbProductionPassword = "ABC@123";
    private final String browserStackUsername = "";
    private final String browserStackKey = "";
    private final String browserStackUrl = "";
    private final String sauceUsername = "";
    private final String sauceKey = "";
    private final String sauceUrl = "";
    private final String crossUsername = "hiennd@nittsu-system.com".replaceAll("@", "%40");
    private final String crossKey = "";
    private final String crossUrl = "";
    private final long shortTimeout = 5;

    private final long longTimeout = 30;
    private final long retryTestFail = 3;

    private static GlobalConstants globalInstance;

    private GlobalConstants() {

    }

    public static synchronized GlobalConstants getGlobalConstants() {
        if (globalInstance == null) {
            globalInstance = new GlobalConstants();
        } else {

        }
        return globalInstance;
    }
}
