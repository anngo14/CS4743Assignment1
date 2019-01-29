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
		//Log save button clicked using Log4j
		
		tempBook.setTitle(bookTitle.getText());
		tempBook.setISBN(bookISBN.getText());
		tempBook.setYear((int) yearPick.getValue());
		tempBook.setSummary(bookSummary.getText());
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/BookListView.fxml"));
			AnchorPane pane = loader.load();
			BookListController bookListController = loader.getController();
			//bookListController.initData(tempBook); 
			content.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		ArrayList<Integer> possibleYears = new ArrayList<Integer>();
		for(int i = 2019; i >= 1900; i--)
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
