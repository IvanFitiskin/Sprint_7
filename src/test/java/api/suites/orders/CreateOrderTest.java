package api.suites.orders;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.OrderData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Create order")
@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final String[] color;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                { new String[] { "BLACK" } },
                { new String[] { "GREY" } },
                { new String[] { "BLACK", "GREY" } },
                { new String[] {} },
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Обычный позитивный кейс создания нового заказа")
    public void positiveCreateOrderTest() {
        OrderData orderData = new OrderData(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                color
        );

        Response response = given()
                .header("Content-type", "application/json")
                .body(orderData)
                .post("/api/v1/orders");

        response.then().assertThat()
                .body("track", notNullValue())
                .and()
                .statusCode(201);
    }
}
