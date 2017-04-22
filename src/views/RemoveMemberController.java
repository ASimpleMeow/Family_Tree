package views;


import controllers.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @file        RemoveMemeberController
 * @author      Oleksandr Kononov 20071032
 * @assignment  FamilyTree
 * @brief       Controls all events for remove member window
 *
 * @notes       
 */

public class RemoveMemberController {
	
	@FXML
	private Button	confirmButton; //Button for confirmation of deletion
	@FXML
	private Button	cancelButton; //Cancels deletion and closes window
	@FXML
	private ComboBox<String> comboBox;   //Contains all the members
	
	/**
	 * First method to be executed
	 */
	@FXML
	private void initialize(){
		comboBox.getItems().addAll(Main.instance.familyTree.getAllNodeNames());
	}
	
	@FXML
	private void onConfirmButton(){
		String name = comboBox.getValue();
		if(name == null || name.isEmpty()) return;
		boolean success = Main.instance.familyTree.removeFamilyMember(name);
		if(success){
			Main.instance.showSuccessMessage("SUCCESS", "Memeber "+name+" deleted successfully", "");
		}else{
			Main.instance.showSuccessMessage("FAILURE", "Could not delete memeber "+name, "");
		}
	}
	
	/**
	 * This method will close the window when the cancel button is pressed.
	 */
	@FXML
	private void onCancelButton()
	{
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		stage.close();
	}
}
