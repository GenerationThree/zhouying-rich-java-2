package rich.unit;

import com.sun.tools.javac.util.Pair;
import rich.Message;
import rich.Player;
import rich.Tool;
import rich.command.Command;
import rich.command.SellToolCommand;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SellToolCommandTest {
    private Player player;
    private Command sellTool;
    private Tool tool;

    @Before
    public void before() {
        player = new Player();
        tool = Tool.Bomb;
        sellTool = new SellToolCommand(tool);
    }

    @Test
    public void should_wait_for_command_when_sell_tool_success() {
        // first, player buy this tool
        player.gainPoints(200);
        player.buyTool(tool);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        Pair<Player.Status, Message> ret = player.execute(sellTool);
        assertThat(ret.snd, is(Message.SELL_TOOL_SUCCESS));
    }

    @Test
    public void should_wait_for_command_when_no_that_tool_to_sell() {
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        Pair<Player.Status, Message> ret = player.execute(sellTool);
        assertThat(ret.snd, is(Message.NO_THAT_TOOL_FOR_SELL));
    }
}
