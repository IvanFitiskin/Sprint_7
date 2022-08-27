package api.suites.createCouriers;

import api.steps.CourierSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Credentials;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("Create courier")
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
    @DisplayName("Создание курьера без обязательных полей")
    @Description("Попытка создать курьера без полей login, password и без двух полей одновременно")
    public void positiveCreateCourierTest() {
        Response response = createCourierRequest(jsonData);

        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Step("Запрос на создание нового курьера")
    public Response createCourierRequest(String jsonData) {
        return given()
                .header("Content-type", "application/json")
                .body(jsonData)
                .post("/api/v1/courier");
    }

}
