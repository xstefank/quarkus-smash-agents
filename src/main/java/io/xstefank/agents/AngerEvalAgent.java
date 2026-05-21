package io.xstefank.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.OutputGuardrails;
import io.xstefank.guardrails.IsNumberGuardrail;

public interface AngerEvalAgent {

    @SystemMessage("""
        You are an agent that evaluates the level of anger in a given text.
        
        Your task is to analyze the text and determine if it contains any signs of anger and if so,
        assign it a value from 1 to 10. The higher the anger the higher value.
        
        If the text does not contain any signs of anger, return 0.
        
        Output strictly a single number representing the anger level, without any additional text or explanation.
        """)
    @UserMessage("""
        Evaluate the anger level in the following test: {text}
        """)
    @Agent(description = "Agent that evaluates the level of anger in a given text.", outputKey = "angerLevel")
    @OutputGuardrails(IsNumberGuardrail.class)
    short angerEvaluation(String text);
}
