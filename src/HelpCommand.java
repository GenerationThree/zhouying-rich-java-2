import com.sun.tools.javac.util.Pair;

public class HelpCommand implements Command {
    @Override
    public Pair<Player.Status, Message> execute(Player player) {
        return new Pair<>(Player.Status.WAIT_FOR_COMMAND, Message.SHOW_HELP_INFO);
    }

    @Override
    public Pair<Player.Status, Message> respondWith(Response response) {
        return null;
    }
}
