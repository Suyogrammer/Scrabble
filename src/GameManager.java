/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Manager - Game manager for the scrabble game
 */
public class GameManager {
    private boolean select;
    private boolean validMove;
    private boolean start;
    private boolean firstMove ;
    private boolean play;
    private boolean pass;
    private String type;
    private String dictionaryPath;
    private int indexType;
    private char assignedVal;
    private GridRoom firstRoom;
    private Tile assignedTile;
    private BoardGUI boardGUI;
    private TrayGUI trayGUI;
    private final Dictionary dictionary;
    private ScrabbleLetter letter;
    private final TileContainer tileBag;
    private final Alert alert;

    /**
     * @param dictionary is the dictionary file
     * @param tileBag is tile bag for the game
     */
    public GameManager(Dictionary dictionary, TileContainer tileBag){
        this.select = false;
        this.play = false;
        this.validMove = false;
        this.type = "N";
        this.start = true;
        this.firstMove = false;
        this.assignedTile = null;
        this.indexType = -1;
        this.firstRoom = null;
        this.dictionary = dictionary;
        this.pass = false;
        this.tileBag = tileBag;
        this.alert = new Alert(Alert.AlertType.WARNING);
    }

    /**
     * playHuman function helps to configure human move
     * @param human is human player
     * @param score is score of the move
     * @param scoreBoard is scoreboard
     * @param playerTrayGUI is the GUI for the player's tray
     * @param tileBag is tile bag
     */
    public void playHuman(Player human, int score, ScoreBoard scoreBoard, TrayGUI playerTrayGUI, TileContainer tileBag){
        if(boardGUI.getSelectedGridRooms().size() == 7) score += 50;
        human.updateScore(score);
        scoreBoard.setHumanScore(human.getScore());
        human.removeTiles(trayGUI.getSelectedTrayRooms());
        int total = 7 - human.getTray().getRack().size();
        human.getTray().getRack().addAll(tileBag.distributeTiles(total));
        playerTrayGUI.drawTray(human.getTray());
        scoreBoard.updateTilesRemaining();
        colorSelectedRooms(boardGUI.getSelectedGridRooms(), boardGUI);
    }

    /**
     * playComputer function helps to configure computer moves
     * @param computer is the computer player
     * @param scoreBoard is the scoreboard of the game
     * @param tileBag is the tile bag of the game
     */
    public void playComputer(Computer computer, ScoreBoard scoreBoard, TileContainer tileBag){
        int total;
        computer.play(boardGUI,dictionary,this,true);
        scoreBoard.setComputerScore(computer.getScore());
        total = 7 - computer.getTray().getRack().size();
        computer.getTray().getRack().addAll(tileBag.distributeTiles(total));
        scoreBoard.updateTilesRemaining();
        scoreBoard.updateScore();
    }

    /**
     * isMoveSequential function checks if the move is sequential
     * @param indexType is the index value given the type
     * @param selectedGridRooms is the grid rooms selected
     * @param boardGUI is the scrabble grid
     * @param type is the type of move
     * @returns true if sequential, false otherwise
     */
    public boolean isMoveSequential(int indexType, LinkedList<GridRoom> selectedGridRooms, BoardGUI boardGUI,
                                    String type){
        int length = selectedGridRooms.size();
        GridRoom[][] grid = boardGUI.getGrid();
        Alert illegalMove = new Alert(Alert.AlertType.WARNING);
        illegalMove.setContentText("Please place the tiles horizontally or vertically");
        illegalMove.setTitle("Illegal Move");

        if(type.equals("ROW")){
            sortList(selectedGridRooms,"row");
            for(int i = 0; i < length; i++){
                if(i > 0) {
                    int currCol = selectedGridRooms.get(i).getCol();
                    int prevCol = selectedGridRooms.get(i - 1).getCol();
                    int diff = currCol - prevCol;
                    if(diff != 1) {
                        if(grid[indexType][currCol - 1].getText().equals(" ")) {
                            setValidMove(false);
                            illegalMove.show();
                            return false;
                        }
                    }
                }
            }
        }
        else if(type.equals("COL")){
            sortList(selectedGridRooms,"col");
            for(int i = 0; i < length; i++){
                if(i > 0) {
                    int currRow = selectedGridRooms.get(i).getRow();
                    int prevRow = selectedGridRooms.get(i - 1).getRow();

                    int diff = currRow - prevRow;
                    if(diff != 1) {
                        if(grid[currRow - 1][indexType].getText().equals(" ")) {
                            setValidMove(false);
                            illegalMove.show();
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * configureStartIndex function helps to configure the start index of the word on board
     * @param indexType is the index Type
     * @param firstRoom is the room of the word
     * @param boardGUI is the grid(scrabble)
     * @param type is the type of move
     * @returns the start index
     */
    public int configureStartIndex(int indexType, Room firstRoom, BoardGUI boardGUI, String type){
        int index;
        String cSi;
        index = indexType;
        Room[][] grid = boardGUI.getGrid();
        if(type.equals("ROW")){
            int col = firstRoom.getCol();
            cSi = grid[index][col].getText();
            while(!cSi.equals(" ")){
                col -=1;
                try{
                    cSi = grid[index][col].getText();
                }
                catch(ArrayIndexOutOfBoundsException e){
                    cSi = grid[index][col + 1].getText();
                    col = col + 1;
                    break;
                }
            }
            index = col + 1;
        }
        else if(type.equals("COL")){
            int row = firstRoom.getRow();
            cSi = grid[row][index].getText();
            while(!cSi.equals(" ")){
                row -= 1;
                try {
                    cSi = grid[row][index].getText();
                }
                catch(ArrayIndexOutOfBoundsException e){
                    cSi =grid[row + 1][index].getText();
                    row = row + 1;
                    break;
                }
            }
            index = row + 1;
        }
        return index;
    }

    /**
     * configureMultipleWords function helps to configure multiple words
     * @param boardGUI is scrabble grid
     * @param dictionary is the dictionary
     * @param type is type of move
     * @returns true if multiple words are valid, false otherwise
     */
    public boolean configureMultipleWords(BoardGUI boardGUI, Dictionary dictionary, String type){
        int start;

        ArrayList<Integer> adjacentIndexes = boardGUI.getnextIndex();
        ArrayList<Object> data = new ArrayList<>();
        GridRoom firstRoom = getFirstRoom();

        if(type.equals("ROW")){
            for(int i: adjacentIndexes){
                start = configureStartIndex(i,firstRoom,boardGUI,"C");
                data.addAll(getPlayedWord(start,i,dictionary,boardGUI,"C",false));
                if((int) data.get(1) == 0) return false;
            }
        }
        else if(type.equals("COL")){
            for(int i: adjacentIndexes){
                start = configureStartIndex(i,firstRoom,boardGUI,"R");
                data.addAll(getPlayedWord(start,i,dictionary,boardGUI,"R",false));
                if((int) data.get(1) == 0) return false;
            }
        }

        return true;
    }

    /**
     * getPlayedWord function returns the word played along with its score value
     * @param startIndex is the starting index
     * @param indexType is the index value of the type
     * @param dictionary is the dictionary
     * @param boardGUI is the scrabble grid
     * @param type is the type of move
     * @param isComp returns true if computer makes the move, false otherwise
     * @returns data of the move made
     */
    public ArrayList<Object> getPlayedWord(int startIndex, int indexType, Dictionary dictionary,
                                           BoardGUI boardGUI, String type, boolean isComp){
        ArrayList<Object> data = new ArrayList<>(3);
        int index;
        GridRoom room;
        String currWord = "";
        int currMoveScore = 0;
        int wordMultiplier = 1;
        index = indexType;
        int width = boardGUI.getBoardWidth();
        GridRoom[][] grid = boardGUI.getGrid();

        if(type.equals("ROW")){
            for(int col = startIndex; col < width; col++){
                room = grid[index][col];
                if(isComp) System.out.println("Row: " + room.getRow() + " Col: " + room.getCol() + " word is: " + room.getText());
                if(room.getText().equals(" ")) break;
                currWord += room.getText();
                wordMultiplier *= room.getwordIncrease();
                currMoveScore += room.getMultiplier() * room.getTile().getScore();
            }
            currMoveScore *= wordMultiplier;
        }
        else if(type.equals("COL")){
            for(int row = startIndex; row < width; row++){
                room = grid[row][index];
                if(isComp) System.out.println("Row: " + room.getRow() + " Col: " + room.getCol() + " word is: " + room.getText());
                if(room.getText().equals(" ")) break;
                currWord += room.getText();
                wordMultiplier *= room.getwordIncrease();
                currMoveScore += room.getMultiplier() * room.getTile().getScore();
            }
            currMoveScore *= wordMultiplier;
        }

        data.add(currWord);
        data.add(currMoveScore);
        data.add(true);

        if(!isComp) {
            if (!dictionary.search(currWord)) {
                data.set(1, 0);
                data.set(2, false);
                data.set(0, " ");
            }
        }

        return data;
    }

    /**
     * touchMultipleCells function checks if the move made touches other rooms
     * @param move is the move made
     * @param boardGUI is the scrabble grid
     * @param type is the type of move
     * @returns true if touches multiple rooms, false otherwise
     */
    public Boolean touchMultipleRooms(LinkedList<GridRoom> move, BoardGUI boardGUI, String type){
        boolean isTouch = false;
        Room[][] grid = boardGUI.getGrid();
        ArrayList<Integer> adjacentIndexes = boardGUI.getnextIndex();
        for(Room c: move){
        }
        if(type.equals("ROW")){
            for(Room c: move){
                try {
                    if (!(grid[c.getRow() - 1][c.getCol()].getLetter() == ' ') || !(grid[c.getRow() + 1][c.getCol()].getLetter() == ' ')) {
                        adjacentIndexes.add(c.getCol());
                    }
                }
                catch(ArrayIndexOutOfBoundsException e){
                    if(c.getRow() - 1 < 0) {
                        if(!(grid[c.getRow()][c.getCol()].getLetter() == ' ')) adjacentIndexes.add(c.getCol());
                    }
                    if(c.getRow() + 1 > 15){
                        if(!(grid[c.getRow()][c.getCol()].getLetter() == ' ')) adjacentIndexes.add(c.getCol());
                    }
                }
            }
        }
        else if(type.equals("COL")){
            for(Room c: move){
                try {
                    if (!grid[c.getRow()][c.getCol() - 1].getText().equals(" ") || !grid[c.getRow()][c.getCol() + 1].getText().equals(" ")) {
                        adjacentIndexes.add(c.getRow());
                    }
                }
                catch(ArrayIndexOutOfBoundsException e){
                    if(c.getCol() - 1 < 0) {
                        if(!grid[c.getRow()][c.getCol() - 1].equals(" ")) adjacentIndexes.add(c.getRow());
                    }
                    if(c.getCol() + 1 > 15){
                        if(!grid[c.getRow()][c.getCol() + 1].equals(" ")) adjacentIndexes.add(c.getRow());
                    }
                }
            }
        }
        if(!adjacentIndexes.isEmpty()) isTouch = true;
        return isTouch;
    }

    /**
     * resetCellsPlayed function resets the Cells played
     * @param boardGUI
     * @param trayGUI
     */
    public void resetRoomsPlayed(BoardGUI boardGUI, TrayGUI trayGUI){
        trayGUI.getSelectedTrayRooms().clear();
        boardGUI.getSelectedGridRooms().clear();
    }

    /**
     * resetFlags resets flags
     */
    public void resetFlags(){
        setFirstMove(true);
        setAssignedTile(null);
    }

    /**
     * sortList function sorts the list
     * @param list is the sorted list
     * @param type is the type of move
     */
    public void sortList(LinkedList<GridRoom> list, String type){
        list.sort((o1, o2) -> {
            if (type.equals("row")) return o1.getCol() - o2.getCol();
            else return o1.getRow() - o2.getRow();
        });
    }

    /**
     * configureMultiplier function set the multipliers for played rooms to 1
     * @param selectedGridRooms selected grid rooms
     */
    public void configureMultiplier(LinkedList<GridRoom> selectedGridRooms){
        for(GridRoom c: selectedGridRooms){
            c.setincrease(1);
            c.setwordIncrease(1);
        }
    }

    /**
     * updateAnchors function updates the anchors after each valid move
     * @param anchorType function type of anchor
     * @param boardGUI is scrabble grid
     * @param type is the type of move made
     */
    public void updateAnchors(HashMap<Integer,LinkedList<Integer>> anchorType, BoardGUI boardGUI, String type){
        GridRoom[][] grid = boardGUI.getGrid();
        if(type.equals("ROW")){
            for(int i = 0; i < 15; i++){
                LinkedList<Integer> list = anchorType.get(i);
                int size = list.size();
                if(!list.isEmpty()) {
                    for (int j = 0; j < size - 1; j++) {
                        if (!(grid[i][list.get(j)].getLetter() == ' ')) list.remove(j);
                    }
                }
            }
        }
        else if(type.equals("COL")){
            for(int i = 0; i < 15; i++){
                LinkedList<Integer> list = anchorType.get(i);
                int size = list.size();
                if(!list.isEmpty()) {
                    for (int j = 0; j < size - 1; j++) {
                        if (!(grid[list.get(j)][i].getLetter() == ' ')) list.remove(j);
                    }
                }
            }
        }
    }

    /**
     *
     * @param selectedGridRooms
     * @param boardGUI
     * @param type
     * @return
     */
    public boolean validPlacement(GridRoom[] selectedGridRooms, BoardGUI boardGUI, String type){
        GridRoom[][] grid = boardGUI.getGrid();
        if(type.equals("ROW")){
        }
        else if(type.equals("COL")){

        }
        return false;
    }

    /**
     * isGameOver checks the conditions for game over
     * @param human is the human player
     * @param computer is the computer player
     * @param tileBag is the tile bag
     * @param scoreBoard is the scoreboard
     * @returns true if game over, false otherwise
     */
    public boolean isGameOver(Player human, Computer computer, TileContainer tileBag, ScoreBoard scoreBoard){
        if(tileBag.getSize() == 0 || (computer.isPass() == true && this.isPass() == true)) {
            determineWinner(human, computer, scoreBoard);
            return true;
        }
        if(human.getTray().getRack().size() == 0 || computer.getTray().getRack().size() == 0) {
            determineWinner(human,computer,scoreBoard);
            return true;
        }
        if(tileBag.getSize() < 7){
            if(!isPlayableCheck(human)){
                determineWinner(human,computer,scoreBoard);
                return true;
            }
        }
        return false;
    }

    /**
     * startCheck checks if the starting move has a tile on the center
     * @param selectedGridRooms is move made
     * @returns true if move satisfies check, false otherwise
     */
    public boolean startCheck(LinkedList<GridRoom> selectedGridRooms){
        if(this.start) {
            for (GridRoom c : selectedGridRooms) {
                if (c.getCol() == 7 && c.getRow() == 7) {
                    this.setStart(false);
                    return true;
                }
            }
            return false;
        }
        else return true;
    }

    /**
     * determineWinner helps to determine the winner of the game
     * @param human is the human player
     * @param computer is the computer player
     * @param scoreBoard is the scoreboard for the game
     */
    private void determineWinner(Player human, Computer computer, ScoreBoard scoreBoard){
        human.decScore(human.getTrayValue());
        computer.decScore(computer.getTrayValue());
        human.updateScore(computer.getTrayValue());
        computer.updateScore(human.getTrayValue());
        scoreBoard.updateScore();
        String winner;
        String scores = "\n Human Score: " + human.getScore() + "    Computer Score: " + computer.getScore();

        if(human.getScore() >= computer.getScore()) winner = "Human wins!";
        else winner = "Computer wins";

        String message = "Game Over! " + winner + " with " + scores + " points.";
        String header = "Game Over";
        showAlert(Alert.AlertType.CONFIRMATION,message,header);
    }

    /**
     * colorSelectedCells helps to color selected grid room for a valid move
     * @param selectedGridRooms is the valid move
     * @param boardGUI is the scrabble grid
     */
    public void colorSelectedRooms(LinkedList<GridRoom> selectedGridRooms, BoardGUI boardGUI){
        GridRoom[][] grid = boardGUI.getGrid();
        for(GridRoom c: selectedGridRooms){
            int row = c.getRow();
            int col = c.getCol();
        }
    }

    /**
     * showAlert helps to view alerts
     * @param type is the type of alert
     * @param message is the message of the alert
     * @param header is the header of the alert
     */
    public void showAlert(Alert.AlertType type, String message, String header){
        this.alert.setAlertType(type);
        this.alert.setContentText(message);
        this.alert.setHeaderText(header);
        this.alert.showAndWait();
    }

    /**
     * getters and setters for instance variables
     */

    public boolean isPlayableCheck(Player player){
        Computer computer = new Computer(player.getTray(),this);
        return computer.play(boardGUI, dictionary, this, false);
    }
    public TileContainer getTileBag() {
        return this.tileBag;
    }

    public boolean isValidMove(){
        return this.validMove;
    }

    public void setValidMove(boolean flag){
        this.validMove = flag;
    }

    public int getIndexType(){
        return this.indexType;
    }

    public void setIndexType(int indexType){
        this.indexType = indexType;
    }

    public boolean isStart(){
        return this.start;
    }

    public void setStart(boolean flag){
        this.start = flag;
    }

    public boolean isFirstMove(){
        return this.firstMove;
    }

    public void setFirstMove(boolean flag){
        this.firstMove = flag;
    }

    public char getAssignedVal(){ return this.assignedVal; }

    public Tile getAssignedTile(){
        return this.assignedTile;
    }

    public void setAssignedTile(Tile tile){
        this.assignedTile = tile;
    }

    public void setFirstRoom(GridRoom room){
        this.firstRoom = room;
    }

    public boolean isSelect(){
        return this.select;
    }

    public void setSelect(boolean flag){
        this.select = flag;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    public BoardGUI getBoardGUI(){
        return this.boardGUI;
    }

    public void setAssignedVal(char c) {
        this.assignedVal = c;
    }

    public TrayGUI getTrayGUI() {
        return this.trayGUI;
    }

    public void setBoardGUI(BoardGUI boardGUI){
        this.boardGUI = boardGUI;
    }

    public void setTrayGUI(TrayGUI trayGUI){
        this.trayGUI = trayGUI;
    }

    public GridRoom getFirstRoom(){
        return this.firstRoom;
    }

    public boolean isPass(){
        return this.pass;
    }

    public void setPass(boolean flag){
        this.pass = flag;
    }

    public Dictionary getDictionary(){
        return this.dictionary;
    }

    public void setScrabbleLetter(ScrabbleLetter letter){
        this.letter = letter;
    }

    public ScrabbleLetter getScrabbleLetter(){
        return this.letter;
    }

    public void setDictionaryPath(String path){
        this.dictionaryPath = path;
    }

    public String getDictionaryPath(){
        return this.dictionaryPath;
    }

    public void setPlay(boolean flag){
        this.play = flag;
    }

    public boolean isPlay(){
        return this.play;
    }
}
