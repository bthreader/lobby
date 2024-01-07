package lobby.matching.engine.infra;

import lobby.protocol.codecs.*;
import lombok.RequiredArgsConstructor;
import org.agrona.ExpandableDirectByteBuffer;

@RequiredArgsConstructor
public class ClientResponderImpl implements ClientResponder {
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
    public void executionSuccess(final int lobbyId) {
        executionReportEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        executionReportEncoder.status(ExecutionStatus.SUCCESS);
        executionReportEncoder.lobbyId(lobbyId);

        context.reply(buffer,
                      0,
                      messageHeaderEncoder.encodedLength()
                              + executionReportEncoder.encodedLength());
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
    }
}
