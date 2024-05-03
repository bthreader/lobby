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
     * Creates a new lobby with one user identified by {@code userId}.
     *
     * @param gameMode the type of game to be played in the lobby
     * @param userId   the id of the user place in the lobby
     */
    void createLobby(GameMode gameMode, long userId);

    /**
     * Removes a lobby, provided it's empty.
     *
     * @param lobbyId the id of the lobby
     */
    void deleteLobby(long lobbyId);

    /**
     * Finds the lobby of the user with the given {@code userId}.
     *
     * @param userId the id of the user to search for
     */
    void locateUser(long userId);
}
