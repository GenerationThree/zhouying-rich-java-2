package rich.unit;

import com.sun.tools.javac.util.Pair;
import rich.GameMap;
import rich.Message;
import rich.Player;
import rich.command.Command;
import rich.command.RollCommand;
import org.junit.Before;
import org.junit.Test;
import rich.place.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RollCommandTest {

    private Player player;
    private GameMap map;
    int step;
    private Command roll;

    @Before
    public void before() {
        map = mock(GameMap.class);
        player = new Player();
        step = 1;
        roll = new RollCommand(map, step);
    }

    @Test
    public void should_wait_for_response_when_roll_to_empty_land() {
        Place emptyLand = mock(Land.class);

        when(map.move(any(), eq(step))).thenReturn(emptyLand);
        when(emptyLand.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_EMPTY_LAND));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(ret.snd, is(Message.COME_TO_EMPTY_LAND));
    }

    @Test
    public void should_wait_for_response_when_roll_to_self_land() {
        Land selfLand = mock(Land.class);
        when(map.move(any(), eq(step))).thenReturn(selfLand);
        when(selfLand.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_SELF_LAND));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(ret.snd, is(Message.COME_TO_SELF_LAND));
    }

    @Test
    public void should_end_turn_when_roll_to_others_land_and_pay_pass_fee() {
        Land othersLand = mock(Land.class);
        when(map.move(any(), eq(step))).thenReturn(othersLand);
        when(othersLand.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.END_TURN, Message.COME_TO_OTHERS_LAND_PAY_SUCCESSFUL));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(ret.snd, is(Message.COME_TO_OTHERS_LAND_PAY_SUCCESSFUL));
    }

    @Test
    public void should_game_over_when_roll_to_others_land_without_enough_pass_fee() {
        Land othersLand = mock(Land.class);
        when(map.move(any(), eq(step))).thenReturn(othersLand);
        when(othersLand.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.GAME_OVER, Message.COME_TO_OTHERS_LAND_PAY_FAILED));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.GAME_OVER));
        assertThat(ret.snd, is(Message.COME_TO_OTHERS_LAND_PAY_FAILED));
    }

    @Test
    public void should_end_turn_when_roll_to_others_land_and_no_need_to_pay_for_mascot() {
        Land othersLand = mock(Land.class);
        when(map.move(any(), eq(step))).thenReturn(othersLand);
        when(othersLand.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.END_TURN, Message.COME_TO_OTHERS_LAND_WITH_MASCOT));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(ret.snd, is(Message.COME_TO_OTHERS_LAND_WITH_MASCOT));
    }

    @Test
    public void should_end_turn_when_roll_to_others_land_and_no_need_to_pay_for_owner_is_in_prison() {
        Land othersLand = mock(Land.class);
        when(map.move(any(), eq(step))).thenReturn(othersLand);
        when(othersLand.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.END_TURN, Message.COME_TO_OTHERS_LAND_WITH_OWNER_IN_PRISON));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(ret.snd, is(Message.COME_TO_OTHERS_LAND_WITH_OWNER_IN_PRISON));
    }

    @Test
    public void should_end_turn_when_roll_to_hospital() {
        Hospital hospital = mock(Hospital.class);
        when(map.move(any(), eq(step))).thenReturn(hospital);
        when(hospital.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.END_TURN, Message.NULL));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(ret.snd, is(Message.NULL));
    }

    @Test
    public void should_wait_for_response_when_come_to_tool_room() {
        ToolRoom toolRoom = mock(ToolRoom.class);
        when(map.move(any(), eq(step))).thenReturn(toolRoom);
        when(toolRoom.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_TOOL_ROOM));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(ret.snd, is(Message.COME_TO_TOOL_ROOM));
    }

    @Test
    public void should_end_turn_when_come_to_tool_room_but_without_enough_point_to_buy_cheapest_tool() {
        ToolRoom toolRoom = mock(ToolRoom.class);
        when(map.move(any(), eq(step))).thenReturn(toolRoom);
        when(toolRoom.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.END_TURN, Message.AUTO_EXIT_TOOL_ROOM));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(ret.snd, is(Message.AUTO_EXIT_TOOL_ROOM));
    }

    @Test
    public void should_wait_for_response_when_roll_to_gift_room() {
        GiftRoom giftRoom = mock(GiftRoom.class);
        when(map.move(any(), eq(step))).thenReturn(giftRoom);
        when(giftRoom.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.WAIT_FOR_RESPONSE, Message.COME_TO_GIFT_ROOM));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.WAIT_FOR_RESPONSE));
        assertThat(ret.snd, is(Message.COME_TO_GIFT_ROOM));
    }

    @Test
    public void should_end_turn_when_roll_to_prison() {
        Prison prison = mock(Prison.class);
        when(map.move(any(), eq(step))).thenReturn(prison);
        when(prison.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.END_TURN, Message.COME_TO_PRISON));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(ret.snd, is(Message.COME_TO_PRISON));
    }

    @Test
    public void should_end_turn_when_roll_to_magic_room() {
        MagicRoom magicRoom = mock(MagicRoom.class);
        when(map.move(any(), eq(step))).thenReturn(magicRoom);
        when(magicRoom.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.END_TURN, Message.NULL));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(ret.snd, is(Message.NULL));
    }

    @Test
    public void should_end_turn_when_roll_to_mineral_estate() {
        MineralEstate mineralEstate = mock(MineralEstate.class);
        when(map.move(any(), eq(step))).thenReturn(mineralEstate);
        when(mineralEstate.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.END_TURN, Message.COME_TO_MINERAL_ESTATE));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(ret.snd, is(Message.COME_TO_MINERAL_ESTATE));
    }

    @Test
    public void should_end_turn_when_roll_to_starting() {
        Starting starting = mock(Starting.class);
        when(map.move(any(), eq(step))).thenReturn(starting);
        when(starting.actionTo(eq(player))).thenReturn(new Pair<>(Player.Status.END_TURN, Message.NULL));

        Pair<Player.Status, Message> ret = player.execute(roll);
        assertThat(player.getStatus(), is(Player.Status.END_TURN));
        assertThat(ret.snd, is(Message.NULL));
    }

}
