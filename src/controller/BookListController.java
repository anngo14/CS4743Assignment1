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
					handleDoubleClick(event);
					//Log item is double clicked using Log4j
					logger.debug("Book double clicked");
				}
			}
		});
	}
	
	private void handleDoubleClick(MouseEvent event) {
		// BELOW LINE BY ITSELF SWITCHES VIEW, BUT HAVEN'T FOUND A WAY TO POPULATE DATA
		// MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW);
		
		
		// MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW);
		try {
			Book selectedBook = (Book) bookTable.getSelectionModel().getSelectedItem();
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/BookDetailedView.fxml"));
			
			AnchorPane pane = loader.load();
			BookDetailedViewController bookDetailedViewController = loader.getController();
			
			bookDetailedViewController.initData((Book) selectedBook);
			// bookList.remove((Book) bookTable.getSelectionModel().getSelectedItem());
			
			content.getChildren().setAll(pane);
		} catch (IOException e) {
			logger.error("Unable to load detailed view for selected book");
			e.printStackTrace();
		}
	}
	
	//Code to transfer data from other controllers
/*	public void initData(Book editedBook)
	{
		tempBook = editedBook;
		bookList.add(tempBook);
		System.out.println("initdata" + bookList.size());
		for(Book b: bookList)
		{
			System.out.println(b.getTitle());
		}
		title.setCellValueFactory(new PropertyValueFactory<>("Title"));
		ObservableList<Book> savedBooks = FXCollections.observableArrayList(bookList);
		bookTable.setItems(savedBooks);
	}*/

}
