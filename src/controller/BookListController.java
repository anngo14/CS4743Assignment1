package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
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

public class BookListController implements Initializable, Controller {

	private ArrayList<Book> bookList = new ArrayList<Book>();
	
	@FXML
	TableView bookTable;
	@FXML 
	TableColumn title;
	@FXML
	AnchorPane content;
	
	Book tempBook;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Fake Data
		Book test = new Book("Chemistry", "ISBN", "ISOTOPES", 2019);
		Book test2 = new Book("Harry Potter: The Series" , "3982-3-424-324", "Cat and mouse game between a boy and his assaulter", 1990);
		Book test3 = new Book("Cat in the Hat", "3940234", "A cat talks to children", 1950);
		Book test4 = new Book("Eragon", "978-3-16-148410-0", "A Dragon destroys our childhood, an Accelarated Reading Program Tragedy.", 2000);
		Book test5 = new Book("Systems Programming", "01-01-10100101", "Clark's notes over inodes", 2018);
		
		bookList.add(test);
		bookList.add(test2);
		bookList.add(test3);
		bookList.add(test4);
		bookList.add(test5);
		title.setCellValueFactory(new PropertyValueFactory<>("Title"));
		ObservableList<Book> savedBooks = FXCollections.observableArrayList(bookList);
		bookTable.setItems(savedBooks);
		bookTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					handleDoubleClick(event);
					//Log item is double clicked using Log4j
				}
			}
		});
	}
	
	private void handleDoubleClick(MouseEvent event) {
		Book selectedBook = (Book) bookTable.getSelectionModel().getSelectedItem();
		
		MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW);
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/BookDetailedView.fxml"));
			
			AnchorPane pane = loader.load();
			BookDetailedViewController bookeDetailedViewController = loader.getController();
			
			bookeDetailedViewController.initData((Book) selectedBook);
			bookList.remove((Book) bookTable.getSelectionModel().getSelectedItem());
			
			content.getChildren().setAll(pane);
		} catch (IOException e) {
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
