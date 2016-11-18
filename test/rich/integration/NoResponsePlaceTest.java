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
import rich.place.MineralEstate;
import rich.place.Prison;
import rich.place.Starting;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NoResponsePlaceTest {
    private GameMap map;
    private Player player;
    private int step;
    private Starting starting;

    @Before
    public void before() {
        step = 1;
        starting = new Starting();

        map = mock(GameMap.class);
        when(map.getStarting()).thenReturn(starting);
        player = new Player(map, 0);
    }

    @Test
    public void should_end_turn_and_add_wait_times_when_come_to_prison() {
        Prison prison = new Prison();
        when(map.move(eq(starting), eq(step))).thenReturn(prison);
        Command roll = new RollCommand(map, step);

        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(commandRet.snd, is(Message.COME_TO_PRISON));
        assertThat(player.getWaitTimes(), is(GameConstant.DAYS_IN_PRISON));
    }

    @Test
    public void should_end_turn_and_add_points_when_come_to_mineral_estate() {
        final int POINT_IN_MINERAL = 20;
        MineralEstate mineralEstate = new MineralEstate(POINT_IN_MINERAL);
        when(map.move(eq(starting), eq(step))).thenReturn(mineralEstate);
        Command roll = new RollCommand(map, step);

        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(commandRet.snd, is(Message.COME_TO_MINERAL_ESTATE));
        assertThat(player.getCurrentPoints(), is(POINT_IN_MINERAL));
    }

}
