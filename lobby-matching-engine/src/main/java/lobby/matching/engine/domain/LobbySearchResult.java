package lobby.matching.engine.domain;

import lombok.Getter;

@Getter

public class LobbySearchResult {
    public static LobbySearchResult INSTANCE = new LobbySearchResult();
    private Lobby lobby;
    private int index;

    private LobbySearchResult() { }

    public LobbySearchResult matchOf(final Lobby lobby, final int index) {
        this.lobby = lobby;
        this.index = index;
        return this;
    }

    public boolean isNoMatch() {
        return index == -1;
    }

    public LobbySearchResult noMatch() {
        index = -1;
        return this;
    }
}
