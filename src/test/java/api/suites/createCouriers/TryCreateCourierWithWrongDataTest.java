package api.suites.createCouriers;

import client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.junit.Assert.assertEquals;

@Epic("Create courier")
@RunWith(Parameterized.class)
public class TryCreateCourierWithWrongDataTest {

    private final String login;
    private final String password;
    private final String firstName;

    private CourierClient courierClient;

    public TryCreateCourierWithWrongDataTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                { "ninja", null, "saske" },
                { null, "password", "saske" },
                { "", "1234", "" },
                { "ninja", "", null }
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";

        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Создание курьера без обязательных полей")
    @Description("Попытка создать курьера без полей login, password и без двух полей одновременно")
    public void positiveCreateCourierTest() {
        Courier courier = new Courier(login, password, firstName);
        ValidatableResponse response = courierClient.create(courier);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is not corrected", SC_BAD_REQUEST, statusCode);

        String message = response.extract().path("message");
        assertEquals("Wrong response message", "Недостаточно данных для создания учетной записи", message);
    }

}
