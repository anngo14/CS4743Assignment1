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





public class BookListController implements Initializable{

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
		Book test = new Book("Title", "ISBN", "SUMMARY", 2019);
		Book test2 = new Book("Test" , "ISBN", "N/A", 1990);

		bookList.add(test);
		bookList.add(test2);
		title.setCellValueFactory(new PropertyValueFactory<>("Title"));
		ObservableList<Book> savedBooks = FXCollections.observableArrayList(bookList);
		bookTable.setItems(savedBooks);
		// TODO: Implement correct behavior for double-clicking table row
		bookTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					try {
						int index = bookTable.getSelectionModel().getSelectedIndex();
						System.out.println("Cell clicked = " + title.getCellData(index));
						Book selected = (Book) bookTable.getSelectionModel().getSelectedItem();
						System.out.println("Book Title: " + selected.getTitle() + " Book ISBN: " + selected.getISBN() + " Book Summary: " + selected.getSummary() + " Book Year Published: " + selected.getYear());
						
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/view/BookDetailedView.fxml"));
						
						AnchorPane pane = loader.load();
						BookDetailedViewController controller = loader.getController();
						
						controller.initData((Book) bookTable.getSelectionModel().getSelectedItem());
						bookList.remove((Book) bookTable.getSelectionModel().getSelectedItem());
						System.out.println("" + bookList.size());

						content.getChildren().setAll(pane);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public void initData(Book editedBook)
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
	}

}
