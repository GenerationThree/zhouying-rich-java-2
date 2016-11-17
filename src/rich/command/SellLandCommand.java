package rich.command;

import com.sun.tools.javac.util.Pair;
import rich.GameMap;
import rich.Message;
import rich.Player;
import rich.place.Land;
import rich.place.Place;

public class SellLandCommand implements Command {
    private int position;
    private GameMap map;

    public SellLandCommand(GameMap map, int position) {
        this.map = map;
        this.position = position;
    }

    @Override
    public Pair<Player.Status, Message> execute(Player player) {
        Place targetPlace = map.findByPosition(position);
        Player owner;
        if (targetPlace instanceof Land) {
            Land cur = (Land) targetPlace;
            owner = cur.getOwner();
            if (owner == player)
                return new Pair<>(Player.Status.WAIT_FOR_COMMAND, Message.SELL_LAND_SUCCESS);
            else
                return new Pair<>(Player.Status.WAIT_FOR_COMMAND, Message.CANT_SELL_OTHERS_LAND);
        }
        return new Pair<>(Player.Status.WAIT_FOR_COMMAND, Message.CANT_SELL_PUBLIC_LAND);

    }

    @Override
    public Pair<Player.Status, Message> respondWith(Player player, Response response) {
        return null;
    }
}
