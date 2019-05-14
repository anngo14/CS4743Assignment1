package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import authenticate.AuthenticatorProc;
import gateway.AuthorTableGateway;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Author;

public class AuthorListController implements Initializable, Controller{

	private static Logger logger = LogManager.getLogger();
	private ArrayList<Author> authorList;
	private Author selected;
	private int currentPageStartRecordNumber;
	private final int MAX_BOOK_RECORDS_PER_PAGE = 50;
	private String currentSearch = "";
	private final int sessionID;
	
	@FXML
	TableView<Author> bookTable;
	@FXML 
	TableColumn<?, ?> title;
	@FXML
	AnchorPane content;
	@FXML
	Button deleteButton;
	@FXML
	TextField userSelectedBook;
	@FXML
	Label fetchedRecords;
	@FXML
	TextField searchField;
	@FXML
	Label searchStatus;
	@FXML
	Button searchButton;
	@FXML
	Button clearSearchButton;
	@FXML
	Label fetchedRecords11;
	
	public AuthorListController(ArrayList<Author> authors, int id)
	{
		this.authorList = authors;
		this.selected = null;
		this.sessionID = id;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if(!AuthenticatorProc.getInstance().hasAccess(sessionID, "Modify Authors"))
		{
			deleteButton.setDisable(true);
		}
		title.setText("Authors");
		fetchedRecords11.setDisable(true);
		searchField.setDisable(true);
		searchButton.setDisable(true);
		clearSearchButton.setDisable(true);
		
		title.setCellValueFactory(new PropertyValueFactory<>("FullName"));
		bookTable.setItems(FXCollections.observableArrayList(authorList));
		bookTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					Author a = (Author) bookTable.getSelectionModel().getSelectedItem();
					handleDoubleClick(a);
				} else if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
					selected = (Author) bookTable.getSelectionModel().getSelectedItem();
					userSelectedBook.setText(selected.getFullName());
				}
			}
		});
		currentPageStartRecordNumber = 1;
		searchStatus.setText("");
		
	}
	private void handleDoubleClick(Author author) 
	{
		MainController.getInstance().changeView(ViewType.AUTHOR_DETAIL_VIEW, Optional.empty(), Optional.empty(), Optional.of(sessionID),Optional.of(selected));
	}

	@FXML
	private void deleteSelectedBook(ActionEvent event) 
	{
		if (selected == null)
			return;
		AuthorTableGateway.getInstance().deleteAuthor(selected);
		MainController.getInstance().changeView(ViewType.AUTHOR_LIST_VIEW, Optional.empty(), Optional.empty(), Optional.of(sessionID),Optional.empty());
	}
	
	@FXML
	private void firstPage(ActionEvent event)
	{
	}
	@FXML
	private void previousPage(ActionEvent event)
	{	
	}
	@FXML
	private void nextPage(ActionEvent event)
	{
	}
	@FXML
	private void lastPage(ActionEvent event)
	{
	}
	@FXML
	private void search(ActionEvent event) {
	}
	@FXML
	private void clearSearch(ActionEvent event) {
	}
}
