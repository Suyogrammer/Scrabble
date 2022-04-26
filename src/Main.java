/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main - main class for the scrabble game
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox layout = new VBox(20);
        ScrabbleGame scrabble = new ScrabbleGame(layout,600,900);
        primaryStage.setTitle("Scrabble");
        primaryStage.setScene(scrabble);
        primaryStage.show();
        primaryStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
