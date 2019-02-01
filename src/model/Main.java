package model;

import java.net.URL;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			URL url = this.getClass().getResource("/view/MenuBar.fxml");
			FXMLLoader loader = new FXMLLoader(url);
			
			MainController mainController = MainController.getInstance();
			loader.setController(mainController);
			
			Parent root = loader.load();
			mainController.setBorderPane((BorderPane) root);
			// Parent root = FXMLLoader.load(getClass().getResource("/view/MenuBar.fxml"));
			primaryStage.setScene(new Scene(root, 1000, 800));
			primaryStage.setTitle("Book List");
			primaryStage.getIcons().add(new Image("view/download.png"));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}

}
