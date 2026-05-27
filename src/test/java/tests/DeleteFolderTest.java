package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class DeleteFolderTest extends BaseTest {

    @Test
    public void testDeleteFolderToTrash() {
        createFolder(FOLDER_NAME);

        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .when()
                .delete(RESOURCE_PATH)
                .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    public void testDeleteFolderPermanently() {
        createFolder(FOLDER_NAME);

        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .queryParam("permanently", "true")
                .when()
                .delete(RESOURCE_PATH)
                .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    public void testDeleteFolderWithNestedFolderToTrash() {
        createFolder(FOLDER_NAME);
        createFolder(FOLDER_NAME + "/" + NESTED_FOLDER);

        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .when()
                .delete(RESOURCE_PATH)
                .then()
                .log().all()
                .statusCode(202)
                .body("method", equalTo("GET"))
                .body("href", containsString("https://cloud-api.yandex.net/v1/disk/operations/"))
                .body("templated", equalTo(false));
    }

    @Test
    public void testDeleteNonExistentFolderToTrash() {
        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .when()
                .delete(RESOURCE_PATH)
                .then()
                .log().all()
                .statusCode(404)
                .body("error", equalTo("DiskNotFoundError"))
                .body("description", equalTo("Resource not found."))
                .body("message", equalTo("Не удалось найти запрошенный ресурс."));
    }

    @Test
    public void testDeleteFolderWithoutOAuthToken() {
        createFolder(FOLDER_NAME);

        RestAssured.given()
                .spec(requestSpecWithoutAuth)
                .queryParam("path", FOLDER_NAME)
                .when()
                .delete(RESOURCE_PATH)
                .then()
                .log().all()
                .statusCode(401)
                .body("error", equalTo("UnauthorizedError"))
                .body("description", equalTo("Unauthorized"))
                .body("message", equalTo("Не авторизован."));
    }
}
