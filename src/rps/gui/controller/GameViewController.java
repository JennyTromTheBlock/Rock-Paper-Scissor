package rps.gui.controller;

// Java imports

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import rps.bll.game.GameManager;
import rps.bll.game.Move;
import rps.bll.game.Result;
import rps.bll.game.ResultType;
import rps.bll.player.IPlayer;
import rps.bll.player.Player;
import rps.bll.player.PlayerType;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 *
 * @author smsj
 */
public class GameViewController implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FlowPane flowPaneHistory;
    @FXML
    private Label labelRockCountHuman, labelRockCountBot, labelPaperCountHuman, labelPaperCountBot, labelScissorsCountHuman, labelScissorsCountBot;
    @FXML
    private Label labelPlayer1Name, labelPlayer2Name, labelPlayer1Score, labelPlayer2Score, labelRoundNumber, labelTieScore, labelRoundResult;
    @FXML
    private ImageView imagePlayerMove, imageBotMove, imageRock, imagePaper, imageScissors, imageStatsRockHuman, imageStatsPaperHuman, imageStatsScissorsHuman, imageStatsRockBot, imageStatsPaperBot, imageStatsScissorsBot;
    private GameManager ge;
    private String playerName;
    private int roundNumber = 1;
    private int scorePlayer1, scorePlayer2, scoreTie;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelRoundNumber.setText("Round " + roundNumber);
        imagePlayerMove.setScaleX(-1);
        imageRock.setImage(new Image("r.png"));
        imagePaper.setImage(new Image("p.png"));
        imageScissors.setImage(new Image("s.png"));
        imageStatsRockHuman.setImage(new Image("r.png"));
        imageStatsPaperHuman.setImage(new Image("p.png"));
        imageStatsScissorsHuman.setImage(new Image(("s.png")));
        imageStatsRockBot.setImage(new Image("r.png"));
        imageStatsPaperBot.setImage(new Image("p.png"));
        imageStatsScissorsBot.setImage(new Image(("s.png")));
        moveAnimations();
    }

    public void moveAnimations() {

        TranslateTransition translate = new TranslateTransition();

        translate.setNode(imagePlayerMove);
        translate.setNode(imageBotMove);
        translate.setDuration(Duration.millis(400));
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.setByX(2);
        translate.setByY(5);
        translate.setAutoReverse(true);
        translate.play();


        RotateTransition rotate = new RotateTransition();

        rotate.setNode(imagePlayerMove);
        //rotate.setNode(imageBotMove);
        rotate.setDuration(Duration.millis(400));
        rotate.setCycleCount(TranslateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setByAngle(10);
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setAutoReverse(true);
        rotate.play();
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
        List<Result> results = new ArrayList<>(ge.getGameState().getHistoricResults());
        Result result = results.get(results.size() - 1);

        PlayerType winner = result.getWinnerPlayer().getPlayerType();
        ResultType type = result.getType();
        Move winnerMove = result.getWinnerMove();
        Move loserMove = result.getLoserMove();

        if (type == ResultType.Tie) {
            handleTie(winnerMove);
        }
        else if (winner == PlayerType.Human) {
            handlePlayerWin(winnerMove, loserMove);

        } else {
            handleBotWin(loserMove, winnerMove);
        }

        ge.getGameState().setRoundNumber(roundNumber++);
        labelRoundNumber.setText("Round " + roundNumber);
        labelRoundResult.setText(result.getWinnerPlayer().getPlayerName() + "'s " + winnerMove + " " + result.getType().name() + " vs "
                + result.getLoserPlayer().getPlayerName() + "'s " + loserMove );

        updateStats(result);
        addToHistory();
    }

    private void updatePlayerImages(Move playerMove, Move botMove) {
        Image playerMoveImage = getMove(playerMove);
        Image botMoveImage = getMove(botMove);
        imagePlayerMove.setImage(playerMoveImage);
        imageBotMove.setImage(botMoveImage);
    }

    private void updatePlayerImages(Move tieMove) {
        Image moveImage = getMove(tieMove);
        imagePlayerMove.setImage(moveImage);
        imageBotMove.setImage(moveImage);
    }

    private void handleTie(Move move) {
        scoreTie++;
        labelTieScore.setText("Ties " + scoreTie);

        updatePlayerImages(move);
    }

    private void handlePlayerWin(Move playerMove, Move botMove) {
        scorePlayer1++;
        labelPlayer1Score.setText(String.valueOf(scorePlayer1));

        updatePlayerImages(playerMove, botMove);
    }

    private void handleBotWin(Move playerMove, Move botMove) {
        scorePlayer2++;
        labelPlayer2Score.setText(String.valueOf(scorePlayer2));

        updatePlayerImages(playerMove, botMove);
    }

    private Image getMove(Move move) {
        return new Image(Move.getMoveImagePath(move));
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

    private void updateStats(Result roundResult) {
        int rockCountHuman = 0;
        int paperCountHuman = 0;
        int scissorsCountHuman = 0;
        int rockCountBot = 0;
        int paperCountBot = 0;
        int scissorsCountBot = 0;
        int total = 0;
        for(Result result : ge.getGameState().getHistoricResults()) {
            total++;
            if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human && result.getType() == ResultType.Win) {
                if (result.getWinnerMove() == Move.Rock) {
                    rockCountHuman++;
                    scissorsCountBot++;
                } else if (result.getWinnerMove() == Move.Paper) {
                    paperCountHuman++;
                    rockCountBot++;
                } else {
                    scissorsCountHuman++;
                    paperCountBot++;
                }
            } else if (result.getWinnerPlayer().getPlayerType() == PlayerType.Human && result.getType() == ResultType.Tie) {
                if (result.getWinnerMove() == Move.Rock) {
                    rockCountHuman++;
                    rockCountBot++;
                } else if (result.getWinnerMove() == Move.Paper) {
                    paperCountHuman++;
                    paperCountBot++;
                } else {
                    scissorsCountHuman++;
                    scissorsCountBot++;
                }
            } else {
                if (result.getWinnerMove() == Move.Rock) {
                    rockCountBot++;
                    paperCountHuman++;
                } else if (result.getWinnerMove() == Move.Paper) {
                    paperCountBot++;
                    rockCountHuman++;
                } else {
                    scissorsCountBot++;
                    paperCountHuman++;
                }
            }
        }

        labelRockCountHuman.setText(Math.round(rockCountHuman*100/total) + "%");
        labelPaperCountHuman.setText(Math.round(paperCountHuman*100/total) + "%");
        labelScissorsCountHuman.setText(Math.round(scissorsCountHuman*100/total) + "%");
        labelRockCountBot.setText(Math.round(rockCountBot*100/total) + "%");
        labelPaperCountBot.setText(Math.round(paperCountBot*100/total) + "%");
        labelScissorsCountBot.setText(Math.round(scissorsCountBot*100/total) + "%");
    }

    private void addToHistory() {
        GridPane history = new GridPane();
        history.add(new Label("Round "), 0, 0);
        history.add(new Label(String.valueOf(roundNumber-1)), 1, 0);

        ImageView imagePlayerMoveHistory = new ImageView(new Image(imagePlayerMove.getImage().getUrl()));
        imagePlayerMoveHistory.setFitWidth(30);
        imagePlayerMoveHistory.setFitHeight(30);
        ImageView imageBotMoveHistory = new ImageView(new Image(imageBotMove.getImage().getUrl()));
        imageBotMoveHistory.setFitWidth(30);
        imageBotMoveHistory.setFitHeight(30);

        history.add(imagePlayerMoveHistory,0,1);
        history.add(imageBotMoveHistory,1,1);

        history.setPadding(new Insets(5, 5, 5, 5));
        history.setBorder(Border.stroke(Color.GREY));
        history.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        flowPaneHistory.getChildren().add(history);
        Platform.runLater(() -> scrollPane.setVvalue(1));

    }
}
