import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {

    @Test
    public void should_stay_in_wait_for_response_status_after_executing_command_need_response() {
        Command command = mock(Command.class);
        Player player = new Player();
        when(command.execute(eq(player))).thenReturn(Player.Status.WAIT_FOR_RESPONSE);
        player.execute(command);

        assertThat(player.getStatus(),is(Player.Status.WAIT_FOR_RESPONSE));

    }

    @Test
    public void should_remain_in_wait_for_command_status_after_executing_command_no_need_response() {
        Command command = mock(Command.class);
        Player player = new Player();
        when(command.execute(eq(player))).thenReturn(Player.Status.WAIT_FOR_COMMAND);
        player.execute(command);

        assertThat(player.getStatus(),is(Player.Status.WAIT_FOR_COMMAND));
    }

    @Test
    public void should_end_turn_after_response() {
        Command command = mock(Command.class);
        Response response = mock(Response.class);
        Player player = new Player();

        when(command.execute(eq(player))).thenReturn(Player.Status.WAIT_FOR_COMMAND);
        when(command.respondWith(eq(response))).thenReturn(Player.Status.END_TURN);

        player.execute(command);
        player.respond(response);

        assertThat(player.getStatus(),is(Player.Status.END_TURN));
    }
}
