package rps.bll.game;

import javafx.scene.image.Image;

/**
 * The various move options in the game
 *
 * @author smsj
 */
public enum Move {
    Rock,
    Paper,
    Scissor;

    public static Move getCounter(Move move) {
        if (move == Rock) {
            return Paper;
        }
        else if (move == Paper) {
            return Scissor;
        }
        else {
            return Rock;
        }
    }

    public static String getMoveImagePath(Move move) {
        String rockPath = "r.png";
        String paperPath = "p.png";
        String scissorPath = "s.png";

        if (move == Rock) {
            return rockPath;
        }
        else if (move == Paper) {
            return paperPath;
        }
        else {
            return scissorPath;
        }
    }
}


