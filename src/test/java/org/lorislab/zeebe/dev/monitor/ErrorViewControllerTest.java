package org.lorislab.zeebe.dev.monitor;

import io.quarkiverse.quinoa.testing.QuinoaTestProfiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@QuarkusTest
@TestProfile(QuinoaTestProfiles.Enable.class)
public class ErrorViewControllerTest {

    @Test
    public void indexPageTest() {
        open("/");
        $("[data-testid=menuErrors]").click();
        $("[data-testid=errorsTable]").should(exist);
    }
}