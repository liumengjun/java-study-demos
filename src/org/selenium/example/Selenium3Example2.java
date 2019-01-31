package org.selenium.example;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Selenium3Example2 {
    public static void main(String[] args) throws IOException {
        WebDriver driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
        driver.get("file://" + System.getProperty("user.dir") + "/var/test.html");

        System.out.println(StringUtils.center("pageSource", 80, '-'));
        String pageSource = driver.getPageSource();
        System.out.println(pageSource);

        System.out.println(StringUtils.center("outerHtml", 80, '-'));
        Object outerHtml = ((ChromeDriver) driver).executeScript("return document.documentElement.outerHTML");
        System.out.println(outerHtml);

        System.out.println(StringUtils.center("end", 80, '-'));

        String filename = "temp/screenshot-test.png";
        System.out.println(StringUtils.center(" screen shot -> " + filename, 80, '*'));
        FileUtils.writeByteArrayToFile(new File(filename), ((ChromeDriver) driver).getScreenshotAs(OutputType.BYTES));
    }
}
