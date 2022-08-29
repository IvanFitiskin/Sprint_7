package api.suites.loginCourier;

import client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.CourierCredentials;
import model.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

@Epic("Login courier")
public class LoginCourierTest {

    private CourierCredentials courierCredentials;
    private int courierId;

    private CourierClient courierClient;

    @Before
    public void setUp() {
        Courier courier = CourierGenerator.getDefault();
        courierCredentials = CourierCredentials.from(courier);

        courierClient = new CourierClient();

        // создаем нового курьера
        courierClient.create(courier);
    }

    @After
    public void tearDown() {
        // удаляем созданный аккаунт после теста
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Обычный позитивный кейс авторизации курьера")
    public void positiveLoginCourierTest() {
        ValidatableResponse response = courierClient.login(courierCredentials);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is not corrected", SC_OK, statusCode);

        courierId = response.extract().path("id");
        assertNotEquals("path id is null", 0, courierId);
    }
}
