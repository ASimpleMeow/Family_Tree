package views;

import java.util.ArrayList;

import controllers.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @file        DetailsViewController
 * @author      Oleksandr Kononov 20071032
 * @assignment  FamilyTree
 * @brief       Controls all events for details window
 *
 * @notes       
 */

public class DetailsViewController {
	
	@FXML
	private TextArea			textArea;		//Area to display information
	@FXML
	private ComboBox<String>	comboBox;		//Holds all members
	@FXML
	private Button				displayButton;	//Displays information to textArea
	@FXML
	private Button				cancelButton;	//Exits the window
	
	/**
	 * First method to be executed
	 */
	@FXML
	private void initialize(){
		comboBox.getItems().addAll(Main.instance.familyTree.getAllNodeNames());
	}
	
	/**
	 * Handle on display button event
	 */
	@FXML
	private void onDisplayButton(){
		String name = comboBox.getValue();
		if(name == null || name.isEmpty()) return;
		ArrayList<String> myInformation = Main.instance.familyTree.getNodeInformation(name);
		String informationText = "My Information : \n\n";
		informationText += myInformation.get(0);
		informationText += "\n\n Parents Information : \n\n";
		informationText += myInformation.get(1);
		informationText += "\n\n Siblings Information : \n\n";
		informationText += myInformation.get(2);
		informationText += "\n\n Children Information : \n\n";
		informationText += myInformation.get(3);
		
		textArea.setText(informationText);
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
