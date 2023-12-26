package lobby.matching.engine;

public class Lobby {
    private static final int MAX_USERS = 12;
    private int users = 0;

    public void addUser() {
        if (users < MAX_USERS) {
            users++;
            return;
        };
        throw new IllegalCallerException("Lobby is full, can't add users");
    }

    public void removeUser() {
        if (users > 0) {
            users--;
            return;
        };
        throw new IllegalCallerException("Lobby is empty, can't remove users");
    }

    public int getUsers() {
        return users;
    }
}
