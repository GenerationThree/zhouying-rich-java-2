package rich.game;

import com.sun.tools.javac.util.Pair;
import rich.command.*;
import rich.place.Land;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    private static Game game = new Game();
    private static Pair<Player.Status, Message> commandRet;
    private static Pair<Player.Status, Message> responseRet;

    public static void main(String[] args) {
        game.setPlayers(initializePlayers());

        List<Player> players = game.getPlayers();
        while (true) {
            for (Player player : players) {
                if (player.getWaitTimes() > 0) {
                    System.out.println(player.getName() + "当前回合等待, 下一个玩家");
                    player.setWaitTimes(player.getWaitTimes() - 1);
                    continue;
                }
                turnOf(player);
            }
        }
    }

    private static void turnOf(Player player) {
        while (player.getStatus() != Player.Status.END_TURN) {
            if (player.getStatus() == Player.Status.WAIT_FOR_COMMAND) {
                Command command = getUserCommand(player);
                commandRet = player.execute(command);
            } else {
                Response response = getUserResponse(player);
                responseRet = player.respond(response);
            }
        }
        game.getMap().showFor();
        player.setStatus(Player.Status.WAIT_FOR_COMMAND);
    }

    private static Response getUserResponse(Player player) {
        Scanner scanner = new Scanner(System.in);
        String responseString;
        if (commandRet.snd == Message.COME_TO_EMPTY_LAND) {
            Land land = (Land) player.getCurrentPlace();
            while (true) {
                System.out.print("是否购买该处空地, " + land.getPrice() + "元 (Y/N)?\n" + player.getName() + "> ");
                responseString = scanner.nextLine().toLowerCase();
                if (responseString.equals("y"))
                    return RollCommand.YesToBuy;
                else if (responseString.equals("n"))
                    return RollCommand.NoToBuy;
            }
        } else if (commandRet.snd == Message.COME_TO_SELF_LAND) {
            Land land = (Land) player.getCurrentPlace();
            System.out.print("是否升级该处地产, " + land.getPrice() + "元 (Y/N)?\n" + player.getName() + "> ");
            while (true) {
                responseString = scanner.nextLine().toLowerCase();
                if (responseString.equals("y"))
                    return RollCommand.YesToUpgrade;
                else if (responseString.equals("n"))
                    return RollCommand.NoToUpgrade;
            }
        }
        return null;
    }

    private static List<Player> initializePlayers() {
        int start_money = getStartMoney();
        System.out.print("请选择2~4位不重复玩家，输入编号即可(1.钱夫人; 2.阿土伯; 3.孙小美; 4.金贝贝), 如输入12: \n> ");
        Scanner scanner = new Scanner(System.in);
        Map<Integer, String> names = new HashMap<Integer, String>() {{
            put(1, "钱夫人");
            put(2, "阿土伯");
            put(3, "孙小美");
            put(4, "金贝贝");
        }};

        List<Integer> numbers;
        List<Player> players = new ArrayList<>();
        while (true) {
            String read = scanner.nextLine();
            try {
                List<Integer> tmp = Arrays.stream(read.split("")).map(Integer::parseInt).collect(Collectors.toList());
                if (tmp.stream().distinct().count() < tmp.size()) {
                    System.out.print("玩家编号重复, 请重新输入:\n> ");
                    continue;
                }

                tmp = tmp.stream().distinct().collect(Collectors.toList());
                if (tmp.stream().anyMatch(i -> i < 1 || i > 4)) {
                    System.out.print("玩家编号不在范围内, 请重新输入:\n> ");
                    continue;
                }

                if (tmp.size() < 2) {
                    System.out.print("玩家数量不足, 请重新输入:\n> ");
                    continue;
                }
                numbers = tmp;
                break;
            } catch (NumberFormatException e) {
                System.out.print("输入不合法, 请输入数字): \n> ");
            }
        }
        players.addAll(numbers.stream().map(i -> new Player(game.getMap(), names.get(i), start_money)).collect(Collectors.toList()));
        return players;
    }

    private static Command getUserCommand(Player player) {
        System.out.print(player.getName() + "> ");
        Scanner scanner = new Scanner(System.in);
        Command command;
        while (true) {
            String input = scanner.nextLine();
            command = parseInputToCommand(input);
            if (command == null) {
                System.out.print("输入命令不合法, 请重新输入, 输入help获取帮助信息: \n" + player.getName() + "> ");
                continue;
            }
            return command;
        }
    }

    private static Command parseInputToCommand(String input) {
        String[] args = input.split(" ");
        Command ret = null;

        if (args.length > 2) ret = null;

        if (args.length == 1) {
            String command = args[0].toLowerCase();
            if (command.equals("roll")) {
                ret = new RollCommand(game.getMap(), Dice.next());
            } else if (command.toLowerCase().equals("robot")) {
                ret = new RobotCommand(game.getMap());
            } else if (command.equals("query")) {
                ret = new QueryCommand();
            } else if (command.equals("help")) {
                ret = new HelpCommand();
            } else {
                ret = null;
            }
        }

        if (args.length == 2) {
            String command = args[0].toLowerCase();
            if (command.equals("sell"))
                ret = new SellLandCommand(game.getMap(), Integer.parseInt(args[1]));
            else if (command.equals("selltool")) {
                switch (Integer.parseInt(args[1])) {
                    case 1:
                        ret = new SellToolCommand(Tool.Block);
                        break;
                    case 2:
                        ret = new SellToolCommand(Tool.Bomb);
                        break;
                    case 3:
                        ret = new SellToolCommand(Tool.Robot);
                        break;
                }
            }
        }
        return ret;
    }

    private static int getStartMoney() {
        System.out.print("请设置初始玩家资金(1000~50000), 默认10000, 按Enter确认:\n> ");
        Scanner scanner = new Scanner(System.in);
        int ret = GameConstant.DEFAULT_STARTING_BALANCE;
        while (true) {
            String read = scanner.nextLine();
            if (read.isEmpty()) break;

            try {
                ret = Integer.parseInt(read);
                if (ret < 1000 || ret > 50000) {
                    System.out.print("输入不合法, 初始金钱范围1000~50000, 请重新输入:\n> ");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.print("输入不合法, 请输入数字(1000~50000)\n> ");
            }
        }
        return ret;
    }
}
