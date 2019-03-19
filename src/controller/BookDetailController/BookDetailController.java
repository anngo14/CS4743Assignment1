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
import gateway.AuditTableGateway;
import gateway.BookTableGateway;
import gateway.PublisherTableGateway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.AuditTrailEntry;
import model.Book;
import model.Publisher;

public class BookDetailController implements Initializable, Controller {
	
	private static Logger logger = LogManager.getLogger();
	private Book book;
	private Book bookOriginal;
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
	Button audit;
	@FXML 
	ComboBox<Integer> yearPick;
	@FXML
	ComboBox<String> publisher;
	
	public BookDetailController(Book book) 
	{
		this.book = book;
		this.bookOriginal = this.book;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		if(book.isNewBook()) {
			audit.setDisable(true);
			audit.setOpacity(0.5);
		}
		
		bookTitle.setText(book.getTitle());
		bookISBN.setText(book.getISBN());
		bookSummary.setText(book.getSummary());
		yearPick.setValue(book.getYearPublished());
		publisher.setValue(PublisherTableGateway.getInstance().getPublisherName(book.getPublisherId()));

		ArrayList<Integer> years = new ArrayList<Integer>();
		ArrayList<Publisher> publisherList = new ArrayList<Publisher>();
		ArrayList<String> publisherNames = new ArrayList<String>();

		for(int year = Calendar.getInstance().get(Calendar.YEAR); year >= 1455; year--) {
			years.add(year);
		}
		yearPick.getItems().addAll(years);
		
		publisherList = PublisherTableGateway.getInstance().fetchPublishers();
		for(Publisher p: publisherList)
		{
			publisherNames.add(p.getName());
		}
		publisher.getItems().addAll(publisherNames);
		
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
		book.setPublisherId(PublisherTableGateway.getInstance().getPublisherId(publisher.getValue()));
		
		if(book.isNewBook()) {
			BookTableGateway.getInstance().insertBookRecord(book);
			int id = BookTableGateway.getInstance().getBookId(book);
			AuditTrailEntry trail = new AuditTrailEntry();
			trail.setMessage("Book Added");
			
			AuditTableGateway.getInstance().insertAudit(trail, id);
		} else {
			updateBookRecord(book);
		}
		updateLastSavedBookInfoValues();
		MainController.getInstance().changeView(ViewType.BOOK_LIST_VIEW, Optional.empty());
		logger.info("Book entry saved: " + book.getTitle());	
	}
	
	public void updateBookRecord(Book book) {
		try {
			this.bookOriginal = BookTableGateway.getInstance().getOriginal(book);
			BookTableGateway.getInstance().updateBookRecord(book);
			AuditTrailEntry trail = new AuditTrailEntry();
			trail.setMessage(book.diffBook(this.bookOriginal, book));
			
			AuditTableGateway.getInstance().insertAudit(trail, book.getId());
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
	
	public void showAudit()
	{
		MainController.getInstance().changeView(ViewType.AUDIT_TRAIL_VIEW, Optional.of(book));
	}
	
}
