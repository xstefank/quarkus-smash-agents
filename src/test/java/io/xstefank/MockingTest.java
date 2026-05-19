package io.xstefank;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.xstefank.agents.AngerEvalWorkflow;
import io.xstefank.agents.SmashSufficiencyAgent;
import io.xstefank.model.Smasher;
import io.xstefank.model.SmashingResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class MockingTest {

    @InjectMock
    AngerEvalWorkflow angerEvalWorkflowMock;

    @BeforeEach
    public void setup() {
        SmashingResponse smashingResponse = new SmashingResponse(Smasher.HULK, "The smash is sufficient to break the wall.", "<div>Mocked HTML</div>");
        Mockito.when(angerEvalWorkflowMock.evaluateAngerAndHulkOut(Mockito.anyString()))
            .thenReturn(smashingResponse);
    }

    @Test
    void testSmashingWithMockito() {
        String result = given().contentType(ContentType.MULTIPART)
                .multiPart("angerText", "I am so angry!")
                .when()
                .post("/anger")
                .then()
                .statusCode(200)
                .extract().asString();

        Assertions.assertEquals("<div>Mocked HTML</div>", result);
    }

}
