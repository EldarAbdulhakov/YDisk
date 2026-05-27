package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateFolderTest extends BaseTest {

    @Test
    public void testCreateFolderInRootDirectory() {
//        registerCreatedResource(FOLDER_NAME);

        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .when()
                .put(RESOURCE_PATH)
                .then()
                .log().all()
                .statusCode(201)
                .body("method", equalTo("GET"))
                .body("href", equalTo("https://cloud-api.yandex.net/v1/disk/resources?path=disk%%3A%%2F%s".formatted(FOLDER_NAME)))
                .body("templated", equalTo(false));
    }

    @Test
    public void testCreateNestedFolder() {
        createFolder(FOLDER_NAME);

        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME + "/" + NESTED_FOLDER)
                .when()
                .put(RESOURCE_PATH)
                .then()
                .log().all()
                .statusCode(201)
                .body("method", equalTo("GET"))
                .body("href", equalTo("https://cloud-api.yandex.net/v1/disk/resources?path=disk%%3A%%2F%s%%2F%s".formatted(FOLDER_NAME, NESTED_FOLDER)))
                .body("templated", equalTo(false));
    }

    @Test
    public void testCreateFolderWithExistingName() {
        createFolder(FOLDER_NAME);

        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .when()
                .put(RESOURCE_PATH)
                .then()
                .log().all()
                .statusCode(409)
                .body("error", equalTo("DiskPathPointsToExistentDirectoryError"))
                .body("description", equalTo("Specified path \"%s\" points to existent directory.".formatted(FOLDER_NAME)))
                .body("message", equalTo("По указанному пути \"%s\" уже существует папка с таким именем.".formatted(FOLDER_NAME)));
    }

    @Test
    public void testCreateFolderWithoutOAuthToken() {
        RestAssured.given()
                .spec(requestSpecWithoutAuth)
                .queryParam("path", FOLDER_NAME)
                .put(RESOURCE_PATH)
                .then()
                .log().all()
                .statusCode(401)
                .body("error", equalTo("UnauthorizedError"))
                .body("description", equalTo("Unauthorized"))
                .body("message", equalTo("Не авторизован."));
    }
}
