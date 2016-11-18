package rich.command;

import com.sun.tools.javac.util.Pair;
import rich.game.GameMap;
import rich.game.Message;
import rich.game.Player;
import rich.game.Tool;

public class BlockCommand implements Command {
    private GameMap map;
    private int distance;

    public BlockCommand(GameMap map, int distance) {
        this.map = map;
        this.distance = distance;
    }

    @Override
    public Pair<Player.Status, Message> execute(Player player) {
        if (player.getQuantityByKind(Tool.Block) <= 0) {
            return new Pair<>(Player.Status.WAIT_FOR_COMMAND, Message.NO_BLOCKS_NOW);
        }

        int curPosition = map.findByPlace(player.getCurrentPlace());
        int targetPosition = (curPosition + distance) % map.length();

        if (map.canPutTool(targetPosition))
            player.useBlock();

        Message message = map.putTool(Tool.Block, targetPosition);
        return new Pair<>(Player.Status.WAIT_FOR_COMMAND, message);

    }

    @Override
    public Pair<Player.Status, Message> respondWith(Player player, Response response) {
        return null;
    }
}
