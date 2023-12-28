package lobby.matching.engine;

import java.util.List;
import java.util.function.Predicate;

public class LobbiesUtils {
    public static final int NO_MATCH = -1;

    /**
     * Performs a linear walk through the lobbies looking for a match based on sufficient free
     * spaces. If a match is found, the {@code slots} are taken from the lobby and the id of that
     * lobby is returned. Otherwise, -1 ({@code NO_MATCH}) is returned.
     *
     * @param slots   number of spaces required in the lobby
     * @param lobbies the lobbies to search through
     * @return the id of the lobby that the user(s) got allocated to, -1 ({@code NO_MATCH}) if zero
     * lobbies meeting the criteria were found.
     */
    public static int searchAndFill(final int slots, final List<Lobby> lobbies) {
        for (final Lobby lobby : lobbies) {
            if (lobby.getUsers() + slots <= Lobby.MAX_USERS) {
                lobby.addUsers(slots);
                return lobby.getId();
            }
        }
        return NO_MATCH;
    }

    /**
     * Performs a linear walk through the lobbies looking for a match based on sufficient free
     * spaces and a provided predicate. If a match is found, the {@code slots} are taken from the
     * lobby and the id of that lobby is returned. Otherwise, -1 ({@code NO_MATCH}) is returned.
     *
     * @param slots   number of spaces required in the lobby
     * @param lobbies the lobbies to search through
     * @return the id of the lobby that the user(s) got allocated to, -1 ({@code NO_MATCH}) if zero
     * lobbies meeting the criteria were found.
     */
    public static int searchAndFill(final int slots, final List<Lobby> lobbies,
                                    Predicate<Lobby> lobbyPredicate) {
        for (final Lobby lobby : lobbies) {
            if (lobby.getUsers() + slots <= Lobby.MAX_USERS && lobbyPredicate.test(lobby)) {
                lobby.addUsers(slots);
                return lobby.getId();
            }
        }
        return NO_MATCH;
    }
}
