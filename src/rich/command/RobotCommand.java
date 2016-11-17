package rich.command;

import com.sun.tools.javac.util.Pair;
import rich.GameMap;
import rich.Message;
import rich.Player;
import rich.Tool;

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

        //       int targetPosition = blockPosition + player.getCurrentPlace();
        int targetPosition = 0;
        Message message = map.robotClean(targetPosition);
        return new Pair<>(Player.Status.WAIT_FOR_COMMAND, message);
    }

    @Override
    public Pair<Player.Status, Message> respondWith(Response response) {
        return null;
    }
}
