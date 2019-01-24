package controller;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Book;

public class BookDetailedViewController {

	@FXML
	TextField bookTitle;
	@FXML
	TextField bookISBN;
	@FXML
	TextField bookYear;
	@FXML
	TextArea bookSummary;
	@FXML
	AnchorPane content;
	
	public void saveBook()
	{
		String title = bookTitle.getText();
		String isbn = bookISBN.getText();
		int year = Integer.parseInt(bookYear.getText());
		String summary = bookSummary.getText();
		
		Book bookToInsert = new Book(title, isbn, summary, year);
		
		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/BookListView.fxml"));
			content.getChildren().setAll(pane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
