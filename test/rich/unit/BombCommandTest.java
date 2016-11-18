package rich.unit;

import com.sun.tools.javac.util.Pair;
import rich.GameMap;
import rich.Message;
import rich.Player;
import rich.Tool;
import rich.command.BombCommand;
import rich.command.Command;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BombCommandTest {

    private Player player;
    private GameMap map;
    int bombPosition;
    private Command bomb;

    @Before
    public void before() {
        map = mock(GameMap.class);
        player = new Player();
        player.gainPoints(200);
        bombPosition = 3;
        bomb = new BombCommand(map, bombPosition);
        when(map.length()).thenReturn(1);
    }

    @Test
    public void should_wait_for_command_when_put_bomb_success() {
        // player buy a block first
        player.buyTool(Tool.Bomb);

        when(map.putTool(eq(Tool.Bomb), anyInt())).thenReturn(Message.PUT_BOMB_SUCCESS);

        Pair<Player.Status, Message> ret = player.execute(bomb);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        assertThat(ret.snd, is(Message.PUT_BOMB_SUCCESS));
    }

    @Test
    public void should_wait_for_command_when_player_does_not_have_a_bomb() {
        Pair<Player.Status, Message> ret = player.execute(bomb);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        assertThat(ret.snd, is(Message.NO_BOMBS_NOW));
    }

    @Test
    public void should_wait_for_command_when_other_player_already_on_target_position() {
        player.buyTool(Tool.Bomb);

        when(map.putTool(eq(Tool.Bomb), anyInt())).thenReturn(Message.CANT_PUT_BOMB_WHEN_PLAYER_ON);

        Pair<Player.Status, Message> ret = player.execute(bomb);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        assertThat(ret.snd, is(Message.CANT_PUT_BOMB_WHEN_PLAYER_ON));
    }

    @Test
    public void should_wait_for_command_when_other_tool_already_on_target_position() {
        player.buyTool(Tool.Bomb);

        when(map.putTool(eq(Tool.Bomb), anyInt())).thenReturn(Message.CANT_PUT_BOMB_WHEN_EXIST_TOOL);

        Pair<Player.Status, Message> ret = player.execute(bomb);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        assertThat(ret.snd, is(Message.CANT_PUT_BOMB_WHEN_EXIST_TOOL));
    }

}
