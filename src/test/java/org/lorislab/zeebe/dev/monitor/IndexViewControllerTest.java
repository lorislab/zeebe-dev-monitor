package org.lorislab.zeebe.dev.monitor;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@QuarkusTest
public class IndexViewControllerTest {

    @Test
    public void indexPageTest() {
        open("/");
        $(By.id("index-title")).shouldHave(text("Zeebe developer monitor"));
    }
}
