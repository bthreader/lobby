package lobby.gateway;

import lobby.protocol.codecs.ExecutionReportDecoder;
import lobby.protocol.codecs.ExecutionStatus;
import lobby.protocol.codecs.MessageHeaderDecoder;
import lobby.protocol.codecs.MessageRejectionDecoder;
import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demultiplexes messages from clients and sends then to the appropriate handler.
 */
public class IngressProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(IngressProcessor.class);

    // Decoders
    private final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private final ExecutionReportDecoder executionReportDecoder = new ExecutionReportDecoder();
    private final MessageRejectionDecoder messageRejectionDecoder = new MessageRejectionDecoder();

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
            return;
        }

        headerDecoder.wrap(buffer, offset);

        switch (headerDecoder.templateId()) {
            case ExecutionReportDecoder.TEMPLATE_ID -> logExecutionReport(buffer, offset);
            case MessageRejectionDecoder.TEMPLATE_ID -> logMessageRejection(buffer, offset);
            default -> {
                LOGGER.error("Unknown message template received: {}, rejected",
                             headerDecoder.templateId());
            }
        }
    }

    private void logExecutionReport(final DirectBuffer buffer, final int offset) {
        executionReportDecoder.wrapAndApplyHeader(buffer, offset, headerDecoder);
        if (executionReportDecoder.status() == ExecutionStatus.FAILURE) {
            LOGGER.info("Received execution report, execution failed with reason: {}",
                        executionReportDecoder.failureReason());
            return;
        }
        LOGGER.info("Received execution report, execution successful lobbyId: {}",
                    executionReportDecoder.lobbyId());
    }

    private void logMessageRejection(final DirectBuffer buffer, final int offset) {
        messageRejectionDecoder.wrapAndApplyHeader(buffer, offset, headerDecoder);
        LOGGER.info("Message rejected with reason: {}", messageRejectionDecoder.rejectionReason());
    }
}
