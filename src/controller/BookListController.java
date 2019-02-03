package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import org.apache.logging.log4j.LogManager;

public class BookListController implements Initializable, Controller {
	
	private static Logger logger = LogManager.getLogger();

	private ArrayList<Book> bookList = new ArrayList<Book>();
	
	@FXML
	TableView<Book> bookTable;
	@FXML 
	TableColumn title;
	@FXML
	AnchorPane content;
	
	Book tempBook;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Fake Data
		bookList.add(new Book("Chemistry", "ISBN", "ISOTOPES", 2019));
		bookList.add(new Book("Harry Potter: The Series" , "3982-3-424-324", "Cat and mouse game between a boy and his assaulter", 1990));
		bookList.add(new Book("Eragon", "978-3-16-148410-0", "A Dragon destroys our childhood, an Accelarated Reading Program Tragedy.", 2000));
		bookList.add(new Book("Cat in the Hat", "3940234", "A cat talks to children", 1950));
		bookList.add(new Book("Systems Programming", "01-01-10100101", "Clark's notes over inodes", 2018));
		
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
		MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW);
	}
	


}
