package controller;

import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.BookDetailController.AlertManager;
import controller.BookDetailController.BookDetailController;
import gateway.BookTableGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import model.Book;

public class MainController {
	
	private static Logger logger = LogManager.getLogger();
	private static MainController instance = null;
	private BorderPane borderPane;
	private Controller currentController = null;
	
	@FXML
	MenuItem bookList;
	@FXML
	MenuItem addBook;
	@FXML
	MenuItem quit;
	
	private MainController() {
		
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
			changeView(ViewType.BOOK_LIST_VIEW, Optional.empty());
		} else if (event.getSource() == addBook) {
			changeView(ViewType.BOOK_DETAILED_VIEW, Optional.of(new Book()));
		} 
	}
	
	public void changeView(ViewType viewType, Optional<Book> book) 
	{
		if (!isViewChangeAuthorized())
		{
			return;
		}
		String viewName = "";
		Controller controller = null;
		switch(viewType) 
		{
			case BOOK_DETAILED_VIEW:
				viewName = "/view/BookDetailedView.fxml";
				controller = new BookDetailController(book.get());
				break;
			case BOOK_LIST_VIEW:
				viewName = "/view/BookListView.fxml";
				controller = new BookListController(BookTableGateway.getInstance().getBooks());
				break;
			case AUDIT_TRAIL_VIEW:
				viewName = "/view/AuditTrailView.fxml";
				controller = new AuditTrailController(book.get());
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
		System.exit(0);
	}
	
	public BorderPane getBorderPane() 
	{
		return borderPane;
	}
	
	public void setBorderPane(BorderPane borderPane) 
	{
		this.borderPane = borderPane;
	}
	
}
