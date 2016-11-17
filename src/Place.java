import com.sun.tools.javac.util.Pair;

public abstract class Place {

    public abstract Pair<Player.Status, Message> actionTo(Player player);

}
