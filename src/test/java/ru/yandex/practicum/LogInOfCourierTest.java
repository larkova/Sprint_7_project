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

public class LogInOfCourierTest {
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
    @DisplayName("Авторизация курьера")
    @Description("Курьер успешно авторизуется с корректными данными, возвращается id курьера")

        public void courierCanLogIn() {
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
    @DisplayName("Авторизация незарегистрированного курьера")
    @Description("Курьер не может авторизоваться, если учетная запись не создана")
    public void courierCanNotLogInWithoutAccount() {

        CourierCredentials courierCredentials = CourierCredentials.from(CourierGenerator.getRandom());

        courierClient.login(courierCredentials)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", is("Учетная запись не найдена"));

    }
    @Test
    @DisplayName("Авторизация без логина")
    @Description("Курьер не может авторизоваться без логина")
    public void courierCanNotLogInWithoutLogin() {

        CourierCredentials courierCredentials=new CourierCredentials(null, "Testpassword");

        courierClient.login(courierCredentials)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для входа"));

    }
    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Курьер не может авторизоваться без пароля")
    public void courierCanNotLogInWithoutPassword() {

        CourierCredentials courierCredentials=new CourierCredentials("Testlogin1111", null);

        courierClient.login(courierCredentials)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для входа"));

    }
    @Test
    @DisplayName("Авторизация c неверным логином")
    @Description("Курьер не может авторизоваться с опечаткой в логине")
    public void courierCanNotLogInWithWrongLogin() {

        Courier courier = new Courier("Testlogin12", "Testpassword", "Elena");

        courierClient.createCourier(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        CourierCredentials courierCredentials=new CourierCredentials("Testlogin 123", "Testpassword");

        courierClient.login(courierCredentials)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", is("Учетная запись не найдена"));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

    }
    @Test
    @DisplayName("Авторизация c неверным паролем")
    @Description("Курьер не может авторизоваться с опечаткой в пароле")
    public void courierCanNotLogInWithWrongPassword() {

        Courier courier = new Courier("Testlogin12", "Testpassword", "Elena");

        courierClient.createCourier(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        CourierCredentials courierCredentials=new CourierCredentials("Testlogin12", "Testpassword12");

        courierClient.login(courierCredentials)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", is("Учетная запись не найдена"));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

    }

}
