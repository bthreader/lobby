package lobby.matching.engine;

import lobby.message.codecs.JoinRejectionReason;
import lobby.message.codecs.SessionRejectionReason;

public interface ClientResponder {
    void joinFilled(int lobbyId);

    void joinReject(JoinRejectionReason reason);

    void reject(SessionRejectionReason reason);
}
