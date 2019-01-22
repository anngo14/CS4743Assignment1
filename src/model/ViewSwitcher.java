package model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewSwitcher {

	private String viewName;
	private Stage primaryStage;
	
	public ViewSwitcher(String name, Stage stage) throws Exception
	{
		viewName = name;
		primaryStage = stage;
		changeView();
	}
	
	public void changeView() throws Exception
	{
		try
		{
			Parent root=FXMLLoader.load(getClass().getResource(viewName));
			Scene scene = new Scene(root, 1000, 800);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
