package lobby.matching.engine.infra;

import lobby.matching.engine.MatchingEngineApplication;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class MatchingEngineServer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingEngineApplication.class);
    private final Selector selector;
    private final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    private final DirectBuffer dBuffer = new UnsafeBuffer();
    private final SessionContextImpl context = new SessionContextImpl();
    private final ClientResponder clientResponder = new ClientResponderImpl(context);
    private final IngressProcessor ingressProcessor = new IngressProcessor(clientResponder);
    private final int port;

    public MatchingEngineServer(final int port) throws IOException {
        this.port = port;
        // Register the selector
        selector = Selector.open();
    }

    /**
     * Starts a TCP server that listens for connections and responds to them.
     */
    @Override
    public void run() {
        try (final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            LOGGER.info("Bound to {}", port);

            try {
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            } catch (final ClosedChannelException e) {
                throw new RuntimeException(e);
            }

            // Start listening for clients
            while (true) {
                selector.select();
                final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    final SelectionKey key = iterator.next();
                    iterator.remove();

                    try {
                        // Process the client connection
                        if (key.isAcceptable()) {
                            accept(key);
                        }
                        if (key.isReadable()) {
                            read(key);
                        }
                    } catch (final Exception ignored) {
                    }
                }
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void accept(final SelectionKey key) throws IOException {
        final SocketChannel client;
        try (final ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel()) {
            client = serverSocketChannel.accept();
        }
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        LOGGER.info("Accepted connection from {}", client.getRemoteAddress());
    }

    private void read(final SelectionKey key) throws IOException {
        final SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();

        // TODO how do we know if we've read a full message here?
        final int bytesRead = client.read(buffer);
        if (bytesRead == -1) {
            client.close();
            key.cancel();
        }
        context.setClient(client);
        context.setSelectionKey(key);
        dBuffer.wrap(buffer);
        ingressProcessor.dispatch(dBuffer, 0, buffer.capacity());
    }
}
