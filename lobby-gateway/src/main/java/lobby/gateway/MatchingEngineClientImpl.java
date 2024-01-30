package lobby.gateway;

import lobby.protocol.MatchOptions;
import lobby.protocol.codecs.MatchRequestEncoder;
import lobby.protocol.codecs.MessageHeaderEncoder;
import org.agrona.ExpandableDirectByteBuffer;
import org.agrona.MutableDirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class MatchingEngineClientImpl implements MatchingEngineClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingEngineClientImpl.class);
    private final SocketChannel client;
    private final MatchRequestEncoder matchRequestEncoder = new MatchRequestEncoder();
    private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    private final MutableDirectBuffer buffer = new ExpandableDirectByteBuffer(1024);

    public MatchingEngineClientImpl() {
        try {
            final SocketAddress address = new InetSocketAddress(8080);
            client = SocketChannel.open(address);
            LOGGER.info("Successfully connected to matching engine");
        } catch (final IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void matchRequest(final MatchOptions matchOptions) {
        matchRequestEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);

        // TODO could maybe make a method that bulk adds all the embedded fields
        matchRequestEncoder.matchOptions().gameMode(matchOptions.gameMode());

        try {
            client.write(buffer.byteBuffer()
                                 .position(0)
                                 .limit(matchRequestEncoder.encodedLength()));
            LOGGER.info("Sent match request to matching engine");
        } catch (final IOException e) {
            LOGGER.info("Could not send a match request");
        }
    }

    @Override
    public void mergeRequest(final int lobbyId, final MatchOptions matchOptions) {

    }
}
