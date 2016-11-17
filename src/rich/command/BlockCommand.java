package rich.command;

import com.sun.tools.javac.util.Pair;
import rich.GameMap;
import rich.Message;
import rich.Player;
import rich.Tool;

public class BlockCommand implements Command {
    private GameMap map;
    private int blockPosition;

    public BlockCommand(GameMap map, int blockPosition) {
        this.map = map;
        this.blockPosition = blockPosition;
    }

    @Override
    public Pair<Player.Status, Message> execute(Player player) {
        if (player.getQuantityByKind(Tool.Block) <= 0) {
            return new Pair<>(Player.Status.WAIT_FOR_COMMAND, Message.NO_BLOCKS_NOW);
        }

//        int targetPosition = blockPosition + player.getCurrentPlace();
        int targetPosition = 0;
        Message message = map.putTool(Tool.Block, targetPosition);
        return new Pair<>(Player.Status.WAIT_FOR_COMMAND, message);
    }

    @Override
    public Pair<Player.Status, Message> respondWith(Player player, Response response) {
        return null;
    }
}
