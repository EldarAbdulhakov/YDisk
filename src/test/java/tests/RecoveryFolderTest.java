package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class RecoveryFolderTest extends BaseTest {

    @Test
    @DisplayName("TC-3.1 Восстановление пустой папки из корзины возвращает 201")
    public void testRestoreEmptyFolderFromTrash() {
        createFolder(FOLDER_NAME);
        deleteFolderToTrash(FOLDER_NAME);

        String folderPath = getTrashFolderPath(FOLDER_NAME);

        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", folderPath)
                .when()
                .put(RESTORE_FROM_TRASH_PATH)
                .then()
                .statusCode(201)
                .body("method", instanceOf(String.class))
                .body("href", equalTo("https://cloud-api.yandex.net/v1/disk/resources?path=disk%%3A%%2F%s".formatted(FOLDER_NAME)));
    }

    @Test
    @DisplayName("TC-3.2 Восстановление папки с вложенной папкой из корзины возвращает 202")
    public void testRestoreFolderWithNestedFolderFromTrash() {
        createFolder(FOLDER_NAME);
        createFolder("%s/%s".formatted(FOLDER_NAME, NESTED_FOLDER));
        deleteFolderToTrash(FOLDER_NAME);

        String folderPath = getTrashFolderPath(FOLDER_NAME);

        Response response = RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", folderPath)
                .when()
                .put(RESTORE_FROM_TRASH_PATH)
                .then()
                .statusCode(202)
                .body("method", equalTo("GET"))
                .body("href", startsWith("https://cloud-api.yandex.net/v1/disk/operations/"))
                .extract().response();

            waitForOperationComplete(response.jsonPath().getString("href"));
    }

    @Test
    @DisplayName("TC-3.3 Восстановление папки из корзины без токена авторизации возвращает 401")
    public void testRestoreFolderFromTrashWithoutAuthToken() {
        createFolder(FOLDER_NAME);
        deleteFolderToTrash(FOLDER_NAME);

        String folderPath = getTrashFolderPath(FOLDER_NAME);

        RestAssured.given()
                .spec(requestSpecWithoutAuth)
                .queryParam("path", folderPath)
                .when()
                .put(RESTORE_FROM_TRASH_PATH)
                .then()
                .statusCode(401)
                .body("error", equalTo("UnauthorizedError"))
                .body("description", equalTo("Unauthorized"))
                .body("message", equalTo("Не авторизован."));
    }

    @Test
    @DisplayName("TC-3.4 Восстановление несуществующей папки из корзины возвращает 404")
    public void testRestoreNonExistentFolder() {
        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", "NonExistentResource")
                .when()
                .put(RESTORE_FROM_TRASH_PATH)
                .then()
                .statusCode(404)
                .body("error", equalTo("DiskNotFoundError"))
                .body("description", equalTo("Resource not found."))
                .body("message", equalTo("Не удалось найти запрошенный ресурс."));
    }
}
