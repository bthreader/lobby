package lobby.protocol;

import lobby.protocol.codecs.GameMode;

public record ImmutableMatchOptions(GameMode gameMode) implements MatchOptions {
}
