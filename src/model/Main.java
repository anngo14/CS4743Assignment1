package model;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import authenticate.AuthenticatorProc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URL;
import java.sql.Connection;
import controller.MainController;
import gateway.AuditTableGateway;
import gateway.AuthorBookTableGateway;
import gateway.AuthorTableGateway;
import gateway.BookTableGateway;
import gateway.PublisherTableGateway;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application{
	
	// CS 4743 Assignment 3 by Richard Azille, Andrew Ngo
	
	private static final String DB_URL = "jdbc:mysql://easel2.fulgentcorp.com/epg755";
	private static final String DB_USER = "epg755";
	private static final String DB_PASSWORD = "0dQKMvg62I89tgv25WNl";
	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) 
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		try {
			URL url = this.getClass().getResource("/view/MenuBar.fxml");
			FXMLLoader loader = new FXMLLoader(url);
			
			MainController mainController = MainController.getInstance();
			loader.setController(mainController);
			
			Parent root = loader.load();
			mainController.setBorderPane((BorderPane) root);

			primaryStage.setScene(new Scene(root, 1000, 800));
			primaryStage.setTitle("Book List");
			primaryStage.getIcons().add(new Image("view/download.png"));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void init() throws Exception
	{
		super.init();
		logger.info("creating connection..."); 
		Connection connection = establishDataSource().getConnection();
		BookTableGateway.getInstance().setConnection(connection);
		PublisherTableGateway.getInstance().setConnection(connection);
		AuditTableGateway.getInstance().setConnection(connection);
		AuthorTableGateway.getInstance().setConnection(connection);
		AuthorBookTableGateway.getInstance().setConnection(connection);
		AuthenticatorProc.getInstance().setConnection(connection);
	}
	
	public MysqlDataSource establishDataSource()
	{
		MysqlDataSource ds = new MysqlDataSource();
		ds.setURL(DB_URL);
		ds.setUser(DB_USER);
		ds.setPassword(DB_PASSWORD);
		return ds;
	}
	
	@Override 
	public void stop() throws Exception
	{
		super.stop();
		logger.info("closing connection...");
		BookTableGateway.getInstance().getConnection().close();
		PublisherTableGateway.getInstance().getConnection().close();
		AuditTableGateway.getInstance().getConnection().close();
		AuthorTableGateway.getInstance().getConnection().close();
		AuthorBookTableGateway.getInstance().getConnection().close();
		AuthenticatorProc.getInstance().getConnection().close();
	}

}
