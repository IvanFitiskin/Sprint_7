package api.suites.couriers;

import api.steps.CourierSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Credentials;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TryCreateCourierWithoutDataTest {

    private CourierSteps courierSteps;

    private String jsonData;

    public TryCreateCourierWithoutDataTest(String jsonData) {
        this.jsonData = jsonData;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                { "{ \"login\": \"ninja\" }" },
                { "{ \"password\": \"1234\" }" }
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";
        courierSteps = new CourierSteps();
    }

    @Test
    @DisplayName("Создание курьера без обязательных полей") // имя теста
    @Description("Попытка создать курьера без полей login, password и без двух полей одновременно") // описание теста
    public void positiveCreateCourierTest() {
        Response response = courierSteps.createCourierRequest(jsonData);
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
}
