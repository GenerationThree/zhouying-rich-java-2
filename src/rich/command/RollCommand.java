package rich.command;

import com.sun.tools.javac.util.Pair;
import rich.GameMap;
import rich.Message;
import rich.Player;
import rich.Tool;
import rich.game.GameConstant;
import rich.place.Land;
import rich.place.Place;
import rich.place.ToolRoom;

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

    public static Response ExitToolRoom = player -> new Pair<>(Player.Status.END_TURN, Message.CHOOSE_EXIT_TOOL_ROOM);

    public static Response BuyBomb = player -> {

        // 买之前检查, 如果点数不够买最便宜的道具或者道具已满, 直接退出
        if (player.getToolQuantityAmount() == GameConstant.MAX_TOOL_QUANTITY) {
            return new Pair<>(Player.Status.END_TURN, Message.TOOL_QUANTITY_LIMITED_EXIT);
        }
        if (player.getCurrentPoints() < ToolRoom.CheapestTool.points()) {
            return new Pair<>(Player.Status.END_TURN, Message.NO_POINTS_BUY_ANYMORE_TOOL_EXIT);
        }

        boolean ret = player.buyTool(Tool.Bomb);

        if (ret)
            return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.BUY_BOMB_SUCCESS);

        else
            return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.NO_ENOUGH_POINTS_BUY_BOMB);
    };


    public static Response BuyRobot = player -> {

        // 买之前检查
        if (player.getToolQuantityAmount() == GameConstant.MAX_TOOL_QUANTITY) {
            return new Pair<>(Player.Status.END_TURN, Message.TOOL_QUANTITY_LIMITED_EXIT);
        }
        if (player.getCurrentPoints() < ToolRoom.CheapestTool.points()) {
            return new Pair<>(Player.Status.END_TURN, Message.NO_POINTS_BUY_ANYMORE_TOOL_EXIT);
        }

        boolean ret = player.buyTool(Tool.Robot);

        if (ret)
            return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.BUY_ROBOT_SUCCESS);
        else
            return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.NO_ENOUGH_POINTS_BUY_ROBOT);
    };

    public static Response BuyBlock = player -> {

        // 买之前检查
        if (player.getToolQuantityAmount() == GameConstant.MAX_TOOL_QUANTITY) {
            return new Pair<>(Player.Status.END_TURN, Message.TOOL_QUANTITY_LIMITED_EXIT);
        }
        if (player.getCurrentPoints() < ToolRoom.CheapestTool.points()) {
            return new Pair<>(Player.Status.END_TURN, Message.NO_POINTS_BUY_ANYMORE_TOOL_EXIT);
        }

        boolean ret = player.buyTool(Tool.Block);

        if (ret)
            return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.BUY_BLOCK_SUCCESS);
        else
            return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.NO_ENOUGH_POINTS_BUY_BLOCK);
    };

    public static Response ChooseMascot = player -> {
        player.blessed();
        return new Pair<>(Player.Status.END_TURN, Message.CHOOSE_MASCOT_BLESS);
    };

    public static Response ChooseMoney = player -> {
        player.gainBonus();
        return new Pair<>(Player.Status.END_TURN, Message.CHOOSE_BONUS_MONEY);
    };

    public static Response ChoosePoints = player -> {
        player.gainPoints(GameConstant.BONUS_POINTS);
        return new Pair<>(Player.Status.END_TURN, Message.CHOOSE_BONUS_POINTS);
    };

    public static Response GiveUpGift = player -> new Pair<>(Player.Status.END_TURN, Message.CHOOS_NO_GIFT);

}

