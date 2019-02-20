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
		PreparedStatement statement = null;
		int success = 0;
		try {
			String sql = "UPDATE Books SET title = ?, isbn = ?, summary = ?, year_published = ? WHERE id = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, newBook.getTitle());
			statement.setString(2,  newBook.getISBN());
			statement.setString(3,  newBook.getSummary());
			statement.setInt(4, newBook.getYear());
			statement.setInt(5,  newBook.getId());
			
			success = statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void insertBook(Book newBook)
	{
		PreparedStatement statement = null;
		int success = 0;
		try {
			String sql = "INSERT INTO Books (title, isbn, summary, year_published) VALUES (?,?,?,?)";
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, newBook.getTitle());
			statement.setString(2, newBook.getISBN());
			statement.setString(3, newBook.getSummary());
			statement.setInt(4, newBook.getYear());
			
			success = statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
