package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gateway.BookTableGateway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import model.Book;

public class BookDetailController implements Initializable, Controller {
	
	private static Logger logger = LogManager.getLogger();
	private Book book;

	@FXML
	TextField bookTitle;
	@FXML
	TextField bookISBN;
	@FXML
	TextArea bookSummary;
	@FXML
	AnchorPane content;
	@FXML 
	ComboBox<Integer> yearPick;
	
	public BookDetailController(Book book) {
		this.book = book;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		bookTitle.setText(book.getTitle());
		bookISBN.setText(book.getISBN());
		bookSummary.setText(book.getSummary());
		yearPick.setValue(book.getYearPublished());
		
		ArrayList<Integer> years = new ArrayList<Integer>();
		for(int year = Calendar.getInstance().get(Calendar.YEAR); year >= 1455; year--) {
			years.add(year);
		}
		yearPick.getItems().addAll(years);
	}
	
	public void saveBook()
	{
		if (!isBookInfoValid()) {
			return;
		}	
		book.setTitle(bookTitle.getText());
		book.setISBN(bookISBN.getText());
		book.setYear((int) yearPick.getValue());
		book.setSummary(bookSummary.getText());
		
		if(book.getId() == -1) {
			BookTableGateway.getInstance().insertBook(book);
		} else {
			updateBook(book);
		}
		MainController.getInstance().changeView(ViewType.BOOK_LIST_VIEW, Optional.empty());
		logger.debug("Book entry saved: " + book.getTitle());	
	}
	
	public void updateBook(Book book) {
		try {
			BookTableGateway.getInstance().updateBook(book);
		} catch (SQLException e) {
			displaySQLExceptionAlert("Unable to update record: " + book.toString());
		}
	}
	
	public boolean isBookInfoValid() 
	{
		if (!isTitleValid(bookTitle.getText())
				|| !isSummaryValid(bookSummary.getText())
				|| !isYearPublishedValid((int) yearPick.getValue())
				|| !isIsbnValid(bookISBN.getText())) {
			return false;
		}	
		return true;
	}
	
	private boolean isTitleValid(String title) 
	{
		if (title != null && title.length() > 0 && title.length() <= 255) {
			return true;
		}
		displayValidationAlert("Title must be between 1 and 255 characters");
		return false;
	}
	
	private boolean isSummaryValid(String summary) 
	{
		if (summary != null && summary.length() <= 65535) {
			return true;
		}
		displayValidationAlert("Summary must be less than 65,535 characters");
		return false;
	}
	
	private boolean isYearPublishedValid(int yearPublished) 
	{
		if (yearPublished >= 1455 && yearPublished <= Calendar.getInstance().get(Calendar.YEAR)) {
			return true;
		}
		displayValidationAlert("Year Published must be between 1455 and current year");
		return false;
	}
	
	private boolean isIsbnValid(String isbn) 
	{
		if (isbn != null && isbn.length() <= 13) {
			return true;
		}
		displayValidationAlert("ISBN must be 13 characters or less");
		return false;
	}
	
	private void displayValidationAlert(String message) 
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Unable to Save");
		alert.setHeaderText("Invalid Book Property");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	private void displaySQLExceptionAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Unable to Save");
		alert.setHeaderText("Update Failed");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
}
