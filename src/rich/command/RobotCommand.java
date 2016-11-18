package rich.command;

import com.sun.tools.javac.util.Pair;
import rich.game.GameMap;
import rich.game.Message;
import rich.game.Player;
import rich.game.Tool;

public class RobotCommand implements Command {
    private GameMap map;

    public RobotCommand(GameMap map) {
        this.map = map;
    }

    @Override
    public Pair<Player.Status, Message> execute(Player player) {
        if (player.getQuantityByKind(Tool.Robot) <= 0) {
            return new Pair<>(Player.Status.WAIT_FOR_COMMAND, Message.NO_ROBOTS_NOW);
        }
        Message message = map.robotClean(map.findByPlace(player.getCurrentPlace()));
        return new Pair<>(Player.Status.WAIT_FOR_COMMAND, message);
    }

    @Override
    public Pair<Player.Status, Message> respondWith(Player player, Response response) {
        return null;
    }
}
