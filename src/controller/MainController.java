package controller;

import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Cryptography.Sha;
import authenticate.AuthenticatorProc;
import authenticate.Authenticator;
import authenticate.LoginDialog;
import authenticate.LoginException;
import controller.BookDetailController.AlertManager;
import controller.BookDetailController.BookDetailController;
import gateway.AuthorTableGateway;
import gateway.BookTableGateway;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import model.Author;
import model.AuthorBook;
import model.Book;

public class MainController implements Controller {
	
	private static Logger logger = LogManager.getLogger();
	private static MainController instance = null;
	private BorderPane borderPane;
	private Controller currentController = null;
	private Authenticator auth;

	int sessionId;

	@FXML
	MenuItem bookList;
	@FXML
	MenuItem addBook;
	@FXML
	MenuItem authorList;
	@FXML
	MenuItem addAuthor;
	@FXML
	MenuItem quit;
	@FXML
	MenuItem login;
	@FXML 
	MenuItem logout;
	@FXML
	Menu userText;
	
	private MainController() {
		
		sessionId = Authenticator.INVALID_SESSION;
	}
	
	public static MainController getInstance() 
	{
		if (instance == null)
			instance = new MainController();
		return instance;
	}
	
	@FXML
	void changeView(ActionEvent event) 
	{
		if (event.getSource() == bookList) {
			changeView(ViewType.BOOK_LIST_VIEW, Optional.empty(), Optional.empty(), Optional.of(sessionId), Optional.empty());
		} else if (event.getSource() == addBook) {
			changeView(ViewType.BOOK_DETAILED_VIEW, Optional.of(new Book()), Optional.empty(), Optional.of(sessionId),Optional.empty());
		} else if (event.getSource() == authorList) {
			changeView(ViewType.AUTHOR_LIST_VIEW, Optional.empty(), Optional.empty(), Optional.of(sessionId), Optional.empty());
		} else if( event.getSource() == addAuthor) {
			changeView(ViewType.AUTHOR_NEW_VIEW, Optional.empty(), Optional.empty(), Optional.of(sessionId), Optional.of(new Author()));

		}
	}
	
	public void changeView(ViewType viewType, Optional<Book> book, Optional<AuthorBook> authorBook, Optional<Integer> session, Optional<Author> author) 
	{
		if (!isViewChangeAuthorized())
		{
			return;
		}
		String viewName = "";
		Controller controller = null;
		switch(viewType) 
		{
			case BLANK_VIEW:
				viewName = "/view/Blank.fxml";
				controller = new BlankController();
				break;
			case AUTHOR_LIST_VIEW:
				viewName = "/view/BookListView.fxml";
				controller = new AuthorListController(AuthorTableGateway.getInstance().getAuthors(), session.get());
				break;
			case BOOK_DETAILED_VIEW:
				viewName = "/view/BookDetailedView.fxml";
				controller = new BookDetailController(book.get(), session.get());
				break;
			case BOOK_LIST_VIEW:
				viewName = "/view/BookListView.fxml";
				controller = new BookListController(BookTableGateway.getInstance().getNextBooks(1, ""), session.get());
				break;
			case AUDIT_TRAIL_VIEW:
				viewName = "/view/AuditTrailView.fxml";
				controller = new AuditTrailController(book.get());
				break;
			case AUTHOR_NEW_VIEW:
				viewName = "/view/AuthorDetailView.fxml";
				controller = new AuthorDetailController(author.get(), session.get());
				break;
			case AUTHOR_DETAIL_VIEW:
				viewName = "/view/AuthorDetailView.fxml";
				controller = new AuthorDetailController(author.get(), session.get());
				break;
			case AUTHORBOOK_DETAIL_VIEW:
				viewName = "/view/AuthorDetailView.fxml";
				controller = new AuthorDetailController(book.get(), authorBook.get(), session.get());
				break;
		}
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(viewName));
			loader.setController(controller);
			borderPane.setCenter(loader.load());
			borderPane.getCenter().setId(viewName);
			currentController = controller;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isViewChangeAuthorized()
	{
		if (borderPane.getCenter().getId() != null
				&& borderPane.getCenter().getId().equals("/view/BookDetailedView.fxml"))
		{
			return getUserResponseToSaveAlert();
		}
		return true;
	}
	
	private boolean getUserResponseToSaveAlert()
	{
		BookDetailController bookDetailController = (BookDetailController) currentController;
		if (bookDetailController.unsavedChangesExist())
		{
			Optional<ButtonType> result = AlertManager.displaySaveBeforeLeavingAlert();
			return getUserResponseToSaveAlert(bookDetailController, result);
		}
		return true;
	}
	
	private boolean getUserResponseToSaveAlert(BookDetailController controller, Optional<ButtonType> result)
	{
		if (result.isPresent()) {
			if (result.get().getText().equals("Yes")) {
				controller.saveBook();
				logger.info("Changes saved. Exited book detail view.");
			} else if (result.get().getText().equals("No")) {
				logger.info("Changes not saved. Exited book detail view.");
			} else if (result.get().getText().equals("Cancel")) {
				logger.info("Changes not saved. Stayed at book detail view.");
				return false;
			}
		}
		return true;
	}
	
	@FXML
	void quitApplication() 
	{
		if (!isViewChangeAuthorized())
		{
			return;
		}
		Platform.exit();
	}
	
	@FXML
	public void loginAction()
	{
		Pair<String, String> creds = LoginDialog.showLoginDialog();
		if(creds == null) 
			return;
		
		String userName = creds.getKey();
		String pw = creds.getValue();
		
		String pwHash = Sha.sha256(pw);
		
		try {
			sessionId = AuthenticatorProc.getInstance().loginSha256(userName, pwHash);
		} catch (LoginException e)
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.getButtonTypes().clear();
			ButtonType buttonTypeOne = new ButtonType("OK");
			alert.getButtonTypes().setAll(buttonTypeOne);
			alert.setTitle("Login Failed");
			alert.setHeaderText("The user name and password you provided do not match stored credentials.");
			alert.showAndWait();

			return;
		}
		userText.setText(AuthenticatorProc.getInstance().getUserNameFromSessionId(sessionId));
		updateGUI();
	}
	
	@FXML
	public void logoutAction()
	{
		sessionId = Authenticator.INVALID_SESSION;
		changeView(ViewType.BLANK_VIEW, Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty());
		userText.setText(AuthenticatorProc.getInstance().getUserNameFromSessionId(sessionId));

		updateGUI();
	}
	
	public void updateGUI()
	{
		if(sessionId == Authenticator.INVALID_SESSION)
		{
			login.setDisable(false);
			logout.setDisable(true);
			addBook.setDisable(true);
			bookList.setDisable(true);
			addAuthor.setDisable(true);
			authorList.setDisable(true);
		}
		else
		{
			login.setDisable(true);
			logout.setDisable(false);
			
			if(sessionId == 1 || sessionId == 2)
			{
				addBook.setDisable(false);
				bookList.setDisable(false);
				authorList.setDisable(false);
				addAuthor.setDisable(false);
			}
			if(sessionId == 3)
			{
				bookList.setDisable(false);
				authorList.setDisable(false);
			}
		}
		
	}
	public BorderPane getBorderPane() 
	{
		return borderPane;
	}
	
	public void setBorderPane(BorderPane borderPane) 
	{
		this.borderPane = borderPane;
	}
	
	public void initialize()
	{
		updateGUI();
	}
	
}
