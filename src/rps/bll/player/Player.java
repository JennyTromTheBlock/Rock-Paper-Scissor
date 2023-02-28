package rps.bll.player;

//Project imports
import rps.bll.game.IGameState;
import rps.bll.game.Move;
import rps.bll.game.Result;

//Java imports
import java.util.*;

/**
 * Example implementation of a player.
 *
 * @author smsj
 */
public class Player implements IPlayer {

    private String name;
    private PlayerType type;

    /**
     * @param name
     */
    public Player(String name, PlayerType type) {
        this.name = name;
        this.type = type;
    }


    @Override
    public String getPlayerName() {
        return name;
    }


    @Override
    public PlayerType getPlayerType() {
        return type;
    }


    /**
     * Decides the next move for the bot...
     * @param state Contains the current game state including historic moves/results
     * @return Next move
     */
    @Override
    public Move doMove(IGameState state) {
        //Historic data to analyze and decide next move...
        ArrayList<Result> results = (ArrayList<Result>) state.getHistoricResults();

        if (results.size() >= 5) {
            Map<Move, Move> playerMoves = convertResultHistory(results);

            Random random = new Random();
            int randomIndex = random.nextInt(playerMoves.size());

            Move counter = playerMoves.get(playerMoves.keySet().toArray()[randomIndex]);
            //Implement better AI here...
            return counter;
        }

        return randomMove();
    }

    private Move randomMove() {
        Random random = new Random();
        int randomMove = random.nextInt(3) + 1;
        Move move;

        if (randomMove == 1) {
            move = Move.Rock;
        }
        else if (randomMove == 2) {
            move = Move.Paper;
        }
        else move = Move.Scissor;

        return move;
    }

    private Map<Move, Move> convertResultHistory(List<Result> results) {
        Map<Move, Move> counters = new HashMap<>();

        for (Result result : results) {
            Move playerMove;
            Move counter;
            PlayerType winner = result.getWinnerPlayer().getPlayerType();

            playerMove = winner == PlayerType.Human ? result.getWinnerMove() : result.getLoserMove();

            if (playerMove == Move.Rock) {
                counter = Move.Paper;
            }
            else if (playerMove == Move.Paper) {
                counter = Move.Scissor;
            }
            else {
                counter = Move.Rock;
            }

            counters.put(playerMove, counter);
        }

        return counters;
    }
}
