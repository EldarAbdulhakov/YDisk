package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import utils.FileFactory;

import java.nio.file.Path;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UploadAndCopyFileWithContentTest extends BaseTest {

    @Test
    public void uploadAndCopyFileTest() {
        createFolder(INPUT_FOLDER_NAME);
        createFolder(OUTPUT_FOLDER_NAME);

        Path file = FileFactory.createAndWriteToFile(PREFIX_FILE_NAME, SUFFIX_FILE_NAME, CONTENT);

        // Получить ссылку на загрузку файла
        String href = RestAssured.given()
                .spec(requestSpec)
                .get(NESTED_UPLOAD_RESOURCE_PATH.formatted(INPUT_FOLDER_NAME, FILE_NAME))
                .then()
                .statusCode(200)
                .extract()
                .path("href");

        // Загрузить файл в папку
        RestAssured.given()
                .spec(requestSpec)
                .body(file.toFile())
                .put(href)
                .then()
                .statusCode(201);

        // Копировать файл
        RestAssured.given()
                .spec(requestSpec)
                .queryParam("from", INPUT_FOLDER_NAME + "/" + FILE_NAME)
                .queryParam("path", OUTPUT_FOLDER_NAME + "/" + FILE_NAME)
                .post(COPY_RESOURCE_PATH)
                .then()
                .statusCode(201)
                .body("method", notNullValue())
                .body("href", notNullValue());

        // Повторить копирование файла
        RestAssured.given()
                .spec(requestSpec)
                .queryParam("from", INPUT_FOLDER_NAME + "/" + FILE_NAME)
                .queryParam("path", OUTPUT_FOLDER_NAME + "/" + FILE_NAME)
                .post(COPY_RESOURCE_PATH)
                .then()
                .log().all()
                .statusCode(409)
                .body("error", equalTo("DiskResourceAlreadyExistsError"))
                .body("description", equalTo("Resource \"%s/%s\" already exists.".formatted(OUTPUT_FOLDER_NAME, FILE_NAME)))
                .body("message", equalTo("Ресурс \"%s/%s\" уже существует.".formatted(OUTPUT_FOLDER_NAME, FILE_NAME)));
    }
}
