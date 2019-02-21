package model;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URL;
import java.sql.Connection;
import controller.MainController;
import gateway.BookTableGateway;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application{
	
	// CS 4743 Assignment 1 by Richard Azille, Andrew Ngo
	
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
		
		MysqlDataSource ds = new MysqlDataSource();
		ds.setURL("jdbc:mysql://easel2.fulgentcorp.com/epg755");
		ds.setUser("epg755");
		ds.setPassword("0dQKMvg62I89tgv25WNl");
		Connection connection = ds.getConnection();
		
		BookTableGateway.getInstance().setConnection(connection);
	}
	
	@Override 
	public void stop() throws Exception
	{
		super.stop();
		logger.info("closing connection...");
		
		BookTableGateway.getInstance().getConnection().close();
	}

}
