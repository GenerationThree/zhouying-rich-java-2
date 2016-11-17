package rich.command;

import com.sun.tools.javac.util.Pair;
import rich.GameMap;
import rich.Message;
import rich.Player;
import rich.place.Place;

public class RollCommand implements Command {
    private GameMap map;
    private int step;

    public RollCommand(GameMap map, int step) {
        this.map = map;
        this.step = step;
    }

    @Override
    public Pair<Player.Status, Message> execute(Player player) {
        Place target = map.move(player.getCurrentPlace(), step);
        return target.actionTo(player);

//        return new Pair<>(rich.Player.Status.WAIT_FOR_RESPONSE, rich.Message.NULL);
    }

    @Override
    public Pair<Player.Status, Message> respondWith(Response response) {
        return new Pair<>(Player.Status.END_TURN, Message.NULL);
    }
}
