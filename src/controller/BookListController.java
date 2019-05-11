package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import model.Book;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.Logger;

import gateway.AuditTableGateway;
import gateway.BookTableGateway;

public class BookListController implements Initializable, Controller {
	
	private static Logger logger = LogManager.getLogger();
	private ArrayList<Book> bookList;
	private Book selectedBook;
	private int currentPageStartRecordNumber;
	private final int MAX_BOOK_RECORDS_PER_PAGE = 50;
	
	@FXML
	TableView<Book> bookTable;
	@FXML 
	TableColumn<?, ?> title;
	@FXML
	AnchorPane content;
	@FXML
	Button deleteButton;
	@FXML
	TextField userSelectedBook;
	@FXML
	Label fetchedRecords;
	
	public BookListController(ArrayList<Book> bookList) {
		this.bookList = bookList;
		this.selectedBook = null;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{			
		title.setCellValueFactory(new PropertyValueFactory<>("Title"));
		bookTable.setItems(FXCollections.observableArrayList(bookList));
		bookTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					Book book = (Book) bookTable.getSelectionModel().getSelectedItem();
					handleDoubleClick(book);
					logger.info("Book double clicked: " + book.getTitle());
				} else if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
					selectedBook = (Book) bookTable.getSelectionModel().getSelectedItem();
					userSelectedBook.setText(selectedBook.getTitle());
				}
			}
		});
		currentPageStartRecordNumber = 1;
		fetchedRecords.setText("Fetched records " + currentPageStartRecordNumber + " to " + bookList.size() + " out of " 
				+ BookTableGateway.getInstance().getCountOfBooks());
	}
	
	private void handleDoubleClick(Book selectedBook) 
	{
		MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW, Optional.of(selectedBook), Optional.empty());
	}
	
	@FXML
	private void deleteSelectedBook(ActionEvent event) 
	{
		if (selectedBook == null)
			return;
		AuditTableGateway.getInstance().deleteAudit(selectedBook);
		BookTableGateway.getInstance().deleteBook(selectedBook);
		MainController.getInstance().changeView(ViewType.BOOK_LIST_VIEW, Optional.empty(), Optional.empty());
		logger.info("Book record deleted: " + selectedBook.getTitle());
	}
	
	@FXML
	private void firstPage(ActionEvent event)
	{
		bookList = BookTableGateway.getInstance().getBooks(1);
		bookTable.setItems(FXCollections.observableArrayList(bookList));
		
		currentPageStartRecordNumber = 1;
		fetchedRecords.setText("Fetched records " + currentPageStartRecordNumber + " to " + bookList.size() + " out of " 
				+ BookTableGateway.getInstance().getCountOfBooks());
	}
	
	@FXML
	private void previousPage(ActionEvent event)
	{
		if (currentPageStartRecordNumber == 1)
			return;
		int previousPageLastId = bookList.get(0).getId() - 1;
		bookList = BookTableGateway.getInstance().getPreviousBooks(previousPageLastId);
		bookTable.setItems(FXCollections.observableArrayList(bookList));
		
		currentPageStartRecordNumber -= MAX_BOOK_RECORDS_PER_PAGE;
		int lastRecordNumber = currentPageStartRecordNumber + bookList.size() - 1;
		fetchedRecords.setText("Fetched records " + currentPageStartRecordNumber + " to " + lastRecordNumber + " out of " 
				+ BookTableGateway.getInstance().getCountOfBooks());
	}
	
	@FXML
	private void nextPage(ActionEvent event)
	{
		int nextPageStartingId = bookList.get(bookList.size() - 1).getId() + 1;
		bookList = BookTableGateway.getInstance().getBooks(nextPageStartingId);
		bookTable.setItems(FXCollections.observableArrayList(bookList));
		
		currentPageStartRecordNumber += MAX_BOOK_RECORDS_PER_PAGE;
		int lastRecordNumber = currentPageStartRecordNumber + bookList.size() - 1;
		fetchedRecords.setText("Fetched records " + currentPageStartRecordNumber + " to " + lastRecordNumber + " out of " 
				+ BookTableGateway.getInstance().getCountOfBooks());
	}
	
	@FXML
	private void lastPage(ActionEvent event)
	{
		int lastBookRecordId = BookTableGateway.getInstance().getCountOfBooks();
		if (currentPageStartRecordNumber + bookList.size() - 1 == lastBookRecordId)
			return;
		bookList = BookTableGateway.getInstance().getPreviousBooks(lastBookRecordId);
		bookTable.setItems(FXCollections.observableArrayList(bookList));
		
		currentPageStartRecordNumber = lastBookRecordId - MAX_BOOK_RECORDS_PER_PAGE + 1;
		int lastRecordNumber = lastBookRecordId;
		fetchedRecords.setText("Fetched records " + currentPageStartRecordNumber + " to " + lastRecordNumber + " out of " 
				+ BookTableGateway.getInstance().getCountOfBooks());
	}

}
