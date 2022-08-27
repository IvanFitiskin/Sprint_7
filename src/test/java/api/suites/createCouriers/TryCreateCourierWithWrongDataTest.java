package api.suites.createCouriers;

import api.steps.CourierSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TryCreateCourierWithWrongDataTest {

    private String jsonData;

    public TryCreateCourierWithWrongDataTest(String jsonData) {
        this.jsonData = jsonData;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                { "{ \"login\": \"ninja\" }" },
                { "{ \"password\": \"1234\" }" },
                { "{ \"password\": \"\" }" },
                { "{ \"\": \"1234\" }" }
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создание курьера без обязательных полей") // имя теста
    @Description("Попытка создать курьера без полей login, password и без двух полей одновременно") // описание теста
    public void positiveCreateCourierTest() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(jsonData)
                .post("/api/v1/courier");

        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
}
