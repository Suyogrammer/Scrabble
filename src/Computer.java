/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */


import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Computer is the  computer player of the game
 */
public class Computer extends Player{
    private final GameManager manager;
    private final GridRoom[][] gridRooms;
    private LinkedList<GridRoom> currentMove;
    private LinkedList<GridRoom> highMove;
    private final BackwardDictionary backwardDictionary;
    private final Dictionary dictionary;
    private final ScrabbleLetter letter;
    private int sCount;
    private boolean pass;
    
    /**
     * @param tray
     * @param manager
     */
    public Computer(Tray tray, GameManager manager) {
        super(tray);
        this.currentMove = new LinkedList<>();
        this.highMove = new LinkedList<>();
        this.sCount = 0;
        this.manager = manager;
        this.pass = false;
        this.backwardDictionary = new BackwardDictionary(manager.getDictionaryPath());
        this.dictionary = manager.getDictionary();
        this.letter =  manager.getScrabbleLetter();
        this.gridRooms = manager.getBoardGUI().getGrid();
    }

    /**
     * play function tries to get the highest score move
     * @param boardGUI is the scrabble grid
     * @param dictionary
     * @param manager
     * @param isComputer
     * @returns true if playable, false otherwise
     */
    public boolean play(BoardGUI boardGUI, Dictionary dictionary, GameManager manager, boolean isComputer){
        int width = boardGUI.getBoardWidth();
        String[][] grid = boardGUI.getAnchors();
        GridRoom[][] gridRooms = boardGUI.getGrid();
        for(int i = 0; i < width; i++){
            for(int j = 0; j < width; j++){
                if(!(grid[i][j].equals("*")) && !(grid[i][j].equals("W"))){
                    switch (grid[i][j]) {
                        case "L*": {
                            int limit;
                            Trie.TrieNode node;
                            String reverse;
                            ArrayList<Object> data;
                            String word;
                            int partScore;

                            data = manager.getPlayedWord(j + 1, i, dictionary, boardGUI, "ROW", true);
                            word = (String) data.get(0);
                            partScore = (int) data.get(1);

                            if (word.length() == 1) reverse = word;
                            else reverse = dictionary.reverseWord(word);

                            node = backwardDictionary.getLastNode(reverse);

                            limit = determineLimit('L',boardGUI,i,j);

                            playRow(reverse, node, limit, i, j, this.getStringRack(),false);
                            if (!currentMove.isEmpty() && (currentMove.get(0) != null)) {
                                int moveScore = partScore + updateScore(currentMove);
                                if(moveScore > sCount || highMove.isEmpty()){
                                    sCount = moveScore;
                                    highMove = currentMove;
                                }
                                currentMove = new LinkedList<>();
                            }
                            break;
                        }
                        case "*R": {
                            int limit;
                            Trie.TrieNode node;
                            ArrayList<Object> data;
                            String word;
                            int partScore;

                            int startIndex = manager.configureStartIndex(i, gridRooms[i][j - 1], boardGUI, "ROW");

                            data = manager.getPlayedWord(startIndex, i, dictionary, boardGUI, "ROW", true);
                            partScore = (int) data.get(1);
                            word = (String) data.get(0);
                            node = dictionary.getLastNode(word);

                            limit = determineLimit('R',boardGUI,i,j);

                            playRow(word, node, limit, i, j, this.getStringRack(),true);
                            if (!currentMove.isEmpty() && (currentMove.get(0) != null)){
                                int moveScore = partScore + updateScore(currentMove);
                                if(moveScore > sCount || highMove.isEmpty()){
                                    sCount = moveScore;
                                    highMove = currentMove;
                                }
                                currentMove = new LinkedList<>();
                            }
                            break;
                        }
                        case "U*": {
                            int limit;
                            Trie.TrieNode node;
                            String reverse;
                            ArrayList<Object> data;
                            String word;
                            int partScore;

                            data = manager.getPlayedWord(i + 1, j, dictionary, boardGUI, "COL", true);
                            word = (String) data.get(0);
                            partScore = (int) data.get(1);

                            if (word.length() == 1) reverse = word;
                            else reverse = dictionary.reverseWord(word);

                            node = backwardDictionary.getLastNode(reverse);

                            limit = determineLimit('U',boardGUI,i,j);

                            playColumn(reverse, node, limit, i, j, this.getStringRack(),false);
                            if (!currentMove.isEmpty() && (currentMove.get(0) != null)) {

                                int moveScore = partScore + updateScore(currentMove);
                                if(moveScore > sCount || highMove.isEmpty()){
                                    sCount = moveScore;
                                    highMove = currentMove;
                                }
                                currentMove = new LinkedList<>();
                            }
                            break;
                        }
                        case "*D": {
                            int limit;
                            Trie.TrieNode node;
                            ArrayList<Object> data;
                            int startIndex = manager.configureStartIndex(j, gridRooms[i - 1][j], boardGUI, "COL");
                            String word;
                            int partScore;

                            data = manager.getPlayedWord(startIndex, j, dictionary, boardGUI, "COL", true);
                            word = (String) data.get(0);
                            partScore = (int) data.get(1);

                            node = dictionary.getLastNode(word);

                            limit = determineLimit('D',boardGUI,i,j);

                            playColumn(word, node, limit, i, j, this.getStringRack(),false);
                            if (!currentMove.isEmpty() && (currentMove.get(0) != null)) {
                                int moveScore = partScore + updateScore(currentMove);
                                if(moveScore > sCount || highMove.isEmpty()){
                                    sCount = moveScore;
                                    highMove = currentMove;
                                }
                                currentMove = new LinkedList<>();

                            }
                            break;
                        }

                    }
                }
            }
        }
        Alert notPlayable = new Alert(Alert.AlertType.ERROR);
        notPlayable.setContentText("Computer passed (Doesn't have playable tiles)");
        notPlayable.setTitle("Cannot play");
        if(highMove.isEmpty()) {
            if(isComputer) {
                if (manager.getTileBag().getSize() > 0) {
                    this.play(boardGUI, dictionary, manager, true);
                } else {
                    notPlayable.show();
                    this.setPass(true);
                }
            }
            return false;
        }
        else {
            if(isComputer) {
                this.updateScore(sCount);
                setGrid(highMove);
                removeTray(highMove);
                highMove.clear();
                currentMove.clear();
                sCount = 0;
            }
            else {
                highMove.clear();
                currentMove.clear();
                sCount = 0;
            }
            return true;
        }
    }

    /**
     * playRow function makes a horizontal move from the anchor point
     * @param word for the word adjacent to the anchor rooms
     * @param node is the current node
     * @param limit sets the limit of the plY
     * @param anchorR is the anchor row
     * @param anchorC is the anchor column
     * @param box makes the play
     * @param anchorCheck checks to see if the anchor is in which direction
     * @returns true if playable, false otherwise
     */
    private boolean playRow(String word,Trie.TrieNode node, int limit, int anchorR, int anchorC,
                             ArrayList<Character> box, boolean anchorCheck){

        if(limit > 0){
            for(int i = 0; i < 26; i++){
                char c = (char) ('a' + i);
                Trie.TrieNode curr = node.getChild(i);
                Character alphabet = c;
                if(curr != null && (box.contains(alphabet) || box.contains('*'))) {
                    boolean isWild = false;
                    GridRoom room;
                    if (reView(manager.getBoardGUI(), alphabet, anchorR, anchorC, "ROW", manager)) {
                        Tile t = new Tile(alphabet, letter.getSCORES()[i]);
                        room = new GridRoom(anchorR, anchorC, 1, 1, alphabet, manager);
                        room.setincrease(gridRooms[anchorR][anchorC].getMultiplier());
                        room.setTile(t);
                        if (!(box.contains(alphabet))) {
                            Character blank = '*';
                            box.remove(blank);
                            isWild = true;
                        } else box.remove(alphabet);

                        boolean recurse;
                        if (anchorCheck) {
                            recurse = playRow(word + alphabet, curr, limit - 1, anchorR, anchorC + 1,
                                    box, true);
                        } else {
                            recurse = playRow(word + alphabet, curr, limit - 1, anchorR, anchorC - 1,
                                    box, false);
                        }
                        if (curr.isCompleteWord || recurse) {
                            currentMove.add(room);
                            return true;
                        } else {
                            currentMove.remove(room);
                            if (isWild) {
                                box.add('*');
                            } else {
                                box.add(alphabet);
                            }
                        }

                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * playColumn function tries to make a vertical move starting from the anchor point
     * @param partialWord is the word adjacent to the anchor rooms
     * @param node is the current node for the partial word in our trie
     * @param limit plays limit for the move
     * @param anchorR is anchor row
     * @param anchorC is anchor column
     * @param rack is the rack to make the play
     * @param forwardMove checks to see if the anchor is in top or bottom
     * @returns true if playable, false otherwise
     */
    private boolean playColumn(String partialWord, Trie.TrieNode node, int limit, int anchorR, int anchorC,
                               ArrayList<Character> rack, boolean forwardMove){

        if(limit > 0){
            for(int i = 0; i < 26; i++){
                char c = (char) ('a' + i);
                Trie.TrieNode curr = node.getChild(i);
                Character alphabet = c;
                if(curr != null && (rack.contains(alphabet) || rack.contains('*'))) {
                    boolean isWild = false;
                    GridRoom room;

                    if (reView(manager.getBoardGUI(), alphabet, anchorR, anchorC, "COL", manager)) {

                        Tile t = new Tile(alphabet, letter.getSCORES()[i]);
                        room = new GridRoom(anchorR, anchorC, 1, 1, alphabet, manager);
                        room.setincrease(gridRooms[anchorR][anchorC].getincrease());
                        room.setTile(t);
                        if (!(rack.contains(alphabet))) {
                            Character blank = '*';
                            rack.remove(blank);
                            isWild = true;
                        } else rack.remove(alphabet);

                        boolean recurse;
                        if (forwardMove) {
                            recurse = playColumn(partialWord + alphabet, curr, limit - 1, anchorR + 1, anchorC, rack, true);
                        } else {
                            recurse = playColumn(partialWord + alphabet, curr, limit - 1, anchorR - 1, anchorC, rack, false);
                        }
                        if (curr.isCompleteWord || recurse) {
                            currentMove.add(room);
                            return true;
                        } else {
                            currentMove.remove(room);
                            if (isWild) {
                                rack.add('*');
                            } else {
                                rack.add(alphabet);
                            }
                        }

                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * removeTray removes the played tiles from the tray
     * @param rooms
     */

    private void removeTray(LinkedList<GridRoom> rooms){
        for(GridRoom c: rooms){
            char ch = c.getLetter();
            Tile t = this.getTile(ch);
            this.getTray().getRack().remove(t);
        }
    }

    /**
     * setGrid sets the grid room for computer move
     * @param rooms
     */
    private void setGrid(LinkedList<GridRoom> rooms){
        for(GridRoom c: rooms){
            int row = c.getRow();
            int col = c.getCol();
            gridRooms[row][col].setLetter(c.getLetter());
            gridRooms[row][col].setincrease(1);
            gridRooms[row][col].setwordIncrease(1);
            gridRooms[row][col].setTile(c.getTile());
        }
    }

    /**
     * updateScore updates the score for the given move
     * @param rooms
     * @returns the score of the move
     */
    private int updateScore(LinkedList<GridRoom> rooms){
        int wordIncrease = 1;
        int score = 0;
        int length = rooms.size();
        if(length == 7) score += 50;
        for(GridRoom c : rooms){
            wordIncrease = c.getwordIncrease();
            int increase = c.getincrease();
            int scores = c.getTile().getScore();
            score += wordIncrease * scores;
        }
        return score * wordIncrease;

    }

    /**
     * reView reviews for each tile played
     * @param boardGUI is the scrabble grid
     * @param letter is the letter we are trying to play
     * @param anchorR is the row of anchor room
     * @param anchorC is the column of anchor room
     * @param type is the type of move
     * @param manager is the manager of the game
     * @returns true if satisfies the reviews, false otherwise
     */
    private boolean reView(BoardGUI boardGUI, Character letter, int anchorR, int anchorC, String type,
                           GameManager manager){
        Room room;
        GridRoom[][] grid = boardGUI.getGrid();
        String word = "";
        boolean between = false;
        if(type.equals("ROW") && maintainBounds(anchorR,anchorC,'R')){
            room = grid[anchorR - 1][anchorC];
            if(!(grid[anchorR - 1][anchorC].getLetter() == ' ')){
                int startIndex = manager.configureStartIndex(anchorC, room, boardGUI, "COL");
                word = (String) manager.getPlayedWord(startIndex,anchorC,dictionary,boardGUI,"COL",true).get(0);
                word = word + letter;
                between = true;
            }
            if(!(grid[anchorR + 1][anchorC].getLetter() == ' ')){
                if (!between) {
                    word += letter;
                }
                word += (String) manager.getPlayedWord(anchorR + 1,anchorC,dictionary,boardGUI,"COL",true).get(0);
            }
            return dictionary.search(word);

        }
        else if(type.equals("COL") && maintainBounds(anchorR, anchorC, 'C')){
            room = grid[anchorR][anchorC - 1];
            if(!(grid[anchorR - 1][anchorC].getLetter() == ' ')){
                int startIndex = manager.configureStartIndex(anchorR, room, boardGUI, "ROW");
                word = (String) manager.getPlayedWord(startIndex,anchorR,dictionary,boardGUI,"ROW",true).get(0);
                word = word + letter;
                between = true;
            }
            if(!(grid[anchorR + 1][anchorC].getLetter() == ' ')){
                if (!between) {
                    word += letter;
                }
                word += (String) manager.getPlayedWord(anchorC + 1,anchorR,dictionary,boardGUI,"ROW",true).get(0);
            }
            return dictionary.search(word);
        }
        return true;
    }



    /**
     * This function helps in configuring the limit for the move
     * @param type is the type of move
     * @param boardGUI is the scrabble grid
     * @param row  starting row
     * @param column  starting column
     * @returns limit for the move
     */
    private int determineLimit(char type, BoardGUI boardGUI, int row, int column){
        String[][] grid = boardGUI.getAnchors();
        int size = this.getTray().getRack().size();

        if(type == 'L'){
            int count = 0;
            while(((column - 1) > 0) && count <= size && !grid[row][column - 1].equals("W")){
                column = column - 1;
                count++;
            }
            if((column - 1 >= 0) && !grid[row][column - 1].equals("W")) return count;
            else return count - 1;
        }
        else if(type == 'R'){
            int count = 0;
            while(((column + 1) < 15) && count <= size && !grid[row][column + 1].equals("W") ){
                column = column + 1;
                count++;
            }
            if((column + 1 < 15) && !grid[row][column + 1].equals("W")) return count;
            else return count - 1;
        }
        else if(type == 'U'){
            int count = 0;
            while(((row - 1) > 0) && count <= size && !grid[row - 1][column].equals("W")){
                row = row - 1;
                count++;
            }
            if((row - 1 >= 0)  && !grid[row - 1][column].equals("W")) return count;
            else return count - 1;
        }
        else{
            int count = 0;
            while(((row + 1) < 15) && count <= size && !grid[row + 1][column].equals("W")){
                row = row + 1;
                count++;
            }
            if((row + 1 < 15) && !grid[row + 1][column].equals("W")) return count;
            else return count - 1;
        }

    }

    /**
     * This function helps in maintaining bound type of the scrabble grid for the move
     * @param row
     * @param column
     * @param type os the type of move
     * @returns true if safe, false otherwise
     */

    private boolean maintainBounds(int row, int column, char type){
        if(row < 0 || row >= 15) return false;
        if(column < 0 || column >= 15) return false;
        if(type == 'R'){
            if(row - 1 < 0) return false;
            return row + 1 < 15;
        }
        else if(type == 'C') {
            if (column - 1 < 0) return false;
            return column + 1 < 15;
        }
        return true;
    }


    public LinkedList<GridRoom> getHighMove(){
        return this.highMove;
    }

    public void setHighMove(LinkedList<GridRoom> move){
        this.highMove = move;
    }

    public boolean isPass(){
        return this.pass;
    }

    public void setPass(boolean flag){
        this.pass = flag;
    }

    public int getsCount(){
        return this.sCount;
    }

    public void setsCount(int count){
        this.sCount = count;
    }

}

