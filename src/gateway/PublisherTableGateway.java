package gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.Publisher;

public class PublisherTableGateway {

	private static PublisherTableGateway instance = null;
	private Connection connection;
	private static Logger logger = LogManager.getLogger();
	
	public PublisherTableGateway()
	{
		
	}
	
	public static PublisherTableGateway getInstance()
	{
		if(instance == null)
		{
			instance = new PublisherTableGateway();
		}
		return instance;
	}
	
	public ArrayList<Publisher> fetchPublishers()
	{
		ArrayList<Publisher> publisherList = new ArrayList<Publisher>();
		PreparedStatement preparedStatement = null;
		
		try {
			String query = "SELECT * FROM Publisher WHERE id >= ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, 0);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next())
			{ 
				publisherList.add(new Publisher(resultSet.getInt("id"),
						resultSet.getString("publisher_name"),
						resultSet.getDate("date_added")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return publisherList;
	}
	public Connection getConnection()
	{
		return connection;
	}
	
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}

}
