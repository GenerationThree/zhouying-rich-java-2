package rich.place;

import com.sun.tools.javac.util.Pair;
import rich.game.Message;
import rich.game.Player;

public class MagicRoom extends Place {
    @Override
    public Pair<Player.Status, Message> actionTo(Player player) {
        return new Pair<>(Player.Status.END_TURN, Message.NULL);
    }
}
