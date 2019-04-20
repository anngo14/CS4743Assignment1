package gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.Author;
import model.AuthorBook;
import model.Book;

public class AuthorBookTableGateway {
	
	private static AuthorBookTableGateway instance = null;
	private Connection connection;
	private static Logger logger = LogManager.getLogger();
	
	public AuthorBookTableGateway() {
		
	}
	public static AuthorBookTableGateway getInstance() {
		if (instance == null) {
			instance = new AuthorBookTableGateway();
		}
		return instance;
	}
	
	public AuthorBook getAuthorBook(int authorId, int bookId) {
		AuthorBook authorBook = new AuthorBook();
		PreparedStatement statement = null;
		try {
			String query = "SELECT * FROM author_book where author_id = ? AND book_id = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, authorId);
			statement.setInt(2, bookId);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				authorBook = new AuthorBook(AuthorTableGateway.getInstance().getAuthor(authorId),
						BookTableGateway.getInstance().getBook(bookId),
						resultSet.getInt("royalty"),
						false);
			}
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
		return authorBook;
	}
	
	public void deleteAuthorBook(int authorId, int bookId)
	{
		PreparedStatement preparedStatement = null;
		try {
			String query = "DELETE FROM author_book WHERE book_id = ? AND author_id = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, bookId);
			preparedStatement.setInt(2, authorId);
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAuthorBook(Author author, Book book, double royalty)
	{
		PreparedStatement preparedStatement = null;
		try {
			String query = "INSERT INTO author_book (author_id, book_id, royalty) VALUES (?,?,?)";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, author.getId());
			preparedStatement.setInt(2, book.getId());
			preparedStatement.setDouble(3, royalty);
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateAuthorBook(Author author, Book book, int royalty)
	{
		PreparedStatement preparedStatement = null;
		try {
			String query = "UPDATE author_book SET royalty = ?, WHERE author_id = ? AND book_id = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setDouble(1, royalty);
			preparedStatement.setInt(2, author.getId());
			preparedStatement.setInt(3, book.getId());
			
			preparedStatement.executeUpdate();
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
