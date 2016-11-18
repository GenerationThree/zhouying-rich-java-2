package rich.unit;

import com.sun.tools.javac.util.Pair;
import rich.game.Message;
import rich.game.Player;
import rich.command.Command;
import rich.command.QueryCommand;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueryCommandTest {

    private Player player;
    Command query;

    @Before
    public void before() {
        player = new Player();
        query = new QueryCommand();
    }

    @Test
    public void should_wait_for_command_after_query_command() {
        Pair<Player.Status, Message> ret = player.execute(query);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        assertThat(ret.snd, is(Message.SHOW_QUERY_INFO));
    }
}
