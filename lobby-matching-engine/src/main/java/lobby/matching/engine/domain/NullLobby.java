package lobby.matching.engine.domain;

public class NullLobby {
    private static final Lobby nullLobby = new Lobby(0);

    public static Lobby get() {
        return nullLobby;
    }
}
