package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Book;

public class MainController {

	private static MainController instance = null;
	
	private BorderPane borderPane;
	private Book book = null;
	private ArrayList<Book> listOfBooks = new ArrayList<Book>();
	
	
	@FXML
	MenuItem bookList;
	
	@FXML
	MenuItem quit;
	
	@FXML
	MenuItem addBook;
	
	private MainController() {
		listOfBooks.add(new Book("Chemistry", "ISBN", "ISOTOPES", 2019));
		listOfBooks.add(new Book("Harry Potter: The Series" , "3982-3-424-324", "Cat and mouse game between a boy and his assaulter", 1990));
		listOfBooks.add(new Book("Eragon", "978-3-16-148410-0", "A Dragon destroys our childhood, an Accelarated Reading Program Tragedy.", 2000));
		listOfBooks.add(new Book("Cat in the Hat", "3940234", "A cat talks to children", 1950));
		listOfBooks.add(new Book("Systems Programming", "01-01-10100101", "Clark's notes over inodes", 2018));
	}
	
	public static MainController getInstance() {
		if (instance == null)
			instance = new MainController();
		return instance;
	}
	
	public void changeView(ViewType viewType) {
		String viewName = "";
		Controller controller = null;
		switch(viewType) {
			case BOOK_DETAILED_VIEW:
				viewName = "/view/BookDetailedView.fxml";
				controller = new BookDetailedViewController();
				break;
			case BOOK_LIST_VIEW:
				viewName = "/view/BookListView.fxml";
				controller = new BookListController();
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
	
	public void changeView(ViewType viewType, Controller viewController) {
		String viewName = "";
		Controller controller = null;
		switch(viewType) {
			case BOOK_DETAILED_VIEW:
				viewName = "/view/BookDetailedView.fxml";
				controller = viewController;
				break;
			case BOOK_LIST_VIEW:
				viewName = "/view/BookListView.fxml";
				controller = viewController;
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
		if (event.getSource() == bookList)
			changeView(ViewType.BOOK_LIST_VIEW);
		else if (event.getSource() == addBook)
			changeView(ViewType.BOOK_DETAILED_VIEW);
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
	
	public ArrayList<Book> getBookList()
	{
		return listOfBooks;
	}
	
	public void setBookList(ArrayList<Book> list)
	{
		listOfBooks = list;
	}
	
}
