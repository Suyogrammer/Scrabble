/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

/**
 * Scrabble tile
 */
public class Tile {
    private final int score;
    private final char letter;

    public Tile(char letter, int score){
        this.score = score;
        this.letter = letter;
    }

    public char getLetter(){
        return this.letter;
    }

    public int getScore(){
        return this.score;
    }

}
