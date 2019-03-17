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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.Logger;

import gateway.BookTableGateway;

public class BookListController implements Initializable, Controller {
	
	private static Logger logger = LogManager.getLogger();
	private ArrayList<Book> bookList;
	private Book selectedBook;
	
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
	}
	
	private void handleDoubleClick(Book selectedBook) 
	{
		MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW, Optional.of(selectedBook));
	}
	
	@FXML
	private void deleteSelectedBook(ActionEvent event) 
	{
		if (selectedBook == null)
			return;
		BookTableGateway.getInstance().deleteBook(selectedBook);
		MainController.getInstance().changeView(ViewType.BOOK_LIST_VIEW, Optional.empty());
		logger.info("Book record deleted: " + selectedBook.getTitle());
	}

}
