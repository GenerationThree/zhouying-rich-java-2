package rich.unit;

import com.sun.tools.javac.util.Pair;
import rich.GameMap;
import rich.Message;
import rich.Player;
import rich.Tool;
import rich.command.Command;
import rich.command.RobotCommand;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RobotCommandTest {

    private Player player;
    private GameMap map;
    private Command robot;

    @Before
    public void before() {
        map = mock(GameMap.class);
        player = new Player();
        robot = new RobotCommand(map);
    }

    @Test
    public void should_wait_for_command_after_clean_the_road() {
        //player buy a robot first
        player.buyTool(Tool.Robot);
        when(map.robotClean(anyInt())).thenReturn(Message.ROBOT_CLEAN_SUCCESS);

        Pair<Player.Status, Message> ret = player.execute(robot);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        assertThat(ret.snd, is(Message.ROBOT_CLEAN_SUCCESS));
    }

    @Test
    public void should_wait_for_command_when_player_has_no_robots_now() {
        Pair<Player.Status, Message> ret = player.execute(robot);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        assertThat(ret.snd, is(Message.NO_ROBOTS_NOW));
    }
}
