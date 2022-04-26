/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * PLayer is the player of the scrabble game
 */
public class Player {
    private int score;
    private Tray tray;

    /**
     * @param tray constructs the player given the tray
     */
    public Player(Tray tray){
        this.tray = tray;
        this.score = 0;
    }

    /**
     * removeTiles removes the tiles from the player's tray
     * @param playedTiles is the tiles played
     */
    public void removeTiles(LinkedList<TrayRoom> playedTiles){
        Tile tile;
        for(TrayRoom c: playedTiles){
            tile = c.getTile();
            this.tray.getRack().remove(tile);
        }
    }

    /**
     * getTile gets the tile of the given letter
     * @param letter
     * @returns tile that contains the letter
     */
    public Tile getTile(char letter){
        ArrayList<Tile> rack = this.tray.getRack();
        for(Tile t: rack){
            if(t.getLetter() == letter) return t;
        }
        return null;
    }

    public ArrayList<Character> getStringRack(){
        ArrayList<Tile> rack = this.tray.getRack();
        ArrayList<Character> stringRack =  new ArrayList<>();
        for(Tile t: rack){
            stringRack.add(t.getLetter());
        }
        return stringRack;
    }

    /**
     * getTrayValue gets the score of the player's tray
     * @returns score for the tray
     */
    public int getTrayValue(){
        ArrayList<Tile> rack = this.tray.getRack();
        if(rack.size() == 0) return 0;
        int trayValue = 0;
        for(Tile t: rack){
            trayValue += t.getScore();
        }
        return  trayValue;
    }

    public void decScore(int count){
        this.score -= count;
    }

    public int getScore(){
        return this.score;
    }

    public void setTray(Tray tray){
        this.tray = tray;
    }

    public Tray getTray(){
        return this.tray;
    }

    public void getTile(TileContainer tileBag){
        tileBag.distributeTiles(1);
    }

    public void updateScore(int moveScore){
        score += moveScore;
    }

}
