package rich.command;

import com.sun.tools.javac.util.Pair;
import rich.Message;
import rich.Player;

public interface Response {
    Pair<Player.Status, Message> execute(Player player);
//    Pair<Player.Status, Message> execute(Player player, Object[] objects);
}
