package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateFileTest extends BaseTest {

    @Test
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
    }

    @Test
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
    }

    @Test
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
