package controller;

import java.io.IOException;
import java.util.Optional;

import gateway.BookTableGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import model.Book;

public class MainController {

	private static MainController instance = null;
	private BorderPane borderPane;
	//private Book book = null;
	
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
	
	public void changeView(ViewType viewType, Optional<Book> book) 
	{
		String viewName = "";
		Controller controller = null;
		switch(viewType) {
			case BOOK_DETAILED_VIEW:
				viewName = "/view/BookDetailedView.fxml";
				controller = new BookDetailController(book.get());
				break;
			case BOOK_LIST_VIEW:
				viewName = "/view/BookListView.fxml";
				controller = new BookListController(BookTableGateway.getInstance().getBooks());
				break;
		}
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(viewName));
			loader.setController(controller);
			borderPane.setCenter(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	@FXML
	void quitApplication() 
	{
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
