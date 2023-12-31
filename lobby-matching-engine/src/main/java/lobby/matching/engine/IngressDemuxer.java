package lobby.matching.engine;

import lobby.matching.engine.domain.Lobbies;
import lobby.matching.engine.domain.LobbiesImpl;
import lobby.message.codecs.*;
import org.agrona.DirectBuffer;

/**
 * Demultiplexes messages from the ingress stream to the appropriate handler.
 */
public class IngressDemuxer {
    private final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private final ClientResponder clientResponder;

    private final Lobbies lobbies;

    public IngressDemuxer(final ClientResponder clientResponder) {
        this.clientResponder = clientResponder;
        this.lobbies = new LobbiesImpl(clientResponder);
    }

    public void dispatch(final DirectBuffer buffer, final int offset, final int length) {
        if (length < MessageHeaderDecoder.ENCODED_LENGTH) {
            // LOGGER.error("Message too short, ignored.");
            // TODO is this worth the effort or should I just return?
            clientResponder.reject(SessionRejectionReason.MESSAGE_TOO_SHORT);
            return;
        }

        headerDecoder.wrap(buffer, offset);

        switch (headerDecoder.templateId()) {
            case MatchRequestDecoder.TEMPLATE_ID -> {
                lobbies.joinLobbyIfMatch(GameMode.CAPTURE_THE_FLAG);
            }
            case MergeRequestDecoder.TEMPLATE_ID -> {
                lobbies.mergeLobbyIfMatch(1, GameMode.CAPTURE_THE_FLAG);
            }
            default -> {
                // LOGGER.error("Unknown message template {}, ignored.", headerDecoder.templateId());
                clientResponder.reject(SessionRejectionReason.UNKNOWN_MESSAGE_TEMPLATE);
            }
        }
    }
}
