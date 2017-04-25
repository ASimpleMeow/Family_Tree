package controllers;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.FamilyTree;
import views.MainViewController;

/**
 * @file        Main
 * @author      Oleksandr Kononov 20071032
 * @assignment  FamilyTree
 * @brief       Main starting point of the program
 *
 * @notes       
 */
public class Main extends Application{
	
	private Stage primaryStage; //The main stage(window)
	public final static Main instance = new Main();	//One instance to access Main
	public FamilyTree familyTree;
	
	public static void main(String[] args){
		launch(args);
	}
	
	/**
	 * This method will be the first to run and will setup the main window.
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Family Tree");
		this.primaryStage.setResizable(true);
		this.primaryStage.setMinWidth(450);
		this.primaryStage.setMinHeight(350);
		
		showMainView();
	}
	
	/**
	 * The Main Window displaying all the main menu options
	 */
	private void showMainView(){
		try{
			//Load Layout
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/views/MainView.fxml"));
			AnchorPane mainLayout = (AnchorPane) loader.load();
			
			//Load Scene
			Scene scene = new Scene(mainLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch(IOException e){
			e.printStackTrace();
		}
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
            	MainViewController.saveFamilyTree();
                System.exit(0);
            }
        });
	}
	
	/**
	 * This method will display the MemberDetails window
	 */
	public Stage showDetails(){
		try{
			return stageSetUp("Member Details","/views/DetailsView.fxml");
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method will display the AddMemberView window
	 */
	public Stage showAddMember(){
		try{
			return stageSetUp("Add Family Member","/views/AddMemberView.fxml");
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method will display the RemoveMemberView window
	 */
	public Stage showRemoveMember(){
		try{
			return stageSetUp("Remove Family Member","/views/RemoveMemberView.fxml");
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method will display the ModifyMemberView window
	 */
	public Stage showModifyMember(){
		try{
			return stageSetUp("Modify Family Member","/views/ModifyMemberView.fxml");
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method will display a message to tell the user the operation resulted in an
	 * error.
	 * 
	 * @param title
	 * @param header
	 * @param message
	 * @param type
	 */
	public void showErrorMessage(String title, String header, String message){
		 // Show the confirmation message.
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
	}
	
	/**
	 * This method will display a message to tell the user the opertation was successful.
	 * 
	 * @param title
	 * @param header
	 * @param message
	 * @param type
	 */
	public void showSuccessMessage(String title, String header, String message){
		 // Show the confirmation message.
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
	}
	
	/**
	 * This method is a template for each stage/window that I use outside
	 * of the main window.
	 * 
	 * @param title
	 * @param location
	 * @throws IOException
	 */
	private Stage stageSetUp(String title, String location) throws IOException{
		// Create the separate Stage.
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        
        //Load layout from fxml file
		AnchorPane stageLayout = 
				(AnchorPane) FXMLLoader.load(Main.class.getResource(location));
        Scene scene = new Scene(stageLayout);
        stage.setResizable(false);
        
        stage.setScene(scene);
        stage.show();
        return stage;
	}
}
