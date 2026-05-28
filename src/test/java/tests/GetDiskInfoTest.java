package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class GetDiskInfoTest extends BaseTest {

    @Test
    public void testGetDiskInfo() {
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(DISK_PATH)
                .then()
                .statusCode(200)
                .body("user.login", equalTo(CONFIG.getLogin()))
                .body("user.display_name", equalTo(CONFIG.getDisplayName()));
    }

    @Test
    public void testGetDiskInfoWithoutToken() {
        RestAssured.given()
                .spec(requestSpecWithoutAuth)
                .when()
                .get(DISK_PATH)
                .then()
                .statusCode(401)
                .body("error", equalTo("UnauthorizedError"))
                .body("description", equalTo("Unauthorized"))
                .body("message", equalTo("Не авторизован."));
    }
}
