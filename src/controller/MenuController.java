package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import model.ViewSwitcher;

public class MenuController implements Initializable{
	
	@FXML
	BorderPane border;
	
	private ViewSwitcher viewSwitcher;

	public void showBookList() throws IOException
	{
		try {
			border.setCenter(FXMLLoader.load(getClass().getResource("/view/BookListVIew.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void quitApplication()
	{
		System.exit(0);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resourceBundle) {
		
	}
	
}
