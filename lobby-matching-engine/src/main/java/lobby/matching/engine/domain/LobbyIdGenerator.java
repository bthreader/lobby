package lobby.matching.engine.domain;

import org.agrona.concurrent.IdGenerator;

public class LobbyIdGenerator implements IdGenerator {
    private long lobbyId = 1;

    @Override
    public long nextId() {
        lobbyId++;
        return lobbyId - 1;
    }
}
