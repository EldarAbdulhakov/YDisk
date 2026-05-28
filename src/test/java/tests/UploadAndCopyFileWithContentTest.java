package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import utils.FileFactory;

import java.nio.file.Path;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class UploadAndCopyFileWithContentTest extends BaseTest {

    @Test
    public void uploadAndCopyFileTest() {
        createFolder(INPUT_FOLDER_NAME);
        createFolder(OUTPUT_FOLDER_NAME);

        Path file = FileFactory.createAndWriteToFile(PREFIX_FILE_NAME, SUFFIX_FILE_NAME, CONTENT);

        // Получить ссылку на загрузку файла
        String href = RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", "%s/%s".formatted(INPUT_FOLDER_NAME, FILE_NAME))
                .when()
                .get(UPLOAD_RESOURCE_PATH)
                .then()
                .statusCode(200)
                .extract()
                .path("href");

        // Загрузить файл в папку INPUT_FOLDER_NAME
        RestAssured.given()
                .spec(requestSpec)
                .body(file.toFile())
                .when()
                .put(href)
                .then()
                .statusCode(201);

        // Копировать файл в папку OUTPUT_FOLDER_NAME
        RestAssured.given()
                .spec(requestSpec)
                .queryParam("from", "%s/%s".formatted(INPUT_FOLDER_NAME, FILE_NAME))
                .queryParam("path", "%s/%s".formatted(OUTPUT_FOLDER_NAME, FILE_NAME))
                .when()
                .post(COPY_RESOURCE_PATH)
                .then()
                .statusCode(201)
                .body("method", instanceOf(String.class))
                .body("href", equalTo("https://cloud-api.yandex.net/v1/disk/resources?path=disk%%3A%%2F%s%%2F%s"
                        .formatted(OUTPUT_FOLDER_NAME, FILE_NAME)));

        // Повторить копирование файла в папку OUTPUT_FOLDER_NAME
        RestAssured.given()
                .spec(requestSpec)
                .queryParam("from", "%s/%s".formatted(INPUT_FOLDER_NAME, FILE_NAME))
                .queryParam("path", "%s/%s".formatted(OUTPUT_FOLDER_NAME, FILE_NAME))
                .when()
                .post(COPY_RESOURCE_PATH)
                .then()
                .statusCode(409)
                .body("error", equalTo("DiskResourceAlreadyExistsError"))
                .body("description", equalTo("Resource \"%s/%s\" already exists.".formatted(OUTPUT_FOLDER_NAME, FILE_NAME)))
                .body("message", equalTo("Ресурс \"%s/%s\" уже существует.".formatted(OUTPUT_FOLDER_NAME, FILE_NAME)));
    }
}
