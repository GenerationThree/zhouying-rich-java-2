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
import rich.place.GiftRoom;
import rich.place.Starting;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GiftRoomTest {

    private GameMap map;

    private Player player;

    private Command roll;
    private int step;
    private Starting starting;
    private GiftRoom target;

    @Before
    public void before() {
        step = 1;
        starting = new Starting();
        target = new GiftRoom();

        map = mock(GameMap.class);
        when(map.getStarting()).thenReturn(starting);
        when(map.move(eq(starting), eq(step))).thenReturn(target);

        player = new Player(map, 0);
        roll = new RollCommand(map, step);

        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(commandRet.snd, is(Message.COME_TO_GIFT_ROOM));
    }

    @Test
    public void should_end_turn_when_choose_mascot() {
        player.respond(RollCommand.ChooseMascot);
        assertThat(player.canBePunished(), is(false));
    }

    @Test
    public void should_end_turn_when_choose_bonus_money() {
        player.respond(RollCommand.ChooseMoney);
        assertThat(player.getBalance(), is(GameConstant.BONUS_MONEY));
    }

    @Test
    public void should_end_turn_when_choose_bonus_points() {
        player.respond(RollCommand.ChoosePoints);
        assertThat(player.getCurrentPoints(), is(GameConstant.BONUS_POINTS));
    }

    @Test
    public void should_end_turn_when_give_up_gift() {
        player.respond(RollCommand.GiveUpGift);
        assertThat(player.getCurrentPoints(), is(0));
        assertThat(player.getCurrentPoints(), is(0));
        assertThat(player.canBePunished(), is(true));
    }

}
