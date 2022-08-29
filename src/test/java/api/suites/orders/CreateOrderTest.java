package api.suites.orders;

import client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.OrderGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.*;

@Epic("Create order")
@RunWith(Parameterized.class)
public class CreateOrderTest {

    private OrderClient orderClient;

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
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Обычный позитивный кейс создания нового заказа")
    public void positiveCreateOrderTest() {
        Order order = OrderGenerator.getOrderByColor(color);

        ValidatableResponse response = orderClient.create(order);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is not corrected", SC_CREATED, statusCode);

        int isCreated = response.extract().path("track");
        assertNotEquals("Order is not created", 0, isCreated);
    }
}
