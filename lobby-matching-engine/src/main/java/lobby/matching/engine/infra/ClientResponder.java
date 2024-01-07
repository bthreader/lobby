package lobby.matching.engine.infra;

import lobby.protocol.codecs.ExecutionFailureReason;
import lobby.protocol.codecs.MessageRejectionReason;

public interface ClientResponder {
    /**
     * Sends an execution report detailing that the requested action was completed.
     *
     * @param lobbyId has a different meaning depending on the initial request TODO
     */
    void executionSuccess(int lobbyId);

    /**
     * Sends an execution report detailing that the requested action could not be completed.
     *
     * @param reason the reason the action could not be completed
     */
    void executionFailure(ExecutionFailureReason reason);

    /**
     * Rejects a request that has been sent to the engine due to a
     *
     * @param reason the reason for the rejection.
     */
    void rejectMessage(MessageRejectionReason reason);
}
