package lobby.matching.engine;

import lobby.matching.engine.infra.MatchingEngineServer;

import java.io.IOException;

public class MatchingEngineApplication {
    public static void main(final String[] args) {
        try {
            final MatchingEngineServer server = new MatchingEngineServer(8080);
            server.run();
        } catch (final IOException e) {
        }
    }
}
