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
import rich.place.Prison;
import rich.place.Starting;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PrisonTest {
    private GameMap map;

    private Player player;

    private Command roll;
    private int step;
    private Starting starting;
    private Prison prison;

    @Before
    public void before() {
        step = 1;
        starting = new Starting();
        prison = new Prison();

        map = mock(GameMap.class);
        when(map.getStarting()).thenReturn(starting);
        when(map.move(eq(starting), eq(step))).thenReturn(prison);

        player = new Player(map, 0);
        roll = new RollCommand(map, step);
    }

    @Test
    public void should_end_turn_and_add_wait_times_when_come_to_prison() {
        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(commandRet.snd, is(Message.COME_TO_PRISON));
        assertThat(player.getWaitTimes(), is(GameConstant.DAYS_IN_PRISON));
    }

}
