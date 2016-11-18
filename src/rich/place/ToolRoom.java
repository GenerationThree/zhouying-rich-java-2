package rich.place;

import com.sun.tools.javac.util.Pair;
import rich.Message;
import rich.Player;
import rich.Tool;
import rich.game.GameConstant;

public class ToolRoom extends Place {
    public static final Tool CheapestTool = Tool.Robot;

    @Override
    public Pair<Player.Status, Message> actionTo(Player player) {
        if (player.getCurrentPoints() < CheapestTool.points()) {
            return new Pair<>(Player.Status.END_TURN, Message.No_POINTS_BUY_ANYMORE_TOOL_EXIT);
        } else if (player.getToolQuantityAmount() >= GameConstant.MAX_TOOL_QUANTITY) {
            return new Pair<>(Player.Status.END_TURN, Message.TOOL_QUANTITY_LIMITED_EXIT);
        } else {
            return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_TOOL_ROOM);
        }
    }
}
