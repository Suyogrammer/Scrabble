/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * BoardGUI represents the actual scrabble board in game
 */
public class BoardGUI extends GridPane {
    final private int width;
    private int sCount;
    private ScrabbleBoard board;
    private GridRoom[][] grid;
    private String[][] gridBluePrint;
    private String[][] anchors;
    private GameManager manager;
    private ArrayList<Integer> nextIndex;
    private LinkedList<GridRoom> selectedGridRooms;

    public BoardGUI(ScrabbleBoard board, GameManager manager){
        this.board = board;
        this.sCount = 0;
        this.manager = manager;
        this.selectedGridRooms = new LinkedList<>();
        this.nextIndex = new ArrayList<>();
        this.setAlignment(Pos.CENTER);
        this.setHgap(5);
        this.setVgap(5);
        width = board.getWidth();
        gridBluePrint = board.getGrid();
        this.anchors = new String[width][width];
        for(int i = 0; i < width; i++){
            for(int j = 0; j  < width; j++){
                anchors[i][j] = "..";
            }
        }
        scrabbleGrid();
    }

    /**
     * scrabbleGrid draws the scrabble grid
     */
    public void scrabbleGrid(){
        this.grid = new GridRoom[width][width];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < width; j++){
                switch (gridBluePrint[i][j]) {
                    case "..":
                        grid[i][j] = new GridRoom(i, j, 30, 30, ' ',manager);
                        break;
                    case "2.":
                        grid[i][j] = new GridRoom(i, j, 30, 30, ' ',manager);
                        grid[i][j].setBackground("-fx-background-color: #ff66cc; ");
                        grid[i][j].setwordIncrease(2);
                        break;
                    case ".2":
                        grid[i][j] = new GridRoom(i, j, 30,30, ' ',manager);
                        grid[i][j].setBackground("-fx-background-color: #00b2ff; ");
                        grid[i][j].setincrease(2);
                        break;
                    case "3.":
                        grid[i][j] = new GridRoom(i, j, 30, 30, ' ',manager);
                        grid[i][j].setBackground("-fx-background-color: #fd1d1d; ");
                        grid[i][j].setwordIncrease(3);
                        break;
                    case ".3":
                        grid[i][j] = new GridRoom(i, j, 30, 30, ' ',manager);
                        grid[i][j].setBackground("-fx-background-color: #1e34eb; ");
                        grid[i][j].setincrease(3);
                        break;
                }
                this.add(grid[i][j],j,i);
            }
        }
    }

    /**
     * retrieveRooms helps to retrieve played tiles if invalid move
     * @param select  tiles that were placed
     */
    public void retrieveRooms(LinkedList<GridRoom> select) {
        for (GridRoom c : select) {
            int row = c.getRow();
            int col = c.getCol();
            grid[row][col].lastState();
            grid[row][col].setTile(null);
            grid[row][col].setLetter(' ');
        }
    }

    /**
     * updates anchors after each play
     * @param gridRoom used for updating gridRoom
     */
    public void updateAnchors(GridRoom[][] gridRoom){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < width; j++){
                if(!(gridRoom[i][j].getLetter() == ' ')) anchors[i][j] = "WW";
                else{
                    if((check(i,j,'u',gridRoom)) && (check(i,j,'d',gridRoom))) anchors[i][j] = "MC";
                    else if((check(i,j,'l',gridRoom)) && (check(i,j,'r',gridRoom))) anchors[i][j] = "MR";
                    else if((check(i,j,'l',gridRoom))) anchors[i][j] = "*R";
                    else if((check(i,j,'r',gridRoom))) anchors[i][j] = "L*";
                    else if((check(i,j,'u',gridRoom))) anchors[i][j] = "*D";
                    else if((check(i,j,'a',gridRoom))) anchors[i][j] = "U*";
                }
            }
        }
    }

    /**
     * check helps to determine the type of anchor point
     * @param row is row of anchor point
     * @param col is column of anchor point
     * @param type is the type of check
     * @param grid is our scrabble grid
     * @returns true if the checks are valid, false otherwise
     */
    private boolean check(int row, int col, char type, GridRoom[][] grid){
        if(type == 'l'){
            if(col - 1 < 0) return false;
            return !(grid[row][col - 1].getLetter() == ' ');
        }
        else if(type == 'r'){
            if(col + 1 >= width) return false;
            return !(grid[row][col + 1].getLetter() == ' ');
        }
        else if(type == 'u'){
            if(row - 1 < 0) return false;
            return !(grid[row - 1][col].getLetter() == ' ');
        }
        else{
            if(row + 1 >= width) return false;
            return !(grid[row + 1][col].getLetter() == ' ');
        }
    }


    public GridRoom[][] getGrid(){ return this.grid; }

    public String[][] getAnchors() { return this.anchors; }

    public int getBoardWidth(){ return this.width; }

    public void  clearnextIndex(){ this.nextIndex.clear(); }

    public ArrayList<Integer> getnextIndex() { return this.nextIndex; }

    public LinkedList<GridRoom> getSelectedGridRooms(){ return this.selectedGridRooms; }

    public void updatesCount(){ this.sCount += 1; }

    public void updateSelectedGridRooms(GridRoom room){ this.selectedGridRooms.add(room); }

    public void resetsCount(){ this.sCount = 0; }

    public int getsCount(){ return this.sCount; }



}
