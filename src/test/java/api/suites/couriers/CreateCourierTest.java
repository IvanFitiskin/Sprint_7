package api.suites.couriers;

import api.steps.CourierSteps;
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
import static org.hamcrest.Matchers.notNullValue;


// курьера можно создать; ++
// нельзя создать двух одинаковых курьеров; ++
// чтобы создать курьера, нужно передать в ручку все обязательные поля;
// запрос возвращает правильный код ответа; ++
// успешный запрос возвращает `ok: true`**;** ++
// если одного из полей нет, запрос возвращает ошибку;
// если создать пользователя с логином, который уже есть, возвращается ошибка.

public class CreateCourierTest {

    private Credentials credentials;

    private CourierSteps courierSteps;

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";

        credentials = new Credentials("ninja42", "1234", "saske");

        courierSteps = new CourierSteps();

        // удаляем аккаунт, если он уже есть
        deleteExactCourier(credentials);
    }

    @Test
    @DisplayName("Создание курьера") // имя теста
    @Description("Обычный позитивный кейс создания нового курьера") // описание теста
    public void positiveCreateCourierTest() {
        Response response = courierSteps.createCourierRequest(credentials);
        response.then().assertThat()
                .body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Test
    @DisplayName("Создание курьера дважды") // имя теста
    @Description("Попытка создать курьера с данными уже зарегистрированного пользователя") // описание теста
    public void tryCreateCourierTwiceTest() {
        Response response = courierSteps.createCourierRequest(credentials);
        response.then().statusCode(201);

        Response secondResponse = courierSteps.createCourierRequest(credentials);
        System.out.println(secondResponse.body().asString());
        secondResponse.then().assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @After
    public void tearDown() {
        // удаляем созданный аккаунт после теста
        deleteExactCourier(credentials);
    }

    public void deleteExactCourier(Credentials credentials) {
        Response responseLogin = courierSteps.loginCourierRequest(credentials);

        if (responseLogin.statusCode() == 200) {
            Courier courier = responseLogin.body().as(Courier.class);
            String id = courier.getId();
            courierSteps.deleteCourierRequest(id).then().statusCode(200);
        }
    }
}
