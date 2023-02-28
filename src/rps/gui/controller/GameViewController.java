package rps.gui.controller;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import rps.bll.game.GameManager;
import rps.bll.game.Move;
import rps.bll.game.Result;
import rps.bll.game.ResultType;
import rps.bll.player.IPlayer;
import rps.bll.player.Player;
import rps.bll.player.PlayerType;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 *
 * @author smsj
 */
public class GameViewController implements Initializable {

    @FXML
    private Label labelPlayer1Name, labelPlayer2Name, labelPlayer1Score, labelPlayer2Score, labelRoundNumber, labelTieScore, labelPlayer1Move, labelPlayer2Move;
    @FXML
    private ImageView imagePlayerMove, imageBotMove;
    private GameManager ge;
    private int roundNumber = 1;
    private int scorePlayer1, scorePlayer2, scoreTie;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
        labelRoundNumber.setText("Round " + roundNumber);
        startGame();
    }

    public void handleRock(ActionEvent actionEvent) {
        ge.playRound(Move.valueOf("Rock"));
        endRound();
    }

    public void handlePaper(ActionEvent actionEvent) {
        ge.playRound(Move.valueOf("Paper"));
        endRound();
    }

    public void handleScissors(ActionEvent actionEvent) {
        ge.playRound(Move.valueOf("Scissor"));
        endRound();
    }

    /**
     * Helper method to count up round no. & scores after each round
     */
    private void endRound() {
        Result result = (Result) ge.getGameState().getHistoricResults().toArray()[ge.getGameState().getHistoricResults().size()-1];
        PlayerType winner = result.getWinnerPlayer().getPlayerType();
        ResultType type = result.getType();
        Move winnerMove = result.getWinnerMove();
        Move loserMove = result.getLoserMove();

        if (type == ResultType.Tie) {
            scoreTie++;
            labelTieScore.setText("Ties " + scoreTie);
            imagePlayerMove.setImage(getMove(winnerMove));
            imageBotMove.setImage(getMove(winnerMove));
        }
        else if (winner == PlayerType.Human) {
            scorePlayer1++;
            labelPlayer1Score.setText(String.valueOf(scorePlayer1));
            imagePlayerMove.setImage(getMove(winnerMove));
            imageBotMove.setImage(getMove(loserMove));

        } else {
            scorePlayer2++;
            labelPlayer2Score.setText(String.valueOf(scorePlayer2));
            imagePlayerMove.setImage(getMove(loserMove));
            imageBotMove.setImage(getMove(winnerMove));
        }
        imagePlayerMove.setScaleX(-1);

        ge.getGameState().setRoundNumber(roundNumber++);
        labelRoundNumber.setText("Round " + roundNumber);
    }

    private Image getMove(Move move) {
        String imagePath = "";
        if (move == Move.Paper) {
            imagePath = "p.png";
        }
        if (move == Move.Rock) {
            imagePath = "r.png";
        }
        if (move == Move.Scissor) {
            imagePath = "s.png";
        }
        Image image = new Image(imagePath);
        return image;
    }

    /**
     * Starts the game
     */
    public void startGame() {
        IPlayer human = new Player("playerName", PlayerType.Human);
        IPlayer bot = new Player(getRandomBotName(), PlayerType.AI);

        labelPlayer1Name.setText(human.getPlayerName());
        labelPlayer2Name.setText(bot.getPlayerName());

        ge = new GameManager(human, bot);
    }

    /**
     * Famous robots...
     * @return
     */
    private String getRandomBotName() {
        String[] botNames = new String[] {
                "R2D2",
                "Mr. Data",
                "3PO",
                "Bender",
                "Marvin the Paranoid Android",
                "Bishop",
                "Robot B-9",
                "HAL"
        };
        int randomNumber = new Random().nextInt(botNames.length - 1);
        return botNames[randomNumber];
    }
}
