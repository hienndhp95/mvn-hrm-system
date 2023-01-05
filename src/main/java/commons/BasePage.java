package commons;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.qameta.allure.Step;
import pageUIs.CommonUI;

public class BasePage {

    // Open URL
    /**
     * The URL to load. It is best to use a fully qualified URL
     *
     * @param driver  driver of browser
     * @param pageUrl The URL to load
     */
    @Step("Open url {1}")
    public void openPageUrl(WebDriver driver, String pageUrl) {
        driver.get(pageUrl);
        takeScreenshot(driver, "Open Url= " + pageUrl);
        Assert.assertTrue(areJQueryAndJSLoadedSuccess(driver));
    }

    /**
     * The title of the current page.
     *
     * @param driver driver of browser
     * @return The title of the current page, with leading and trailing whitespace stripped, or nullif one is not already set
     */
    public String getPageTitle(WebDriver driver) {
        return driver.getTitle();
    }

    /**
     * Get the source of the last loaded page
     *
     * @param driver driver of browser
     * @return The source of the current page
     */
    public String getPageSourceCode(WebDriver driver) {
        return driver.getPageSource();
    }

    /**
     * Move back a single "item" in the browser's history.
     *
     * @param driver driver of browser
     */
    public void backToPage(WebDriver driver) {
        driver.navigate().back();
    }

    /**
     * Move a single "item" forward in the browser's history. Does nothing if we are on the latest page viewed.
     *
     * @param driver driver of browser
     */
    public void forwardToPage(WebDriver driver) {
        driver.navigate().forward();
    }

    /**
     * Refresh the current page
     *
     * @param driver driver of browser
     */
    @Step("Refresh screen")
    public void refreshCurrentPage(WebDriver driver) {
        driver.navigate().refresh();
        takeScreenshot(driver, "Refresh page");
    }

    /**
     * Repeatedly applies this instance's input value to the given function until one of the followingoccurs: 1.the function returns neither null nor false 2.the
     * function throws an unignored exception 3.the timeout expires 4.the current thread is interrupted
     *
     * @param driver driver of browser
     * @return The function's return value if the function returned something different from null or false before the timeout expired.
     */
    public Alert waitForAlertPresence(WebDriver driver) {
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        return explicitWait.until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Click to Accept button in Alert
     *
     * @param driver driver of browser
     */
    public void acceptAlert(WebDriver driver) {
        waitForAlertPresence(driver).accept();
    }

    /**
     * Click to Cancel button in Alert
     *
     * @param driver driver of browser
     */
    public void cancelAlert(WebDriver driver) {
        waitForAlertPresence(driver).dismiss();
    }

    /**
     * Get text in the alert
     *
     * @param driver driver of browser
     * @return Returns the text displayed in the alert.
     */
    public String getTextAlert(WebDriver driver) {
        return waitForAlertPresence(driver).getText();
    }

    /**
     * Send key to alert
     *
     * @param driver    driver of browser
     * @param textValue value to input
     */
    public void senkeyToAlert(WebDriver driver, String textValue) {
        waitForAlertPresence(driver).sendKeys(textValue);
    }

    /**
     * Switch to window by ID of window
     *
     * @param driver   driver of browser
     * @param parentID ID of window
     */
    public void switchToWindowByID(WebDriver driver, String parentID) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(parentID)) {
                driver.switchTo().window(window);
                break;
            }
        }
    }

    /**
     * Switch to window by title of window
     *
     * @param driver driver of browser
     * @param title  title of window
     */
    public void switchToWindowByTitle(WebDriver driver, String title) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            driver.switchTo().window(window);
            String currentWinTitle = driver.getTitle();
            if (currentWinTitle.equals(title)) {
                break;
            }
        }
    }

    /**
     * Close all windows without parent window
     *
     * @param driver   driver of browser
     * @param parentID ID of window
     */
    public void closeAllWindowWithoutParent(WebDriver driver, String parentID) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String id : allWindows) {
            if (!id.equals(parentID)) {
                driver.switchTo().window(id);
                driver.close();
            }
        }
        driver.switchTo().window(parentID);
    }

    /**
     * Get By locator
     *
     * @param locatorType type of element start with id or class or name or css or xpath
     * @return A By which locates elements by the value of the "id" or "class" or "name" or "css" or "xpath".
     */
    private By getByLocator(String locatorType) {
        By by = null;
        if (locatorType.startsWith("ID=") || locatorType.startsWith("Id=") || locatorType.startsWith("id=")) {
            by = By.id(locatorType.substring(3));
        } else if (locatorType.startsWith("CLASS=") || locatorType.startsWith("Class=") || locatorType.startsWith("class=")) {
            by = By.className(locatorType.substring(6));
        } else if (locatorType.startsWith("NAME=") || locatorType.startsWith("Name=") || locatorType.startsWith("name=")) {
            by = By.name(locatorType.substring(5));
        } else if (locatorType.startsWith("CSS=") || locatorType.startsWith("Css=") || locatorType.startsWith("css=")) {
            by = By.cssSelector(locatorType.substring(4));
        } else if (locatorType.startsWith("XPATH=") || locatorType.startsWith("Xpath=") || locatorType.startsWith("xpath=") || locatorType.startsWith("XPath=")) {
            by = By.xpath(locatorType.substring(6));
        } else {
            throw new RuntimeException("Locator type is not supported!");
        }
        return by;
    }

    /**
     * Returns a formatted string using the specified format string and arguments. The locale always used is the one returned by Locale.getDefault().
     *
     * @param locatorType   type of element start with id or class or name or css or xpath contains arguments.
     * @param dynamicValues arguments of @param locatorType
     * @return A formatted string
     */
    private String getDynamicXpath(String locatorType, String... dynamicValues) {
        if (locatorType.startsWith("XPATH=") || locatorType.startsWith("Xpath=") || locatorType.startsWith("xpath=") || locatorType.startsWith("XPath=")) {
            locatorType = String.format(locatorType, (Object[]) dynamicValues);
        }
        return locatorType;
    }

    /**
     * Find the first WebElement using the given method
     *
     * @param driver      driver of browser
     * @param locatorType type of element start with id or class or name or css or xpath
     * @return The first matching element on the current page
     */
    public WebElement getWebElement(WebDriver driver, String locatorType) {
        return driver.findElement(getByLocator(locatorType));
    }

    /**
     * Find all elements within the current page using the given mechanism.
     *
     * @param driver      driver of browser
     * @param locatorType type of element start with id or class or name or css or xpath
     * @return A list of all WebElements, or an empty list if nothing matches
     */
    public List<WebElement> getListWebElement(WebDriver driver, String locatorType) {
        return driver.findElements(getByLocator(locatorType));
    }

    /**
     * Click to Element
     *
     * @param driver      driver of browser
     * @param locatorType type of element start with id or class or name or css or xpath
     */
    public void clickToElement(WebDriver driver, String locatorType) {
        highlightElement(driver, locatorType);
        getWebElement(driver, locatorType).click();
    }

    /**
     * Click to Element with dynamic values
     *
     * @param driver        driver of browser
     * @param locatorType   type of element start with id or class or name or css or xpath contains arguments.
     * @param dynamicValues arguments of @param locatorType
     */
    public void clickToElement(WebDriver driver, String locatorType, String... dynamicValues) {
        highlightElement(driver, locatorType, dynamicValues);
        getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).click();
    }

    /**
     * Double click to Element
     *
     * @param driver      driver of browser
     * @param locatorType type of element start with id or class or name or css or xpath
     */
    public void doubleClickToElement(WebDriver driver, String locatorType) {
        highlightElement(driver, locatorType);
        Actions actions = new Actions(driver);
        WebElement element = getWebElement(driver, locatorType);
        actions.doubleClick(element).perform();
    }

    /**
     * Double click to Element with dynamic values
     *
     * @param driver        driver of browser
     * @param locatorType   type of element start with id or class or name or css or xpath contains arguments.
     * @param dynamicValues arguments of @param locatorType
     */
    public void doubleClickToElement(WebDriver driver, String locatorType, String... dynamicValues) {
        highlightElement(driver, locatorType, dynamicValues);
        Actions actions = new Actions(driver);
        WebElement element = getWebElement(driver, getDynamicXpath(locatorType, dynamicValues));
        actions.doubleClick(element).perform();
    }

    /**
     * Send key to Element
     *
     * @param driver      driver of browser
     * @param locatorType type of element start with id or class or name or css or xpath
     * @param textValue   value to input
     */
    public void sendkeyToElement(WebDriver driver, String locatorType, String textValue) {
        highlightElement(driver, locatorType);
        WebElement element = getWebElement(driver, locatorType);
        element.clear();
        element.sendKeys(textValue);
    }

    /**
     * Send key to Element with dynamic values
     *
     * @param driver        driver of browser
     * @param locatorType   type of element start with id or class or name or css or xpath contains arguments.
     * @param textValue     value to input
     * @param dynamicValues arguments of @param locatorType.
     */
    public void sendkeyToElement(WebDriver driver, String locatorType, String textValue, String... dynamicValues) {
        highlightElement(driver, locatorType, dynamicValues);
        WebElement element = getWebElement(driver, getDynamicXpath(locatorType, dynamicValues));
        element.clear();
        element.sendKeys(textValue);
    }

    /**
     * Select options that display text matching the argument
     *
     * @param driver      driver of browser
     * @param locatorType type of element start with id or class or name or css or xpath
     * @param textItem    The visible text to match against
     */
    public void selectItemInDefaultDropdown(WebDriver driver, String locatorType, String textItem) {
        Select select = new Select(getWebElement(driver, locatorType));
        select.selectByVisibleText(textItem);
    }

    /**
     * Select options that display text matching the argument
     *
     * @param driver        driver of browser
     * @param locatorType   type of element start with id or class or name or css or xpath contains arguments.
     * @param textItem      The visible text to match against
     * @param dynamicValues arguments of @param locatorType.
     */
    public void selectItemInDefaultDropdown(WebDriver driver, String locatorType, String textItem, String... dynamicValues) {
        Select select = new Select(getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
        select.selectByVisibleText(textItem);
    }

    /**
     * @param driver
     * @param locatorType
     * @return
     */
    public String getSelectItemDefaultDropdown(WebDriver driver, String locatorType) {
        Select select = new Select(getWebElement(driver, locatorType));
        return select.getFirstSelectedOption().getText();
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     * @return
     */
    public String getSelectItemDefaultDropdown(WebDriver driver, String locatorType, String... dynamicValues) {
        Select select = new Select(getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
        return select.getFirstSelectedOption().getText();
    }

    /**
     * @param driver
     * @param locatorType
     * @return
     */
    public boolean isDropdownMultiple(WebDriver driver, String locatorType) {
        Select select = new Select(getWebElement(driver, locatorType));
        return select.isMultiple();
    }

    /**
     * @param driver
     * @param parentXpath
     * @param childXpath
     * @param expectedText
     */
    public void selectItemInDropdown(WebDriver driver, String parentXpath, String childXpath, String expectedText) {
        highlightElement(driver, parentXpath);
        getWebElement(driver, parentXpath).click();
        sleepInSecond(1);
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        List<WebElement> allItems = explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByLocator(childXpath)));
        for (WebElement item : allItems) {
            if (item.getText().trim().equals(expectedText)) {
                if (item.isDisplayed()) {
                    item.click();
                } else {
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", item);
                    item.click();
                }
                break;
            }
        }
    }

    /**
     * @param driver
     * @param parentXpath
     * @param childXpath
     * @param expectedText
     * @param dynamicValues
     */
    public void selectItemInDropdown(WebDriver driver, String parentXpath, String childXpath, String expectedText, String... dynamicValues) {
        getWebElement(driver, parentXpath).click();
        sleepInSecond(1);
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        List<WebElement> allItems = explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByLocator(getDynamicXpath(childXpath, dynamicValues))));
        for (WebElement item : allItems) {
            if (item.getText().trim().equals(expectedText)) {
                if (item.isDisplayed()) {
                    item.click();
                } else {
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", item);
                    item.click();
                }
                break;
            }
        }
    }

    /**
     * @param driver
     * @param attributeName
     * @return
     */
    public String getElementAttribute(WebDriver driver, String attributeName) {
        return getWebElement(driver, attributeName).getAttribute(attributeName);
    }

    /**
     * @param driver
     * @param attributeName
     * @param dynamicValues
     * @return
     */
    public String getElementAttribute(WebDriver driver, String attributeName, String... dynamicValues) {
        return getWebElement(driver, getDynamicXpath(attributeName, dynamicValues)).getAttribute(attributeName);
    }

    /**
     * @param driver
     * @param locatorType
     * @return
     */
    public String getElementText(WebDriver driver, String locatorType) {
        return getWebElement(driver, locatorType).getText();
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     * @return
     */
    public String getElementText(WebDriver driver, String locatorType, String... dynamicValues) {
        return getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).getText();
    }

    /**
     * @param driver
     * @param locatorType
     * @param propertyName
     * @return
     */
    public String getElementCssValue(WebDriver driver, String locatorType, String propertyName) {
        return getWebElement(driver, locatorType).getCssValue(propertyName);
    }

    /**
     * @param driver
     * @param locatorType
     * @param propertyName
     * @param dynamicValues
     * @return
     */
    public String getElementCssValue(WebDriver driver, String locatorType, String propertyName, String... dynamicValues) {
        return getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).getCssValue(propertyName);
    }

    /**
     * @param rgbaValue
     * @return
     */
    public String getHexaColorFromRGBA(String rgbaValue) {
        return Color.fromString(rgbaValue).asHex();
    }

    /**
     * @param driver
     * @param locatorType
     * @return
     */
    public int getElementSize(WebDriver driver, String locatorType) {
        return getListWebElement(driver, locatorType).size();
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     * @return
     */
    public int getElementSize(WebDriver driver, String locatorType, String... dynamicValues) {
        return getListWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).size();
    }

    /**
     * @param driver
     * @param locatorType
     */
    public void checkToDefaultCheckboxOrRadio(WebDriver driver, String locatorType) {
        highlightElement(driver, locatorType);
        WebElement element = getWebElement(driver, locatorType);
        if (!element.isSelected()) {
            element.click();
        }
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     */
    public void checkToDefaultCheckboxOrRadio(WebDriver driver, String locatorType, String... dynamicValues) {
        highlightElement(driver, locatorType, dynamicValues);
        WebElement element = getWebElement(driver, getDynamicXpath(locatorType, dynamicValues));
        if (!element.isSelected()) {
            element.click();
        }
    }

    /**
     * @param driver
     * @param locatorType
     */
    public void uncheckToDefaultCheckbox(WebDriver driver, String locatorType) {
        highlightElement(driver, locatorType);
        WebElement element = getWebElement(driver, locatorType);
        if (element.isSelected()) {
            element.click();
        }
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     */
    public void uncheckToDefaultCheckbox(WebDriver driver, String locatorType, String... dynamicValues) {
        highlightElement(driver, locatorType, dynamicValues);
        WebElement element = getWebElement(driver, getDynamicXpath(locatorType, dynamicValues));
        if (element.isSelected()) {
            element.click();
        }
    }

    /**
     * @param driver
     * @param locatorType
     * @return
     */
    public boolean isElementDisplayed(WebDriver driver, String locatorType) {
        return getWebElement(driver, locatorType).isDisplayed();
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     * @return
     */
    public boolean isElementDisplayed(WebDriver driver, String locatorType, String... dynamicValues) {
        return getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).isDisplayed();
    }

    /**
     * @param driver
     * @param locatorType
     * @return
     */
    public boolean isElementUndisplayed(WebDriver driver, String locatorType) {
        overrideImplicitTimeout(driver, shortTimeout);
        List<WebElement> elements = getListWebElement(driver, locatorType);
        overrideImplicitTimeout(driver, longTimeout);
        if (elements.size() == 0) {
            // Không có trong DOM
            return true;
        } else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
            // Có trong DOM nhưng không hiển thị
            return true;
        } else {
            // Có trong DOM và hiển thị
            return false;
        }
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     * @return
     */
    public boolean isElementUndisplayed(WebDriver driver, String locatorType, String... dynamicValues) {
        overrideImplicitTimeout(driver, shortTimeout);
        List<WebElement> elements = getListWebElement(driver, getDynamicXpath(locatorType, dynamicValues));
        overrideImplicitTimeout(driver, longTimeout);
        if (elements.size() == 0) {
            // Không có trong DOM
            return true;
        } else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
            // Có trong DOM nhưng không hiển thị
            return true;
        } else {
            // Có trong DOM và hiển thị
            return false;
        }
    }

    /**
     * @param driver
     * @param timeOut
     */
    public void overrideImplicitTimeout(WebDriver driver, long timeOut) {
        driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
    }

    /**
     * @param driver
     * @param locatorType
     * @return
     */
    public boolean isElementEnabled(WebDriver driver, String locatorType) {
        highlightElement(driver, locatorType);
        return getWebElement(driver, locatorType).isEnabled();
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     * @return
     */
    public boolean isElementEnabled(WebDriver driver, String locatorType, String... dynamicValues) {
        highlightElement(driver, locatorType, dynamicValues);
        return getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).isEnabled();
    }

    /**
     * @param driver
     * @param locatorType
     * @return
     */
    public boolean isElementSelected(WebDriver driver, String locatorType) {
        return getWebElement(driver, locatorType).isSelected();
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     * @return
     */
    public boolean isElementSelected(WebDriver driver, String locatorType, String... dynamicValues) {
        return getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).isSelected();
    }

    /**
     * @param driver
     * @param locatorType
     */
    public void switchToFrameIframe(WebDriver driver, String locatorType) {
        waitForElementVisible(driver, locatorType);
        driver.switchTo().frame(getWebElement(driver, locatorType));
    }

    /**
     * @param driver
     */
    public void switchToDefaultContent(WebDriver driver) {
        driver.switchTo().defaultContent();
    }

    /**
     * @param driver
     * @param locatorType
     */
    public void hoverMouseToElement(WebDriver driver, String locatorType) {
        Actions action = new Actions(driver);
        action.moveToElement(getWebElement(driver, locatorType)).perform();
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     */
    public void hoverMouseToElement(WebDriver driver, String locatorType, String... dynamicValues) {
        Actions action = new Actions(driver);
        action.moveToElement(getWebElement(driver, getDynamicXpath(locatorType, dynamicValues))).perform();
    }

    /**
     * @param driver
     * @param locatorType
     * @param key
     */
    public void pressKeyToElement(WebDriver driver, String locatorType, Keys key) {
        Actions action = new Actions(driver);
        action.sendKeys(getWebElement(driver, locatorType), key).perform();
    }

    /**
     * @param driver
     * @param locatorType
     * @param key
     * @param dynamicValues
     */
    public void pressKeyToElement(WebDriver driver, String locatorType, Keys key, String... dynamicValues) {
        Actions action = new Actions(driver);
        action.sendKeys(getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)), key).perform();
    }

    /**
     * @param driver
     */
    public void scrollToBottomPage(WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    /**
     * @param driver
     * @param locatorType
     */
    public void highlightElement(WebDriver driver, String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement element = getWebElement(driver, locatorType);
        String originalStyle = element.getAttribute("style");
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     */
    public void highlightElement(WebDriver driver, String locatorType, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement element = getWebElement(driver, getDynamicXpath(locatorType, dynamicValues));
        String originalStyle = element.getAttribute("style");
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    /**
     * @param driver
     * @param locatorType
     */
    public void clickToElementByJS(WebDriver driver, String locatorType) {
        highlightElement(driver, locatorType);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].click();", getWebElement(driver, locatorType));
    }

    /**
     * @param driver
     * @param locatorType
     */
    public void scrollToElement(WebDriver driver, String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, locatorType));
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     */
    public void scrollToElement(WebDriver driver, String locatorType, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
    }

    /**
     * @param driver
     * @param locatorType
     * @param attributeRemove
     */
    public void removeAttributeInDOM(WebDriver driver, String locatorType, String attributeRemove) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", getWebElement(driver, locatorType));
    }

    /**
     * @param driver
     * @return
     */
    public boolean areJQueryAndJSLoadedSuccess(WebDriver driver) {
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) jsExecutor.executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            }
        };
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return jsExecutor.executeScript("return document.readyState").toString().equals("complete");
            }
        };
        return explicitWait.until(jQueryLoad) && explicitWait.until(jsLoad);
    }

    /**
     * @param driver
     * @param locatorType
     * @return
     */
    public String getElementValidationMessage(WebDriver driver, String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", getWebElement(driver, locatorType));
    }

    /**
     * @param driver
     * @param locatorType
     * @return
     */
    public boolean isImageLoaded(WebDriver driver, String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        boolean status = (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", getWebElement(driver, locatorType));
        if (status) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     * @return
     */
    public boolean isImageLoaded(WebDriver driver, String locatorType, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        boolean status = (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
        return status;
    }

    /**
     * @param driver
     * @param locatorType
     */
    public void waitForElementVisible(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locatorType)));
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     */
    public void waitForElementVisible(WebDriver driver, String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
    }

    /**
     * @param driver
     * @param locatorType
     */
    public void waitForAllElementVisible(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByLocator(locatorType)));
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     */
    public void waitForAllElementVisible(WebDriver driver, String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
    }

    /**
     * @param driver
     * @param locatorType
     */
    public void waitForElementInvisible(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locatorType)));
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     */
    public void waitForElementInvisible(WebDriver driver, String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
    }

    /**
     * Wait for element undisplayed in DOM or not in DOM and override implicit timeout
     *
     * @param driver
     * @param locatorType
     */
    public void waitForElementUndisplayed(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, shortTimeout);
        overrideImplicitTimeout(driver, shortTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locatorType)));
        overrideImplicitTimeout(driver, longTimeout);
    }

    /**
     * Wait for dynamic element undisplayed in DOM or not in DOM and override implicit timeout
     *
     * @param driver
     * @param locatorType
     * @param dynamicValues
     */
    public void waitForElementUndisplayed(WebDriver driver, String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, shortTimeout);
        overrideImplicitTimeout(driver, shortTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
        overrideImplicitTimeout(driver, longTimeout);
    }

    /**
     * @param driver
     * @param locatorType
     */
    public void waitForAllElementInvisible(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver, locatorType)));
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     */
    public void waitForAllElementInvisible(WebDriver driver, String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver, getDynamicXpath(locatorType, dynamicValues))));
    }

    /**
     * @param driver
     * @param locatorType
     */
    public void waitForElementClickable(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByLocator(locatorType)));
    }

    /**
     * @param driver
     * @param locatorType
     * @param dynamicValues
     */
    public void waitForElementClickable(WebDriver driver, String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
    }

    /**
     * Screenshot attachments for Allure
     *
     * @param driver driver of browser
     * @param name   name of picture
     */
    public static void takeScreenshot(WebDriver driver, String name) {
        Allure.addAttachment(name, new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    /* Upload for all site */
    /**
     * @param driver
     * @param fileNames
     */
    public void uploadMultipleFiles(WebDriver driver, String... fileNames) {
        // Đường dẫn của thư mục upload file
        String filePath = GlobalConstants.getGlobalConstants().getUploadFile();
        // Đường dẫn của tất cả các file
        String fullFileName = "";
        for (String file : fileNames) {
            fullFileName = fullFileName + filePath + file + "\n";
        }
        fullFileName = fullFileName.trim();
        getWebElement(driver, CommonUI.UPLOAD_FILE).sendKeys(fullFileName);
    }

    /* Check file downloaded */
    /**
     * @param downloadPath
     * @param fileName
     * @return
     */
    public boolean isFileDownloaded(String downloadPath, String fileName) {
        boolean flag = false;
        File dir = new File(downloadPath);
        File[] dir_contents = dir.listFiles();
        for (int i = 0; i < dir_contents.length; i++) {
            if (dir_contents[i].getName().contains(fileName)) {
                dir_contents[i].delete();
                return flag = true;
            }
        }
        return flag;
    }

    /* Check file downloaded */
    /**
     * @param downloadPath
     * @param fileName
     * @return
     */
    public boolean isExpectedFileName(String downloadPath, String fileName) {
        boolean flag = false;
        File getLatestFile = getLatestFilefromDir(downloadPath);
        if (getLatestFile.getName().contains(fileName)) {
            return flag = true;
        }
        return flag;
    }

    /* Get the latest file from a specific directory */
    /**
     * @param dirPath
     * @return
     */
    private File getLatestFilefromDir(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile;
    }

    /**
     * @param timeoutInSecond
     */
    public void sleepInSecond(long timeoutInSecond) {
        try {
            Thread.sleep(timeoutInSecond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private long longTimeout = GlobalConstants.getGlobalConstants().getLongTimeout();
    private long shortTimeout = GlobalConstants.getGlobalConstants().getShortTimeout();
}
