package StepDefenition;

import io.cucumber.java.ru.Когда;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;

public class createUser {
    private static ResourceBundle rb = ResourceBundle.getBundle("application");
    static String webTask2 = rb.getString("webTask2");
    private static RequestSpecification reqSpec = new RequestSpecBuilder()
            .setBaseUri(webTask2)
            .build();
    @Step("Изменение данных в Json на \"name\" : \"Tomato\", \"job\" : \"Eat maket\"")
    @Когда("вводят новую информацию обновить Json файл")
    public static void userCreate() throws IOException {
        String name = rb.getString("names");
        String job = rb.getString("jobs");
        String nameValue = rb.getString("nameValue");
        String jobValue = rb.getString("jobValue");
        String pathJson = rb.getString("pathJson");
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get(pathJson))));
        body.put(name, nameValue);
        body.put(job, jobValue);
        Response sendJson = given()
                .header("Content-type","application/json")
                .spec(reqSpec)
                .body(body.toString())
                .when()
                .put("/users/")
                .then()
                .statusCode(200)
                .extract()
                .response();
        Assertions.assertEquals((new JSONObject(sendJson.getBody().asString()).get("name")), (body.get("name")), "Неудача");
        Assertions.assertEquals((new JSONObject(sendJson.getBody().asString()).get("job")), (body.get("job")), "Неудача");
        Allure.addAttachment("Result", String.valueOf(body));
    }
}
