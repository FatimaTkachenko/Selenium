package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardOrderTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldTestSuccessOrderIfCorrectFilling() {
        driver.get("http://localhost:9999");
        wait.until(d -> driver.findElements(By.className("input__control")).size() > 1);

        var elements = driver.findElements(By.className("input__control"));
        elements.get(0).sendKeys("Иван Петров");
        elements.get(1).sendKeys("+79123456789");

        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();

        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test-id=order-success]"))).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                text.trim());
    }

    @Test
    void shouldTestWarnIfIncorrectTel() {
        driver.get("http://localhost:9999");
        wait.until(d -> driver.findElements(By.className("input__control")).size() > 1);

        var elements = driver.findElements(By.className("input__control"));
        elements.get(0).sendKeys("Иван Петров");
        elements.get(1).sendKeys("+791234");

        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    // Остальные тесты аналогично с проверкой элементов
}
