package rich.game;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private GameMap map;

    public Game() {
        this.map = new GameMap();
        this.players = new ArrayList<>();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public GameMap getMap() {
        return map;
    }

    public int nextStep() {
        return Dice.next();
    }
}
