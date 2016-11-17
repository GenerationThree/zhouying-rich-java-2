package rich.command;

import com.sun.tools.javac.util.Pair;
import rich.Message;
import rich.Player;

public class QueryCommand implements Command {
    @Override
    public Pair<Player.Status, Message> execute(Player player) {
        return new Pair<>(Player.Status.WAIT_FOR_COMMAND, Message.SHOW_QUERY_INFO);
    }

    @Override
    public Pair<Player.Status, Message> respondWith(Response response) {
        return null;
    }
}
