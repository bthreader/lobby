package lobby.matching.engine.domain;

import lobby.matching.engine.infra.ClientResponder;
import lobby.protocol.MatchOptions;
import lobby.protocol.codecs.ExecutionFailureReason;
import lobby.protocol.codecs.GameMode;
import org.agrona.collections.ArrayListUtil;
import org.agrona.collections.Int2ObjectHashMap;

import java.util.ArrayList;
import java.util.Map;

/**
 * A container for all the (mutable) state relating to lobbies.
 */
public class LobbiesImpl implements Lobbies {
    private final ClientResponder clientResponder;

    /**
     * TODO could perform some bid ask type optimizations by storing the pointer
     * to the first index with a free space.
     * <p>
     * Could also vary by size, rather than all being vanilla Lobby
     * halo.fandom.com/wiki/Category:Playlists
     */
    private final ArrayList<Lobby> captureTheFlagLobbies = new ArrayList<>();
    private final ArrayList<Lobby> freeForAllLobbies = new ArrayList<>();
    /**
     * A "party" is a special case of lobby which will never play a game.
     * <p>
     * It serves as a holding place for players before their party gets merged with a non-party
     * lobby.
     */
    private final ArrayList<Lobby> parties = new ArrayList<>();

    private final Map<Integer, LobbyIndex> lobbyIdToLobbyIndex = new Int2ObjectHashMap<>();

    /**
     * The index of the next lobby. Used to create auto increment style indices. Indices are not
     * reclaimed upon lobby deletion.
     * <p>
     * TODO can reserve a value for null representation, i.e. start from 1.
     */
    private int lobbyId = 0;

    /**
     * Creates a container for all the (mutable) state relating to lobbies.
     *
     * @param clientResponder the responder used to send results back to the client
     */
    public LobbiesImpl(final ClientResponder clientResponder) {
        this.clientResponder = clientResponder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void joinLobbyIfMatch(final MatchOptions matchOptions) {
        final int resultLobbyId = LobbiesUtils.searchAndFill(1,
                                                             getLobbiesForGameMode(matchOptions.gameMode()));

        if (resultLobbyId == LobbiesUtils.NO_MATCH) {
            clientResponder.executionFailure(ExecutionFailureReason.ALL_LOBBIES_FULL);
            return;
        }

        clientResponder.executionSuccess(resultLobbyId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeLobbyIfMatch(final int lobbyId, final MatchOptions matchOptions) {
        final LobbyIndex lobbyIndex = lobbyIdToLobbyIndex.get(lobbyId);

        if (lobbyIndex == null) {
            clientResponder.executionFailure(ExecutionFailureReason.UNKNOWN_LOBBY);
            return;
        }

        final int resultLobbyId = LobbiesUtils.searchAndFill(getLobbyFromLobbyIndex(lobbyIndex).getUsers(),
                                                             getLobbiesForGameMode(matchOptions.gameMode()),
                                                             // Don't merge into an empty lobby
                                                             // Don't merge into the same lobby we're in now
                                                             (lobby) -> lobby.getUsers() > 0
                                                                     && lobby.getId() != lobbyId);

        if (resultLobbyId == LobbiesUtils.NO_MATCH) {
            clientResponder.executionFailure(ExecutionFailureReason.ALL_LOBBIES_FULL);
            return;
        }

        clientResponder.executionSuccess(resultLobbyId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createLobby(final GameMode gameMode) {
        // TODO some limit on amount of lobbies you can create base on uint32
        getLobbiesForGameMode(gameMode).add(new Lobby(lobbyId));
        final int arrayIndex = getLobbiesForGameMode(gameMode).size() - 1;
        lobbyIdToLobbyIndex.put(lobbyId, new LobbyIndex(gameMode, arrayIndex));
        lobbyId++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLobby(final int lobbyId) {
        final LobbyIndex lobbyIndex = lobbyIdToLobbyIndex.get(lobbyId);

        if (lobbyIndex == null) {
            // TODO handle
            // Can't delete non-existent lobby
            return;
        }

        if (getLobbyFromLobbyIndex(lobbyIndex).getUsers() > 0) {
            // TODO handle
            // Can't delete a non-empty lobby
            return;
        }

        // Remove
        ArrayListUtil.fastUnorderedRemove(getLobbiesForGameMode(lobbyIndex.gameMode()),
                                          lobbyIndex.arrayIndex());
    }

    private ArrayList<Lobby> getLobbiesForGameMode(final GameMode gameMode) {
        return switch (gameMode) {
            case CAPTURE_THE_FLAG -> captureTheFlagLobbies;
            case FREE_FOR_ALL -> freeForAllLobbies;
            case NULL_VAL -> parties;
        };
    }

    private Lobby getLobbyFromLobbyIndex(final LobbyIndex lobbyIndex) {
        return getLobbiesForGameMode(lobbyIndex.gameMode()).get(lobbyIndex.arrayIndex());
    }
}
