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
				publisherList.add(new Publisher(resultSet.getInt("id")
						, resultSet.getString("publisher_name")
						, resultSet.getDate("date_added")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return publisherList;
	}
	public String getPublisherName(int id)
	{
		String name = "";
		PreparedStatement statement = null;
		
		try {
			String query = "SELECT publisher_name FROM Publisher WHERE id = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1,  id);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				name = resultSet.getString("publisher_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}
	public int getPublisherId(String name)
	{
		int id = -1;
		PreparedStatement statement = null;
		
		try {
			String query = "SELECT id FROM Publisher WHERE publisher_name = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, name);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				id = resultSet.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
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
