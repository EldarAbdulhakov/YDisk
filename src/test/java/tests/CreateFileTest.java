package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateFileTest extends BaseTest {

    @Test
    @DisplayName("TC-4.1 Создание текстового файла в корневой директории возвращает 201")
    public void testCreateTextFileInRoot() {
        String href = RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", FILE_NAME)
                .when()
                .get(UPLOAD_RESOURCE_PATH)
                .then()
                .extract().path("href");

        RestAssured.given()
                .spec(requestSpec)
                .when()
                .put(href)
                .then()
                .statusCode(201);

        registerCreatedResource(FILE_NAME);
    }

    @Test
    @DisplayName("TC-4.2 Создание текстового файла в папке возвращает 201")
    public void testCreateTextFileInFolder() {
        createFolder(FOLDER_NAME);

        String href = RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", "%s/%s".formatted(FOLDER_NAME, FILE_NAME))
                .when()
                .get(UPLOAD_RESOURCE_PATH)
                .then()
                .extract().path("href");

        RestAssured.given()
                .spec(requestSpec)
                .put(href)
                .then()
                .statusCode(201);

        registerCreatedResource(FOLDER_NAME);
    }

    @Test
    @DisplayName("TC-4.3 Создание текстового файла без токена авторизации возвращает 401")
    public void testCreateTextFileWithoutOAuthToken() {
        RestAssured.given()
                .spec(requestSpecWithoutAuth)
                .queryParam("path", FILE_NAME)
                .when()
                .get(UPLOAD_RESOURCE_PATH)
                .then()
                .statusCode(401)
                .body("error", equalTo("UnauthorizedError"))
                .body("description", equalTo("Unauthorized"))
                .body("message", equalTo("Не авторизован."));
    }
}
