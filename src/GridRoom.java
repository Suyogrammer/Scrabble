/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import javafx.scene.control.Alert;

/**
 * GridRoom is the individual rooms of the scrabble grid
 */
public class GridRoom extends Room {
    private boolean isSet;
    private int increase;
    private int wordIncrease;

    /**
     * @param row is the row
     * @param col is the column
     * @param width is the width
     * @param height is the height
     * @param text is the text value
     * @param manager is the manager
     */
    public GridRoom(int row, int col, double width, double height, char text, GameManager manager) {
        super(row, col, width, height, text, manager);
        this.isSet = false;
        this.increase = 1;
        this.wordIncrease = 1;
        this.setOnAction(event -> {
            if(!isSet) performGridAction();
        });
    }

    /**
     * performGridAction performs the necessary actions when a grid cell is clicked
     */
    public void performGridAction(){
        GameManager manager = this.getManager();
        char assignedVal = manager.getAssignedVal();
        Tile assignedTile = manager.getAssignedTile();
        char letter;
        boolean start = manager.isStart();
        boolean select = manager.isSelect();
        boolean isFirstMove = manager.isFirstMove();
        int row = this.getRow();
        int col = this.getCol();
        String constraint = manager.getType();
        BoardGUI boardGUI = manager.getBoardGUI();

        if(start && select){
            if(constraint.equals("ROW")) {
                boardGUI.updatesCount();
                manager.setFirstRoom(this);
                manager.setIndexType(row);
                manager.setFirstMove(false);
                letter = assignedVal;
                this.setLetter(letter);
                this.setTile(assignedTile);
                boardGUI.updateSelectedGridRooms(this);
                isSet = true;
            }
            else if(constraint.equals("COL")){
                boardGUI.updatesCount();
                manager.setFirstRoom(this);
                manager.setIndexType(col);
                manager.setFirstMove(false);
                letter = assignedVal;
                this.setLetter(letter);
                this.setTile(assignedTile);
                boardGUI.updateSelectedGridRooms(this);
                isSet = true;
            }
        }
        else if(!start && isFirstMove  && constraint.equals("ROW")){
            boardGUI.updatesCount();
            manager.setFirstRoom(this);
            manager.setIndexType(row);
            manager.setFirstMove(false);
            letter = assignedVal;
            this.setLetter(letter);
            this.setTile(assignedTile);
            boardGUI.updateSelectedGridRooms(this);
            isSet = true;
        }
        else if(!start && isFirstMove && constraint.equals("COL")){
            boardGUI.updatesCount();
            manager.setFirstRoom(this);
            manager.setIndexType(col);
            manager.setFirstMove(false);
            letter = assignedVal;
            this.setLetter(letter);
            this.setTile(assignedTile);
            boardGUI.updateSelectedGridRooms(this);
            isSet = true;
        }
        else if((!start)){
            if(constraint.equals("ROW") && (manager.getIndexType() == row)){
                boardGUI.updatesCount();
                letter = assignedVal;
                this.setLetter(letter);
                this.setTile(assignedTile);
                boardGUI.updateSelectedGridRooms(this);
                isSet = true;
            }
            else if(constraint.equals("COL") && (manager.getIndexType() == col)){
                boardGUI.updatesCount();
                letter = assignedVal;
                this.setLetter(letter);
                this.setTile(assignedTile);
                boardGUI.updateSelectedGridRooms(this);
                isSet = true;
            }
            else{
                Alert illegalMove = new Alert(Alert.AlertType.WARNING);
                illegalMove.getDialogPane().setContentText("Not allowed");
                illegalMove.setTitle("Illegal Move");
                illegalMove.show();

            }
        }
        if(!manager.isSelect()) {
            Alert notSelected = new Alert(Alert.AlertType.INFORMATION);
            notSelected.getDialogPane().setContentText("Select Vertically or Horizontally first");
            notSelected.setTitle("Illegal Move");
            notSelected.show();
        }
    }

    public void lastState(){this.setText(Character.toString(getLetter()));}

    /**
     * to set the letter for the grid
     * @param letter used for value to set the letter
     */
    @Override
    public void setLetter(char letter){
        if(!isSet){
            super.setLetter(letter);
        }
    }

    public void setincrease(int increase){
        this.increase = increase;
    }

    public int getincrease(){
        return this.increase;
    }

    public int getwordIncrease(){
        return this.wordIncrease;
    }

    public void setwordIncrease(int increment){
        this.wordIncrease = increment;
    }

}
