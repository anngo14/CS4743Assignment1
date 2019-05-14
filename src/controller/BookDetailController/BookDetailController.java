package controller.BookDetailController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import authenticate.AuthenticatorProc;
import controller.Controller;
import controller.MainController;
import controller.ViewType;
import gateway.AuditTableGateway;
import gateway.AuthorBookTableGateway;
import gateway.AuthorTableGateway;
import gateway.BookTableGateway;
import gateway.PublisherTableGateway;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.AuditTrailEntry;
import model.Author;
import model.AuthorBook;
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
	private AuthorBook selectedAuthorBook;
	private ArrayList<AuthorBook> authorList = new ArrayList<AuthorBook>();
	private final int sessionID;

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
	Button saveButton;
	@FXML
	Button deleteButton;
	@FXML
	Button addAuthorButton;
	@FXML 
	ComboBox<Integer> yearPick;
	@FXML
	ComboBox<String> publisher;
	@FXML
	TableView<AuthorBook> authorTable;
	@FXML
	TableColumn<?,?> author;
	@FXML
	TableColumn<?,?> royalty;
	
	public BookDetailController(Book book, int id) 
	{
		this.book = book;
		this.bookOriginal = this.book;
		this.sessionID = id;
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
		authorList = BookTableGateway.getInstance().getAuthorsForBook(book);
		
		if(AuthenticatorProc.getInstance().hasAccess(sessionID, "view Books"))
		{
			saveButton.setDisable(true);
			deleteButton.setDisable(true);
			addAuthorButton.setDisable(true);
			bookTitle.setDisable(true);
			bookISBN.setDisable(true);
			bookSummary.setDisable(true);
			yearPick.setDisable(true);
			publisher.setDisable(true);
		}
		
		updateLastSavedBookInfoValues();
		initializeTable(authorList);
	}
	
	public void initializeTable(ArrayList<AuthorBook> list)
	{
		author.setCellValueFactory(new PropertyValueFactory<>("AuthorName"));
		royalty.setCellValueFactory(new PropertyValueFactory<>("Royalty"));
		authorTable.setItems(FXCollections.observableArrayList(list));
		authorTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				 if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
					selectedAuthorBook = (AuthorBook) authorTable.getSelectionModel().getSelectedItem();
				} else if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					selectedAuthorBook = (AuthorBook) authorTable.getSelectionModel().getSelectedItem();
					updateAuthorBook(selectedAuthorBook);
				}
			}
		});
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
			book.setId(BookTableGateway.getInstance().getBookId(book));
			AuditTableGateway.getInstance().insertAudit(trail, id);
		} else {
			updateBookRecord(book);
		}
		updateLastSavedBookInfoValues();
		// MainController.getInstance().changeView(ViewType.BOOK_LIST_VIEW, Optional.empty(), Optional.empty());
		logger.info("Book entry saved: " + book.getTitle());	
	}
	
	public void updateBookRecord(Book book) {
		try {
			this.bookOriginal = BookTableGateway.getInstance().getOriginal(book);
			BookTableGateway.getInstance().updateBookRecord(book);
			AuditTrailEntry trail = new AuditTrailEntry();
			String message = book.diffBook(this.bookOriginal, book);
			trail.setMessage(message);

			if(message.compareTo("") != 0)
			{
				AuditTableGateway.getInstance().insertAudit(trail, book.getId());
			}
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
		MainController.getInstance().changeView(ViewType.AUDIT_TRAIL_VIEW, Optional.of(book), Optional.empty(), Optional.of(sessionID),Optional.empty());
	}
	
	public void addAuthor()
	{
		MainController.getInstance().changeView(ViewType.AUTHORBOOK_DETAIL_VIEW, Optional.of(book), Optional.of(new AuthorBook(book)), Optional.of(sessionID),Optional.empty());
	}
	
	public void deleteAuthor()
	{
		if(selectedAuthorBook == null)
		{
			AlertManager.displayErrorAlert("No Author Selected to Delete");
		}
		else if (selectedAuthorBook != null)
		{
			AuthorBookTableGateway.getInstance().deleteAuthorBook(selectedAuthorBook.getAuthor().getId(), selectedAuthorBook.getBook().getId());
			authorList = BookTableGateway.getInstance().getAuthorsForBook(book);
			AuditTrailEntry audit = new AuditTrailEntry();
			audit.setMessage("Author Deleted From " + book.getTitle());
			AuditTableGateway.getInstance().insertAudit(audit, book.getId());
			initializeTable(authorList);

		}
	}
	
	public void updateAuthorBook(AuthorBook authorBook)
	{
		MainController.getInstance().changeView(ViewType.AUTHORBOOK_DETAIL_VIEW, Optional.of(book), Optional.of(authorBook), Optional.of(sessionID),Optional.empty());
	}
}
