package controller;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Book;

public class BookDetailedViewController implements Initializable{

	@FXML
	TextField bookTitle;
	@FXML
	TextField bookISBN;
	@FXML
	TextArea bookSummary;
	@FXML
	AnchorPane content;
	@FXML 
	ComboBox yearPick;
	
	Book tempBook;
	
	public void saveBook()
	{
		String title = bookTitle.getText();
		String isbn = bookISBN.getText();
		int year = (int) yearPick.getValue();
		System.out.println("Year Published : " + year);
		String summary = bookSummary.getText();
		
		tempBook.setTitle(title);
		tempBook.setISBN(isbn);
		tempBook.setYear(year);
		tempBook.setSummary(summary);
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/BookListView.fxml"));
			
			AnchorPane pane = loader.load();
			BookListController controller = loader.getController();
			controller.initData(tempBook); 
			content.getChildren().setAll(pane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		ArrayList<Integer> possibleYears = new ArrayList<Integer>();
		for(int i = 1900; i <= 2019; i++)
		{
			possibleYears.add(i);
		}
		yearPick.getItems().addAll(possibleYears);
	}
	
	public void initData(Book bookToEdit)
	{
		tempBook = bookToEdit;
		bookTitle.setText(bookToEdit.getTitle());
		bookISBN.setText(bookToEdit.getISBN());
		bookSummary.setText(bookToEdit.getSummary());
		yearPick.setValue(bookToEdit.getYear());
	}
}
