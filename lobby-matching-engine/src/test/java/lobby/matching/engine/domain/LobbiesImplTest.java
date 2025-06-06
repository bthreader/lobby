package lobby.matching.engine.domain;

import lobby.matching.engine.infra.ClientResponder;
import lobby.protocol.ImmutableMatchOptions;
import lobby.protocol.codecs.ExecutionFailureReason;
import lobby.protocol.codecs.GameMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LobbiesImplTest {
    private LobbiesImpl lobbies;
    @Mock
    private ClientResponder clientResponderMock;

    @BeforeEach
    void beforeEach() {
        lobbies = new LobbiesImpl(clientResponderMock);
    }

    @Nested
    class lobbies {
        @Test
        void no_lobbies_on_start() {
            lobbies.joinLobbyIfMatch(1L, new ImmutableMatchOptions(GameMode.CAPTURE_THE_FLAG));

            verify(clientResponderMock).executionFailure(ExecutionFailureReason.ALL_LOBBIES_FULL);
        }

        @Test
        void createLobby() {
            lobbies.createLobby(GameMode.CAPTURE_THE_FLAG);

            verify(clientResponderMock).executionSuccess(LobbyIdGenerator.FIRST_INDEX);
        }

        @Test
        void deleteLobby() {
            lobbies.createLobby(GameMode.CAPTURE_THE_FLAG);
            lobbies.deleteLobby(LobbyIdGenerator.FIRST_INDEX);
            verify(clientResponderMock, times(2))
                    .executionSuccess(LobbyIdGenerator.FIRST_INDEX);
        }
    }

    @Nested
    class joinLobbyIfMatch {
        @Test
        void success() {
            // Given a game mode
            final GameMode gameMode = GameMode.CAPTURE_THE_FLAG;

            // Given one matching lobby with space
            lobbies.createLobby(gameMode);

            // When we request to match
            lobbies.joinLobbyIfMatch(1L, new ImmutableMatchOptions(gameMode));

            // Then the client should be informed that there was a match
            verify(clientResponderMock).executionSuccess(1L);
        }
    }
}
