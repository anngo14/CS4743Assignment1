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
	public void saveAuthor(Author author)
	{
		PreparedStatement preparedStatement = null;
		try {
			String query = "INSERT INTO Author (first_name, last_name, dob, gender, web_site) VALUES(?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, author.getFirstName());
			preparedStatement.setString(2, author.getLastName());
			preparedStatement.setDate(3, author.getDateOfBirth());
			preparedStatement.setString(4, author.getGender()+" ");
			preparedStatement.setString(5, author.getWebSite());
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public int getAuthorId(Author author)
	{
		PreparedStatement preparedStatement = null;
		int lid = -1;
		try {
			String query = "SELECT id FROM Author WHERE first_name = ? AND last_name = ? AND web_site = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, author.getFirstName());
			preparedStatement.setString(2, author.getLastName());
			preparedStatement.setString(3, author.getWebSite());
			
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()) {
				lid = result.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lid;
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
