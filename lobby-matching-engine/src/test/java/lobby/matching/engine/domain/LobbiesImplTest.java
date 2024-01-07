package lobby.matching.engine.domain;

import lobby.matching.engine.domain.utils.ImmutableMatchOptions;
import lobby.matching.engine.infra.ClientResponder;
import lobby.protocol.codecs.ExecutionFailureReason;
import lobby.protocol.codecs.GameMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
            lobbies.joinLobbyIfMatch(new ImmutableMatchOptions(GameMode.CAPTURE_THE_FLAG));

            verify(clientResponderMock).executionFailure(ExecutionFailureReason.ALL_LOBBIES_FULL);
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
            lobbies.joinLobbyIfMatch(new ImmutableMatchOptions(gameMode));

            // Then the client should be informed that there was a match
            verify(clientResponderMock).executionSuccess(0);
        }
    }
}
