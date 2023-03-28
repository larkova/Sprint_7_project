package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.practicum.client.CourierClient;
import ru.yandex.practicum.model.Courier;
import ru.yandex.practicum.model.CourierCredentials;
import ru.yandex.practicum.model.CourierGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreatingCourierTest {
    private CourierClient courierClient;
    private int courierId;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void clearData() {
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Создание курьера со всеми корректно заполненными полями")
    @Description("Курьер успешно создается со всеми заполненными полями: логин, пароль, имя")

    public void courierCanBeCreatedWithValidData() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.createCourier(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

            }

    @Test
    @DisplayName("Создание курьера с заполненными обязательными полями")
    @Description("Курьер успешно создается с заполненными обязательными полями: логин, пароль")

    public void courierCanBeCreatedWithObligatoryFields() {
        Courier courier = new Courier("Testlogin12", "Testpassword", null);
        courierClient.createCourier(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Нельзя создать курьера без пароля")
    public void courierCanNoCreatedWithoutObligatoryFields() {
        Courier courier = new Courier("Testlogin12", null, null);
        courierClient.createCourier(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для создания учетной записи"));

    }
    @Test
    @DisplayName("Создание двух курьеров с одинаковым логином")
    @Description("Нельзя создать двух курьеров с одинаковым логином")
    public void twoSameLoginCanNotBeCreated() {
        Courier courier = new Courier("Testlogin12", "Testpassword", "Elena");
        Courier secondCourier = new Courier("Testlogin", "Testpassword1234", "Alex");

        courierClient.createCourier(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

        courierClient.createCourier(secondCourier)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .assertThat()
                .body("message", is("Этот логин уже используется. Попробуйте другой."));

    }
    @Test
    @DisplayName("Создание двух курьеров с одинаковыми логином, паролем, именем")
    @Description("Нельзя создать двух курьеров с одинаковыми данными: логином, паролем, именем")
    public void twoSameCouriersCanNotBeCreated() {
        Courier courier = new Courier("Testlogin12", "Testpassword", "Elena");

        courierClient.createCourier(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

        courierClient.createCourier(courier)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .assertThat()
                .body("message", is("Этот логин уже используется. Попробуйте другой."));

    }

}
