package lobby.gateway.matching.engine;

import lobby.core.DeMuxer;
import lobby.protocol.codecs.ExecutionReportDecoder;
import lobby.protocol.codecs.ExecutionStatus;
import lobby.protocol.codecs.MessageHeaderDecoder;
import lobby.protocol.codecs.MessageRejectionDecoder;
import lombok.extern.slf4j.Slf4j;
import org.agrona.DirectBuffer;

/**
 * Demultiplexes messages from clients and sends then to the appropriate handler.
 */
@Slf4j
public class MatchingEngineResponseProcessor implements DeMuxer {
    // Decoders
    private final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private final ExecutionReportDecoder executionReportDecoder = new ExecutionReportDecoder();
    private final MessageRejectionDecoder messageRejectionDecoder = new MessageRejectionDecoder();

    @Override
    public void dispatch(final DirectBuffer buffer, final int offset, final int length) {
        if (length < MessageHeaderDecoder.ENCODED_LENGTH) {
            log.error("Message too short, rejected");
            return;
        }

        headerDecoder.wrap(buffer, offset);

        switch (headerDecoder.templateId()) {
            case ExecutionReportDecoder.TEMPLATE_ID -> logExecutionReport(buffer, offset);
            case MessageRejectionDecoder.TEMPLATE_ID -> logMessageRejection(buffer, offset);
            default -> {
                log.error("Unknown message template received: {}, rejected",
                          headerDecoder.templateId());
            }
        }
    }

    private void logExecutionReport(final DirectBuffer buffer, final int offset) {
        executionReportDecoder.wrapAndApplyHeader(buffer, offset, headerDecoder);
        if (executionReportDecoder.status() == ExecutionStatus.FAILURE) {
            log.info("Received execution report, execution failed with reason: {}",
                     executionReportDecoder.failureReason());
            return;
        }
        log.info("Received execution report, execution successful lobbyId: {}",
                 executionReportDecoder.lobbyId());
    }

    private void logMessageRejection(final DirectBuffer buffer, final int offset) {
        messageRejectionDecoder.wrapAndApplyHeader(buffer, offset, headerDecoder);
        log.info("Message rejected with reason: {}", messageRejectionDecoder.rejectionReason());
    }
}
