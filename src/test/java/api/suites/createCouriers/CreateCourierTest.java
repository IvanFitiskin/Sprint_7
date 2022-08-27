package api.suites.createCouriers;

import api.steps.CourierSteps;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import model.Credentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private Credentials credentials;

    private CourierSteps courierSteps;

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";

        credentials = new Credentials("ninja42", "1234", "saske");

        courierSteps = new CourierSteps();

        // удаляем аккаунт, если он уже есть
        deleteExistCourier(credentials);
    }

    @After
    public void tearDown() {
        // удаляем созданный аккаунт после теста
        deleteExistCourier(credentials);
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Обычный позитивный кейс создания нового курьера")
    public void positiveCreateCourierTest() {
        Response response = courierSteps.createCourierRequest(credentials);
        response.then().assertThat()
                .body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Test
    @DisplayName("Попытка создания курьера дважды")
    @Description("Попытка создать курьера с данными уже зарегистрированного пользователя")
    public void tryCreateCourierTwiceTest() {
        Response response = courierSteps.createCourierRequest(credentials);
        response.then().statusCode(201);

        // Вызываем запрос второй раз с той же связкой логин-пароль
        Response secondResponse = courierSteps.createCourierRequest(credentials);
        secondResponse.then().assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    public void deleteExistCourier(Credentials credentials) {
        Response responseLogin = courierSteps.loginCourierRequest(credentials);

        if (responseLogin.statusCode() == 200) {
            Courier courier = responseLogin.body().as(Courier.class);
            String id = courier.getId();
            courierSteps.deleteCourierRequest(id).then().statusCode(200);
        }
    }
}
