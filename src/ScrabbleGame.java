/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * GUI for the scrabble game
 */
public class ScrabbleGame extends Scene{
    private final GameManager manager;
    private final ScoreBoard scoreBoard;
    private final Dictionary scrabbleDictionary;
    private final TileContainer tileBag;
    private final TrayGUI playerTrayGUI;
    private final Player human;
    private final Computer computer;
    private final BoardGUI boardGUI;

    /**
     *
     * @param width is the width of the scene
     * @param height is the height of the scene
     */
    public ScrabbleGame(Parent root, int width, int height){
        super(root,width,height);

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Sowpods.txt","Dictionary.txt","Animals.txt");
        dialog.getDialogPane().setContentText("Choose a file:");
        dialog.showAndWait();
        String dictionaryPath = "";

        if(dialog.getSelectedItem().equals("Sowpods.txt")) dictionaryPath = "resource/sowpods.txt";
        else if(dialog.getSelectedItem().equals("Dictionary.txt")) dictionaryPath = "resource/dictionary.txt";
        else if(dialog.getSelectedItem().equals("Animals.txt"))dictionaryPath = "resource/animals.txt";

        ScrabbleLetter scrabbleLetter = new ScrabbleLetter();
        tileBag = new TileContainer(scrabbleLetter);
        scrabbleDictionary = new Dictionary(dictionaryPath);
        ScrabbleBoard scrabbleBoard = new ScrabbleBoard("resource/scrabble_board.txt");
        this.manager = new GameManager(scrabbleDictionary,tileBag);
        manager.setDictionaryPath(dictionaryPath);
        manager.setScrabbleLetter(scrabbleLetter);
        boardGUI = new BoardGUI(scrabbleBoard, manager);
        manager.setBoardGUI(boardGUI);
        int MAX_TILES = 7;
        human = new Player(new Tray(tileBag.distributeTiles(MAX_TILES)));
        computer = new Computer(new Tray(tileBag.distributeTiles(MAX_TILES)), manager);
        playerTrayGUI = new TrayGUI(human.getTray(),manager);
        manager.setTrayGUI(playerTrayGUI);
        scoreBoard = new ScoreBoard(human,computer,tileBag);
        setupGUI((VBox) root);
    }

    /**
     * @param layout is the main layout of GUI
     */
    public void setupGUI(VBox layout){

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Vertical","Horizontal");
        choiceBox.setValue(" Horizontal / vertical");

        choiceBox.setOnAction(event -> {
            manager.setSelect(true);
            switch (choiceBox.getValue()){
                case "Horizontal":
                    manager.setType("ROW");
                    break;
                case "Vertical":
                    manager.setType("COL");
                    break;
            }
        });


        Button play = new Button("PLAY");
        play.setOnAction(event -> this.play());

        Button pass = new Button("Can't think. Pass Computer??");
        pass.setOnAction(event -> {
            manager.setPass(true);
            if(!manager.isStart()) {
                if (!boardGUI.getSelectedGridRooms().isEmpty()) {
                    playerTrayGUI.retrieveRooms(playerTrayGUI.getSelectedTrayRooms());
                    boardGUI.retrieveRooms(boardGUI.getSelectedGridRooms());
                    manager.resetRoomsPlayed(boardGUI, playerTrayGUI);
                }
                manager.playComputer(computer, scoreBoard, tileBag);
                boardGUI.updateAnchors(boardGUI.getGrid());
                if(manager.isGameOver(human,computer,tileBag,scoreBoard)) Platform.exit();
                manager.setPass(false);
                computer.setPass(false);
            }
            else{
                String message = "Please start the play first!!!";
                String header = "Invalid action intended!";
                manager.showAlert(Alert.AlertType.ERROR,message,header);
            }
        });

        Button quit = new Button("Exit game");
        quit.setOnAction(event -> {
        System.exit(0);});

        Label scrabbleHeading = new Label("SCRABBLE");
        scrabbleHeading.setAlignment(Pos.CENTER);
        scrabbleHeading.setTextFill(Color.RED);
        scrabbleHeading.setFont(Font.font("Verdana",50));

        HBox bottomBox = new HBox(10);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BASELINE_CENTER);
        buttonBox.setPrefHeight(100);
        buttonBox.setPrefWidth(1000);
        buttonBox.getChildren().addAll(choiceBox,play,pass,quit);

        HBox blankBox = new HBox(5);
        blankBox.getChildren().add(new Label(" "));

        VBox blankBox2 = new VBox(0);
        blankBox2.getChildren().add(new Label(""));
        blankBox2.getChildren().add(boardGUI);
        blankBox.getChildren().add(blankBox2);

        StackPane boardGroup = new StackPane();
        boardGroup.getChildren().add(boardGUI);
        boardGroup.getChildren().add(blankBox);

        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(scrabbleHeading);
        layout.getChildren().add(scoreBoard);
        layout.getChildren().add(boardGUI);
        layout.getChildren().add(playerTrayGUI);
        layout.getChildren().add(buttonBox);
    }


    /**
     * play method
     */
    public void play(){

        int startIndex;
        int sCount;
        boolean isMoveSequential;
        ArrayList<Object> data;
        boolean isValidMove;
        LinkedList<TrayRoom> selectedTrayRooms = playerTrayGUI.getSelectedTrayRooms();
        LinkedList<GridRoom> selectedGridRooms = boardGUI.getSelectedGridRooms();
        String constraint = manager.getType();
        GridRoom firstRoom = manager.getFirstRoom();
        sCount = boardGUI.getsCount();
        int indexConstraint = manager.getIndexType();

        if(!(sCount == 0) && manager.startCheck(selectedGridRooms)) {

            boolean multiple = manager.touchMultipleRooms(selectedGridRooms, boardGUI,constraint);
            if(multiple) {

                isValidMove = manager.configureMultipleWords(boardGUI,scrabbleDictionary, constraint);

                if(isValidMove){

                    isMoveSequential = manager.isMoveSequential(indexConstraint, selectedGridRooms,boardGUI,constraint);
                    startIndex = manager.configureStartIndex(indexConstraint, firstRoom, boardGUI, constraint);
                    data = manager.getPlayedWord(startIndex,indexConstraint,scrabbleDictionary,boardGUI,constraint,
                            false);

                    if(!data.get(0).equals(" ") && isMoveSequential) {
                        manager.playHuman(human,(int) (data.get(1)), scoreBoard, playerTrayGUI, tileBag);
                        manager.configureMultiplier(selectedGridRooms);
                        boardGUI.updateAnchors(boardGUI.getGrid());
                        manager.playComputer(computer,scoreBoard,tileBag);
                        boardGUI.updateAnchors(boardGUI.getGrid());
                    }
                    else{
                        playerTrayGUI.retrieveRooms(selectedTrayRooms);
                        boardGUI.retrieveRooms(selectedGridRooms);
                        String message = "Please check your tiles";
                        String header = "Check";
                        manager.showAlert(Alert.AlertType.WARNING,message,header);
                    }
                }
                boardGUI.clearnextIndex();
            }
            else{
                isMoveSequential = manager.isMoveSequential(indexConstraint, selectedGridRooms,boardGUI,constraint);
                startIndex = manager.configureStartIndex(indexConstraint, firstRoom, boardGUI, constraint);
                data = manager.getPlayedWord(startIndex,indexConstraint,scrabbleDictionary,boardGUI,constraint,false);

                if(isMoveSequential && !data.get(0).equals(" ")) {
                    manager.playHuman(human,(int) (data.get(1)), scoreBoard, playerTrayGUI, tileBag);
                    manager.configureMultiplier(selectedGridRooms);
                    boardGUI.updateAnchors(boardGUI.getGrid());
                    manager.playComputer(computer,scoreBoard,tileBag);
                    boardGUI.updateAnchors(boardGUI.getGrid());
                }
                else{
                    playerTrayGUI.retrieveRooms(selectedTrayRooms);
                    boardGUI.retrieveRooms(selectedGridRooms);
                    String message = "The word placed is not valid. Try Again!";
                    String header = "Invalid word!";
                    manager.showAlert(Alert.AlertType.ERROR,message,header);
                }
            }
            boardGUI.resetsCount();
            manager.resetFlags();
            manager.resetRoomsPlayed(boardGUI,playerTrayGUI);
        }
        else{
            if(sCount == 0) {
                String message = "Place tiles on the grid to play!!";
                String header = "No tiles placed!";
                manager.showAlert(Alert.AlertType.ERROR,message,header);
            }
            else {
                String message = "You need to start by placing the tile on the center.";
                String header = "Error";
                manager.showAlert(Alert.AlertType.ERROR,message,header);
                boardGUI.resetsCount();
                manager.resetFlags();
                playerTrayGUI.retrieveRooms(selectedTrayRooms);
                boardGUI.retrieveRooms(selectedGridRooms);
                manager.resetRoomsPlayed(boardGUI,playerTrayGUI);
            }
        }
        manager.setPlay(false);
        if(manager.isGameOver(human,computer,tileBag,scoreBoard)) Platform.exit();
    }

    public Dictionary getScrabbleDictionary(){
        return this.scrabbleDictionary;
    }

}
