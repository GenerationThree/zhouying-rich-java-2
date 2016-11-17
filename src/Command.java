import com.sun.tools.javac.util.Pair;

public interface Command {
    Pair<Player.Status, MessageType> execute(Player player);

    Pair<Player.Status, MessageType> respondWith(Response response);
}
