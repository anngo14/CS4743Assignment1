package controller;

import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;

import authenticate.AuthenticatorProc;
import controller.BookDetailController.AlertManager;
import gateway.AuditTableGateway;
import gateway.AuthorBookTableGateway;
import gateway.AuthorTableGateway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.AuditTrailEntry;
import model.Author;
import model.AuthorBook;
import model.Book;

public class AuthorDetailController implements Initializable, Controller{

	private AuthorBook selectedABook;
	private Author selectedAuthor;
	private Book selectedBook;
	private final int sessionID;
	
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
	@FXML
	Button saveButton;
	@FXML
	Text royaltyText;
	
	public AuthorDetailController(Book book, AuthorBook authorBook, int id)
	{
		selectedABook = authorBook;
		selectedBook = book;
		selectedAuthor = selectedABook.getAuthor();
		this.sessionID = id;
	}

	public AuthorDetailController(Author author, int id)
	{
		selectedAuthor = author;
		sessionID = id;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(AuthenticatorProc.getInstance().hasAccess(sessionID, "view Authors"))
		{
			saveButton.setDisable(true);
		}
		
		if(selectedBook == null)
		{
			royalty.setDisable(true);
			royaltyText.setDisable(true);
		}
		if(selectedABook != null)
		{
			royalty.setText("" + selectedABook.getRoyalty()/100000);
		}
		firstName.setText(selectedAuthor.getFirstName());
		lastName.setText(selectedAuthor.getLastName());
		dob.setText("" + selectedAuthor.getDateOfBirth());
		gender.setText("" + selectedAuthor.getGender());
		website.setText(selectedAuthor.getWebSite());
		
	}
	public void saveAuthor() throws ParseException
	{
		if (!isAuthorValid()) {
			return;
		}
		String first = firstName.getText();
		String last = lastName.getText();
		String date = dob.getText();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date d1 = sdf.parse(date);
		java.sql.Date sqlDate = new java.sql.Date(d1.getTime());
		String gen = gender.getText().toUpperCase();
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
			if (!AuthorTableGateway.getInstance().authorExistsInAuthorTable(temp)) {
				AuthorTableGateway.getInstance().saveAuthor(temp);
			}
			if(selectedBook == null || selectedABook == null)
			{
				MainController.getInstance().changeView(ViewType.AUTHOR_LIST_VIEW, Optional.empty(), Optional.empty(), Optional.of(sessionID),Optional.empty());
				return;
			}
			AuditTrailEntry audit = new AuditTrailEntry();
			audit.setMessage("New Author Added for " + selectedBook.getTitle());
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
		MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW, Optional.of(selectedBook), Optional.empty(), Optional.of(sessionID),Optional.empty());
	}
	
	public boolean isAuthorValid() {
		if (firstName.getText().length() == 0) {
			AlertManager.displayAuthorValidationAlert("Missing First Name");
			return false;
		} else if (lastName.getText().length() == 0) {
			AlertManager.displayAuthorValidationAlert("Missing Last Name");
			return false;
		} else if (gender.getText().length() == 0) {
			AlertManager.displayAuthorValidationAlert("Missing gender");
			return false;
		}
		return true;
	}
	
	public void cancelSave()
	{
		MainController.getInstance().changeView(ViewType.AUTHOR_LIST_VIEW, Optional.empty(), Optional.empty(), Optional.of(sessionID), Optional.empty());	}
}
