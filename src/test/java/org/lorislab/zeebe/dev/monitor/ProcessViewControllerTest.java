package org.lorislab.zeebe.dev.monitor;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@QuarkusTest
public class ProcessViewControllerTest {

    @Test
    public void definitionsTest() {
        open("/process");
        $(By.id("def-count")).shouldHave(text("0 process definitions"));
    }
}
