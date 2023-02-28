package rps.gui.controller;

// Java imports

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rps.bll.game.GameManager;
import rps.bll.game.Move;
import rps.bll.game.Result;
import rps.bll.game.ResultType;
import rps.bll.player.IPlayer;
import rps.bll.player.Player;
import rps.bll.player.PlayerType;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

/**
 *
 * @author smsj
 */
public class GameViewController implements Initializable {

    @FXML
    private Label labelPlayer1Name, labelPlayer2Name, labelPlayer1Score, labelPlayer2Score, labelRoundNumber, labelTieScore, labelRoundResult;
    @FXML
    private ImageView imagePlayerMove, imageBotMove, imageRock, imagePaper, imageScissors;
    private GameManager ge;
    private String playerName;
    private int roundNumber = 1;
    private int scorePlayer1, scorePlayer2, scoreTie;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
        labelRoundNumber.setText("Round " + roundNumber);
        imagePlayerMove.setScaleX(-1);
        imageRock.setImage(new Image("r.png"));
        imagePaper.setImage(new Image("p.png"));
        imageScissors.setImage(new Image("s.png"));
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

        ge.getGameState().setRoundNumber(roundNumber++);
        labelRoundNumber.setText("Round " + roundNumber);
        labelRoundResult.setText(result.getWinnerPlayer().getPlayerName() + "'s " + winnerMove + " " + result.getType().name() + " vs "
                + result.getLoserPlayer().getPlayerName() + "'s " + loserMove );
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
        IPlayer human = new Player(playerName, PlayerType.Human);
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

    public void setName(String name) {
        labelPlayer1Name.setText(name);
        playerName = name;
        startGame();
    }

    public void handleResetGame(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to reset?");
        alert.setContentText("Resetting will restart the entire game");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            Stage s = (Stage) labelRoundNumber.getScene().getWindow();
            s.close();

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rps/gui/view/NameInput.fxml"));
            Parent root = loader.load();
            stage.setTitle("Welcome to the not-implemented Rock-Paper-Scissor game!");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
}
