package controller.BookDetailController;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class AlertManager {
	
	public static void displayValidationAlert(String message) 
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Unable to Save");
		alert.setHeaderText("Invalid Book Property");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public static void displaySQLExceptionAlert(String message) 
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Unable to Save");
		alert.setHeaderText("Update Failed");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public static Optional<ButtonType> displaySaveBeforeLeavingAlert()
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Record Modified");
		alert.setContentText("Save changes before leaving?.");
		ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
		ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);
		Optional<ButtonType> result = alert.showAndWait();
		return result;
	}
	
	public static void displayUpdateAlert()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Attention");
		alert.setHeaderText("Unable to Save/Update Book Information");
		alert.setContentText("Click OK to return to the Book List to get the most current version of this"
				+ " book record");
		alert.showAndWait();
	}
}
