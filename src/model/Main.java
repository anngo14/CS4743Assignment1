package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/MenuBar.fxml"));
			Scene scene = new Scene(root, 1000, 800);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Book List");
			primaryStage.getIcons().add(new Image("view/download.png"));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}

}
