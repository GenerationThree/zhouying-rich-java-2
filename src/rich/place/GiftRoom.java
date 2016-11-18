package rich.place;

import com.sun.tools.javac.util.Pair;
import rich.game.Message;
import rich.game.Player;

public class GiftRoom extends Place {
    @Override
    public Pair<Player.Status, Message> actionTo(Player player) {
        return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_GIFT_ROOM);
    }
}
