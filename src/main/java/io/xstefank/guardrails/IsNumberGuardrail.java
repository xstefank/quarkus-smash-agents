package io.xstefank.guardrails;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IsNumberGuardrail implements OutputGuardrail {

    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        try {
            int value = Integer.parseInt(responseFromLLM.text().trim());
            if (value < 1 || value > 10) {
                return retry("Output must be a number between 1 and 10. Please provide a single number in that range.");
            }
            return OutputGuardrailResult.success();
        } catch (NumberFormatException _) {
            return retry("Output is not a valid number. Please provide a single number without any additional text or explanation.");
        }
    }
}
