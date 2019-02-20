package controller;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gateway.BookTableGateway;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import model.Book;

public class BookDetailedViewController implements Initializable, Controller {
	
	private static Logger logger = LogManager.getLogger();

	@FXML
	TextField bookTitle;
	@FXML
	TextField bookISBN;
	@FXML
	TextArea bookSummary;
	@FXML
	AnchorPane content;
	@FXML 
	ComboBox yearPick;
	
	Book tempBook;
	
	public void saveBook()
	{		

		tempBook.setTitle(bookTitle.getText());
		tempBook.setISBN(bookISBN.getText());
		tempBook.setYear((int) yearPick.getValue());
		tempBook.setSummary(bookSummary.getText());
		
		MainController.getInstance().changeView(ViewType.BOOK_LIST_VIEW, Optional.empty());
		logger.debug("Book entry saved: " + tempBook.getTitle());

		if (isTitleValid(bookTitle.getText())
				&& isSummaryValid(bookSummary.getText())
				&& isYearPublishedValid((int) yearPick.getValue())
				&& isIsbnValid(bookISBN.getText())) {
			
			// TODO: Insert new book or update existing book in database
			BookTableGateway.getInstance().updateBook(tempBook);
			MainController.getInstance().changeView(ViewType.BOOK_LIST_VIEW, Optional.empty());
			logger.debug("Book entry saved: " + tempBook.getTitle());	
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		tempBook = MainController.getInstance().getBook();
		if (tempBook != null) {
			bookTitle.setText(tempBook.getTitle());
			bookISBN.setText(tempBook.getISBN());
			bookSummary.setText(tempBook.getSummary());
			yearPick.setValue(tempBook.getYear());
		}
		ArrayList<Integer> possibleYears = new ArrayList<Integer>();
		for(int i = 2019; i >= 1900; i--)
		{
			possibleYears.add(i);
		}
		yearPick.getItems().addAll(possibleYears);
	}
	
	private boolean isTitleValid(String title) {
		if (title != null && title.length() > 0 && title.length() <= 255) {
			return true;
		}
		displayValidationAlert("Title must be between 1 and 255 characters");
		return false;
	}
	
	private boolean isSummaryValid(String summary) {
		if (summary != null && summary.length() <= 65535) {
			return true;
		}
		displayValidationAlert("Summary must be less than 65,535 characters");
		return false;
	}
	
	private boolean isYearPublishedValid(int yearPublished) {
		if (yearPublished >= 1455 && yearPublished <= Calendar.getInstance().get(Calendar.YEAR)) {
			return true;
		}
		displayValidationAlert("Year Published must be between 1455 and current year");
		return false;
	}
	
	private boolean isIsbnValid(String isbn) {
		if (isbn != null && isbn.length() <= 13) {
			return true;
		}
		displayValidationAlert("ISBN must be 13 characters or less");
		return false;
	}
	
	private void displayValidationAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Unable to Save");
		alert.setHeaderText("Invalid Book Property");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
}
