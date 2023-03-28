package ru.yandex.practicum.client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.client.base.ScooterRestClient;
import ru.yandex.practicum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends ScooterRestClient {
    @Step("Create order")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when()
                .post(BASE_URI+ "orders/")
                .then();
    }
    @Step("Get order")
    public ValidatableResponse getting (Order order) {
        return given()
                .spec(getBaseReqSpec())
                //.body(order)
                .when()
                .get(BASE_URI+ "orders/")
                .then();
    }


}
