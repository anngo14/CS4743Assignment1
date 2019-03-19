package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
	
	public AuditTrailController(ArrayList<AuditTrailEntry> list)
	{
		auditList = list;
		book = null;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bookTitle.setText(book.getTitle());
		times.setCellFactory(new PropertyValueFactory<>("Date"));
		messages.setCellFactory(new PropertyValueFactory<>("Message"));
		table.setItems(FXCollections.observableArrayList(auditList));
	}
	
	@FXML
	public void backButton(ActionEvent event)
	{
		
	}

}
