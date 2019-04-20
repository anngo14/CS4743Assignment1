package controller;

import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;

import gateway.AuthorBookTableGateway;
import gateway.AuthorTableGateway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.Author;
import model.Book;

public class AuthorDetailController implements Initializable, Controller{

	private Book selectedBook;
	
	@FXML
	TextField firstName;
	@FXML
	TextField lastName;
	@FXML
	TextField gender;
	@FXML
	TextField dob;
	@FXML
	TextField royalty;
	@FXML
	TextField website;
	
	public AuthorDetailController(Book book)
	{
		selectedBook = book;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	public void saveAuthor() throws ParseException
	{
		String first = firstName.getText();
		String last = lastName.getText();
		String date = dob.getText();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date d1 = sdf.parse(date);
		java.sql.Date sqlDate = new java.sql.Date(d1.getTime());
		String gen = gender.getText();
		char[] genChar = gen.toCharArray();
		String royal = royalty.getText();
		String web = website.getText();
		
		Author temp = new Author(first, last, sqlDate, genChar[0], web);
		AuthorTableGateway.getInstance().saveAuthor(temp);
		int id = AuthorTableGateway.getInstance().getAuthorId(temp);
		temp.setId(id);
		
		AuthorBookTableGateway.getInstance().saveAuthorBook(temp, selectedBook, Double.parseDouble(royal));
		
	}
	public void cancelSave()
	{
		MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW, Optional.of(selectedBook));
	}
}
