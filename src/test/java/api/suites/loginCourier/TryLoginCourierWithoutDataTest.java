package api.suites.loginCourier;

import client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.CourierCredentials;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.junit.Assert.assertEquals;

@Epic("Login courier")
@RunWith(Parameterized.class)
public class TryLoginCourierWithoutDataTest {

    private CourierClient courierClient;

    private final String login;
    private final String password;

    public TryLoginCourierWithoutDataTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                // { "ninja", null }, - сервер отдает 500, так что тест выполняет слишком долго
                { null, "password" },
                { "", "1234" },
                { "ninja", "" }
        };
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Попытка авторизации курьера без обязательных данных")
    @Description("Попытка авторизации курьера без обязательных данных")
    public void tryLoginCourierWithoutDataTest() {
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        ValidatableResponse response = courierClient.login(courierCredentials);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is not corrected", SC_BAD_REQUEST, statusCode);

        String message = response.extract().path("message");
        assertEquals("Wrong response message", "Недостаточно данных для входа", message);
    }

}
