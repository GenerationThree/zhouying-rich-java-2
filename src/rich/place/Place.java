package rich.place;

import com.sun.tools.javac.util.Pair;
import rich.game.Message;
import rich.game.Player;

public abstract class Place {

    public abstract Pair<Player.Status, Message> actionTo(Player player);

}
