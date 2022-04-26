/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import java.util.ArrayList;

/**
 * Tray for the players in the scrabble game
 */
public class Tray {
    private final ArrayList<Tile> rack;

    public Tray (ArrayList<Tile> rack){
        this.rack = rack;
    }

    public ArrayList<Tile> getRack(){
        return this.rack;
    }

    /**
     * getStringRack helps to get a String rack from the given the tray
     * @returns the arraylist of the characters of the rack
     */
    public ArrayList<Character> getStringRack(){
        ArrayList<Character> charRack = new ArrayList<>();
        for(Tile t: rack){
            charRack.add(t.getLetter());
        }
        return charRack;
    }

}
