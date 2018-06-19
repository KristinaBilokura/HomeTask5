package HomeTask4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Hometask5 {
    protected EventFiringWebDriver driver;
    protected boolean isMobileTesting;

    @BeforeClass
    @Parameters({"selenium.browser", "selenium.grid"})
    public void setUp(@Optional("chrome") String browser, @Optional("") String gridUrl) {
        driver = new EventFiringWebDriver(getDriver(browser));
        driver.register(new EventHandler());
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        if (!isMobileTesting(browser))
            driver.manage().window().maximize();

        isMobileTesting = isMobileTesting(browser);
    }

    private WebDriver getDriver(String browser) {
        switch (browser) {
            case "firefox":
                System.setProperty(
                        "webdriver.gecko.driver",
                        "src/main/resources/geckodriver.exe");
                return new FirefoxDriver();
            case "ie":
            case "internet explorer":
                System.setProperty(
                        "webdriver.ie.driver",
                        "src/main/resources/IEDriverServer.exe");
                return new InternetExplorerDriver();
            case "chrome":
            default:
                System.setProperty(
                        "webdriver.chrome.driver",
                        "src/main/resources/chromedriver.exe");
                return new ChromeDriver();
        }
    }

    @Test
    public void test() {
        driver.get("http://prestashop-automation.qatestlab.com.ua/uk/");
        driver.findElement(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']")).click();
        driver.findElement(By.xpath("//h1[@class='h3 product-title']/a")).click();
        driver.findElement(By.xpath("//a[@class='nav-link active']")).click();

        driver.findElement(By.xpath("//button[@class='btn btn-primary add-to-cart']")).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.xpath("//div[@class='cart-content']/a")).click();

        driver.findElement(By.xpath("//div[@class='text-xs-center']//a")).click();
        driver.findElement(By.name("firstname")).sendKeys(getRandomString(7));
        driver.findElement(By.name("lastname")).sendKeys(getRandomString(7));
        driver.findElement(By.name("email")).sendKeys("webinar.test2@gmail.com");
        driver.findElement(By.name("birthday")).sendKeys(getRandomDate());
        driver.findElement(By.name("continue")).submit();
        driver.findElement(By.name("address1")).sendKeys(String.valueOf(getRandomInteger()));
        driver.findElement(By.name("postcode")).sendKeys("79066");
        driver.findElement(By.name("city")).sendKeys(getRandomString(7));
        driver.findElement(By.id("checkout-payment-step")).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElements(By.xpath("//h1[@class='step-title h3']")).get(3).click();
        String price = driver.findElements(By.xpath("//div[@id='cart-subtotal-products']/span")).get(1).getText();
        driver.findElement(By.id("payment-option-2")).click();
        driver.findElement(By.id("conditions_to_approve[terms-and-conditions]")).click();
        driver.findElement(By.xpath("//button[@class='btn btn-primary center-block']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//i[@class='material-icons done']")).isDisplayed());
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='col-xs-5 text-sm-right text-xs-left']")).getText(),
                price);
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='col-xs-2']")).getText(),
                String.valueOf(1));
        driver.findElement(By.xpath("//h1[@class='h3 product-title']/a")).click();
        driver.findElement(By.xpath("//a[@class='nav-link active']")).click();


    }

    public String getRandomString(int length) {
        String randomChars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder rand = new StringBuilder();
        Random rnd = new Random();
        while (rand.length() < length) {
            int index = (int) (rnd.nextFloat() * randomChars.length());
            rand.append(randomChars.charAt(index));
        }
        String randomStr = rand.toString();
        return randomStr;
    }

    public static String getRandomDate() {
        Random rand = new Random();
        int dd = 1 + rand.nextInt((28 - 1) + 1);
        int MM = 1 + rand.nextInt((12 - 1) + 1);
        int yyyy = 1970 + rand.nextInt((2018 - 1970) + 1);
        Object[] date = {yyyy, MM, dd};
        return String.format("%d-%d-%d", date);
    }

    public Integer getRandomInteger() {
        Random rand = new Random();
        int randomNum = rand.nextInt((100 - 1) + 1) + 1;
        return randomNum;
    }


    @AfterClass
    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }

    private boolean isMobileTesting(String browser) {
        switch (browser) {
            case "android":
                return true;
            case "firefox":
            case "ie":
            case "internet explorer":
            case "chrome":
            default:
                return false;
        }
    }
}
