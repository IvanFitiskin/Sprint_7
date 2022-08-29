package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient {

    private static final String COURIER_PATH = "/api/v1/courier";

    @Step("Отправка запроса на создание нового курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Отправка запроса на авторизацию курьера")
    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(courierCredentials)
                .post(COURIER_PATH + "/login")
                .then();
    }

    @Step("Отправка запроса на удаление курьера")
    public ValidatableResponse delete(int id) {
        return given()
                .spec(getBaseSpec())
                .pathParam("id", String.valueOf(id))
                .when()
                .delete(COURIER_PATH + "/{id}")
                .then();
    }

}
