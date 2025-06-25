package com.artyb.tapyou;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserApiTests {

    @Test
    public void getUsersByGenderMale_shouldReturnValidIdList() {
        // Настройка базового URL
        RestAssured.baseURI = "https://hr-challenge.dev.tapyou.com";

        Response response = given()
                .queryParam("gender", "male")
                .when()
                .get("/api/test/users")
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Проверки
        boolean isSuccess = response.path("isSuccess");
        int errorCode = response.path("errorCode");

        assertThat("isSuccess должен быть true", isSuccess, is(true));
        assertThat("errorCode должен быть 0", errorCode, equalTo(0));
        assertThat("idList не должен быть пустым",
                response.path("idList"), is(not(empty())));
    }

    @Test
    public void getUserById_shouldReturnValidUser() {
        RestAssured.baseURI = "https://hr-challenge.dev.tapyou.com";

        Response response = given()
                .pathParam("id", 5)
                .when()
                .get("/api/test/user/{id}")
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Проверки на корневом уровне
        boolean isSuccess = response.path("isSuccess");
        int errorCode = response.path("errorCode");

        assertThat("isSuccess должен быть true", isSuccess, is(true));
        assertThat("errorCode должен быть 0", errorCode, equalTo(0));

        // Проверки объекта user
        int userId = response.path("user.id");
        String name = response.path("user.name");
        String gender = response.path("user.gender");
        int age = response.path("user.age");
        String city = response.path("user.city");
        String registrationDate = response.path("user.registrationDate");

        assertThat("id должен быть 5", userId, equalTo(5));
        assertThat("name не должен быть пустым", name, not(emptyOrNullString()));
        assertThat("gender должен быть male или female", gender, anyOf(equalTo("male"), equalTo("female")));
        assertThat("age должен быть > 0", age, greaterThan(0));
        assertThat("city не должен быть пустым", city, not(emptyOrNullString()));
        assertThat("registrationDate не должен быть пустым", registrationDate, not(emptyOrNullString()));
    }
}