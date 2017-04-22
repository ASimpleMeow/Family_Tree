package views;

import java.io.File;
import java.util.ArrayList;

import controllers.Main;
import edu.princeton.cs.introcs.In;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.WindowEvent;
import models.FamilyTree;
import utils.XMLSerializer;

public class MainViewController {
	
	private static FamilyTree familyTree;
	
	@FXML
	private Button confirmButton;//Confirms users choice
	@FXML
	private Button addMemberButton; //Opens the add member window
	@FXML
	private Button removeMemberButton; //Opens the remove member window
	@FXML
	private Button modifyMemberButton; //Opens the modify member window
	@FXML
	private ChoiceBox<String> choiceBox; //Displays members
	@FXML
	private TextArea textArea;
	
	/**
	 * First method to be executed
	 */
	@FXML
	private void initialize(){
		File file = new File("saveData.xml");
		if(file.exists()){
			familyTree = new FamilyTree(new XMLSerializer(file));
			try {
				familyTree.load();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("ERROR LOADING!");
				e.printStackTrace();
			}
		}
		else familyTree = new FamilyTree(loadMemebers(), file);
		fillChoiceBox(familyTree.getTreeRootNames());
	}
	
	public static void saveFamilyTree(){
		try {
			familyTree.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR SAVING!");
			e.printStackTrace();
		}
	}
	
	/**
	 * On confirm button clicked event, displays family tree
	 * in the text area
	 */
	@FXML
	public void onConfirmButton(){
		String member = choiceBox.getValue();
		if(member == null) return;
		textArea.setText(familyTree.getFamilyTree(member));
	}
	
	/**
	 * On add member button clicked event, opens the window to add
	 * new family members
	 */
	@FXML
	public void onAddMemberButton(){
		Main.instance.showAddMember().setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
        		updateChoiceBox();
            }
        });
	}
	
	/**
	 * On remove member button clicked event, opens the window to remove
	 * a family member
	 */
	@FXML
	public void onRemoveMemberButton(){
		Main.instance.showRemoveMember();
		updateChoiceBox();
	}
	
	/**
	 * On modify member button clicked event, opens the window to modify
	 * a family member
	 */
	@FXML
	public void onModifyMemberButton(){
		Main.instance.showModifyMember();
		updateChoiceBox();
	}
	
	/**
	 * Populates the choice box with family members
	 * 
	 * @param members ArrayList of family members name
	 */
	private void fillChoiceBox(ArrayList<String> members){
		choiceBox.getItems().clear();
		for (String member : members){
			choiceBox.getItems().add(member);
		}
	}
	
	private void updateChoiceBox(){
		ArrayList<String> nodeNames = familyTree.getTreeRootNames();
		if(choiceBox.getItems().containsAll(nodeNames)) return;
		for(String nodeName : nodeNames){
			if(choiceBox.getItems().contains(nodeName)) continue;
			choiceBox.getItems().add(nodeName);
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
