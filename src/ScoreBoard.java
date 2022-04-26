/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Scoreboard display scores
 */
public class ScoreBoard extends HBox {
    private Label humanScore;
    private Label computerScore;
    private final Player human;
    private final Player computer;
    private Label tilesRemaining;
    private final TileContainer tileBag;

    /**
     * @param human is human player
     * @param computer is computer player
     * @param tileBag is tile bag for the game
     */
    public ScoreBoard(Player human, Player computer, TileContainer tileBag){
        super(95);
        this.human = human;
        this.computer = computer;
        this.tileBag = tileBag;
        Label hScore = new Label("Human:");
        hScore.setFont(new Font(25));

        humanScore = new Label("0");
        humanScore.setFont(new Font(30));

        Label cScore = new Label("Computer:");
        cScore.setFont(new Font(25));

        computerScore = new Label("0");
        computerScore.setFont(new Font(30));

        tilesRemaining = new Label(Integer.toString(tileBag.getSize()));
        tilesRemaining.setFont(new Font(60));

        HBox humanBox = new HBox(10);
        humanBox.setAlignment(Pos.CENTER);

        HBox computerBox = new HBox(20);
        computerBox.setAlignment(Pos.CENTER);

        VBox updates = new VBox(10);
        updates.setPrefHeight(310);
        updates.setAlignment(Pos.TOP_LEFT);

        StackPane group = new StackPane();
        group.getChildren().addAll(tilesRemaining);

        humanBox.getChildren().addAll(hScore, humanScore);
        computerBox.getChildren().addAll(cScore,computerScore);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(humanBox,group,computerBox);
    }

    /**
     * updateScore updates the score for player and computer
     */
    public void updateScore(){
        humanScore.setText(Integer.toString(human.getScore()));
        computerScore.setText(Integer.toString(computer.getScore()));
    }

    /**
     * updateTilesRemaining shows the remaining tiles
     */
    public void updateTilesRemaining(){
        tilesRemaining.setText(Integer.toString(tileBag.getSize()));
    }

    /**
     * setHumanScore sets the human score
     * @param score
     */
    public void setHumanScore(int score){
        this.humanScore.setText(Integer.toString(score));
    }

    /**
     * setComputerScore sets the computer score
     * @param score
     */
    public void setComputerScore(int score){
        this.humanScore.setText(Integer.toString(score));
    }
}
