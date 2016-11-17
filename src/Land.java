import com.sun.tools.javac.util.Pair;

public class Land extends Place {

    private Player owner;

    public Player getOwner() {
        return owner;
    }

    @Override
    public Pair<Player.Status, Message> actionTo(Player player) {
        if (getOwner() == player) {
            return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_EMPTY_LAND);
        }
        return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_EMPTY_LAND);
    }
}
