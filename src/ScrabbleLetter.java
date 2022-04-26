/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import java.util.HashMap;

/**
 * ScrabbleLetter - class for scrabble letters in the game
 */
public class ScrabbleLetter{
    private final HashMap<Character,Integer> VALUES;
    private final HashMap<Character,Integer> FREQUENCY;
    private final int[] SCORES;
    private final int[] RECURRENCE;

    public ScrabbleLetter(){
        VALUES = new HashMap<>();
        FREQUENCY = new HashMap<>();
        SCORES = new int[] {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
        RECURRENCE = new int[] {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1,2};
        mapValues();
    }

    /**
     * mapValues sets the value and frequency for each letter
     */
    private void mapValues(){
        int count = 0;
        for (char i = 'a'; i <= 'z'; i++){
            VALUES.put(i, SCORES[count]);
            FREQUENCY.put(i,RECURRENCE[count]);
            count++;
        }
        FREQUENCY.put('*',2);
    }

    public HashMap<Character, Integer> getVALUES(){
        return this.VALUES;
    }

    public HashMap<Character, Integer> getFREQUENCY(){
        return this.FREQUENCY;
    }

    public int[] getSCORES(){
        return this.SCORES;
    }
}
