package lobby.gateway;

import lobby.protocol.ImmutableMatchOptions;
import lobby.protocol.codecs.GameMode;

public class GatewayApplication {
    public static void main(final String[] args) {
        final MatchingEngineClient matchingEngineClient = new MatchingEngineClientImpl();
        matchingEngineClient.matchRequest(new ImmutableMatchOptions(GameMode.CAPTURE_THE_FLAG));
        matchingEngineClient.matchRequest(new ImmutableMatchOptions(GameMode.FREE_FOR_ALL));
    }
}
