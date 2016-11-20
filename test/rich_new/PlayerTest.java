package rich_new;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PlayerTest {

    class Player {

        private List<CommandType> initialCommands;
        private List<CommandType> availableCommands;

        public Player(CommandType... initialCommands) {
            this.initialCommands = asList(initialCommands);
            this.availableCommands = this.initialCommands;
        }

        public Optional<Command> getAvailableCommand(String input) {
            return availableCommands.stream().map(type -> type.parse(input)).filter(r -> r.isPresent()).findFirst().get();
        }

        public void execute(Command command) {
            availableCommands = command.execute(this, initialCommands);
        }
    }

    interface CommandType {
        Optional<Command> parse(String input);
    }

    interface Command {
        List<CommandType> execute(Player player, List<CommandType> initialCommands);
    }


    @Test
    public void should_return_initial_commands_as_available_command() {
        CommandType buyType = mock(CommandType.class);

        Command buy = mock(Command.class);

        when(buyType.parse(eq("buy"))).thenReturn(Optional.of(buy));

        Player player = new Player(buyType);

        Optional<Command> command = player.getAvailableCommand("buy");

        assertThat(command, is(Optional.of(buy)));
    }

    @Test
    public void should_change_available_command_if_state_changed() {
        CommandType buyType = mock(CommandType.class);
        Command buy = mock(Command.class);
        CommandType yesToBuyType = mock(CommandType.class);
        Command yesToBuy = mock(Command.class);

        when(buyType.parse(eq("buy"))).thenReturn(Optional.of(buy));

        Player player = new Player(buyType);

        when(buy.execute(eq(player), any())).thenReturn(asList(yesToBuyType));
        when(yesToBuyType.parse(eq("Y"))).thenReturn(Optional.of(yesToBuy));

        player.execute(buy);

        Optional<Command> y = player.getAvailableCommand("Y");

        assertThat(y, is(Optional.of(yesToBuy)));
    }
}
