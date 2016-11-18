package rich.unit;

import com.sun.tools.javac.util.Pair;
import rich.game.GameMap;
import rich.game.Message;
import rich.game.Player;
import rich.game.Tool;
import rich.command.BlockCommand;
import rich.command.Command;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BlockCommandTest {

    private Player player;
    private GameMap map;
    int blockPosition;
    private Command block;

    @Before
    public void before() {
        map = mock(GameMap.class);
        player = new Player();
        player.gainPoints(200);
        blockPosition = 3;
        block = new BlockCommand(map, blockPosition);
        when(map.length()).thenReturn(1);
    }

    @Test
    public void should_wait_for_command_when_put_block_success() {
        // player buy a block first
        player.buyTool(Tool.Block);

        when(map.putTool(eq(Tool.Block), anyInt())).thenReturn(Message.PUT_BLOCK_SUCCESS);

        Pair<Player.Status, Message> ret = player.execute(block);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        assertThat(ret.snd, is(Message.PUT_BLOCK_SUCCESS));
    }

    @Test
    public void should_wait_for_command_when_player_does_not_have_block() {

        Pair<Player.Status, Message> ret = player.execute(block);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        assertThat(ret.snd, is(Message.NO_BLOCKS_NOW));
    }

    @Test
    public void should_wait_for_command_when_other_player_already_on_target_position() {
        // player buy a block first
        player.buyTool(Tool.Block);

        when(map.putTool(eq(Tool.Block), anyInt())).thenReturn(Message.CANT_PUT_BLOCK_WHEN_PLAYER_ON);

        Pair<Player.Status, Message> ret = player.execute(block);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        assertThat(ret.snd, is(Message.CANT_PUT_BLOCK_WHEN_PLAYER_ON));
    }

    @Test
    public void should_wait_for_command_when_other_tool_already_on_target_position() {
        // player buy a block first
        player.buyTool(Tool.Block);

        when(map.putTool(eq(Tool.Block), anyInt())).thenReturn(Message.CANT_PUT_BLOCK_WHEN_EXIST_TOOL);

        Pair<Player.Status, Message> ret = player.execute(block);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        assertThat(ret.snd, is(Message.CANT_PUT_BLOCK_WHEN_EXIST_TOOL));
    }

}
