package rich_new;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Player2Test {

    interface CommandHandler {
        Optional<CommandHandler> execute(String input, Player player);
    }

    class Player {

        private PlayerState state;

        public Player(PlayerState state) {
            this.state = state;
        }

        public Optional<CommandHandler> execute(String input) {
            return state.execute(input, this);
        }
    }

    interface PlayerState extends CommandHandler {

    }

    @Test
    public void should_return_available_commands() {
        PlayerState waitForCommand = mock(PlayerState.class);


        Player player = new Player(waitForCommand);

        when(waitForCommand.execute("buy", eq(player))).thenReturn(Optional.of(waitForCommand));


        Optional<CommandHandler> handler = player.execute("buy");
        assertThat(handler.isPresent(), is(true));

    }
}
