package rich.game;

import java.util.Random;

public class Dice {
    public static int next() {
        int ret = new Random().nextInt(7);
        while (ret == 0) {
            ret = new Random().nextInt(7);
        }
        return ret;
    }
}
