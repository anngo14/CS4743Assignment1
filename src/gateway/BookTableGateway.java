package gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Book;

public class BookTableGateway {
	
	private static BookTableGateway instance = null;
	private Connection connection;
	
	public BookTableGateway()
	{
		
	}
	
	public static BookTableGateway getInstance()
	{
		if(instance == null)
		{
			instance = new BookTableGateway();
		}
		return instance;
	}
	
	public ArrayList<Book> getBooks()
	{
		ArrayList<Book> books = new ArrayList<Book>();
		PreparedStatement preparedStatement = null;
		try {
			String query = "SELECT * FROM Books WHERE id >= ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, 0);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next())
			{ 
				books.add(new Book(resultSet.getString("title")
						, resultSet.getString("isbn")
						, resultSet.getString("summary")
						, resultSet.getInt("year_published")
						, resultSet.getInt("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return books;
	}
	
	public void updateBook(Book book) throws SQLException
	{
		PreparedStatement statement = null;
		try {
			String query = "UPDATE Books SET title = ?, isbn = ?, summary = ?, year_published = ? WHERE id = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getISBN());
			statement.setString(3, book.getSummary());
			statement.setInt(4, book.getYearPublished());
			statement.setInt(5, book.getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public void insertBook(Book book)
	{
		PreparedStatement statement = null;
		try {
			String query = "INSERT INTO Books (title, isbn, summary, year_published) VALUES (?,?,?,?)";
			statement = connection.prepareStatement(query);
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getISBN());
			statement.setString(3, book.getSummary());
			statement.setInt(4, book.getYearPublished());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteBook(Book book) {
		PreparedStatement statement = null;
		try {
			String query = "DELETE FROM Books WHERE id = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, book.getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
