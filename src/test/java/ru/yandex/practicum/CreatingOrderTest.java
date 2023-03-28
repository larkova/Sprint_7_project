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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.model.Order;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreatingOrderTest {
    private OrderClient orderClient;
    private final List<String> colour;
    public CreatingOrderTest(List<String> colour){
        this.colour=colour;
    }
    @Parameterized.Parameters
    public static Object[][] getColour(){
        return new Object[][]{
             new List []{List.of("GRAY")},
             new List []{List.of("BLACK")},
             new List []{List.of("GRAY", "BLACK")},
             new List []{List.of("")}
        };
    }

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
    @DisplayName("Создание заказа")
    @Description("Заказ успешно создается(позитивный сценарий)")
    public void canCreateOrder() {
        Order order = new Order("Name", "LastName", "Address", "Metro", "9000000000", "2", "20-Mar-2033 20:09:07 GMT", "comment", colour);

        orderClient.createOrder(order)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("track", notNullValue());
    }
}
