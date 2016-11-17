import com.sun.tools.javac.util.Pair;

public class Starting extends Place {
    @Override
    public Pair<Player.Status, Message> actionTo(Player player) {
        return new Pair<>(Player.Status.END_TURN, Message.NULL);
    }
}
