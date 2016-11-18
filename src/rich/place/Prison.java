package rich.place;

import com.sun.tools.javac.util.Pair;
import rich.game.Message;
import rich.game.Player;

public class Prison extends Place {
    @Override
    public Pair<Player.Status, Message> actionTo(Player player) {
        player.pauseByPrison();
        return new Pair<>(Player.Status.END_TURN, Message.COME_TO_PRISON);
    }
}
