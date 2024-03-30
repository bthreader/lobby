package lobby.matching.engine.domain;

import lobby.matching.engine.infra.ClientResponder;
import lobby.protocol.MatchOptions;
import lobby.protocol.codecs.ExecutionFailureReason;
import lobby.protocol.codecs.GameMode;
import org.agrona.collections.ArrayListUtil;
import org.agrona.collections.Long2ObjectHashMap;

import java.util.ArrayList;
import java.util.Map;

/**
 * A container for all the (mutable) state relating to lobbies.
 */
public class LobbiesImpl implements Lobbies {
    private final ClientResponder clientResponder;

    /** TODO could perform some bid ask type optimizations */
    private final ArrayList<Lobby> lobbies = new ArrayList<>(20);
    private final Map<Long, Integer> lobbyIdToLobbyIndex = new Long2ObjectHashMap<>();
    private final Map<Long, Integer> userIdToLobbyIndex = new Long2ObjectHashMap<>();
    private final LobbyIdGenerator lobbyIdGenerator = new LobbyIdGenerator();

    public LobbiesImpl(final ClientResponder clientResponder) {
        this.clientResponder = clientResponder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void joinLobbyIfMatch(final long userId, final MatchOptions matchOptions) {
        final LobbySearchResult result = LobbiesUtils.search(lobbies,
                                                             lobby -> lobby.isNotFull()
                                                                      && lobby.matches(matchOptions));

        if (result.isNoMatch()) {
            clientResponder.executionFailure(ExecutionFailureReason.ALL_LOBBIES_FULL);
            return;
        }

        // TODO what if the user is already in a lobby?
        result.getLobby().addUser(userId);
        userIdToLobbyIndex.put(userId, result.getIndex());
        clientResponder.executionSuccess(result.getLobby().getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeLobbyIfMatch(final long lobbyId, final MatchOptions matchOptions) {
        final Integer lobbyIndex = lobbyIdToLobbyIndex.get(lobbyId);

        if (lobbyIndex == null) {
            clientResponder.executionFailure(ExecutionFailureReason.UNKNOWN_LOBBY);
            return;
        }

        final LobbySearchResult result = LobbiesUtils.search(lobbies, lobby ->
                lobby.isNotEmpty()
                && lobby.getId() != lobbyId
                && lobby.matches(matchOptions));

        if (result.isNoMatch()) {
            clientResponder.executionFailure(ExecutionFailureReason.ALL_LOBBIES_FULL);
            return;
        }

        // Merge
        final Lobby oldLobby = lobbies.get(result.getIndex());
        result.getLobby().merge(oldLobby);
        for (final long userId : oldLobby.getUsers()) {
            userIdToLobbyIndex.replace(userId, result.getIndex());
        }
        clientResponder.executionSuccess(result.getLobby().getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createLobby(final GameMode gameMode) {
        final long lobbyId = lobbyIdGenerator.nextId();
        lobbies.add(new Lobby(lobbyId, gameMode));
        lobbyIdToLobbyIndex.put(lobbyId, lobbies.size() - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createLobby(final GameMode gameMode, final long userId) {
        final long lobbyId = lobbyIdGenerator.nextId();
        final Lobby lobby = new Lobby(lobbyId, gameMode);
        lobbies.add(lobby);
        lobbyIdToLobbyIndex.put(lobbyId, lobbies.size() - 1);

        // Quit the lobby the user is currently if they are in one
        final Integer existingLobbyIndex = userIdToLobbyIndex.get(userId);
        if (existingLobbyIndex != null) {
            lobbies.get(existingLobbyIndex).removeUser(userId);
        }

        // Add the user to the new lobby
        lobby.addUser(userId);
        userIdToLobbyIndex.put(userId, 111);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLobby(final long lobbyId) {
        final Integer lobbyIndex = lobbyIdToLobbyIndex.get(lobbyId);

        if (lobbyIndex == null) {
            // TODO handle
            // Can't delete non-existent lobby
            return;
        }

        if (lobbies.get(lobbyIndex).isNotEmpty()) {
            // TODO handle
            // Can't delete a non-empty lobby
            return;
        }

        // Remove
        ArrayListUtil.fastUnorderedRemove(lobbies, lobbyIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void locateUser(final long userId) {
        final Integer lobbyIndex = userIdToLobbyIndex.get(userId);

        if (lobbyIndex == null) {
            //
            return;
        }
        final long result = lobbies.get(lobbyIndex).getId();
    }
}
