package lobby.matching.engine;

import lobby.message.codecs.GameMode;

public record LobbyIndex(GameMode gameMode, int arrayIndex) {
}
