import com.sun.tools.javac.util.Pair;

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

//        return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.NULL);
    }

    @Override
    public Pair<Player.Status, Message> respondWith(Response response) {
        return new Pair<>(Player.Status.END_TURN, Message.NULL);
    }
}
