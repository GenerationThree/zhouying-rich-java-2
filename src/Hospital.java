import com.sun.tools.javac.util.Pair;

public class Hospital extends Place {
    @Override
    public Pair<Player.Status, Message> actionTo(Player player) {
        return new Pair<>(Player.Status.END_TURN, Message.NULL);
    }
}
