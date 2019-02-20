package controller;

import java.io.IOException;
import java.net.URL;

import java.util.Optional;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import model.Book;

public class MainController {

	private static MainController instance = null;
	
	private BorderPane borderPane;
	private Book book = null;
	
	
	@FXML
	MenuItem bookList;

	@FXML
	MenuItem addBook;
	
	@FXML
	MenuItem quit;
	
	private MainController() {
		
	}
	
	public static MainController getInstance() {
		if (instance == null)
			instance = new MainController();
		return instance;
	}
	
	public void changeView(ViewType viewType, Optional<Controller> viewController) {
		String viewName = "";
		Controller controller = null;
		switch(viewType) {
			case BOOK_DETAILED_VIEW:
				viewName = "/view/BookDetailedView.fxml";
				controller = viewController.isPresent() ? viewController.get() : new BookDetailedViewController();
				break;
			case BOOK_LIST_VIEW:
				viewName = "/view/BookListView.fxml";
				controller = viewController.isPresent() ? viewController.get() : new BookListController();
				break;
		}
		try {
			URL url = this.getClass().getResource(viewName);
			FXMLLoader loader = new FXMLLoader(url);
			loader.setController(controller);
			Parent viewNode = loader.load();
			borderPane.setCenter(viewNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void changeView(ActionEvent event) {
		if (event.getSource() == bookList) {
			changeView(ViewType.BOOK_LIST_VIEW, Optional.empty());
		} else if (event.getSource() == addBook) {
			setBook(new Book());
			changeView(ViewType.BOOK_DETAILED_VIEW, Optional.empty());
		}
	}
	
	@FXML
	void quitApplication() {
		System.exit(0);
	}
	
	
	public BorderPane getBorderPane() {
		return borderPane;
	}
	
	public void setBorderPane(BorderPane borderPane) {
		this.borderPane = borderPane;
	}
	
	public Book getBook()
	{
		return book;
	}
	
	public void setBook(Book newBook)
	{
		book = newBook;
	}
	
}
