package lobby.matching.engine.infra;

import lobby.matching.engine.App;
import org.agrona.DirectBuffer;
import org.agrona.ExpandableDirectByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class Server implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private final SessionContextImpl context = new SessionContextImpl();
    private final ClientResponder clientResponder = new ClientResponderImpl(context);
    private final IngressProcessor ingressProcessor = new IngressProcessor(clientResponder);

    /**
     * Starts a TCP server that listens for connections and responds to them.
     */
    @Override
    public void run() {
        try (final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // TODO is the port necessary?
            serverSocketChannel.bind(new InetSocketAddress(8080));

            // Set up and register the selector
            final Selector selector;
            selector = Selector.open();
            try {
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            } catch (final ClosedChannelException e) {
                throw new RuntimeException(e);
            }

            final DirectBuffer buffer = new ExpandableDirectByteBuffer(1024);

            // Start listening for clients
            while (true) {
                try {
                    selector.select();
                } catch (final IOException ex) {
                    break;
                }
                final Set<SelectionKey> readyKeys = selector.selectedKeys();
                final Iterator<SelectionKey> iterator = readyKeys.iterator();

                while (iterator.hasNext()) {
                    final SelectionKey key = iterator.next();
                    iterator.remove();

                    // Process the client connection
                    try {
                        if (key.isAcceptable()) {
                            final SocketChannel client;
                            try (final ServerSocketChannel server = (ServerSocketChannel) key.channel()) {
                                client = server.accept();
                            }
                            LOGGER.info("Accepted connection from {}", client);
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_WRITE);
                            context.setClient(client);
                            buffer.wrap((ByteBuffer) key.attachment());
                            ingressProcessor.dispatch(buffer, 0, buffer.capacity());
                        }
                    } catch (final IOException e) {
                        LOGGER.info("Could not accept connection");
                    }
                }
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
