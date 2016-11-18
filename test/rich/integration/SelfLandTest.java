package rich.integration;


import com.sun.tools.javac.util.Pair;
import org.junit.Before;
import org.junit.Test;
import rich.GameMap;
import rich.Message;
import rich.Player;
import rich.command.Command;
import rich.command.RollCommand;
import rich.game.GameConstant;
import rich.place.Land;
import rich.place.Starting;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SelfLandTest {

    private GameMap map;

    private Player player;
    private Player noMoneyUpgradePlayer;

    private Command roll;
    private int step;
    private Starting starting;
    private Land target;

    @Before
    public void before() {
        step = 1;
        starting = new Starting();
        target = new Land(GameConstant.FIRST_DISTRICT_LAND_PRICE);

        map = mock(GameMap.class);
        when(map.getStarting()).thenReturn(starting);
        when(map.move(eq(starting), eq(step))).thenReturn(target);

        player = new Player(map, GameConstant.StartMoney);
        noMoneyUpgradePlayer = new Player(map, GameConstant.FIRST_DISTRICT_LAND_PRICE);

        roll = new RollCommand(map, step);
    }

    @Test
    public void should_upgrade_land_when_response_y_and_have_enough_balance() {
        player.buyLand(target);
        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(commandRet.snd, is(Message.COME_TO_SELF_LAND));

        Pair<Player.Status, Message> responseRet = player.respond(RollCommand.YesToUpgrade);
        assertThat(player.getBalance(), is(GameConstant.StartMoney - GameConstant.FIRST_DISTRICT_LAND_PRICE * 2));
        assertThat(player.getLands().size(), is(1));

        assertThat(target.getOwner(), is(player));
        assertThat(target.getLevel(), is(1));
        assertThat(target.getPassFee(), is(GameConstant.FIRST_DISTRICT_LAND_PRICE));

        assertThat(responseRet.snd, is(Message.NULL));
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
    }

    @Test
    public void should_not_upgrade_land_when_response_y_but_do_not_have_enough_balance() {
        noMoneyUpgradePlayer.buyLand(target);
        Pair<Player.Status, Message> commandRet = noMoneyUpgradePlayer.execute(roll);
        assertThat(noMoneyUpgradePlayer.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(commandRet.snd, is(Message.COME_TO_SELF_LAND));

        Pair<Player.Status, Message> responseRet = noMoneyUpgradePlayer.respond(RollCommand.YesToUpgrade);
        assertThat(noMoneyUpgradePlayer.getBalance(), is(0));
        assertThat(noMoneyUpgradePlayer.getLands().size(), is(1));

        assertThat(target.getOwner(), is(noMoneyUpgradePlayer));
        assertThat(target.getPassFee(), is(GameConstant.FIRST_DISTRICT_LAND_PRICE / 2));
        assertThat(target.getLevel(), is(0));

        assertThat(responseRet.snd, is(Message.NO_ENOUGH_MONEY_TO_UPGRADE_LAND));
        assertThat(noMoneyUpgradePlayer.getStatus(), is(Player.Status.END_TURN));
    }

    @Test
    public void should_end_turn_when_the_land_is_already_top_level() {
        // buy and upgrade to top level
        player.buyLand(target);
        player.upgradeLand(target);
        player.upgradeLand(target);
        player.upgradeLand(target);

        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(commandRet.snd, is(Message.LAND_ALREADY_TOP_LEVEL));

        assertThat(target.getOwner(), is(player));
        assertThat(target.getPassFee(), is(GameConstant.FIRST_DISTRICT_LAND_PRICE * 4));
        assertThat(target.getLevel(), is(GameConstant.LAND_TOP_LEVEL));

    }

    @Test
    public void should_end_turn_when_when_response_n() {
        player.buyLand(target);

        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(commandRet.snd, is(Message.COME_TO_SELF_LAND));

        Pair<Player.Status, Message> responseRet = player.respond(RollCommand.NoToUpgrade);
        assertThat(player.getBalance(), is(GameConstant.StartMoney - GameConstant.FIRST_DISTRICT_LAND_PRICE));
        assertThat(player.getLands().size(), is(1));

        assertThat(target.getOwner(), is(player));
        assertThat(target.getPassFee(), is(GameConstant.FIRST_DISTRICT_LAND_PRICE / 2));
        assertThat(target.getLevel(), is(0));

        assertThat(responseRet.snd, is(Message.NULL));
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
    }
}
