package controller;

import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;

import gateway.AuditTableGateway;
import gateway.AuthorBookTableGateway;
import gateway.AuthorTableGateway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.AuditTrailEntry;
import model.Author;
import model.AuthorBook;
import model.Book;

public class AuthorDetailController implements Initializable, Controller{

	private AuthorBook selectedABook;
	private Author selectedAuthor;
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
	
	public AuthorDetailController(Book book, AuthorBook authorBook)
	{
		selectedABook = authorBook;
		selectedBook = book;
		selectedAuthor = selectedABook.getAuthor();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		firstName.setText(selectedAuthor.getFirstName());
		lastName.setText(selectedAuthor.getLastName());
		dob.setText("" + selectedAuthor.getDateOfBirth());
		royalty.setText("" + selectedABook.getRoyalty()/100000);
		gender.setText("" + selectedAuthor.getGender());
		website.setText(selectedAuthor.getWebSite());
		
	}
	public void saveAuthor() throws ParseException
	{
		
		String first = firstName.getText();
		String last = lastName.getText();
		String date = dob.getText();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date d1 = sdf.parse(date);
		java.sql.Date sqlDate = new java.sql.Date(d1.getTime());
		String gen = gender.getText();
		char[] genChar = gen.toCharArray();
		String royal = royalty.getText();
		String web = website.getText();
		
		Author temp = new Author(first, last, sqlDate, genChar[0], web);
		temp.setId(selectedAuthor.getId());
		
		if(selectedAuthor.getId() > 0)
		{
			AuthorTableGateway.getInstance().updateAuthor(temp);
		}
		else
		{
			AuthorTableGateway.getInstance().saveAuthor(temp);
			AuditTrailEntry audit = new AuditTrailEntry();
			audit.setMessage("New Author Added");
			AuditTableGateway.getInstance().insertAudit(audit, selectedBook.getId());

			int id = AuthorTableGateway.getInstance().getAuthorId(temp);
			temp.setId(id);
		}
		if(selectedABook.isNewRecord())
		{
			AuthorBookTableGateway.getInstance().saveAuthorBook(temp, selectedBook, Double.parseDouble(royal));
		}
		else
		{
			if(selectedABook.getRoyalty() != (Double.parseDouble(royal) * 100000))
			{
				AuditTrailEntry audit = new AuditTrailEntry();
				audit.setMessage("Royalty Changed");
				AuditTableGateway.getInstance().insertAudit(audit, selectedBook.getId());
			}
			
			AuthorBookTableGateway.getInstance().updateAuthorBook(temp, selectedBook, Double.parseDouble(royal));
		}	
		MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW, Optional.of(selectedBook), Optional.empty());
	}
	public void cancelSave()
	{
		MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW, Optional.of(selectedBook), Optional.empty());
	}
}
