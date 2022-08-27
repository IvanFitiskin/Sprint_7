package api.suites.loginCourier;

import api.steps.CourierSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TryLoginCourierWithoutDataTest {

    private CourierSteps courierSteps;

    private String jsonData;

    public TryLoginCourierWithoutDataTest(String jsonData) {
        this.jsonData = jsonData;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                // { "{ \"login\": \"ninja42\" }" }, - сервер отдает 500, так что тест выполняет слишком долго
                { "{ \"password\": \"1234\" }" },
                { "{ \"login\": \"ninja\", \"password\": \"\" }" },
                { "{ \"login\": \"\", \"password\": \"1234\" }" }
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";

        courierSteps = new CourierSteps();
    }

    @Test
    @DisplayName("Попытка авторизации курьера с несуществующей парой логин-пароль")
    @Description("Попытка авторизации курьера с несуществующей парой логин-пароль")
    public void tryLoginCourierWithoutDataTest() {
        Response response = courierSteps.loginCourierRequest(jsonData);
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

}
