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
            Move randomKey = (Move) playerMoves.keySet().toArray()[randomIndex];

            return playerMoves.get(randomKey);
            //Implement better AI here...
        }

        return randomMove();
    }

    private Move randomMove() {
        Move[] moves = Move.values();
        Random random = new Random();
        int randomMove = random.nextInt(moves.length);

        return moves[randomMove];
    }

    private Map<Move, Move> convertResultHistory(List<Result> results) {
        Map<Move, Move> movesAndCounters = new HashMap<>();

        for (Result result : results) {
            Move playerMove;
            Move counter;
            PlayerType winner = result.getWinnerPlayer().getPlayerType();

            playerMove = (winner == PlayerType.Human) ? result.getWinnerMove() : result.getLoserMove();

            movesAndCounters.put(playerMove, Move.getCounter(playerMove));
        }

        return movesAndCounters;
    }
}
