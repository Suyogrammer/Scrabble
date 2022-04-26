/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import java.util.ArrayList;
import java.util.Random;

/**
 * TileContainer contains tiles for the game
 */

public class TileContainer {
    private final ArrayList<Tile> tileBag;
    private final ScrabbleLetter letter;

    public TileContainer(ScrabbleLetter letter){
        tileBag = new ArrayList<>();
        this.letter = letter;
        tileBag();
    }

    private void tileBag(){
        int frequency;
        int score;
        int f = 0;

        for(char i = 'a'; i <= 'z'; i++){
            frequency = letter.getFREQUENCY().get(i);
            score = letter.getVALUES().get(i);

            for(int j = 0; j < frequency; j++) {
                f = f + 1;
                tileBag.add(new Tile(i,score));
            }
        }

        frequency = letter.getFREQUENCY().get('*');
        for(int i = 0; i < frequency; i++){
            tileBag.add(new Tile(' ',0));
        }
    }

    /**
     * distributeTiles distributes the tiles
     * @param total is the amount of tiles
     * @returns list of tiles
     */
    public ArrayList<Tile> distributeTiles(int total){
        ArrayList<Tile> random = new ArrayList<>();
        Tile temp;
        if(getSize() < total) total = getSize();
        for(int i = 0; i < total; i++){
            temp = tileBag.get(new Random().nextInt(tileBag.size()));
            tileBag.remove(temp);
            random.add(temp);
        }
        return random;
    }

    public int getSize(){
        return this.tileBag.size();
    }

}
