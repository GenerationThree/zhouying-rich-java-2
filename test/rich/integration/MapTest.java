package rich.integration;

import com.sun.tools.javac.util.Pair;
import org.junit.Before;
import org.junit.Test;
import rich.game.GameMap;
import rich.game.Message;
import rich.game.Player;
import rich.game.Tool;
import rich.command.BlockCommand;
import rich.command.BombCommand;
import rich.command.RollCommand;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MapTest {

    private GameMap map;
    private Player player;

    @Before
    public void before() {
        map = new GameMap();
        player = new Player(map, 0);
    }

    @Test
    public void should_initialize_map() {
        map.display();
    }

    @Test
    public void should_find_starting_of_player() {
        map.findByPlace(player.getCurrentPlace());
        assertThat(map.findByPlace(map.getStarting()), is(0));
    }

    @Test
    public void should_player_move_on_map() {
        final int step = 1;
        for (int i = 1; i < 100; ++i) {
            player.execute(new RollCommand(map, step));
            assertThat(player.getCurrentPlace(), is(map.findByPosition(i % map.length())));
        }
    }

    @Test
    public void should_set_bomb_on_map() {
        player.gainPoints(2000);
        player.buyTool(Tool.Bomb);
        player.execute(new BombCommand(map, 10));
        assertThat(map.getToolOnPosition(10), is(Tool.Bomb));
    }

    @Test
    public void should_not_set_bomb_on_position_when_there_already_exist_another_tool() {
        player.gainPoints(2000);
        player.buyTool(Tool.Bomb);
        player.buyTool(Tool.Block);

        player.execute(new BlockCommand(map, 10));
        assertThat(map.getToolOnPosition(10), is(Tool.Block));
        assertThat(player.getToolQuantityAmount(), is(1));

        Pair<Player.Status, Message> ret = player.execute(new BombCommand(map, 10));
        assertThat(map.getToolOnPosition(10), is(Tool.Block));
        assertThat(player.getToolQuantityAmount(), is(1));
        assertThat(ret.snd, is(Message.CANT_PUT_BOMB_WHEN_EXIST_TOOL));
    }

    @Test
    public void should_not_set_block_on_position_when_there_already_exist_another_player() {
        Player otherPlayer = new Player(map, 0);
        otherPlayer.execute(new RollCommand(map, 1));

        player.gainPoints(2000);
        player.buyTool(Tool.Block);
        Pair<Player.Status, Message> ret = player.execute(new BlockCommand(map, 1));

        assertThat(player.getToolQuantityAmount(), is(1));
        assertThat(ret.snd, is(Message.CANT_PUT_BLOCK_WHEN_PLAYER_ON));
    }

}
