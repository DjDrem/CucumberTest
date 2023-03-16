package StepDefenition;

import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.qameta.allure.Allure;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;

public class RickAndMorti {
    public static String charId;
    public static String charName;
    public static String charLocation;
    public static int charEpisode;
    public static String charStatus;
    public static String charGender;
    public static String charSpecies;
    public static int lastEpisode;
    public static String charLastId;
    public static String charLastName;
    public static String charLastSpecies;
    public static String charLastLocation;
    public static int idCharacter;
    private static ResourceBundle rb = ResourceBundle.getBundle("application");
    static String webTask1 = rb.getString("webTask1");
    static String id = rb.getString("Id");

    private static RequestSpecification reqSpecFirst = new RequestSpecBuilder()
            .setBaseUri(webTask1)
            .build();
    @Когда("пользователь вводит ID персонажа")
    public static void findInformationCharacter(){

        Response infoCharacter = given()
                .spec(reqSpecFirst)
                .when()
                .get("/character/" + id)
                .then()
                .extract()
                .response();
        charId = new JSONObject(infoCharacter.getBody().asString()).get("id").toString();
        charName = new JSONObject(infoCharacter.getBody().asString()).get("name").toString();
        charGender = new JSONObject(infoCharacter.getBody().asString()).get("gender").toString();
        charSpecies = new JSONObject(infoCharacter.getBody().asString()).get("species").toString();
        charLocation = new JSONObject(infoCharacter.getBody().asString()).getJSONObject("location").get("name").toString();
        charEpisode = (new JSONObject(infoCharacter.getBody().asString()).getJSONArray("episode").length());
        charStatus = new JSONObject(infoCharacter.getBody().asString()).get("status").toString();
        Assertions.assertNotNull(charId);
        Allure.addAttachment("ID", charId);
        Allure.addAttachment("Name", charName);
        Allure.addAttachment("Gender", charGender);
        Allure.addAttachment("Species", charSpecies);
        Allure.addAttachment("Location", charLocation);
        Allure.addAttachment("Episode", String.valueOf(charEpisode));
        Allure.addAttachment("Status", charStatus);
    }

    @Тогда("показать информацию о крайнем эпизоде")
    public static void selectEpisode() {
        Response gettingLastEpisode = given()
                .spec(reqSpecFirst)
                .when()
                .get("/character/" + charId)
                .then()
                .extract()
                .response();
        int episode = (new JSONObject(gettingLastEpisode.getBody().asString()).getJSONArray("episode").length() - 1);
        lastEpisode = Integer.parseInt(new JSONObject(gettingLastEpisode.getBody().asString())
                .getJSONArray("episode").get(episode).toString().replaceAll("[^0-9]", ""));
        Assertions.assertEquals(51, lastEpisode, "Ошибка");
        Allure.addAttachment("LastEpisode", String.valueOf(lastEpisode));
    }

    @Когда("получена информация о эпизоде")
    public static void gettingLastCharacterID(){
        Response gettingCharacter = given()
                .spec(reqSpecFirst)
                .when()
                .get("/episode/" + lastEpisode)
                .then()
                .extract()
                .response();
        int lastCharacter = (new JSONObject(gettingCharacter.getBody().asString()).getJSONArray("characters")
                .length()-1);
        idCharacter = Integer.parseInt(new JSONObject(gettingCharacter.getBody().asString())
                .getJSONArray("characters").get(lastCharacter).toString().replaceAll("[^0-9]", ""));
        Assertions.assertEquals(825, idCharacter);
        Allure.addAttachment("ID character", String.valueOf(idCharacter));
    }

    @Когда("получен ID последнего персонажа")
    public static void infoLastCharacters(int idCharacter){
        String characterId = Integer.toString(idCharacter);
        Response infoLastCharacter = given()
                .spec(reqSpecFirst)
                .when()
                .get("/character/" + characterId)
                .then()
                .extract()
                .response();
        charLastId = new JSONObject(infoLastCharacter.getBody().asString()).get("id").toString();
        charLastName = new JSONObject(infoLastCharacter.getBody().asString()).get("name").toString();
        charLastSpecies = new JSONObject(infoLastCharacter.getBody().asString()).get("species").toString();
        charLastLocation = new JSONObject(infoLastCharacter.getBody().asString()).getJSONObject("location")
                .get("name").toString();
        Assertions.assertEquals(charSpecies, charLastSpecies, "Расса персонажей");
        Assertions.assertEquals(charLocation, charLastLocation, "Местоположение персонажей");
        Allure.addAttachment("LastID", charLastId);
        Allure.addAttachment("LastName", charLastName);
        Allure.addAttachment("LastSpecies", charLastSpecies);
        Allure.addAttachment("LastLocation", charLastLocation);
    }
}