package rich.command;

import com.sun.tools.javac.util.Pair;
import rich.GameMap;
import rich.Message;
import rich.Player;
import rich.place.Land;
import rich.place.Place;

public class RollCommand implements Command {
    private GameMap map;
    private int step;

    public RollCommand(GameMap map, int step) {
        this.map = map;
        this.step = step;
    }

    @Override
    public Pair<Player.Status, Message> execute(Player player) {
        Place target = map.move(player.getCurrentPlace(), step);
        player.moveTo(target);
        return target.actionTo(player);

//        return new Pair<>(rich.Player.Status.WAIT_FOR_RESPONSE, rich.Message.NULL);
    }

    @Override
    public Pair<Player.Status, Message> respondWith(Player player, Response response) {
        return response.execute(player);
//        return new Pair<>(Player.Status.END_TURN, Message.NULL);
    }

    public static Response YesToBuy = player -> {
        Land land = (Land) player.getCurrentPlace();
        boolean ret = player.buyLand(land);
        if (ret)
            return new Pair<>(Player.Status.END_TURN, Message.NULL);
        else
            return new Pair<>(Player.Status.END_TURN, Message.NO_ENOUGH_MONEY_TO_BUY_LAND);
    };

    public static Response NoToBuy = player -> new Pair<>(Player.Status.END_TURN, Message.NULL);

    public static Response YesToUpgrade = player -> {
        Land land = (Land) player.getCurrentPlace();
//        if (land.isTopLevel()) {
//            return new Pair<>(Player.Status.END_TURN, Message.LAND_ALREADY_TOP_LEVEL);
//        }
        boolean ret = player.upgradeLand(land);
        if (ret)
            return new Pair<>(Player.Status.END_TURN, Message.NULL);
        else
            return new Pair<>(Player.Status.END_TURN, Message.NO_ENOUGH_MONEY_TO_UPGRADE_LAND);
    };

    public static Response NoToUpgrade = player -> new Pair<>(Player.Status.END_TURN, Message.NULL);

}

