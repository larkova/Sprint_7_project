package ru.yandex.practicum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.client.base.ScooterRestClient;
import ru.yandex.practicum.model.Courier;
import ru.yandex.practicum.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient extends ScooterRestClient {
    private static final String COURIER_URI = BASE_URI + "courier/";

    @Step("Create courier {courier}")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .spec(getBaseReqSpec())
                .body(courier)
                .when()
                .post(COURIER_URI)
                .then();
    }

    @Step("Login as {courierCredentials}")
    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseReqSpec())
                .body(courierCredentials)
                .when()
                .post(COURIER_URI+ "login/")
                .then();
    }

    @Step("Delete courier {id}")
    public ValidatableResponse delete(int id) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(COURIER_URI + id)
                .then();
    }

}
