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

import authenticate.AuthenticatorProc;
import gateway.AuditTableGateway;
import gateway.BookTableGateway;

public class BookListController implements Initializable, Controller {
	
	private static Logger logger = LogManager.getLogger();
	private ArrayList<Book> bookList;
	private Book selectedBook;
	private int currentPageStartRecordNumber;
	private final int MAX_BOOK_RECORDS_PER_PAGE = 50;
	private String currentSearch = "";
	private final int sessionID;
	
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
	@FXML
	TextField searchField;
	@FXML
	Label searchStatus;
	@FXML
	Button searchButton;
	@FXML
	Button clearSearchButton;
	
	public BookListController(ArrayList<Book> bookList, int id) {
		this.bookList = bookList;
		this.selectedBook = null;
		this.sessionID = id;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{	
		if(!AuthenticatorProc.getInstance().hasAccess(sessionID, "Modify Books"))
		{
			deleteButton.setDisable(true);
		}
		
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
		searchStatus.setText("");
		fetchedRecords.setText("Fetched records " + currentPageStartRecordNumber + " to " + bookList.size() + " out of " 
				+ BookTableGateway.getInstance().getCountOfBooks(currentSearch));
	}
	
	private void handleDoubleClick(Book selectedBook) 
	{
		MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW, Optional.of(selectedBook), Optional.empty(), Optional.of(sessionID),Optional.empty());
	}
	
	@FXML
	private void deleteSelectedBook(ActionEvent event) 
	{
		if (selectedBook == null)
			return;
		AuditTableGateway.getInstance().deleteAudit(selectedBook);
		BookTableGateway.getInstance().deleteBook(selectedBook);
		MainController.getInstance().changeView(ViewType.BOOK_LIST_VIEW, Optional.empty(), Optional.empty(), Optional.of(sessionID),Optional.empty());
		logger.info("Book record deleted: " + selectedBook.getTitle());
	}
	
	@FXML
	private void firstPage(ActionEvent event)
	{
		firstPage();
	}
	
	private void firstPage() {
		bookList = BookTableGateway.getInstance().getNextBooks(1, currentSearch);
		bookTable.setItems(FXCollections.observableArrayList(bookList));
		
		currentPageStartRecordNumber = 1;
		fetchedRecords.setText("Fetched records " + currentPageStartRecordNumber + " to " + bookList.size() + " out of " 
				+ BookTableGateway.getInstance().getCountOfBooks(currentSearch));
	}
	
	@FXML
	private void previousPage(ActionEvent event)
	{
		if (currentPageStartRecordNumber == 1)
			return;
		int previousPageLastId = bookList.get(0).getId() - 1;
		bookList = BookTableGateway.getInstance().getPreviousBooks(previousPageLastId, currentSearch);
		bookTable.setItems(FXCollections.observableArrayList(bookList));
		
		currentPageStartRecordNumber -= MAX_BOOK_RECORDS_PER_PAGE;
		int lastRecordNumber = currentPageStartRecordNumber + bookList.size() - 1;
		fetchedRecords.setText("Fetched records " + currentPageStartRecordNumber + " to " + lastRecordNumber + " out of " 
				+ BookTableGateway.getInstance().getCountOfBooks(currentSearch));
	}
	
	@FXML
	private void nextPage(ActionEvent event)
	{
		if (currentPageStartRecordNumber >= BookTableGateway.getInstance().getCountOfBooks(currentSearch) - MAX_BOOK_RECORDS_PER_PAGE + 1)
			return;
		int nextPageStartingId = bookList.get(bookList.size() - 1).getId() + 1;
		bookList = BookTableGateway.getInstance().getNextBooks(nextPageStartingId, currentSearch);
		bookTable.setItems(FXCollections.observableArrayList(bookList));
		
		currentPageStartRecordNumber += MAX_BOOK_RECORDS_PER_PAGE;
		int lastRecordNumber = currentPageStartRecordNumber + bookList.size() - 1;
		fetchedRecords.setText("Fetched records " + currentPageStartRecordNumber + " to " + lastRecordNumber + " out of " 
				+ BookTableGateway.getInstance().getCountOfBooks(currentSearch));
	}
	
	@FXML
	private void lastPage(ActionEvent event)
	{
		int lastBookRecordId = BookTableGateway.getInstance().getLastBookId();
		if (currentPageStartRecordNumber + bookList.size() - 1 == lastBookRecordId)
			return;
		bookList = BookTableGateway.getInstance().getPreviousBooks(lastBookRecordId, currentSearch);
		bookTable.setItems(FXCollections.observableArrayList(bookList));
		
		int lastRecordNumber = BookTableGateway.getInstance().getCountOfBooks(currentSearch);
		currentPageStartRecordNumber = lastRecordNumber - bookList.size() + 1;
		fetchedRecords.setText("Fetched records " + currentPageStartRecordNumber + " to " + lastRecordNumber + " out of " 
				+ BookTableGateway.getInstance().getCountOfBooks(currentSearch));
	}
	
	@FXML
	private void search(ActionEvent event) {
		if (searchField.getText().equals(""))
			return;
		int countOfBooks = BookTableGateway.getInstance().getCountOfBooks(searchField.getText());
		System.out.println(countOfBooks);
		if (countOfBooks == 0) {
			this.searchStatus.setText("No search results to display for \"" + searchField.getText() + "\"");
			searchField.setText("");
			return;
		}
		currentSearch = searchField.getText();
		searchField.setText("");
		searchStatus.setText("Current displaying search results for \"" + currentSearch + "\"");
		firstPage();
	}
	
	@FXML
	private void clearSearch(ActionEvent event) {
		if (currentSearch.equals(""))
			return;
		currentSearch = "";
		searchStatus.setText("");
		searchField.setText("");
		firstPage();
	}

}
