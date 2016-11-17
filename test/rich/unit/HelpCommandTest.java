package rich.unit;

import com.sun.tools.javac.util.Pair;
import rich.Message;
import rich.Player;
import rich.command.Command;
import rich.command.HelpCommand;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HelpCommandTest {

    private Player player;
    Command help;

    @Before
    public void before() {
        player = new Player();
        help = new HelpCommand();
    }

    @Test
    public void should_wait_for_command_after_help_command() {
        Pair<Player.Status, Message> ret = player.execute(help);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        assertThat(ret.snd, is(Message.SHOW_HELP_INFO));
    }
}
