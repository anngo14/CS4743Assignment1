package gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.BookDetailController.AlertManager;
import model.AuditTrailEntry;
import model.Author;
import model.AuthorBook;
import model.Book;

public class BookTableGateway {
	
	private static BookTableGateway instance = null;
	private Connection connection;
	private static Logger logger = LogManager.getLogger();
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
	
	public Book getBook(int bookId)
	{
		Book book = new Book();
		PreparedStatement preparedStatement = null;
		try {
			String query = "SELECT * FROM Books WHERE id = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, bookId);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next())
			{ 
				book = new Book(resultSet.getString("title")
						, resultSet.getString("isbn")
						, resultSet.getString("summary")
						, resultSet.getInt("year_published")
						, resultSet.getTimestamp("last_modified").toLocalDateTime()
						, resultSet.getInt("id")
						, resultSet.getInt("publisher_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return book;
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
						, resultSet.getTimestamp("last_modified").toLocalDateTime()
						, resultSet.getInt("id")
						, resultSet.getInt("publisher_id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return books;
	}
	
	public ArrayList<AuthorBook> getAuthorsForBook(Book book) {
		ArrayList<AuthorBook> authorBooks = new ArrayList<AuthorBook>();
		PreparedStatement preparedStatement = null;
		try {
			String query = "SELECT * FROM author_book WHERE book_id = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, book.getId());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next())
			{ 
				int authorId = resultSet.getInt("author_id");
				int royalty = resultSet.getInt("royalty");
				Author author = AuthorTableGateway.getInstance().getAuthor(authorId);
				authorBooks.add(new AuthorBook(author, book, royalty, false));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return authorBooks;
	}
	
	public void updateBookRecord(Book book) throws SQLException
	{
		LocalDateTime actualLastModified = getActualLastModifiedFromRecord(book);
		if (actualLastModified == null || !actualLastModified.equals(book.getLastModified())) {
			AlertManager.displayUpdateAlert();
			logger.info("Update failed. Newer book record data appeared during edit.");
			return;
		}
		PreparedStatement statement = null;
		try {
			String query = "UPDATE Books SET title = ?, isbn = ?, summary = ?, year_published = ?, publisher_id = ? WHERE id = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getISBN());
			statement.setString(3, book.getSummary());
			statement.setInt(4, book.getYearPublished());
			statement.setInt(5, book.getPublisherId());
			statement.setInt(6, book.getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public LocalDateTime getActualLastModifiedFromRecord(Book book)
	{
		PreparedStatement preparedStatement = null;
		LocalDateTime actualLastModified = null;
		try {
			String query = "SELECT * FROM Books WHERE id = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, book.getId());			
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				actualLastModified = resultSet.getTimestamp("last_modified").toLocalDateTime();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actualLastModified;
	}
	
	public void insertBookRecord(Book book)
	{
		PreparedStatement statement = null;
		try {
			String query = "INSERT INTO Books (title, isbn, summary, year_published, publisher_id) VALUES (?,?,?,?,?)";
			statement = connection.prepareStatement(query);
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getISBN());
			statement.setString(3, book.getSummary());
			statement.setInt(4, book.getYearPublished());
			statement.setInt(5, book.getPublisherId());
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
	
	public String getBookName(int id)
	{
		String title = "";
		PreparedStatement statement = null;
		try {
			String query = "SELECT title FROM Books where id = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				title = resultSet.getString("title");
			}
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return title;
	}
	public int getBookId(Book book)
	{
		int id = -1;
		PreparedStatement statement = null;
		try {
			String query = "SELECT id FROM Books where title = ? AND isbn = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getISBN());
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				id = resultSet.getInt("id");
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return id;
	}
	public Book getOriginal(Book book)
	{
		Book original = new Book();
		PreparedStatement statement = null;
		try {
			String query = "SELECT * FROM Books where id = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, book.getId());
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				original = new Book(resultSet.getString("title")
						, resultSet.getString("isbn")
						, resultSet.getString("summary")
						, resultSet.getInt("year_published")
						, resultSet.getTimestamp("last_modified").toLocalDateTime()
						, resultSet.getInt("id")
						, resultSet.getInt("publisher_id"));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return original;
	}
	public ArrayList<AuditTrailEntry> getSpecificAuditTrail(Book book)
	{
		ArrayList<AuditTrailEntry> trail = new ArrayList<AuditTrailEntry>();
		PreparedStatement statement = null;
		try {
			String query = "SELECT * FROM book_audit_trail WHERE book_id = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, book.getId());
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{ 
				Date date = resultSet.getTimestamp("date_added");
				trail.add(new AuditTrailEntry(resultSet.getInt("id")
						, date
						, resultSet.getString("entry_msg")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return trail;
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
