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
	
	@FXML
	private void initialize(){
		familyTree = loadMemebers(new FamilyTree());
		fillChoiceBox(familyTree.getNodes());
	}
	
	private void fillChoiceBox(ArrayList<String> members){
		for (String member : members){
			choiceBox.getItems().add(member);
		}
	}
	
	public FamilyTree loadMemebers(FamilyTree familyTree){
		File memebersFile = new File("data/large-database.txt");
        In inMemebers = new In(memebersFile);
          //each field is separated(delimited) by a '|'
        String delims = "\\s+";
        while (!inMemebers.isEmpty()) {
            // get user and rating from data source
            String memeberDetails = inMemebers.readLine();

            // parse user details string
            String[] memeberTokens = memeberDetails.split(delims);
            
            if (memeberTokens.length == 5) {
            	familyTree.addFamilyMember(memeberTokens[0],memeberTokens[1].charAt(0)
            			,Integer.parseInt(memeberTokens[2]),memeberTokens[3],memeberTokens[4]);
            }else{
                try {
					throw new Exception("Invalid member length: "+memeberTokens.length);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        return familyTree;
	}
	
}
