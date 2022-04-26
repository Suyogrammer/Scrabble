/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * BackwardDictionary is a backward dictionary
 */
public class BackwardDictionary extends Trie{
    Scanner scanner;

    /**
     * @param spot is the dictionary file location
     */
    public BackwardDictionary(String spot) {
        Path path = Paths.get(spot);
        try {
            scanner = new Scanner(path);
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();
                this.insert(reverseWord(word));
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
