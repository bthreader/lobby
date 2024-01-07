package lobby.matching.engine;

import lobby.matching.engine.infra.Server;

public class App {
    public static void main(final String[] args) {
        final Server server = new Server();
        server.run();
    }
}
