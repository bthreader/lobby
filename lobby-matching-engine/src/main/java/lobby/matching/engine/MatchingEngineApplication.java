package lobby.matching.engine;

import lobby.matching.engine.infra.MatchingEngineServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class MatchingEngineApplication {
    public static void main(final String[] args) {
        try {
            final MatchingEngineServer server = new MatchingEngineServer(8080);
            server.run();
        } catch (final IOException e) {
            log.info("Terminated with IO exception: {}", e.getMessage());
        }
    }
}
