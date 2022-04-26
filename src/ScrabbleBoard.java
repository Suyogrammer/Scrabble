/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ScrabbleBoard represents scrabble board
 */

public class ScrabbleBoard{
    private int width;
    private String[][] grid;

    public ScrabbleBoard(String boardConfig){
        makeBoard(boardConfig);
    }

    /**
     * makeBoard constructs the board
     * @param board is the path to the file
     */
    private void makeBoard(String board){
        Path path = Paths.get(board);
        ArrayList<String> temp = new ArrayList<>();
        int index = 0;
        try {
            Scanner scan = new Scanner(path);
            while(scan.hasNext()){
                String block = scan.next();
                if(!block.equals(" ")) temp.add(block);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        width = Integer.parseInt(temp.get(0));
        temp.remove(0);
        grid = new String[width][width];

        for(int i = 0; i < width; i++){
            for(int j = 0; j < width; j++) {
                grid[i][j] = temp.get(index);
                index++;
            }
        }

    }

    public String[][] getGrid(){
        return this.grid;
    }

    public int getWidth(){ return this.width;
    }


}
