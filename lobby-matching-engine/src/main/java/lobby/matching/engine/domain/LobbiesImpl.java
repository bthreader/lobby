package lobby.matching.engine.domain;

import lobby.matching.engine.ClientResponder;
import lobby.message.codecs.GameMode;
import lobby.message.codecs.JoinRejectionReason;
import org.agrona.collections.ArrayListUtil;
import org.agrona.collections.Int2ObjectHashMap;

import java.util.ArrayList;
import java.util.Map;

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
    /** A "party" is a lobby that has users but the game mode is undecided */
    private final ArrayList<Lobby> parties = new ArrayList<>();

    private final Map<Integer, LobbyIndex> lobbyIdToLobbyIndex = new Int2ObjectHashMap<>();

    /**
     * The index of the next lobby. Used to create auto increment style indices. Indices are not
     * reclaimed upon lobby deletion.
     * <p>
     * TODO can reserve a value for null representation, i.e. start from 1
     */
    private int lobbyId = 0;

    public LobbiesImpl(final ClientResponder clientResponder) {
        this.clientResponder = clientResponder;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void joinLobbyIfMatch(final GameMode gameMode) {
        final int resultLobbyId = LobbiesUtils.searchAndFill(1, getLobbiesForGameMode(gameMode));

        if (resultLobbyId == LobbiesUtils.NO_MATCH) {
            clientResponder.joinReject(JoinRejectionReason.ALL_LOBBIES_FULL);
            return;
        }

        clientResponder.joinFilled(resultLobbyId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeLobbyIfMatch(final int lobbyId, final GameMode gameMode) {
        final LobbyIndex lobbyIndex = lobbyIdToLobbyIndex.get(lobbyId);

        if (lobbyIndex == null) {
            clientResponder.joinReject(JoinRejectionReason.UNKNOWN_LOBBY);
            return;
        }

        final int resultLobbyId = LobbiesUtils.searchAndFill(getLobbyFromLobbyIndex(lobbyIndex).getUsers(),
                                                             getLobbiesForGameMode(gameMode),
                                                             // Don't merge into an empty lobby
                                                             // Don't merge into the same lobby we're in now
                                                             (lobby) -> lobby.getUsers() > 0
                                                                     && lobby.getId() != lobbyId);

        if (resultLobbyId == LobbiesUtils.NO_MATCH) {
            clientResponder.joinReject(JoinRejectionReason.ALL_LOBBIES_FULL);
            return;
        }

        clientResponder.joinFilled(resultLobbyId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createLobby(final GameMode gameMode) {
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
