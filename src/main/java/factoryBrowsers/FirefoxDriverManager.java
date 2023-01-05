package factoryBrowsers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FirefoxDriverManager implements BrowserFactory {

    @Override
    public WebDriver getBrowserDriver() {
//        FirefoxOptions options = new FirefoxOptions();
//        options.addArguments("-private");
        return WebDriverManager.firefoxdriver().create();
    }

}
