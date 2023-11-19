package data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import lombok.val;

import java.util.Locale;

import static data.DataGenerator.getRandomLogin;
import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    public static void sendRequest(RegistrationDto user) {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }


    public static String getRandomLogin() {
        var login = faker.name().username();
        return login;
    }

    public static String getRandomPassword() {
        var password = faker.internet().password();
        return password;
    }

public static class Registration {
    private Registration() {
    }

    public static RegistrationDto getUser(String status) {
        var user = new RegistrationDto(getRandomLogin(), DataGenerator.getRandomPassword(),status);
        return user;
    }

    public static RegistrationDto getRegisteredUser(String status) {
        var registeredUser = getUser(status);
        sendRequest(registeredUser);
        return registeredUser;
    }
}

@Value
public static class RegistrationDto {
    String login;
    String password;
    String status;
}
}