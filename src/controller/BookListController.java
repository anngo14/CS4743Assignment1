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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;




public class BookListController implements Initializable{

	private ArrayList<Book> bookList = new ArrayList<Book>();
	
	@FXML
	TableView bookTable;
	@FXML 
	TableColumn title;
	@FXML
	AnchorPane content;
	
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
					try {
						int index = bookTable.getSelectionModel().getSelectedIndex();
						System.out.println("Cell clicked = " + title.getCellData(index));
						Book selected = (Book) bookTable.getSelectionModel().getSelectedItem();
						System.out.println("Book Title: " + selected.getTitle() + " Book ISBN: " + selected.getISBN() + " Book Summary: " + selected.getSummary() + " Book Year Published: " + selected.getYear());
						//AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/BookDetailedView.fxml"));
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/view/BookDetailedView.fxml"));
						Parent root = (Parent) loader.load();
						Scene scene = new Scene(root);
						BookDetailedViewController controller = loader.getController();
						controller.initData((Book) bookTable.getSelectionModel().getSelectedItem());
						Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
						window.setScene(scene);
						window.show();
						//content.getChildren().setAll(pane);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

}
