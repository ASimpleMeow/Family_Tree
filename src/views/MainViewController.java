package views;

import java.io.File;
import java.util.ArrayList;

import controllers.Main;
import edu.princeton.cs.introcs.In;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.WindowEvent;
import models.FamilyTree;
import utils.XMLSerializer;

/**
 * @file        MainViewController
 * @author      Oleksandr Kononov 20071032
 * @assignment  FamilyTree
 * @brief       Controls all events main window
 *
 * @notes       
 */

public class MainViewController {
	
	@FXML
	private Label familyCountLabel; //Tells the number of familes
	@FXML
	private Button confirmButton;//Confirms users choice
	@FXML
	private Button detailsButton;	//Opens the details window
	@FXML
	private Button addMemberButton; //Opens the add member window
	@FXML
	private Button removeMemberButton; //Opens the remove member window
	@FXML
	private Button modifyMemberButton; //Opens the modify member window
	@FXML
	private ComboBox<String> comboBox; //Displays members
	@FXML
	private TextArea textArea;
	
	/**
	 * First method to be executed
	 */
	@FXML
	private void initialize(){
		File file = new File("saveData.xml");
		if(file.exists()){
			Main.instance.familyTree = new FamilyTree(new XMLSerializer(file));
			try {
				Main.instance.familyTree.load();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("ERROR LOADING!");
				e.printStackTrace();
			}
		}
		else Main.instance.familyTree = new FamilyTree(loadMemebers(), file);
		update();
		
	}
	
	public static void saveFamilyTree(){
		try {
			Main.instance.familyTree.save();
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
	private void onConfirmButton(){
		String member = comboBox.getValue();
		if(member == null) return;
		textArea.setText(Main.instance.familyTree.getFamilyTree(member));
	}
	
	/**
	 * On details button clicked event, open details window
	 */
	@FXML
	private void onDetailsButton(){
		Main.instance.showDetails();
	}
	
	/**
	 * On add member button clicked event, opens the window to add
	 * new family members
	 */
	@FXML
	private void onAddMemberButton(){
		try{
			Main.instance.showAddMember().setOnCloseRequest(new EventHandler<WindowEvent>() {
	            public void handle(WindowEvent we) {
	            	update();
	            }
	        });
		}catch(NullPointerException e){
			Main.instance.showErrorMessage("ERROR", "Building Add Member Stage could not be built", "");
		}
	}
	
	/**
	 * On remove member button clicked event, opens the window to remove
	 * a family member
	 */
	@FXML
	private void onRemoveMemberButton(){
		try{
			Main.instance.showRemoveMember().setOnCloseRequest(new EventHandler<WindowEvent>() {
	            public void handle(WindowEvent we) {
	            	update();
	            }
	        });
		}catch(NullPointerException e){
			Main.instance.showErrorMessage("ERROR", "Building Remove Member Stage could not be built", "");
		}
	}
	
	/**
	 * On modify member button clicked event, opens the window to modify
	 * a family member
	 */
	@FXML
	private void onModifyMemberButton(){
		try{
			Main.instance.showModifyMember().setOnCloseRequest(new EventHandler<WindowEvent>() {
	            public void handle(WindowEvent we) {
	            	update();
	            }
	        });
		}catch(NullPointerException e){
			Main.instance.showErrorMessage("ERROR", "Building Modify Member Stage could not be built", "");
		}
	}
	
	private void update(){
		comboBox.getItems().clear();
    	comboBox.getItems().addAll(Main.instance.familyTree.getAllNodeNames());
    	int familyCount = Main.instance.familyTree.getFamilyCount();
    	familyCountLabel.setText("(There are "+familyCount+" familes)");
	}
	
	/**
	 * Extracts the family members from the provided file
	 * to be later populated into a family tree
	 * 
	 * @param familyTree Empty family tree class
	 * @return FamilyTree
	 */
	private ArrayList<String[]> loadMemebers(){
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
