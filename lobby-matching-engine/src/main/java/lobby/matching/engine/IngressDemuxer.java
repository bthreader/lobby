package lobby.matching.engine;

import lobby.message.codecs.GameMode;
import lobby.message.codecs.MatchDecoder;
import lobby.message.codecs.MessageHeaderDecoder;
import org.agrona.DirectBuffer;

/**
 * Demultiplexes messages from the ingress stream to the appropriate handler.
 */
public class IngressDemuxer {
    private final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private final MatchDecoder matchDecoder = new MatchDecoder();
    private final Lobbies lobbies = new LobbiesImpl();

    public void dispatch(final DirectBuffer buffer, final int offset, final int length) {
        if (length < MessageHeaderDecoder.ENCODED_LENGTH) {
            // LOGGER.error("Message too short, ignored.");
            return;
        }
        headerDecoder.wrap(buffer, offset);

        switch (headerDecoder.templateId()) {
            case MatchDecoder.TEMPLATE_ID -> {
                lobbies.joinLobbyIfMatch(GameMode.CAPTURE_THE_FLAG);
            }
            default -> {
            }// LOGGER.error("Unknown message template {}, ignored.", headerDecoder.templateId());
        }
    }
}
