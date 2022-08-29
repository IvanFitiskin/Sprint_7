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

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.junit.Assert.assertEquals;

@Epic("Login courier")
@RunWith(Parameterized.class)
public class TryLoginCourierWithWrongDataTest {

    private CourierClient courierClient;

    private final String login;
    private final String password;

    private CourierCredentials courierCredentials;

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
        courierCredentials = new CourierCredentials(login, password);
        courierClient = new CourierClient();

        // Если аккаунт курьера существует - удаляем его
        ValidatableResponse responseLogin = courierClient.login(courierCredentials);

        if (responseLogin.extract().statusCode() == 200) {
            int id = responseLogin.extract().path("id");
            courierClient.delete(id).statusCode(200);
        }
    }

    @Test
    @DisplayName("Попытка авторизации курьера с несуществующей парой логин-пароль")
    @Description("Попытка авторизации курьера с несуществующей парой логин-пароль")
    public void tryLoginCourierWithWrongCredentialsTest() {
        ValidatableResponse response = courierClient.login(courierCredentials);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is not corrected", SC_NOT_FOUND, statusCode);

        String message = response.extract().path("message");
        assertEquals("Wrong response message", "Учетная запись не найдена", message);
    }

}
