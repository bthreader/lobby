package lobby.matching.engine.domain.utils;

import lobby.protocol.MatchOptions;
import lobby.protocol.codecs.GameMode;

public record ImmutableMatchOptions(GameMode gameMode) implements MatchOptions {
}
