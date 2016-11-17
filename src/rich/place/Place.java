package rich.place;

import com.sun.tools.javac.util.Pair;
import rich.Message;
import rich.Player;

public abstract class Place {

    public abstract Pair<Player.Status, Message> actionTo(Player player);

}
