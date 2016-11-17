import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RollCommandTest {

    private Player player;

    @Before
    public void before() {
        player = new Player();
    }

    @Test
    public void should_wait_for_response_when_roll_to_empty_land() {
        Command roll = new RollCommand();
        player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
    }
}
