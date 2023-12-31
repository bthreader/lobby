package lobby.matching.engine.domain;

import lobby.message.codecs.GameMode;

public record LobbyIndex(GameMode gameMode, int arrayIndex) {
}
