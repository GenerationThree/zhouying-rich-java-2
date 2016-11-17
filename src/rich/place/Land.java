package rich.place;

import com.sun.tools.javac.util.Pair;
import rich.Message;
import rich.Player;

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
            return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_EMPTY_LAND);
        }
        return new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_EMPTY_LAND);
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
}
