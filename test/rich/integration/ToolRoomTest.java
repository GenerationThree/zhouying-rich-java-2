package rich.integration;

import com.sun.tools.javac.util.Pair;
import org.junit.Before;
import org.junit.Test;
import rich.GameMap;
import rich.Message;
import rich.Player;
import rich.Tool;
import rich.command.Command;
import rich.command.RollCommand;
import rich.game.GameConstant;
import rich.place.Starting;
import rich.place.ToolRoom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ToolRoomTest {

    private static final int ENOUGH_POINTS = 2000000;
    private static int START_POINTS = 200;
    private GameMap map;

    private Player player;

    private Command roll;
    private int step;
    private Starting starting;
    private ToolRoom target;

    @Before
    public void before() {
        step = 1;
        starting = new Starting();
        target = new ToolRoom();

        map = mock(GameMap.class);
        when(map.getStarting()).thenReturn(starting);
        when(map.move(eq(starting), eq(step))).thenReturn(target);

        player = new Player(map, GameConstant.StartMoney);
        roll = new RollCommand(map, step);
    }

    @Test
    public void should_auto_end_turn_when_points_is_not_enough_to_buy_any_tool() {
        player.gainPoints(0);
        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(commandRet.snd, is(Message.No_POINTS_BUY_ANYMORE_TOOL_EXIT));
    }

    @Test
    public void should_auto_end_turn_when_tool_quantity_is_max() {
        player.gainPoints(ENOUGH_POINTS);
        for (int i = 0; i < GameConstant.MAX_TOOL_QUANTITY; ++i) {
            player.buyTool(Tool.Bomb);
        }

        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(commandRet.snd, is(Message.TOOL_QUANTITY_LIMITED_EXIT));
    }

    @Test
    public void should_end_turn_when_response_f() {
        player.gainPoints(START_POINTS);
        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(commandRet.snd, is(Message.COME_TO_TOOL_ROOM));

        player.respond(RollCommand.ExitToolRoom);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
    }

    @Test
    public void should_buy_bomb_and_stay_in_wait_for_response_until_not_enough_points() {
        player.gainPoints(START_POINTS);
        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(commandRet.snd, is(Message.COME_TO_TOOL_ROOM));

        // 前四次购买, 点数足够, 可以一直购买
        for (int i = 0; i < START_POINTS / Tool.Bomb.points(); ++i) {
            Pair<Player.Status, Message> respondRet = player.respond(RollCommand.BuyBomb);
            assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
            assertThat(respondRet.snd, is(Message.BUY_BOMB_SUCCESS));
            assertThat(player.getQuantityByKind(Tool.Bomb), is(i + 1));
        }

        // 第五次购买, 点数不够, 自动退出
        Pair<Player.Status, Message> respondRet = player.respond(RollCommand.BuyBomb);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(respondRet.snd, is(Message.No_POINTS_BUY_ANYMORE_TOOL_EXIT));
        assertThat(player.getToolQuantityAmount(), is(START_POINTS / Tool.Bomb.points()));
        assertThat(player.getCurrentPoints(), is(START_POINTS - START_POINTS / Tool.Bomb.points() * Tool.Bomb.points()));
    }

    @Test
    public void should_buy_bomb_and_stay_in_wait_for_response_until_reaching_max_quantity_limit() {
        player.gainPoints(ENOUGH_POINTS);
        Pair<Player.Status, Message> commandRet = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(commandRet.snd, is(Message.COME_TO_TOOL_ROOM));

        // 前十次购买, 在数量限制内, 可以一直购买
        for (int i = 0; i < GameConstant.MAX_TOOL_QUANTITY; ++i) {
            Pair<Player.Status, Message> respondRet = player.respond(RollCommand.BuyBomb);
            assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
            assertThat(respondRet.snd, is(Message.BUY_BOMB_SUCCESS));
            assertThat(player.getQuantityByKind(Tool.Bomb), is(i + 1));
        }

        // 第十一次购买, 超过数量限制, 自动退出
        Pair<Player.Status, Message> respondRet = player.respond(RollCommand.BuyBomb);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(respondRet.snd, is(Message.TOOL_QUANTITY_LIMITED_EXIT));
        assertThat(player.getToolQuantityAmount(), is(GameConstant.MAX_TOOL_QUANTITY));
        assertThat(player.getCurrentPoints(), is(ENOUGH_POINTS - GameConstant.MAX_TOOL_QUANTITY * Tool.Bomb.points()));
    }

}
