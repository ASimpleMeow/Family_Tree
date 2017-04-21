package views;

import java.io.File;
import java.util.ArrayList;

import edu.princeton.cs.introcs.In;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import models.FamilyTree;

public class MainViewController {
	
	private FamilyTree familyTree;
	
	@FXML
	private Button confirmButton;//Confirms users choice
	@FXML
	private ChoiceBox<String> choiceBox; //Displays members
	@FXML
	private TextArea textArea;
	
	/**
	 * First method to be executed
	 */
	@FXML
	private void initialize(){
		familyTree = new FamilyTree(loadMemebers());
		fillChoiceBox(familyTree.getTreeRootNames());
	}
	
	/**
	 * On confirm button clicked event, displays family tree
	 * in the text area
	 */
	@FXML
	public void displayFamilyTree(){
		String member = choiceBox.getValue();
		if(member == null) return;
		textArea.setText(familyTree.getFamilyTree(member));
	}
	
	/**
	 * Populates the choice box with family members
	 * 
	 * @param members ArrayList of family members name
	 */
	private void fillChoiceBox(ArrayList<String> members){
		for (String member : members){
			choiceBox.getItems().add(member);
		}
	}
	
	/**
	 * Extracts the family members from the provided file
	 * to be later populated into a family tree
	 * 
	 * @param familyTree Empty family tree class
	 * @return FamilyTree
	 */
	public ArrayList<String[]> loadMemebers(){
		ArrayList<String[]> dataNodes = new ArrayList<String[]>();
		File memebersFile = new File("data/large-database.txt");
        In inMembers = new In(memebersFile);
          //each field is separated(delimited) by a '|'
        String delims = "\\s+";
        while (!inMembers.isEmpty()) {
            // get user and rating from data source
            String memeberDetails = inMembers.readLine();

            // parse user details string
            String[] memberTokens = memeberDetails.split(delims);
            
            if (memberTokens.length == 5) {
            	dataNodes.add(memberTokens);
            }else{
            	inMembers.close();
                throw new IllegalArgumentException();
            }
        }
        inMembers.close();
        return dataNodes;
	}
	
}
