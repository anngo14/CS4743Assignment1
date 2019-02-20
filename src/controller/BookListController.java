package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;

import model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.Logger;

import gateway.BookTableGateway;

import org.apache.logging.log4j.LogManager;

public class BookListController implements Initializable, Controller {
	
	private static Logger logger = LogManager.getLogger();
	private ArrayList<Book> bookList;
	private Book tempBook;
	
	@FXML
	TableView<Book> bookTable;
	@FXML 
	TableColumn title;
	@FXML
	AnchorPane content;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		bookList = BookTableGateway.getInstance().getBookList();
		
		title.setCellValueFactory(new PropertyValueFactory<>("Title"));
		ObservableList<Book> savedBooks = FXCollections.observableArrayList(bookList);
		bookTable.setItems(savedBooks);
		bookTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					Book selectedBook = (Book) bookTable.getSelectionModel().getSelectedItem();
					handleDoubleClick(event, selectedBook);
					logger.debug("Book double clicked: " + selectedBook.getTitle());
				}
			}
		});
	}
	
	private void handleDoubleClick(MouseEvent event, Book selectedBook) {
		MainController.getInstance().setBook(selectedBook);
		MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW, Optional.empty());
	}
	


}
