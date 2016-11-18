package rich.place;

import com.sun.tools.javac.util.Pair;
import rich.game.Message;
import rich.game.Player;

public class MineralEstate extends Place {
    private int points;

    public MineralEstate(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public Pair<Player.Status, Message> actionTo(Player player) {
        player.gainPoints(points);
        return new Pair<>(Player.Status.END_TURN, Message.COME_TO_MINERAL_ESTATE);
    }
}
