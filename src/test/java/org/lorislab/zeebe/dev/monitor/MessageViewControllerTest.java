package org.lorislab.zeebe.dev.monitor;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@QuarkusTest
public class MessageViewControllerTest {

    @Test
    public void indexPageTest() {
        open("/message");
        $(By.id("msg-count")).shouldHave(text("0 messages"));
    }
}