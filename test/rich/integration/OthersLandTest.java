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

public class OthersLandTest {

    private GameMap map;

    private Player player;

    private Command roll;
    private int step;
    private Starting starting;
    private Land target;
    private Player otherPlayer;

    @Before
    public void before() {
        step = 1;
        starting = new Starting();
        target = new Land(GameConstant.FirstDistrictLandPrice);

        map = mock(GameMap.class);
        when(map.getStarting()).thenReturn(starting);
        when(map.move(eq(starting), eq(step))).thenReturn(target);

        otherPlayer = new Player(map, 0);
        player = new Player(map, GameConstant.StartMoney);

        target.setOwner(otherPlayer);

        roll = new RollCommand(map, step);
    }

    @Test
    public void should_end_turn_and_pay_pass_fee() {
        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(commandRet.snd, is(Message.COME_TO_OTHERS_LAND_PAY_SUCCESSFUL));

        assertThat(player.getBalance(), is(GameConstant.StartMoney - target.getPassFee()));
        assertThat(otherPlayer.getBalance(), is(target.getPassFee()));
    }

    @Test
    public void should_game_over_when_balance_is_not_enough_to_pay_pass_fee() {
        Player noMoneyPlayer = new Player(map, 0);
        Pair<Player.Status, Message> commandRet = noMoneyPlayer.execute(roll);
        assertThat(noMoneyPlayer.getStatus(), is(Player.Status.GAME_OVER));
        assertThat(commandRet.snd, is(Message.COME_TO_OTHERS_LAND_PAY_FAILED));
    }

    @Test
    public void should_not_pay_when_owner_is_in_prison() {
        otherPlayer.pauseByPrison();

        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(commandRet.snd, is(Message.COME_TO_OTHERS_LAND_WITH_OWNER_IN_PRISON));

        assertThat(player.getBalance(), is(GameConstant.StartMoney));
        assertThat(otherPlayer.getBalance(), is(0));
        assertThat(otherPlayer.getWaitTimes(), is(GameConstant.DAYS_IN_PRISON));
    }

    @Test
    public void should_not_pay_when_owner_is_bombed_into_hospital() {
        otherPlayer.pauseByBomb();

        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(commandRet.snd, is(Message.COME_TO_OTHERS_LAND_WITH_OWNER_BOMBED_INTO_PRISON));

        assertThat(player.getBalance(), is(GameConstant.StartMoney));
        assertThat(otherPlayer.getBalance(), is(0));
        assertThat(otherPlayer.getWaitTimes(), is(GameConstant.DAYS_BOMBED_INTO_HOSPITAL));
    }

    @Test
    public void should_not_pay_when_player_with_mascot() {
        player.blessed();
        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(commandRet.snd, is(Message.COME_TO_OTHERS_LAND_WITH_MASCOT));

        assertThat(player.getBalance(), is(GameConstant.StartMoney));
        assertThat(player.canBePunished(), is(false));
        assertThat(otherPlayer.getBalance(), is(0));
    }

}
