package api.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Credentials;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    @Step("Запрос на создание нового курьера")
    public Response createCourierRequest(Credentials credentials) {
        return given()
                .header("Content-type", "application/json")
                .body(credentials)
                .post("/api/v1/courier");
    }

    @Step("Запрос на создание нового курьера")
    public Response createCourierRequest(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier");
    }

    @Step("Запрос на логина по имени и паролю")
    public Response loginCourierRequest(Credentials credentials) {
        return given()
                .header("Content-type", "application/json")
                .body(credentials)
                .post("/api/v1/courier/login");
    }

    @Step("Запрос на удаление курьера по id")
    public Response deleteCourierRequest(String id) {
        return given()
                .header("Content-type", "application/json")
                .pathParam("id", id)
                .delete("/api/v1/courier/{id}");
    }

}
