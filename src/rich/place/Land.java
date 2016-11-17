package rich.place;

import com.sun.tools.javac.util.Pair;
import rich.Message;
import rich.Player;
import rich.game.GameConstant;

public class Land extends Place {

    private Player owner;
    private int price;
    private int passFee;
    private int level;

    public Land(int price) {
        super();
        this.price = price;
        this.level = 0;
    }

    public Player getOwner() {
        return owner;
    }

    @Override
    public Pair<Player.Status, Message> actionTo(Player player) {
        if (getOwner() == player) {
            if (level == GameConstant.LAND_TOP_LEVEL)
                return new Pair<>(Player.Status.END_TURN, Message.LAND_ALREADY_TOP_LEVEL);
            else
                return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_SELF_LAND);
        } else if (getOwner() == null) {
            return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_EMPTY_LAND);
        } else {
            int passFee = getPassFee();
            player.payPassFee(passFee);
            owner.gainPassFee(passFee);
            return new Pair<>(Player.Status.END_TURN, Message.COME_TO_OTHERS_LAND_PAY_SUCCESSFUL);
        }

//        if (level == GameConstant.LAND_TOP_LEVEL) {
//            return new Pair<>(Player.Status.END_TURN, Message.LAND_ALREADY_TOP_LEVEL);
//        } else {
//            if (getOwner() == player)
//                return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_SELF_LAND);
//            else
//                return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_EMPTY_LAND);
//        }
    }

    public int getPassFee() {
        if (owner == null) return 0;
        passFee = price / 2;
        for (int tmp = level; tmp != 0; --tmp)
            passFee *= 2;
        return passFee;
    }

    public int getPrice() {
        return price;
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    public boolean isTopLevel() {
        return level == GameConstant.LAND_TOP_LEVEL;
    }

    public void upgrade() {
        if (level < GameConstant.LAND_TOP_LEVEL) level += 1;
    }

    public int getLevel() {
        return level;
    }
}
