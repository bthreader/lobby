package lobby.matching.engine;

import lobby.message.codecs.GameMode;

/**
 * A container for all the (mutable) state relating to lobbies.
 */
public interface Lobbies {
    /**
     * TODO user ids in here?
     * <p>
     * Attempts to fill a single lobby with a single user.
     *
     * @param gameMode the type of game being requested
     */
    void joinLobbyIfMatch(final GameMode gameMode);

    /**
     * Attempts to put the users in the lobby with {@code lobbyId} into another lobby (with
     * {@code gameMode}) that contains at least one other user.
     *
     * @param lobbyId  the id of the lobby the users are currently in
     * @param gameMode the game mode of the lobby to merge into
     */
    void mergeLobbyIfMatch(final int lobbyId, final GameMode gameMode);

    /**
     * Creates a new empty lobby.
     *
     * @param gameMode the type of game to be played in the lobby
     */
    void createLobby(final GameMode gameMode);

    /**
     * Removes a lobby, provided it's empty.
     *
     * @param id the id of the lobby
     */
    void deleteLobby(final int id);
}
