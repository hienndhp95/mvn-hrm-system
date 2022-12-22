package factoryBrowsers;

public class BrowserNotSupportException extends IllegalStateException{

    private static final long serialVersionUID = 1L;

    public BrowserNotSupportException(String browser) {
        super(String.format("Browser not supported: %s", browser));
    }
}
