package lobby.matching.engine.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Predicate;

public class LobbiesUtils {
    private static final SearchResult result = new SearchResult();

    /**
     * Performs a linear walk through the lobbies looking for a match based on a provided
     * predicate.
     *
     * @param lobbies        the lobbies to search through
     * @param lobbyPredicate only return a lobby that tests true
     * @return the lobby that the fulfills the space requirements, the null lobby otherwise
     */
    public static SearchResult search(final List<Lobby> lobbies,
                                      final Predicate<Lobby> lobbyPredicate) {
        for (int index = 0; index < lobbies.size(); index++) {
            final Lobby lobby = lobbies.get(index);
            if (lobbyPredicate.test(lobby)) {
                result.setLobby(lobby);
                result.setIndex(index);
                return result;
            }
        }
        return result.noMatch();
    }

    @Setter
    @Getter
    public static class SearchResult {
        private Lobby lobby;
        private int index;

        public boolean isNoMatch() {
            return index == -1;
        }

        public SearchResult noMatch() {
            index = -1;
            return this;
        }
    }
}
