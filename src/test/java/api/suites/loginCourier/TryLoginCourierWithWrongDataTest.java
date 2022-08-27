package api.suites.loginCourier;

import api.steps.CourierSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import model.Credentials;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TryLoginCourierWithWrongDataTest {

    private CourierSteps courierSteps;

    private String login;
    private String password;

    private Credentials credentials;

    public TryLoginCourierWithWrongDataTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                { "ninj", "1234" },
                { "ninja42", "1111" }
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";

        credentials = new Credentials(login, password, "saske");
        courierSteps = new CourierSteps();

        // Если аккаунт курьера существует - удаляем его
        Response responseLogin = courierSteps.loginCourierRequest(credentials);

        if (responseLogin.statusCode() == 200) {
            Courier courier = responseLogin.body().as(Courier.class);
            String id = courier.getId();
            courierSteps.deleteCourierRequest(id).then().statusCode(200);
        }
    }

    @Test
    @DisplayName("Попытка авторизации курьера с несуществующей парой логин-пароль")
    @Description("Попытка авторизации курьера с несуществующей парой логин-пароль")
    public void tryLoginCourierWithWrongCredentialsTest() {
        Response response = courierSteps.loginCourierRequest(credentials);
        response.then().assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

}
