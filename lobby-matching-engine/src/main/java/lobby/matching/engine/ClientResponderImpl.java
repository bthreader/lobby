package lobby.matching.engine;

import lobby.message.codecs.*;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class ClientResponderImpl implements ClientResponder {
    private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    private final ExecutionReportEncoder executionReportEncoder = new ExecutionReportEncoder();
    private final RejectionEncoder rejectionEncoder = new RejectionEncoder();

    @Override
    public void joinFilled(final int lobbyId) {
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(128); // TODO 128?
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);
        executionReportEncoder.wrapAndApplyHeader(directBuffer, 0, messageHeaderEncoder);
        executionReportEncoder.status(JoinStatus.FILLED);
        executionReportEncoder.lobbyId(lobbyId);
    }

    @Override
    public void joinReject(final JoinRejectionReason reason) {
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(128); // TODO 128?
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);
        executionReportEncoder.wrapAndApplyHeader(directBuffer, 0, messageHeaderEncoder);
        executionReportEncoder.status(JoinStatus.REJECTED);
        executionReportEncoder.rejectionReason(reason);
        executionReportEncoder.lobbyId(0); // TODO maybe make 0 reserved for negative ack?
    }

    @Override
    public void reject(final SessionRejectionReason reason) {
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(128); // TODO 128?
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);
        rejectionEncoder.wrapAndApplyHeader(directBuffer, 0, messageHeaderEncoder);
        rejectionEncoder.rejectionReason(reason);
    }
}
