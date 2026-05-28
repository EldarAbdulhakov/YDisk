package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.FileFactory;

import java.nio.file.Path;

import static org.hamcrest.Matchers.startsWith;

public class DownloadFileWithContentTest extends BaseTest {

    @Test
    public void downloadFileTest() {
        Path file = FileFactory.createAndWriteToFile(PREFIX_FILE_NAME, SUFFIX_FILE_NAME, CONTENT);
        createFolder(FOLDER_NAME);

        // Получить ссылку на загрузку файла
        String uploadHref = RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", "%s/%s".formatted(FOLDER_NAME, FILE_NAME))
                .when()
                .get(UPLOAD_RESOURCE_PATH)
                .then()
                .statusCode(200)
                .extract()
                .path("href");

        // Загрузить файл в папку
        RestAssured.given()
                .spec(requestSpec)
                .body(file.toFile())
                .when()
                .put(uploadHref)
                .then()
                .statusCode(201);

        // Получить ссылку на скачивание
        String downloadHref = RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", "%s/%s".formatted(FOLDER_NAME, FILE_NAME))
                .when()
                .get(DOWNLOAD_RESOURCE_PATH)
                .then()
                .statusCode(200)
                .body("href", startsWith("https://downloader.disk.yandex.ru/disk/"))
                .extract()
                .path("href");

        // Скачать и прочитать файл
        String actualText = RestAssured.given()
                .urlEncodingEnabled(false)
                .when()
                .get(downloadHref)
                .then()
                .statusCode(200)
                .extract()
                .asString();

        Assertions.assertEquals(CONTENT, actualText, "The text in the file is different");
    }
}
