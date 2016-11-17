package rich.function;

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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseOfEmptyLandTest {

    private GameMap map;

    private Player player;
    private Player noMoneyPlayer;

    private Command roll;
    private int step;
    private Starting starting;
    private Land target;

    @Before
    public void before() {
        step = 1;
        starting = new Starting();
        target = new Land(GameConstant.FirstDistrictLandPrice);

        map = mock(GameMap.class);
        when(map.getStarting()).thenReturn(starting);
        when(map.move(eq(starting), eq(step))).thenReturn(target);
        player = new Player(map, GameConstant.StartMoney);
        noMoneyPlayer = new Player(map, 0);

        roll = new RollCommand(map, step);
    }

    @Test
    public void should_buy_land_when_response_y_and_have_enough_balance() {
        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(commandRet.snd, is(Message.COME_TO_EMPTY_LAND));

        Pair<Player.Status, Message> responseRet = player.respond(RollCommand.YesToBuy);
        assertThat(player.getBalance(), is(GameConstant.StartMoney - GameConstant.FirstDistrictLandPrice));
        assertThat(player.getLands().size(), is(1));

        assertThat(target.getOwner(), is(player));
        assertThat(target.getPassFee(), is(GameConstant.FirstDistrictLandPrice / 2));

        assertThat(responseRet.snd, is(Message.NULL));
    }

    @Test
    public void should_buy_land_when_response_y_but_do_not_have_enough_balance() {
        Pair<Player.Status, Message> commandRet = noMoneyPlayer.execute(roll);
        assertThat(noMoneyPlayer.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(commandRet.snd, is(Message.COME_TO_EMPTY_LAND));

        Pair<Player.Status, Message> responseRet = noMoneyPlayer.respond(RollCommand.YesToBuy);
        assertThat(noMoneyPlayer.getBalance(), is(0));
        assertThat(noMoneyPlayer.getLands().size(), is(0));

        assertThat(target.getOwner(), is(nullValue()));
        assertThat(target.getPassFee(), is(0));

        assertThat(responseRet.snd, is(Message.NO_ENOUGH_MONEY_TO_BUY_LAND));
    }

    @Test
    public void should_not_buy_land_when_response_n() {
        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(commandRet.snd, is(Message.COME_TO_EMPTY_LAND));

        Pair<Player.Status, Message> responseRet = player.respond(RollCommand.NoToBuy);
        assertThat(player.getBalance(), is(GameConstant.StartMoney));
        assertThat(player.getLands().size(), is(0));

        assertThat(target.getOwner(), is(nullValue()));
        assertThat(target.getPassFee(), is(0));

        assertThat(responseRet.snd, is(Message.NULL));
    }
}
