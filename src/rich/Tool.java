package rich;

import rich.game.GameConstant;

public enum Tool {
    Bomb {
        @Override
        public int points() {
            return GameConstant.BOMB_POINTS;
        }
    }, Robot {
        @Override
        public int points() {
            return GameConstant.ROBOT_POINTS;
        }
    }, Block {
        @Override
        public int points() {
            return GameConstant.BLOCK_POINTS;
        }
    };

    public abstract int points();
}
