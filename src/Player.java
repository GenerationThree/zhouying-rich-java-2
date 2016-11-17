import com.sun.tools.javac.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private Status status;
    private Command lastExecuted;
    private Place currentPlace;
    private Map<Tool, Integer> tools;

    public Player() {
        this.status = Status.WAIT_FOR_COMMAND;
        this.lastExecuted = null;
        this.currentPlace = null;
        this.tools = new HashMap<>();
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
        Pair<Status, Message> result = lastExecuted.respondWith(response);
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

    public enum Status {WAIT_FOR_RESPONSE, END_TURN, GAME_OVER, WAIT_FOR_COMMAND}
}
