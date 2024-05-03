package lobby.matching.engine.domain;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Predicate;

@UtilityClass
public class LobbiesUtils {
    private static final LobbySearchResult searchResult = LobbySearchResult.INSTANCE;

    /**
     * Performs a linear walk through the lobbies looking for a match based on a provided predicate
     *
     * @param lobbies        the lobbies to search through
     * @param lobbyPredicate only return a lobby that tests true
     * @return the lobby that the fulfills the space requirements, the null lobby otherwise
     */
    public static LobbySearchResult search(final List<Lobby> lobbies,
                                           final Predicate<Lobby> lobbyPredicate) {
        for (int index = 0; index < lobbies.size(); index++) {
            final Lobby lobby = lobbies.get(index);
            if (lobbyPredicate.test(lobby)) {
                return searchResult.matchOf(lobby, index);
            }
        }
        return searchResult.noMatch();
    }
}
