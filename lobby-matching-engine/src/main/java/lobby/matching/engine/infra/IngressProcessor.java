package lobby.matching.engine.infra;

import lobby.core.DeMuxer;
import lobby.matching.engine.domain.Lobbies;
import lobby.matching.engine.domain.LobbiesImpl;
import lobby.protocol.codecs.MatchRequestDecoder;
import lobby.protocol.codecs.MergeRequestDecoder;
import lobby.protocol.codecs.MessageHeaderDecoder;
import lobby.protocol.codecs.MessageRejectionReason;
import lombok.extern.slf4j.Slf4j;
import org.agrona.DirectBuffer;

/**
 * Demultiplexes messages from clients and sends then to the appropriate handler.
 */
@Slf4j
public class IngressProcessor implements DeMuxer {
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

    @Override
    public void dispatch(final DirectBuffer buffer, final int offset, final int length) {
        if (length < MessageHeaderDecoder.ENCODED_LENGTH) {
            log.error("Message too short, rejected");
            clientResponder.rejectMessage(MessageRejectionReason.MESSAGE_TOO_SHORT);
            return;
        }

        headerDecoder.wrap(buffer, offset);

        switch (headerDecoder.templateId()) {
            case MatchRequestDecoder.TEMPLATE_ID -> match(buffer, offset);
            case MergeRequestDecoder.TEMPLATE_ID -> merge(buffer, offset);
            default -> {
                log.error("Unknown message template received: {}, rejected",
                          headerDecoder.templateId());
                clientResponder.rejectMessage(MessageRejectionReason.UNKNOWN_MESSAGE_TEMPLATE);
            }
        }
    }

    private void match(final DirectBuffer buffer, final int offset) {
        log.info("Received match request");
        matchRequestDecoder.wrapAndApplyHeader(buffer, offset, headerDecoder);
        matchOptions.wrap(matchRequestDecoder.matchOptions());
        lobbies.joinLobbyIfMatch(matchRequestDecoder.userId(), matchOptions);
    }

    private void merge(final DirectBuffer buffer, final int offset) {
        log.info("Received merge request");
        mergeRequestDecoder.wrapAndApplyHeader(buffer, offset, headerDecoder);
        matchOptions.wrap(mergeRequestDecoder.matchOptions());
        lobbies.mergeLobbyIfMatch(mergeRequestDecoder.lobbyId(), matchOptions);
    }
}
