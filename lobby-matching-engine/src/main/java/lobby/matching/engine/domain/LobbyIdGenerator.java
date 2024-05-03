package lobby.matching.engine.domain;

import org.agrona.concurrent.IdGenerator;

public class LobbyIdGenerator implements IdGenerator {
    public static final long FIRST_INDEX = 1;
    private long lobbyId = FIRST_INDEX;

    @Override
    public long nextId() {
        lobbyId++;
        return lobbyId - 1;
    }
}
