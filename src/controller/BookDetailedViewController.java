package controller;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Book;

public class BookDetailedViewController implements Initializable, Controller {
	
	private static Logger logger = LogManager.getLogger();

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
		tempBook.setTitle(bookTitle.getText());
		tempBook.setISBN(bookISBN.getText());
		tempBook.setYear((int) yearPick.getValue());
		tempBook.setSummary(bookSummary.getText());
		
		MainController.getInstance().setBook(tempBook);
		MainController.getInstance().changeView(ViewType.BOOK_LIST_VIEW);
		logger.debug("Book entry saved: " + tempBook.getTitle());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		tempBook = MainController.getInstance().getBook();
		bookTitle.setText(tempBook.getTitle());
		bookISBN.setText(tempBook.getISBN());
		bookSummary.setText(tempBook.getSummary());
		yearPick.setValue(tempBook.getYear());
		
		ArrayList<Integer> possibleYears = new ArrayList<Integer>();
		for(int i = 2019; i >= 1900; i--)
		{
			possibleYears.add(i);
		}
		yearPick.getItems().addAll(possibleYears);
	}
	
}
