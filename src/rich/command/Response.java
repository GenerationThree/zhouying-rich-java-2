package rich.command;

import com.sun.tools.javac.util.Pair;
import rich.game.Message;
import rich.game.Player;

public interface Response {
    Pair<Player.Status, Message> execute(Player player);
}
