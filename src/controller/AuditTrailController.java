package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import gateway.AuditTableGateway;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.AuditTrailEntry;
import model.Book;

public class AuditTrailController implements Initializable, Controller {

	private ArrayList<AuditTrailEntry> auditList;
	private Book book;
	private int sessionID;
	
	@FXML
	Button back;
	@FXML
	Label bookTitle;
	@FXML
	TableView table;
	@FXML
	TableColumn times;
	@FXML
	TableColumn messages;
	
	public AuditTrailController(Book book)
	{
		this.book = book;
		this.auditList = book.getAuditTrail();
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bookTitle.setText(book.getTitle());
		times.setCellValueFactory(new PropertyValueFactory<>("Date"));
		messages.setCellValueFactory(new PropertyValueFactory<>("Message"));
		table.setItems(FXCollections.observableArrayList(auditList));
	}
	
	public void backButton()
	{
		MainController.getInstance().changeView(ViewType.BOOK_DETAILED_VIEW, Optional.of(this.book), Optional.empty(), Optional.of(sessionID),Optional.empty());
	}

}
