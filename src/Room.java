/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import javafx.scene.control.*;

/**
 * Room is the superclass for the gridroom and the playable tiles
 */
public class Room extends Button {
    private int row;
    private int col;
    private int multiplier;
    private Tile tile;
    private char letter;
    private char assign;
    private GameManager manager;

    /**
     * @param row is the row
     * @param column is the column
     * @param width is the width
     * @param height is the height
     * @param text is the text value
     * @param manager is the game manager
     */
    public Room(int row, int column, double width, double height, char text, GameManager manager){
        this.row = row;
        this.col = column;
        this.multiplier = 1;
        this.letter = text;
        this.setPrefWidth(width);
        this.setPrefHeight(height);
        this.setText(Character.toString(text));
        this.manager = manager;
    }

    public void setLetter(char letter){
        this.letter = letter;
        this.setText(Character.toString(letter));
    }

    public void setBackground(String color){
        this.setStyle(color);
    }

    public char getLetter(){
        return this.letter;
    }


    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }

    public Tile getTile(){
        return this.tile;
    }

    public void setTile(Tile tile){
        this.tile = tile;
    }

    public int getMultiplier(){
        return this.multiplier;
    }

    public GameManager getManager(){
        return this.manager;
    }

    public char getAssign(){
        return this.assign;
    }

    public void setAssign(char assign){
        this.assign = assign;
    }

}
