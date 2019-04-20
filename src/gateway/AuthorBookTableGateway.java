package gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.AuthorBook;

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
	
	public Connection getConnection()
	{
		return connection;
	}
	
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}
		
}
