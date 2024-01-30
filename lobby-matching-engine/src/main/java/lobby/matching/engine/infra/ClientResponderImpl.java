package lobby.matching.engine.infra;

import lobby.protocol.codecs.*;
import lombok.RequiredArgsConstructor;
import org.agrona.ExpandableDirectByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class ClientResponderImpl implements ClientResponder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientResponderImpl.class);
    private final SessionContext context;
    private final ExpandableDirectByteBuffer buffer = new ExpandableDirectByteBuffer(1024);

    // Encoders
    private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    private final ExecutionReportEncoder executionReportEncoder = new ExecutionReportEncoder();
    private final MessageRejectionEncoder messageRejectionEncoder = new MessageRejectionEncoder();

    /**
     * {@inheritDoc}
     */
    @Override
    public void executionSuccess(final long lobbyId) {
        executionReportEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        executionReportEncoder.status(ExecutionStatus.SUCCESS);
        executionReportEncoder.lobbyId(lobbyId);

        context.reply(buffer,
                      0,
                      messageHeaderEncoder.encodedLength()
                      + executionReportEncoder.encodedLength());

        LOGGER.info("Sent execution success to {}", context.remoteAddress());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executionFailure(final ExecutionFailureReason reason) {
        executionReportEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        executionReportEncoder.status(ExecutionStatus.FAILURE);
        executionReportEncoder.failureReason(reason);
        executionReportEncoder.lobbyId(ExecutionReportEncoder.lobbyIdNullValue());

        context.reply(buffer,
                      0,
                      messageHeaderEncoder.encodedLength()
                      + executionReportEncoder.encodedLength());

        LOGGER.info("Sent execution failure to {}", context.remoteAddress());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rejectMessage(final MessageRejectionReason reason) {
        messageRejectionEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        messageRejectionEncoder.rejectionReason(reason);

        context.reply(buffer,
                      0,
                      messageHeaderEncoder.encodedLength()
                      + messageRejectionEncoder.encodedLength());

        LOGGER.info("Sent reject message to {}", context.remoteAddress());
    }
}
