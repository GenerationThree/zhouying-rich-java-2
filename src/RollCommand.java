import com.sun.tools.javac.util.Pair;

public class RollCommand implements Command {
    @Override
    public Pair<Player.Status, MessageType> execute(Player player) {
        return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, MessageType.NULL);
    }

    @Override
    public Pair<Player.Status, MessageType> respondWith(Response response) {
        return new Pair<>(Player.Status.END_TURN, MessageType.NULL);
    }
}
