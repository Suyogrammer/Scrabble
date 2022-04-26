/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Dictionary - dictionary file for the game
 */
public class Dictionary extends Trie {
    private Scanner scanner;

    /**
     * constructor
     * @param spot - spot for the dictionary file
     */
    public Dictionary(String spot) {
        Path path = Paths.get(spot);
        try {
            scanner = new Scanner(path);
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();
                this.insert(word);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
