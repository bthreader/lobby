package lobby.matching.engine.domain;

import lobby.protocol.MatchOptions;
import lobby.protocol.codecs.GameMode;

/**
 * A container for all the (mutable) state relating to lobbies.
 */
public interface Lobbies {
    /**
     * Attempt to fill a single lobby with a single user.
     *
     * @param matchOptions the criteria for the match
     */
    void joinLobbyIfMatch(long userId, MatchOptions matchOptions);

    /**
     * Attempt to put the users in the lobby with {@code lobbyId} into another lobby that matches
     * some criteria.
     *
     * @param lobbyId      the id of the lobby the users are currently in
     * @param matchOptions the criteria for the match
     */
    void mergeLobbyIfMatch(long lobbyId, MatchOptions matchOptions);

    /**
     * Creates a new empty lobby.
     *
     * @param gameMode the type of game to be played in the lobby
     */
    void createLobby(GameMode gameMode);

    /**
     * Removes a lobby, provided it's empty.
     *
     * @param lobbyId the id of the lobby
     */
    void deleteLobby(long lobbyId);
}
