package tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.PropertyProvider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class BaseTest {

    protected static final String FOLDER_NAME = "folderName";
    protected static final String NESTED_FOLDER = "nestedFolder";
    protected static final String INPUT_FOLDER_NAME = "input_data";
    protected static final String OUTPUT_FOLDER_NAME = "output_data";
    protected static final String PREFIX_FILE_NAME = "data";
    protected static final String SUFFIX_FILE_NAME = ".txt";
    protected static final String FILE_NAME = PREFIX_FILE_NAME + SUFFIX_FILE_NAME;
    protected static final String CONTENT = """
            username=Petr
            password=secret_key
            """;
    protected static final String DISK_PATH = "/v1/disk";
    protected static final String RESOURCE_PATH = "v1/disk/resources";
    protected static final String RESOURCE_TRASH_PATH = "v1/disk/trash/resources";
    protected static final String RESTORE_FROM_TRASH_PATH = "v1/disk/trash/resources/restore";
    protected static final String UPLOAD_RESOURCE_PATH = "v1/disk/resources/upload";
    protected static final String COPY_RESOURCE_PATH = "v1/disk/resources/copy";
    protected static final String DOWNLOAD_RESOURCE_PATH = "v1/disk/resources/download";

    protected static RequestSpecification requestSpec;
    protected static RequestSpecification requestSpecWithoutAuth;
    protected static final PropertyProvider CONFIG = PropertyProvider.getInstance();
    protected List<String> createdResources;

    @BeforeAll
    protected static void setup() {
        RestAssured.baseURI = PropertyProvider.getInstance().getBaseUrl();

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(CONFIG.getBaseUrl())
                .addHeader("Authorization", "OAuth " + CONFIG.getToken())
                .build();

        requestSpecWithoutAuth = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(CONFIG.getBaseUrl())
                .build();
    }

    @BeforeEach
    protected void initTestContent() {
        createdResources = new ArrayList<>();
    }

    @AfterEach
    protected void cleaUp() {
        for (String folderName : createdResources) {
            if (isResourceExists(folderName)) {
                deleteResourcePermanently(folderName);
            }
            if (isResourceExistsInTrash(folderName)) {
                deleteFolderFromTrash(folderName);
            }
        }
        createdResources.clear();
    }

    protected void registerCreatedResource(String resource) {
        createdResources.add(resource);
    }

    protected void createFolder(String folderName) {
        RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", folderName)
                .when()
                .put(RESOURCE_PATH);

        registerCreatedResource(folderName);
    }

    protected void deleteFolderToTrash(String folderName) {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", folderName)
                .when()
                .delete(RESOURCE_PATH);

        if (response.statusCode() == 202) {
            String operationHref = response.jsonPath().getString("href");
            waitForOperationComplete(operationHref);
        }
    }

    protected void waitForOperationComplete(String operationUrl) {
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> {
                    String status = RestAssured.given()
                            .spec(requestSpec)
                            .when()
                            .get(operationUrl)
                            .then()
                            .extract()
                            .path("status");
                    return "success".equals(status);
                });
    }

    protected String getTrashFolderPath(String folderName) {
        List<Map<String, Object>> items = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(RESOURCE_TRASH_PATH)
                .then()
                .extract()
                .path("_embedded.items");

        return items.stream()
                .filter(item -> folderName.equals(item.get("name")))
                .map(item -> (String) item.get("path"))
                .findFirst()
                .orElse(null);
    }

    protected void deleteResourcePermanently(String resourcePath) {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", resourcePath)
                .queryParam("permanently", "true")
                .when()
                .delete(RESOURCE_PATH);

        if (response.statusCode() == 202) {
            waitForOperationComplete(response.jsonPath().getString("href"));
        }
    }

    protected void deleteFolderFromTrash(String resource) {
        String folderPath = getTrashFolderPath(resource);

        Response response = RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", folderPath)
                .when()
                .delete(RESOURCE_TRASH_PATH);

        if (response.statusCode() == 202) {
            waitForOperationComplete(response.jsonPath().getString("href"));
        }
    }

    protected boolean isResourceExists(String path) {
        int statusCode = RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", path)
                .when()
                .get(RESOURCE_PATH)
                .statusCode();

        return statusCode == 200;
    }

    protected boolean isResourceExistsInTrash(String resource) {
        return getTrashFolderPath(resource) != null;
    }

    protected String getFileUploadLink(String path) {
        return RestAssured.given()
                .spec(requestSpec)
                .queryParam("path", path)
                .when()
                .get(UPLOAD_RESOURCE_PATH)
                .then()
                .extract()
                .path("href");
    }

    protected void uploadFileToFolder(Path file, String fileUploadLink) {
        RestAssured.given()
                .spec(requestSpec)
                .body(file.toFile())
                .when()
                .put(fileUploadLink)
                .then();
    }
}
