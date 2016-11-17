import com.sun.tools.javac.util.Pair;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SellLandCommandTest {

    private GameMap map;
    private Player player;
    private Command sellLand;
    private int position;

    @Before
    public void before() {
        map = mock(GameMap.class);
        player = new Player();
        position = 1;
        sellLand = new SellLandCommand(map, position);
    }

    @Test
    public void should_wait_for_command_when_sell_land_success() {
        Land selfLand = mock(Land.class);
        when(map.findByPosition(eq(position))).thenReturn(selfLand);
        when(selfLand.getOwner()).thenReturn(player);

        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        Pair<Player.Status, Message> ret = player.execute(sellLand);
        assertThat(ret.snd, is(Message.SELL_LAND_SUCCESS));
    }

    @Test
    public void should_wait_for_command_when_sell_others_land() {
        Land othersLand = mock(Land.class);
        when(map.findByPosition(eq(position))).thenReturn(othersLand);
        when(othersLand.getOwner()).thenReturn(new Player());

        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        Pair<Player.Status, Message> ret = player.execute(sellLand);
        assertThat(ret.snd, is(Message.CANT_SELL_OTHERS_LAND));
    }

    @Test
    public void should_wait_for_command_when_sell_public_land() {
        Hospital hospital = mock(Hospital.class);
        when(map.findByPosition(eq(position))).thenReturn(hospital);

        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_COMMAND));
        Pair<Player.Status, Message> ret = player.execute(sellLand);
        assertThat(ret.snd, is(Message.CANT_SELL_PUBLIC_LAND));
    }


}
