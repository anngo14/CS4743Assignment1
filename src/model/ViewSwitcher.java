package model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewSwitcher {

	private String viewName;
	
	public ViewSwitcher(String name) throws Exception 
	{
		viewName = name;
		changeView();
	}
	
	public void changeView() throws Exception
	{
	}
}
