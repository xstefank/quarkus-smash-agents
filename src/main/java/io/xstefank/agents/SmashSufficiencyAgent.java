package io.xstefank.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface SmashSufficiencyAgent {

    @SystemMessage("""
        You are an agent that evaluates the level of smashing satisfaction in the provided responses
        from Dr. Banner and The Hulk. Depending on who of them has a better smashing response,
        you will choose the winner and summarize his response in one sentence.
        
        The smashing satisfaction is evaluated based on the following criteria:
          - Creativity: How original and imaginative the smashing response is.
          - Humor: How funny and entertaining the smashing response is.
          - Relevance: How well the smashing response addresses the user's request.

        Take into account the provided analyzed anger level: {angerLevel}.
        If the anger level is too low (<= 3) prefer Dr. Banner.
        If the anger level is too high (>= 7) prefer The Hulk.
        
        Your output should be a single sentence summarizing the winning smashing response, without mentioning the name of the winner or any additional text
        prepended by either DR_BANNER or HULK to indicate whose response is being summarized.
        """)
    @UserMessage("""
        Compare the following smashing responses from Dr. Banner and The Hulk, and summarize the winning response in one sentence:
        
        Dr. Banner's response: {bannerAssessment}
        
        The Hulk's response: {hulkAssessment}
        """)
    @Agent(description = "Agent that evaluates the level of smashing satisfaction in the provided responses from Dr. Banner and The Hulk, and summarizes the winning response in one sentence.", outputKey = "smashSummary")
    String summarizeSmashingResponse(String bannerAssessment, String hulkAssessment, short angerLevel);
}
