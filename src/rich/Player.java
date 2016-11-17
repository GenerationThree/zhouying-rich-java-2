package rich;

import com.sun.tools.javac.util.Pair;
import rich.command.Command;
import rich.command.Response;
import rich.place.Land;
import rich.place.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private Status status;
    private Command lastExecuted;
    private Place currentPlace;
    private Map<Tool, Integer> tools;
    private GameMap map;
    private int balance;
    private List<Land> lands;

    public Player() {
        this.status = Status.WAIT_FOR_COMMAND;
        this.lastExecuted = null;
        this.currentPlace = null;
        this.tools = new HashMap<>();
        this.balance = 0;
    }

    public Player(GameMap map, int startMoney) {
        this.status = Status.WAIT_FOR_COMMAND;
        this.lastExecuted = null;
        this.currentPlace = map.getStarting();
        this.tools = new HashMap<>();
        this.map = map;
        this.balance = startMoney;
        this.lands = new ArrayList<>();
    }

    public Status getStatus() {
        return status;
    }

    public Pair<Status, Message> execute(Command command) {
        Pair<Status, Message> result = command.execute(this);
        lastExecuted = command;
        status = result.fst;
        return result;
    }

    public Pair<Status, Message> respond(Response response) {
        Pair<Status, Message> result = lastExecuted.respondWith(this, response);
        status = result.fst;
        return result;
    }

    public Place getCurrentPlace() {
        return currentPlace;
    }

    public boolean buyTool(Tool tool) {
        int cur = tools.getOrDefault(tool, 0);
        tools.put(tool, cur + 1);
        return true;
    }

    public int getQuantityByKind(Tool tool) {
        return tools.getOrDefault(tool, 0);
    }

    public boolean buyLand(Land land) {
        int price = land.getPrice();
        if (balance >= price) {
            lands.add(land);
            balance -= price;
            land.setOwner(this);
            return true;
        }
        return false;
    }

    public int getBalance() {
        return balance;
    }

    public List<Land> getLands() {
        return lands;
    }

    public void moveTo(Place target) {
        this.currentPlace = target;
    }

    public enum Status {WAIT_FOR_RESPONSE, END_TURN, GAME_OVER, WAIT_FOR_COMMAND}
}
