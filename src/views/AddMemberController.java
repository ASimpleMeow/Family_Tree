package views;

import controllers.Main;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.Button;

/**
 * @file        AddMemberController
 * @author      Oleksandr Kononov 20071032
 * @assignment  FamilyTree
 * @brief       Controls all events for add member window
 *
 * @notes       
 */

public class AddMemberController {
	
	@FXML
	private TextField	nameField;		//Text field for name
	@FXML
	private TextField	genderField;	//Text field for gender
	@FXML
	private TextField	yearField;		//Text field for year
	@FXML
	private TextField	parent1Field;	//Text field for parent1
	@FXML
	private TextField	parent2Field;	//Text field for parent2
	@FXML
	private Button		confirmButton;	//Button for confirm
	@FXML
	private Button		cancelButton;	//Button for cancelling 
	
	/**
	 * When confirmButton is pressed, checks fields and adds a new family member to
	 * familyTree
	 */
	@FXML
	private void onConfirmButton(){
		if(nameField.getText().isEmpty() || genderField.getText().isEmpty() || 
				yearField.getText().isEmpty()){
			Main.instance.showErrorMessage("ERROR", "Fields not filled in", "Name, Gender, Year must not be "
					+ "empty!");
			return;
		}
		String name = nameField.getText().trim();
		char gender = genderField.getText().trim().toUpperCase().charAt(0);
		int year = 0;
		String parent1 = "";
		String parent2 = "";
		
		if(yearField.getText().trim().length() != 4){
			Main.instance.showErrorMessage("ERROR", "Year field must contain 4 characters", "");
			return;
		}
		try{
			year = Integer.parseInt(yearField.getText());
		}catch(Exception e){
			Main.instance.showErrorMessage("ERROR", "Year field cannot be non digit value", "");
			return;
		}
		
		if(parent1Field.getText().isEmpty()) parent1 = "?";
		else parent1 = (parent1Field.getText().substring(0, 1).toUpperCase() +
				parent1Field.getText().substring(1)).trim();
		
		if(parent2Field.getText().isEmpty()) parent2 = "?";
		else parent2 = (parent2Field.getText().substring(0, 1).toUpperCase() +
				parent2Field.getText().substring(1)).trim();
		
		Main.instance.familyTree.addFamilyMember(name, gender, year, parent1, parent2);
		Main.instance.showSuccessMessage("SUCCESS", "Successfully added a new family member!", "");
	}
	
	/**
	 * This method will close the window when the cancel button is pressed.
	 */
	@FXML
	private void onCancelButton(){
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		stage.close();
	}
	
}
