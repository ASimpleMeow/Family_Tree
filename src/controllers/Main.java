package controllers;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{
	
	private Stage primaryStage; //The main stage(window)
	
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
	private void showMainView()
	{
		try
		{
			//Load Layout
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/views/MainView.fxml"));
			AnchorPane mainLayout = (AnchorPane) loader.load();
			
			//Load Scene
			Scene scene = new Scene(mainLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });
	}
}
