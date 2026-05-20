package io.xstefank;

import io.quarkiverse.langchain4j.evaluation.junit5.Evaluate;
import io.quarkiverse.langchain4j.evaluation.junit5.SampleLocation;
import io.quarkiverse.langchain4j.evaluation.junit5.ScorerConfiguration;
import io.quarkiverse.langchain4j.testing.evaluation.EvaluationReport;
import io.quarkiverse.langchain4j.testing.evaluation.Parameters;
import io.quarkiverse.langchain4j.testing.evaluation.Samples;
import io.quarkiverse.langchain4j.testing.evaluation.Scorer;
import io.quarkiverse.langchain4j.testing.evaluation.similarity.SemanticSimilarityStrategy;
import io.quarkus.test.junit.QuarkusTest;
import io.xstefank.agents.AngerEvalWorkflow;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

import static io.quarkiverse.langchain4j.testing.evaluation.EvaluationAssertions.assertThat;

@QuarkusTest
@Evaluate
public class EvaluationTest {

    @Inject
    AngerEvalWorkflow angerEvalWorkflow;

    @Test
    void testAiService(
        @ScorerConfiguration(concurrency = 5) Scorer scorer,
        @SampleLocation("src/test/resources/samples.yaml") Samples<String> samples) throws IOException {

        Function<Parameters, String> function = parameters -> angerEvalWorkflow.evaluateAngerAndHulkOut(parameters.get(0)).toString();

        EvaluationReport<String> report = scorer.evaluate(samples, function,
            new SemanticSimilarityStrategy(0.4));
        report.saveAs(Path.of("target/semantic-similarity-report.md"), "markdown");
        assertThat(report)
            .hasAtLeastPassedEvaluations(2)
            .hasScoreGreaterThan(30);
    }
}
