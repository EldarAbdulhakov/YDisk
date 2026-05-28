package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class DeleteFolderTest extends BaseTest {

    @Test
    @DisplayName("TC-2.1 Удаление пустой папки в корзину возвращает 204")
    public void testDeleteEmptyFolderToTrash() {
        createFolder(FOLDER_NAME);

        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .when()
                .delete(RESOURCE_PATH)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("TC-2.2 Безвозвратное удаление пустой папки возвращает 204")
    public void testDeleteEmptyFolderPermanently() {
        createFolder(FOLDER_NAME);

        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .queryParam("permanently", "true")
                .when()
                .delete(RESOURCE_PATH)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("TC-2.3 Удаление папки с вложенной папкой в корзину возвращает 202")
    public void testDeleteFolderWithNestedFolderToTrash() {
        createFolder(FOLDER_NAME);
        createFolder("%s/%s".formatted(FOLDER_NAME, NESTED_FOLDER));

        Response response = RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .when()
                .delete(RESOURCE_PATH)
                .then()
                .statusCode(202)
                .body("method", equalTo("GET"))
                .body("href", containsString("https://cloud-api.yandex.net/v1/disk/operations/"))
                .body("templated", equalTo(false))
                .extract().response();

        waitForOperationComplete(response.jsonPath().getString("href"));
    }

    @Test
    @DisplayName("TC-2.4 Удаление несуществующей папки в корзину возвращает 404")
    public void testDeleteNonExistentFolderToTrash() {
        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .when()
                .delete(RESOURCE_PATH)
                .then()
                .statusCode(404)
                .body("error", equalTo("DiskNotFoundError"))
                .body("description", equalTo("Resource not found."))
                .body("message", equalTo("Не удалось найти запрошенный ресурс."));
    }

    @Test
    @DisplayName("TC-2.5 Удаление папки без токена авторизации возвращает 401")
    public void testDeleteFolderWithoutOAuthToken() {
        createFolder(FOLDER_NAME);

        RestAssured.given()
                .spec(requestSpecWithoutAuth)
                .queryParam("path", FOLDER_NAME)
                .when()
                .delete(RESOURCE_PATH)
                .then()
                .statusCode(401)
                .body("error", equalTo("UnauthorizedError"))
                .body("description", equalTo("Unauthorized"))
                .body("message", equalTo("Не авторизован."));
    }
}
