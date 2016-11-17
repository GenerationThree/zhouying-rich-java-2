import com.sun.tools.javac.util.Pair;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {

    private Player player;
    private Command command;

    @Before
    public void before() {
        command = mock(Command.class);
        player = new Player();
    }

    @Test
    public void should_stay_in_wait_for_response_status_after_executing_command_need_response() {
        when(command.execute(eq(player))).thenReturn(new Pair<>(Player.Status.WAIT_FOR_RESPONSE, MessageType.NULL));
        player.execute(command);

        assertThat(player.getStatus(),is(Player.Status.WAIT_FOR_RESPONSE));
    }

    @Test
    public void should_remain_in_wait_for_command_status_after_executing_command_no_need_response() {
        when(command.execute(eq(player))).thenReturn(new Pair<>(Player.Status.WAIT_FOR_COMMAND, MessageType.NULL));
        player.execute(command);

        assertThat(player.getStatus(),is(Player.Status.WAIT_FOR_COMMAND));
    }

    @Test
    public void should_end_turn_after_response() {
        Response response = mock(Response.class);
        when(command.execute(eq(player))).thenReturn(new Pair<>(Player.Status.WAIT_FOR_RESPONSE, MessageType.NULL));
        when(command.respondWith(eq(response))).thenReturn(new Pair<>(Player.Status.END_TURN, MessageType.NULL));

        player.execute(command);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));

        player.respond(response);
        assertThat(player.getStatus(),is(Player.Status.END_TURN));
    }
}
