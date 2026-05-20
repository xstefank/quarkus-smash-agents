package io.xstefank;

import dev.langchain4j.model.chat.ChatModel;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.xstefank.util.JudgeModelAssertions;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class JudgeTest {

    @Inject
    ChatModel judge;

    @Test
    void testJudge() {
        String result = given().contentType(ContentType.MULTIPART)
            .multiPart("angerText", "I am so angry with slow Maven builds!")
            .when()
            .post("/anger")
            .then()
            .statusCode(200)
            .extract().asString();

        JudgeModelAssertions.with(judge)
            .assertThat(result)
            .satisfies("The text is an HTML. Don't check validity.")
            .satisfies("The text is about smashing.")
            .satisfies("It mentions the Hulk.");
    }
}
