package lobby.matching.engine;

import lobby.message.codecs.GameMode;

public class App {
    public String getGreeting() {
        GameMode gameMode = GameMode.CAPTURE_THE_FLAG;

        return "hello";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
