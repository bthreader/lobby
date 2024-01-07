package lobby.matching.engine.infra;

import lobby.matching.engine.domain.Lobbies;
import lobby.matching.engine.domain.LobbiesImpl;
import lobby.protocol.codecs.MatchRequestDecoder;
import lobby.protocol.codecs.MergeRequestDecoder;
import lobby.protocol.codecs.MessageHeaderDecoder;
import lobby.protocol.codecs.MessageRejectionReason;
import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demultiplexes messages from the clients to the appropriate handler.
 */
public class IngressProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(IngressProcessor.class);
    private final ClientResponder clientResponder;
    private final Lobbies lobbies;

    // Decoders
    private final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private final MatchRequestDecoder matchRequestDecoder = new MatchRequestDecoder();
    private final MergeRequestDecoder mergeRequestDecoder = new MergeRequestDecoder();
    private final MatchOptionsDecoderWrapper matchOptions = new MatchOptionsDecoderWrapper();

    public IngressProcessor(final ClientResponder clientResponder) {
        this.clientResponder = clientResponder;
        this.lobbies = new LobbiesImpl(clientResponder);
    }

    /**
     * Takes a message contained within a buffer and sends it to the appropriate handler.
     *
     * @param buffer the buffer containing the message
     * @param offset in the supplied buffer to begin decoding
     * @param length of the supplied buffer
     */
    public void dispatch(final DirectBuffer buffer, final int offset, final int length) {
        if (length < MessageHeaderDecoder.ENCODED_LENGTH) {
            LOGGER.error("Message too short, rejected.");
            clientResponder.rejectMessage(MessageRejectionReason.MESSAGE_TOO_SHORT);
            return;
        }

        headerDecoder.wrap(buffer, offset);

        switch (headerDecoder.templateId()) {
            case MatchRequestDecoder.TEMPLATE_ID -> match(buffer, offset);
            case MergeRequestDecoder.TEMPLATE_ID -> merge(buffer, offset);
            default -> {
                LOGGER.error("Unknown message template {}, rejected.", headerDecoder.templateId());
                clientResponder.rejectMessage(MessageRejectionReason.UNKNOWN_MESSAGE_TEMPLATE);
            }
        }
    }

    private void match(final DirectBuffer buffer, final int offset) {
        matchRequestDecoder.wrapAndApplyHeader(buffer, offset, headerDecoder);
        matchOptions.wrap(matchRequestDecoder.matchOptions());
        lobbies.joinLobbyIfMatch(matchOptions);
    }

    private void merge(final DirectBuffer buffer, final int offset) {
        mergeRequestDecoder.wrapAndApplyHeader(buffer, offset, headerDecoder);
        matchOptions.wrap(mergeRequestDecoder.matchOptions());
        lobbies.mergeLobbyIfMatch(1, // mergeRequestDecoder.lobbyId() TODO,
                                  matchOptions);
    }
}
