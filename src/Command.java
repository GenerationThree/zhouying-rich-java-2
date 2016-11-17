import com.sun.tools.javac.util.Pair;

public interface Command {
    Pair<Player.Status, Message> execute(Player player);

    Pair<Player.Status, Message> respondWith(Response response);
}
