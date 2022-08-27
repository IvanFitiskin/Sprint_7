package api.suites.createCouriers;

import api.steps.CourierSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Credentials;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TryCreateCourierWithEmptyDataTest {

    private CourierSteps courierSteps;

    private String login;
    private String password;

    public TryCreateCourierWithEmptyDataTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                { "", "1234" },
                { "ninja42", "" }
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";

        courierSteps = new CourierSteps();
    }

    @Test
    @DisplayName("Создание курьера") // имя теста
    @Description("Попытка создать курьера с пустыми полями login и password") // описание теста
    public void tryCreateCourierWithEmptyCredentialsTest() {
        Credentials credentials = new Credentials(login, password, "saske");

        Response response = courierSteps.createCourierRequest(credentials);
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
}
