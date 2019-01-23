package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class BookListController implements Initializable{

	private ArrayList<Book> bookList = new ArrayList<Book>();
	
	@FXML
	TableView bookTable;
	@FXML 
	TableColumn title;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Book test = new Book("Title", "ISBN", "SUMMARY", 2019);
		Book test2 = new Book("Test" , "ISBN", "N/A", 2019);

		bookList.add(test);
		bookList.add(test2);
		title.setCellValueFactory(new PropertyValueFactory<>("Title"));
		ObservableList<Book> savedBooks = FXCollections.observableArrayList(bookList);
		bookTable.setItems(savedBooks);
		// TODO: Implement correct behavior for double-clicking table row
		bookTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					System.out.println("Double click detected");
				}
			}
		});
	}

}
