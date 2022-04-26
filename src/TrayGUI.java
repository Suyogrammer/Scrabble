/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

/**
 * TrayGUI is the GUI for player's tray
 */
public class TrayGUI extends VBox {
    private final Tray tray;
    private final GridPane tiles;
    private TrayRoom[] playerTiles;
    private final GameManager manager;
    private final LinkedList<TrayRoom> selectedTrayRooms;

    /**
     * @param playerTray is the player's tray
     * @param manager is the game manager
     */
    public TrayGUI(Tray playerTray, GameManager manager){
        super(20);
        this.selectedTrayRooms = new LinkedList<>();
        this.tray = playerTray;
        this.tiles = new GridPane();
        this.manager = manager;
        this.setPrefHeight(100);
        this.setPrefWidth(700);
        this.setAlignment(Pos.CENTER);
        tiles.setHgap(3);
        drawTray(tray);

        HBox space = new HBox(20);
        space.setAlignment(Pos.CENTER);
        space.getChildren().add(tiles);

        Label playerTiles = new Label("GIVEN TILES");

        this.getChildren().add(space);
        this.getChildren().add(playerTiles);
        tiles.relocate(50,0);
    }

    /**
     * drawTray draws the tray
     * @param tray is the tray to draw
     */
    public void drawTray(Tray tray){
        int length = tray.getRack().size();
        Tile tile;
        playerTiles = new TrayRoom[length];
        char letter;

        for(int i = 0; i < length; i++){
            letter = tray.getRack().get(i).getLetter();
            tile = tray.getRack().get(i);
            if(letter == '.') letter = ' ';
            playerTiles[i] = new TrayRoom(0,i,30,30,letter, manager);
            playerTiles[i].setTile(tile);
            tiles.add(playerTiles[i],i,0);
        }
    }

    /**
     * recoverRooms helps to recover tray rooms in case of invalid move
     * @param selectedTrayRooms is the rooms that were selected
     */
    public void retrieveRooms(LinkedList<TrayRoom> selectedTrayRooms){
        for(Room c: selectedTrayRooms){
            int col = c.getCol();
            playerTiles[col].lastState();
            playerTiles[col].setTile(c.getTile());
            playerTiles[col].setLetter(c.getLetter());
        }
    }

    public void updateSelectedTrayRooms(TrayRoom room){
        this.selectedTrayRooms.add(room);
    }

    public LinkedList<TrayRoom> getSelectedTrayRooms(){
        return this.selectedTrayRooms;
    }

}
