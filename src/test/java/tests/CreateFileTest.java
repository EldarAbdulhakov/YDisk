package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateFileTest extends BaseTest {

    @Test
    public void testCreateTextFileInRoot() {
        String href = RestAssured.given()
                .spec(requestSpec)
                .get(UPLOAD_RESOURCE_PATH.formatted(FILE_NAME))
                .then()
                .extract().path("href");

        RestAssured.given()
                .spec(requestSpec)
                .put(href)
                .then()
                .statusCode(201);
    }

    @Test
    public void testCreateTextFileInFolder() {
        createFolder(FOLDER_NAME);

        String href = RestAssured.given()
                .spec(requestSpec)
                .get(NESTED_UPLOAD_RESOURCE_PATH.formatted(FOLDER_NAME, FILE_NAME))
                .then()
                .extract().path("href");

        RestAssured.given()
                .spec(requestSpec)
                .put(href)
                .then()
                .statusCode(201);
    }

    @Test
    public void testCreateTextFileWithoutOAuthToken() {
        RestAssured.given()
                .spec(requestSpecWithoutAuth)
                .get(UPLOAD_RESOURCE_PATH.formatted(FILE_NAME))
                .then()
                .statusCode(401)
                .body("error", equalTo("UnauthorizedError"))
                .body("description", equalTo("Unauthorized"))
                .body("message", equalTo("Не авторизован."));
    }
}
