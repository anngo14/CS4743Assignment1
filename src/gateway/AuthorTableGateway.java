package gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.Author;
import model.Book;

public class AuthorTableGateway {

	private static AuthorTableGateway instance = null;
	private Connection connection;
	private static Logger logger = LogManager.getLogger();
	
	public static AuthorTableGateway getInstance() {
		if (instance == null) {
			instance = new AuthorTableGateway();
		}
		return instance;
	}
	
	public AuthorTableGateway() {
		
	}
	
	public Author getAuthor(int authorId)
	{
		Author author = new Author();
		PreparedStatement preparedStatement = null;
		try {
			String query = "SELECT * FROM Author WHERE id = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, authorId);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next())
			{ 
				author = new Author(resultSet.getInt("id")
						, resultSet.getString("first_name")
						, resultSet.getString("last_name")
						, resultSet.getDate("dob")
						, resultSet.getString("gender").charAt(0)
						, resultSet.getString("web_site"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return author;
	}
	
}
