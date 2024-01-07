package lobby.matching.engine.domain;

import lobby.protocol.codecs.GameMode;

/**
 * TODO Low cardinality, potential to lazy initialize and pool.
 */
public record LobbyIndex(GameMode gameMode, int arrayIndex) {
}
