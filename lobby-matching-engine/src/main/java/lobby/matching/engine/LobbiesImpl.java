package lobby.matching.engine;

import lobby.message.codecs.GameMode;
import lobby.message.codecs.MatchResultEncoder;
import lobby.message.codecs.MessageHeaderEncoder;
import org.agrona.collections.ArrayListUtil;
import org.agrona.collections.Int2ObjectHashMap;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;

public class LobbiesImpl implements Lobbies {
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
    private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    private final MatchResultEncoder matchResultEncoder = new MatchResultEncoder();

    /**
     * The index of the next lobby. Used to create auto increment style indices. Indices are not
     * reclaimed upon lobby deletion.
     */
    private int lobbyId = 0;


    /**
     * {@inheritDoc}
     */
    @Override
    public void joinLobbyIfMatch(final GameMode gameMode) {
        final int resultLobbyId = LobbiesUtils.searchAndFill(1, getLobbiesForGameMode(gameMode));

        if (resultLobbyId == LobbiesUtils.NO_MATCH) {
            // TODO Handle
            return;
        }

        // TODO put the below in a client responder
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(128);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);

        matchResultEncoder.wrapAndApplyHeader(directBuffer, 0, messageHeaderEncoder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeLobbyIfMatch(final int lobbyId, final GameMode gameMode) {
        final LobbyIndex lobbyIndex = lobbyIdToLobbyIndex.get(lobbyId);

        if (lobbyIndex == null) {
            // TODO handle
            return;
        }

        final int resultLobbyId = LobbiesUtils.searchAndFill(getLobbyFromLobbyIndex(lobbyIndex).getUsers(),
                                                             getLobbiesForGameMode(gameMode),
                                                             // Don't merge into an empty lobby
                                                             // Don't merge into the same lobby we're in now
                                                             (lobby) -> lobby.getUsers() > 0
                                                                     && lobby.getId() != lobbyId);

        if (resultLobbyId == LobbiesUtils.NO_MATCH) {
            // TODO handle
            // Send no match -> share response with joinLobbyIfMatch
        }

        // TODO handle
        // Send positive result
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
    public void deleteLobby(final int id) {
        final LobbyIndex lobbyIndex = lobbyIdToLobbyIndex.get(id);

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
