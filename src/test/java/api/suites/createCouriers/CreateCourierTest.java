package api.suites.createCouriers;

import client.CourierClient;
import io.qameta.allure.Epic;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.CourierGenerator;
import model.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Epic("Create courier")
public class CreateCourierTest {

    private Courier courier;
    private CourierCredentials courierCredentials;
    private CourierClient courierClient;

    @Before
    public void setUp() {
        courier = CourierGenerator.getDefault();
        courierCredentials = CourierCredentials.from(courier);
        courierClient = new CourierClient();

        // удаляем аккаунт, если он уже есть
        deleteExistCourier(courierCredentials);
    }

    @After
    public void tearDown() {
        // удаляем созданный аккаунт после теста
        deleteExistCourier(courierCredentials);
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Обычный позитивный кейс создания нового курьера")
    public void positiveCreateCourierTest() {
        ValidatableResponse response = courierClient.create(courier);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is not corrected", SC_CREATED, statusCode);

        boolean isCreated = response.extract().path("ok");
        assertTrue("Courier is not created", isCreated);
    }

    @Test
    @DisplayName("Попытка создания курьера дважды")
    @Description("Попытка создать курьера с данными уже зарегистрированного пользователя")
    public void tryCreateCourierTwiceTest() {
        ValidatableResponse response = courierClient.create(courier);
        response.statusCode(201);

        // Вызываем запрос второй раз с той же связкой логин-пароль
        ValidatableResponse secondResponse = courierClient.create(courier);

        int statusCode = secondResponse.extract().statusCode();
        assertEquals("Status code is not corrected", SC_CONFLICT, statusCode);

        String message = secondResponse.extract().path("message");
        assertEquals("Wrong response message", "Этот логин уже используется. Попробуйте другой.", message);
    }

    private void deleteExistCourier(CourierCredentials courierCredentials) {
        ValidatableResponse responseLogin = courierClient.login(courierCredentials);

        if (responseLogin.extract().statusCode() == 200) {
            int courierId = responseLogin.extract().path("id");
            courierClient.delete(courierId).statusCode(200);
        }
    }
}
