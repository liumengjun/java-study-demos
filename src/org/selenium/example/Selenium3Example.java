package org.selenium.example;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Selenium3Example {

    static final String ATTR_OUTER_HTML = "outerHTML";
    static final String ATTR_VALUE = "value";

    public static void main(String[] args) throws IOException {
        // Create a new instance of the ChromeDriver
        // 并设置无图形界面(without GUI)
        WebDriver driver = new ChromeDriver(new ChromeOptions().setHeadless(true));

        // And now use this to visit Google
        driver.get("http://www.google.com");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());

        // Find the text input element by its name
        WebElement element = driver.findElement(By.name("q"));

        // 获取q元素具体的HTML源文件
        System.out.println(element.getAttribute(ATTR_OUTER_HTML));

        // Enter something to search for
        element.sendKeys("Cheese!");

        System.out.println(element.getAttribute(ATTR_VALUE));

        // 在此查看q元素具体的HTML源文件
        System.out.println(element.getAttribute(ATTR_OUTER_HTML));
        // 查看元素截图
        FileUtils.writeByteArrayToFile(new File("temp/screenshot-q.png"), element.getScreenshotAs(OutputType.BYTES));

        // 执行js得到整个HTML。
        // see driver.getPageSource()
        Object html = ((ChromeDriver) driver).executeScript("return document.documentElement.outerHTML");
        System.out.println(html.getClass());
        // System.out.println(html);

        // Now submit the form. WebDriver will find the form for us from the element
        element.submit();

        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());

        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("cheese!");
            }
        });

        // Should see: "cheese! - Google Search"
        System.out.println("Page title is: " + driver.getTitle());

        // 截图（Screenshot）
        byte[] imageBytes = ((ChromeDriver) driver).getScreenshotAs(OutputType.BYTES);
        FileUtils.writeByteArrayToFile(new File("temp/screenshot.png"), imageBytes);

        //Close the browser
        driver.quit();
    }
}
