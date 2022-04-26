/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.*;

public class TrayRoom extends Room {
    private boolean clicked;

    /**
     * @param row is tray row
     * @param col is tray column (always 0)
     * @param width is width for the room
     * @param height is height for the room
     * @param text is text value for the room
     * @param manager is game manager
     */
    public TrayRoom(int row, int col, double width, double height, char text, GameManager manager) {
        super(row, col, width, height, text, manager);
        this.clicked = false;
        this.setOnAction(event -> {
            performTrayAction();
        });

    }

    /**
     * performTrayAction - performs necessary action when a tray room is clicked
     */
    public void performTrayAction(){
        GameManager manager = this.getManager();
        boolean select = manager.isSelect();
        char assignedVal = manager.getAssignedVal();
        char letter = getLetter();
        char assign = getAssign();
        TrayGUI trayGUI = manager.getTrayGUI();

        if(!clicked && select){
            if(this.getLetter() == ' ') blank();
            clicked = true;
            assign = getLetter();
            this.setText("-");
            manager.setAssignedVal(assign);
            manager.setAssignedTile(getTile());
            trayGUI.updateSelectedTrayRooms(this);
        }
        if(!select) {
            Alert notSelected = new Alert(Alert.AlertType.INFORMATION);
            notSelected.getDialogPane().setContentText("Please select the move first");
            notSelected.setTitle("Illegal Move");
            notSelected.show();
        }
    }

    /**
     * blank method is used for blank tiles
     */
    public void blank(){
        BooleanBinding isInvalid;
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText("Input any character between a and z");
        Button ok = (Button) inputDialog.getDialogPane().lookupButton(ButtonType.OK);
        TextField inputField = inputDialog.getEditor();
        isInvalid = Bindings.createBooleanBinding(() -> isInvalid(inputField.getText()), inputField.textProperty());
        ok.disableProperty().bind(isInvalid);
        inputDialog.showAndWait();
    }

    /**
     * isInvalid function checks if the input is a letter
     * @param input
     * @returns true if letter, false otherwise
     */
    private Boolean isInvalid(String input) {
        if(input.isEmpty()) return true;
        if(input.length() > 1) return true;
        char letter = input.charAt(0);
        if(letter < 'a' || letter > 'z') return true;
        this.setLetter(letter);
        return false;
    }

    public void lastState(){
        clicked = false;
        //setAssign('-');
        this.setText(Character.toString(getLetter()));
    }

    public void setClicked(boolean click){
        this.clicked = click;
    }


}
