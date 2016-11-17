import com.sun.tools.javac.util.Pair;

public class SellToolCommand implements Command {
    private Tool tool;

    public SellToolCommand(Tool tool) {
        this.tool = tool;
    }

    @Override
    public Pair<Player.Status, Message> execute(Player player) {
        if (player.getQuantityByKind(tool) <= 0) {
            return new Pair<>(Player.Status.WAIT_FOR_COMMAND, Message.NO_THAT_TOOL_FOR_SELL);
        }
        return new Pair<>(Player.Status.WAIT_FOR_COMMAND, Message.SELL_TOOL_SUCCESS);
    }

    @Override
    public Pair<Player.Status, Message> respondWith(Response response) {
        return null;
    }
}
