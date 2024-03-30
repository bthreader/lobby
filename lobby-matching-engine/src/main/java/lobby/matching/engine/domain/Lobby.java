package lobby.matching.engine.domain;

import lobby.protocol.MatchOptions;
import lobby.protocol.codecs.GameMode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class Lobby {
    private final int maxUsers;
    @Getter
    private final long id;
    private final Set<Long> users;
    private final GameMode gameMode;
    private int numberOfUsers = 0;

    public Lobby(final long id, final GameMode gameMode) {
        this.id = id;
        this.gameMode = gameMode;

        switch (gameMode) {
            case FREE_FOR_ALL -> this.maxUsers = 8;
            case CAPTURE_THE_FLAG -> this.maxUsers = 12;
            default -> throw new IllegalArgumentException("null lobby not allowed");
        }

        this.users = HashSet.newHashSet(maxUsers);
    }

    public void addUser(final Long userId) {
        if (isNotFull()) {
            users.add(userId);
            numberOfUsers++;
            return;
        }
        throw new IllegalCallerException("Lobby is full, can't add user");
    }

    public int freeSpaces() {
        return maxUsers - numberOfUsers;
    }

    /**
     * Puts all the users from a provided lobby into the current one.
     *
     * @param lobby the lobby to merge into the current lobby
     */
    public void merge(final Lobby lobby) {
        users.addAll(lobby.users);
    }

    public void removeUser(final Long userId) {
        final boolean result = users.remove(userId);
        if (!result) {
            throw new IllegalCallerException("User is not in the lobby");
        }
    }

    public boolean isNull() {
        return id == 0;
    }

    public boolean isNotEmpty() {
        return numberOfUsers != 0;
    }

    public boolean isNotFull() { return numberOfUsers != maxUsers; }

    public boolean matches(final MatchOptions matchOptions) {
        return gameMode == matchOptions.gameMode();
    }

    public Iterable<Long> getUsers() {
        return users;
    }
}
