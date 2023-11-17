import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;

public class YaTest {

    @BeforeAll
    static void beforeAll() {
        // Configuration.browser = "firefox";
        // Configuration.browserSize = "1800x1000";
        Configuration.pageLoadTimeout = 60000;
    }

    @DisplayName("Поиск в www.google.com слово Selenide")
    @Tag("blocker")
    @Tag("WEB")
    @Test
    void SearchTest1() {
        open("https://www.google.com");  //Открыт сайт www.google.com
        $("[name=q]").setValue("Selenide").pressEnter();
        $$("[id=rso]").find(Condition.text("Selenide")).shouldBe(Condition.visible);
    }

    @ValueSource(strings = {"Selenide", "Allure"})
    @Tag("blocker")
    @Tag("WEB")
    @ParameterizedTest(name = "Поиск в www.google.com слова {0}")
    void SearchTest2(String searchQuery) {
        open("https://www.google.com");  //Открыт сайт www.google.com
        $("[name=q]").setValue(searchQuery).pressEnter();
        $$("[id=rso]").find(Condition.text(searchQuery)).shouldBe(Condition.visible);
    }

    @CsvSource(value = {
            "Selenide| лаконичные и стабильные",
            "Allure| Beauty Tips"
    }, delimiter = '|')
    @Tag("blocker")
    @Tag("WEB")
    @ParameterizedTest(name = "Поиск в www.google.com слова {0} и проверка текста {1}")
    void SearchTest3(String searchQuery, String expectedResult) {
        open("https://www.google.com");  //Открыт сайт www.google.com
        zoom(0.8);
        $("[name=q]").setValue(searchQuery).pressEnter();
        $$("[id=rso]").find(Condition.text(expectedResult)).shouldBe(Condition.visible);
    }

    static Stream<Arguments> SearchTest4() {
        return Stream.of(
                Arguments.of("Selenide", List.of("лаконичные и стабильные")),
                Arguments.of("Allure", List.of("Beauty Tips"))
        );
    }
    @MethodSource
    @Tag("blocker")
    @Tag("WEB")
    @ParameterizedTest(name = "Поиск в www.google.com слова {0} и проверка текста {1}")
    void SearchTest4(String searchQuery, List<String> expectedResult) {
        open("https://www.google.com");  //Открыт сайт www.google.com
        zoom(0.8);
        $("[name=q]").setValue(searchQuery).pressEnter();
        $$("[id=rso]").find(Condition.text(expectedResult.get(0))).shouldBe(Condition.visible);
    }

    // @EnumSource(SearchQuery.class)

}
