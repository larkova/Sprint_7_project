package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.model.Order;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderTest {
    private OrderClient orderClient;
    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }
    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказа")
    @Description("При отправке запроса получения списка заказов, возвращается список заказов (положительный тест)")
    public void canGetOrder(){


        Order order = null;
        orderClient.getting(order)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("orders", notNullValue());
    }


}
