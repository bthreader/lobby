package lobby.gateway.matching.engine;

import lobby.protocol.MatchOptions;
import lobby.protocol.codecs.MatchRequestEncoder;
import lobby.protocol.codecs.MergeRequestEncoder;
import lobby.protocol.codecs.MessageHeaderEncoder;
import lombok.extern.slf4j.Slf4j;
import org.agrona.ExpandableDirectByteBuffer;
import org.agrona.MutableDirectBuffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

@Slf4j
public class MatchingEngineClientImpl implements MatchingEngineClient {
    private final SocketChannel client;
    private final MutableDirectBuffer buffer = new ExpandableDirectByteBuffer(1024);
    private final MatchingEngineResponseProcessor matchingEngineResponseProcessor = new MatchingEngineResponseProcessor();

    // Encoders
    private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    private final MatchRequestEncoder matchRequestEncoder = new MatchRequestEncoder();
    private final MergeRequestEncoder mergeRequestEncoder = new MergeRequestEncoder();

    public MatchingEngineClientImpl() {
        try {
            final SocketAddress address = new InetSocketAddress(8080);
            client = SocketChannel.open(address);
            client.configureBlocking(true);
            log.info("Successfully connected to matching engine");
        } catch (final IOException e) {
            log.info("Could not connect to the matching engine: {}", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void matchRequest(final MatchOptions matchOptions) {
        matchRequestEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        matchRequestEncoder.matchOptions().gameMode(matchOptions.gameMode());

        try {
            client.write(buffer.byteBuffer()
                                 .position(0)
                                 .limit(MessageHeaderEncoder.ENCODED_LENGTH
                                        + matchRequestEncoder.encodedLength()));
            log.info("Sent match request to matching engine, match options: {}",
                     matchOptions);
        } catch (final IOException e) {
            log.info("Error sending a match request, error: {}", e.toString());
        }

        try {
            client.read(buffer.byteBuffer().rewind());
            matchingEngineResponseProcessor.dispatch(buffer, 0, buffer.capacity());
        } catch (final IOException e) {
            log.info("Could not read from the matching engine");
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeRequest(final int lobbyId, final MatchOptions matchOptions) {
        mergeRequestEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        mergeRequestEncoder.lobbyId(lobbyId);
        mergeRequestEncoder.matchOptions().gameMode(matchOptions.gameMode());

        try {
            client.write(buffer.byteBuffer()
                                 .position(0)
                                 .limit(MessageHeaderEncoder.ENCODED_LENGTH
                                        + matchRequestEncoder.encodedLength()));
            log.info("Sent match request to matching engine, match options: {}",
                     matchOptions);
        } catch (final IOException e) {
            log.info("Error sending a match request, error: {}", e.toString());
        }

        try {
            client.read(buffer.byteBuffer().rewind());
            matchingEngineResponseProcessor.dispatch(buffer, 0, buffer.capacity());
        } catch (final IOException e) {
            log.info("Could not read from the matching engine");
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        try {
            client.close();
        } catch (final IOException e) {
            log.error("Error shutting down client: {}", e.toString());
            throw new RuntimeException(e);
        }
    }
}
