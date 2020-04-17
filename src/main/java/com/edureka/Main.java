package com.edureka;

import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException{

        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        firefoxBinary.addCommandLineOptions("--no-sandbox");
        System.setProperty("webdriver.gecko.driver", "/usr/bin/geckodriver");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        FirefoxDriver driver = new FirefoxDriver(firefoxOptions);
        driver.get("http://127.0.0.1:1998/index.php");
        WebElement button = driver.findElement(By.id("Contact"));
        assert(button.isDisplayed());
        System.out.println("Contact element is there: " + button.isDisplayed());
	System.out.println("Executed");
        button.click();
        assert(button.getText().equals("Contact"));
        driver.close();
    }
}