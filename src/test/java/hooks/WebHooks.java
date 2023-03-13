package hooks;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class WebHooks {
    private static boolean started = false;
    @BeforeClass
    public static void befores() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide().
                        screenshots(true).
                        savePageSource(true)
        );
    }
    @BeforeEach
    public void before(){
        if (!started) {
            started = true;
            RestAssured.filters(new AllureRestAssured());
        }
    }

    @AfterEach
    public void after(){
        WebDriverRunner.closeWebDriver();
    }
}

