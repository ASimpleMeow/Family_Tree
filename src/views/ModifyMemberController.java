package views;

import controllers.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @file        ModifyMemeberController
 * @author      Oleksandr Kononov 20071032
 * @assignment  FamilyTree
 * @brief       Controls all events for modify member window
 *
 * @notes       
 */

public class ModifyMemberController {
	
	@FXML
	private ComboBox<String>	comboBox;	//Holds all members
	@FXML
	private TextField			nameField;	//Holds name
	@FXML
	private TextField			genderField; //Holds gender
	@FXML
	private TextField			yearField;	//Holds year
	@FXML
	private TextField			parent1Field; //Holds parent1
	@FXML
	private TextField			parent2Field; //Holds parent2
	
	@FXML
	private Button				confirmButton;
	@FXML
	private Button				cancelButton;
	
	/**
	 * First method to be executed
	 */
	@FXML
	private void initialize(){
		comboBox.getItems().addAll(Main.instance.familyTree.getAllNodeNames());
		changeTextFieldEdit(false);
		
		comboBox.valueProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.isEmpty()) changeTextFieldEdit(true);
				else changeTextFieldEdit(false);
				
				String[] node = Main.instance.familyTree.getNode(comboBox.getValue());
				
				nameField.setText(node[0]);
				genderField.setText(node[1]);
				yearField.setText(node[2]);
				parent1Field.setText(node[3]==null?"":node[3]);
				parent2Field.setText(node[4]==null?"":node[4]);
				
			}
		});
	}
	
	/**
	 * Perform various checks and modify the member when confirm is pressed
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
		
		if(parent2Field.getText().isEmpty()) 
			parent2 = "?";
		else parent2 = (parent2Field.getText().substring(0, 1).toUpperCase() +
				parent2Field.getText().substring(1)).trim();
		
		if(!Main.instance.familyTree.removeFamilyMember(comboBox.getValue())){
			System.out.println("NAME : "+comboBox.getValue());
			Main.instance.showErrorMessage("ERROR", "Could not remove old version of member", "");
			return;
		}
		Main.instance.familyTree.addFamilyMember(name, gender, year, parent1, parent2);
		Main.instance.showSuccessMessage("SUCCESS", "Successfully added a new family member!", "");
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
	
	private void changeTextFieldEdit(boolean edit){
		nameField.setEditable(edit);
		genderField.setEditable(edit);
		yearField.setEditable(edit);
		parent1Field.setEditable(edit);
		parent2Field.setEditable(edit);
	}
	
}
