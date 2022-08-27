package api.suites.loginCourier;

import api.steps.CourierSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import model.Credentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

@Epic("Login courier")
public class LoginCourierTest {

    private Credentials credentials;
    private String id;

    private CourierSteps courierSteps;

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";

        credentials = new Credentials("ninja42", "1234", "saske");

        courierSteps = new CourierSteps();

        // создаем нового курьера
        courierSteps.createCourierRequest(credentials);
    }

    @After
    public void tearDown() {
        // удаляем созданный аккаунт после теста
        if (id != null) {
            courierSteps.deleteCourierRequest(id);
        }
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Обычный позитивный кейс авторизации курьера")
    public void positiveLoginCourierTest() {
        Response response = courierSteps.loginCourierRequest(credentials);
        response.then().assertThat()
                .body("id", notNullValue())
                .and()
                .statusCode(200);

        Courier courier = response.body().as(Courier.class);

        // сохраняем id для блока After
        id = courier.getId();
    }
}
