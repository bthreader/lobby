package lobby.gateway.matching.engine;

import lobby.protocol.MatchOptions;

public interface MatchingEngineClient {
    /**
     * Sends a MatchRequest message to the matching engine.
     *
     * @param matchOptions the criteria for the match
     */
    void matchRequest(MatchOptions matchOptions);

    /**
     * Sends a MergeRequest message to the matching engine.
     *
     * @param lobbyId      the lobby to be merged
     * @param matchOptions the criteria for the match
     */
    void mergeRequest(int lobbyId, MatchOptions matchOptions);

    /** Shutdown the client */
    void disconnect();
}
