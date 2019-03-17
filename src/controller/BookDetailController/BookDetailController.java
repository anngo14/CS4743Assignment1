package controller.BookDetailController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.Controller;
import controller.MainController;
import controller.ViewType;
import gateway.BookTableGateway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Book;

public class BookDetailController implements Initializable, Controller {
	
	private static Logger logger = LogManager.getLogger();
	private Book book;
	private String lastSavedTitle;
	private String lastSavedISBN;
	private String lastSavedSummary;
	private int lastSavedYear;

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
	
	public BookDetailController(Book book) 
	{
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
		
		updateLastSavedBookInfoValues();
	}
	
	public void updateLastSavedBookInfoValues()
	{
		lastSavedTitle = bookTitle.getText();
		lastSavedISBN = bookISBN.getText();
		lastSavedSummary = bookSummary.getText();
		lastSavedYear = (int) yearPick.getValue();
	}
	
	public void saveBook()
	{
		if (!RecordValidator.isBookInfoValid(bookTitle.getText(), bookSummary.getText()
				, yearPick.getValue().intValue(), bookISBN.getText())) 
		{
			return;
		}
		book.setTitle(bookTitle.getText());
		book.setISBN(bookISBN.getText());
		book.setYear((int) yearPick.getValue());
		book.setSummary(bookSummary.getText());
		
		if(book.isNewBook()) {
			BookTableGateway.getInstance().insertBookRecord(book);
		} else {
			updateBookRecord(book);
		}
		updateLastSavedBookInfoValues();
		MainController.getInstance().changeView(ViewType.BOOK_LIST_VIEW, Optional.empty());
		logger.info("Book entry saved: " + book.getTitle());	
	}
	
	public void updateBookRecord(Book book) {
		try {
			BookTableGateway.getInstance().updateBookRecord(book);
		} catch (SQLException e) {
			AlertManager.displaySQLExceptionAlert("Unable to update record: " + book.toString());
		}
	}
	
	public boolean unsavedChangesExist()
	{
		if (bookTitle.getText().equals(lastSavedTitle)
				&& bookSummary.getText().equals(lastSavedSummary)
				&& bookISBN.getText().equals(lastSavedISBN)
				&& (int) yearPick.getValue() == lastSavedYear) {
			return false;
		}
		return true;
	}
	
}
