package rich.game;

import rich.place.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {
    private List<Place> places;
    private Map<Integer, Tool> toolOnPosition;
    private List<Player> players;

    public GameMap() {
        toolOnPosition = new HashMap<>();
        places = new ArrayList<>();
        players = new ArrayList<>();
        initializePlaces();
    }

    public Place move(Place currentPlace, int step) {
        int position = places.indexOf(currentPlace);
        int target = (position + step) % length();

        return places.get(target);
    }

    public Message putTool(Tool tool, int targetPosition) {
        if (tool.equals(Tool.Bomb)) {
            if (isPlayerOn(targetPosition)) {
                return Message.CANT_PUT_BOMB_WHEN_PLAYER_ON;
            } else if (isToolOn(targetPosition)) {
                return Message.CANT_PUT_BOMB_WHEN_EXIST_TOOL;
            } else {
                toolOnPosition.put(targetPosition, tool);
                return Message.PUT_BOMB_SUCCESS;
            }
        }
        else {
            if (isPlayerOn(targetPosition)) {
                return Message.CANT_PUT_BLOCK_WHEN_PLAYER_ON;
            } else if (isToolOn(targetPosition)) {
                return Message.CANT_PUT_BLOCK_WHEN_EXIST_TOOL;
            } else {
                toolOnPosition.put(targetPosition, tool);
                return Message.PUT_BLOCK_SUCCESS;
            }
        }
    }

    private boolean isPlayerOn(int targetPosition) {
        for (Player player : players) {
            if (findByPlace(player.getCurrentPlace()) == targetPosition) return true;
        }
        return false;
    }

    private boolean isToolOn(int targetPosition) {
        return toolOnPosition.getOrDefault(targetPosition, null) != null;
    }

    public Message robotClean(int curPosition) {
        for (int i = 0; i < GameConstant.ROBOT_CLEAN_RANGE; ++i) {
            int targetPosition = (curPosition + i) % length();
            if (isToolOn(targetPosition)) {
                toolOnPosition.remove(targetPosition);
            }
        }
        return Message.ROBOT_CLEAN_SUCCESS;
    }

    public Place findByPosition(int position) {
        if (position < 0 || position > 69) return null;
        return places.get(position);
    }

    public int findByPlace(Place place) {
        return places.indexOf(place);
    }

    public Place getStarting() {
        return places.get(0);
    }

    public void display() {
        printFirstLine();
        printMiddle();
        printLastLine();
    }

    private void printLastLine() {
        final int PLACE_QUANTITY_OF_LAST_LINE = 29;
        for (int i = 0; i < PLACE_QUANTITY_OF_LAST_LINE; ++i) {
            char symbol = 'M';
            if (places.get(i) instanceof MagicRoom) symbol = 'M';
            else if (places.get(i) instanceof Land) symbol = '0';
            else if (places.get(i) instanceof Prison) symbol = 'P';
            else if (places.get(i) instanceof GiftRoom) symbol = 'G';
            System.out.print(symbol);
        }
        System.out.print("\n");
    }

    private void printMiddle() {
        final int MIDDLE_LINES_QUANTITY = 6;
        String line = "$                           0";
        for (int i = 0; i < MIDDLE_LINES_QUANTITY; ++i)
            System.out.println(line);
    }

    private void printFirstLine() {
        final int PLACE_QUANTITY_OF_FIRST_LINE = 29;
        for (int i = 0; i < PLACE_QUANTITY_OF_FIRST_LINE; ++i) {
            char symbol = 'S';
            if (places.get(i) instanceof Starting) symbol = 'S';
            else if (places.get(i) instanceof Land) symbol = '0';
            else if (places.get(i) instanceof Hospital) symbol = 'H';
            else if (places.get(i) instanceof ToolRoom) symbol = 'T';
            System.out.print(symbol);
        }
        System.out.print("\n");
    }

    private void initializePlaces() {
        places.add(new Starting());
        for (int i = 0; i < GameConstant.FIRST_DISTRICT_LAND_QUANTITY; ++i)
            places.add(new Land(GameConstant.FIRST_DISTRICT_LAND_PRICE));
        places.add(new Hospital());
        for (int i = 0; i < GameConstant.SECOND_DISTRICT_LAND_QUANTITY; ++i)
            places.add(new Land(GameConstant.SECOND_DISTRICT_LAND_PRICE));
        places.add(new ToolRoom());
        for (int i = 0; i < GameConstant.THIRD_DISTRICT_LAND_QUANTITY; ++i) {
            places.add(new Land(GameConstant.THIRD_DISTRICT_LAND_PRICE));
        }
        places.add(new GiftRoom());
        for (int i = 0; i < GameConstant.FOURTH_DISTRICT_LAND_QUANTITY; ++i) {
            places.add(new Land(GameConstant.FOURTH_DISTRICT_LAND_PRICE));
        }
        places.add(new Prison());
        for (int i = 0; i < GameConstant.FIFTH_DISTRICT_LAND_QUANTITY; ++i) {
            places.add(new Land(GameConstant.FIFTH_DISTRICT_LAND_PRICE));
        }
        places.add(new MagicRoom());
        places.add(new MineralEstate(20));
        places.add(new MineralEstate(80));
        places.add(new MineralEstate(100));
        places.add(new MineralEstate(40));
        places.add(new MineralEstate(80));
        places.add(new MineralEstate(60));
    }

    public int length() {
        return places.size();
    }

    public Tool getToolOnPosition(int position) {
        return toolOnPosition.getOrDefault(position, null);
    }

    public boolean canPutTool(int targetPosition) {
        return !(isPlayerOn(targetPosition) || isToolOn(targetPosition));
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}
