package io.xstefank.exception;

import io.quarkus.logging.Log;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SmashExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException e) {
        Log.errorf(e, "Smash processing failed: %s", e.getMessage());
        return Response.serverError()
                .type(MediaType.TEXT_HTML)
                .entity(errorHtml(e.getMessage()))
                .build();
    }

    private String errorHtml(String message) {
        return """
                <div class="result-card hulk-result" style="border-color:var(--red);box-shadow:0 0 40px rgba(239,68,68,0.25)">
                    <div class="result-header" style="color:var(--red)">SMASHING FAILED. HULK SAD.</div>
                    <svg class="char-svg" viewBox="0 0 80 110" style="filter:drop-shadow(0 0 18px rgba(239,68,68,0.5)) grayscale(0.4)"><use href="#symbol-hulk"/></svg>
                    <p class="result-text">Hulk tried to smash. Hulk could not smash. Hulk sad.</p>
                    <p style="font-size:0.75rem;color:var(--muted);margin-top:0.6rem">%s</p>
                    <button class="close-btn" onclick="closeModal()">&#x2715; Close</button>
                </div>
                """.formatted(message != null ? message : "Unknown error");
    }
}
