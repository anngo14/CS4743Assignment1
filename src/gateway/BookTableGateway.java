package gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	public ArrayList<Book> getBookList()
	{
		ArrayList<Book> books = new ArrayList<Book>();
		PreparedStatement statement = null;
	
		try {
			String sql = "SELECT *FROM Books WHERE id >= ?";
			statement = connection.prepareStatement(sql);
			
			statement.setInt(1, 0);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next())
			{
				int bookId = rs.getInt("id");
				String bookTitle = rs.getString("title");
				String bookSum = rs.getString("summary");
				int bookYear = rs.getInt("year_published");
				String bookIsbn = rs.getString("isbn");

				Book b = new Book(bookTitle, bookIsbn, bookSum, bookYear, bookId);

				books.add(b);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return books;
	}
	
	public void updateBook(Book newBook)
	{
		
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
