package ru.netology.delivery;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class ReplanDeliveryTest {

    @BeforeAll
    static void setUpAllureListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }

    @BeforeEach
    void setUp() {
        Configuration.baseUrl = "http://localhost:9999";
        open("/");
    }

    private void setFieldValue(SelenideElement field, String value) {
        field.click();
        field.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        field.sendKeys(value);
        field.sendKeys(Keys.ESCAPE);
    }

    private void fillForm(DataGenerator.UserInfo user, String dateDigits) {
        setFieldValue($("[data-test-id='city'] input"), user.getCity());
        setFieldValue($("[data-test-id='date'] input"), dateDigits);
        setFieldValue($("[data-test-id='name'] input"), user.getName());
        setFieldValue($("[data-test-id='phone'] input"), user.getPhone());

        SelenideElement agreementCheckbox = $("[data-test-id='agreement'] input");
        if (!agreementCheckbox.isSelected()) {
            $("[data-test-id='agreement']").click();
        }

        $x("//button[contains(., 'Запланировать')]").click();
    }

    @Test
    void shouldReplanDeliveryOnSecondSubmit() {
        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser("ru");

        int firstShift = 4;
        String firstDateFormatted = DataGenerator.generateDate(firstShift, "dd.MM.yyyy");
        String firstDateDigits = DataGenerator.generateDate(firstShift, "ddMMyyyy");

        fillForm(user, firstDateDigits);

        $("[data-test-id='success-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно запланирована на " + firstDateFormatted));

        int secondShift = 7;
        String secondDateFormatted = DataGenerator.generateDate(secondShift, "dd.MM.yyyy");
        String secondDateDigits = DataGenerator.generateDate(secondShift, "ddMMyyyy");

        fillForm(user, secondDateDigits);

        $("[data-test-id='replan-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));

        $("[data-test-id='replan-notification']")
                .$(byText("Перепланировать"))
                .click();

        $("[data-test-id='success-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно запланирована на " + secondDateFormatted));
    }
}
