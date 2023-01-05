package factoryEnvironment;

import enums.BrowserList;
import org.openqa.selenium.WebDriver;

import factoryBrowsers.BrowserNotSupportException;
import factoryBrowsers.ChromeDriverManager;
import factoryBrowsers.EdgeDriverManager;
import factoryBrowsers.FirefoxDriverManager;

public class LocalFactory {
    private WebDriver driver;
    String browserName;

    public LocalFactory(String browserName) {
        this.browserName = browserName;
    }

    public WebDriver createDriver() {
        BrowserList browser = BrowserList.valueOf(browserName.toUpperCase());

        System.out.println("Run with - " + browserName);

        switch (browser) {
            case CHROME:
                driver= new ChromeDriverManager().getBrowserDriver();
                break;
            case FIREFOX:
                driver= new FirefoxDriverManager().getBrowserDriver();
                break;
            case EDGE:
                driver= new EdgeDriverManager().getBrowserDriver();
                break;
            default:
                throw new BrowserNotSupportException(browserName);
        }

        return driver;
    }

}
