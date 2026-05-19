package io.xstefank;

import dev.langchain4j.model.chat.ChatModel;
import io.quarkiverse.langchain4j.evaluation.junit5.Evaluate;
import io.quarkiverse.langchain4j.evaluation.junit5.SampleLocation;
import io.quarkiverse.langchain4j.evaluation.junit5.ScorerConfiguration;
import io.quarkiverse.langchain4j.testing.evaluation.EvaluationReport;
import io.quarkiverse.langchain4j.testing.evaluation.Parameters;
import io.quarkiverse.langchain4j.testing.evaluation.Samples;
import io.quarkiverse.langchain4j.testing.evaluation.Scorer;
import io.quarkiverse.langchain4j.testing.evaluation.judge.AiJudgeStrategy;
import io.quarkus.test.junit.QuarkusTest;
import io.xstefank.agents.AngerEvalWorkflow;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static io.quarkiverse.langchain4j.testing.evaluation.EvaluationAssertions.assertThat;

@QuarkusTest
@Evaluate
public class EvaluationWithAiJudgeTest {

    @Inject
    AngerEvalWorkflow angerEvalWorkflow;

    @Inject
    ChatModel judge;

    @Test
    void testAiService(
        @ScorerConfiguration(concurrency = 5) Scorer scorer,
        @SampleLocation("src/test/resources/samples.yaml") Samples<String> samples) {

        Function<Parameters, String> function = parameters -> angerEvalWorkflow.evaluateAngerAndHulkOut(parameters.get(0)).toString();

        String prompt = """
            You are an AI judge evaluating a SmashingResponse against an expected description.
            The actual response has the format: SmashingResponse[smasher=<CHARACTER>, response=<TEXT>, html=<HTML>]

            Evaluate whether the actual response satisfies the expected description:
            1. Does the correct character (HULK or DR_BANNER) respond?
            2. Is the response tone and content semantically appropriate for the description?
            Ignore all HTML completely.

            Expected description: {expected_output}
            Actual response: {response}

            Respond with exactly one word — true if the response matches, false if it does not.
            """;

        EvaluationReport report = scorer.evaluate(samples, function,
            new AiJudgeStrategy(judge, prompt));
        assertThat(report)
            .hasAtLeastPassedEvaluations(2)
            .hasScoreGreaterThan(30);
    }
}
