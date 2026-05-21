package io.xstefank.guardrail;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.test.guardrail.GuardrailAssertions;
import io.quarkus.test.junit.QuarkusTest;
import io.xstefank.guardrails.IsNumberGuardrail;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
public class IsNumberGuardrailTest {

    @Inject
    IsNumberGuardrail isNumberGuardrail;

    @Test
    void testNumberSuccess() {
        GuardrailAssertions.assertThat(isNumberGuardrail.validate(AiMessage.from("5")))
            .isSuccessful();
    }

    @Test
    void testNumberOutOfRange() {
        GuardrailAssertions.assertThat(isNumberGuardrail.validate(AiMessage.from("15")))
            .hasResult(GuardrailResult.Result.FATAL)
            .hasSingleFailureWithMessage("Output must be a number between 1 and 10. Please provide a single number in that range.");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "this is not a number",
        "Forty two",
        "3/14",
    })
    void testNumberInvalid(String output) {
        GuardrailAssertions.assertThat(isNumberGuardrail.validate(AiMessage.from(output)))
            .hasResult(GuardrailResult.Result.FATAL)
            .hasSingleFailureWithMessage("Output is not a valid number. Please provide a single number without any additional text or explanation.");
    }
}
