package gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.AuditTrailEntry;
import model.Book;

public class AuditTableGateway {

	private static AuditTableGateway instance = null;
	private Connection connection;
	private static Logger logger = LogManager.getLogger();
	
	public AuditTableGateway()
	{
		
	}
	public static AuditTableGateway getInstance()
	{
		if(instance == null)
		{
			instance = new AuditTableGateway();
		}
		return instance;
	}
	public void insertAudit(AuditTrailEntry audit, int bookId)
	{
		PreparedStatement statement = null;
		try {
			String query = "INSERT INTO book_audit_trail (book_id, entry_msg) VALUES (?,?)";
			statement = connection.prepareStatement(query);
			statement.setInt(1, bookId);
			statement.setString(2, audit.getMessage());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void deleteAudit(Book book)
	{
		PreparedStatement statement = null;
		try {
			String query = "DELETE FROM book_audit_trail WHERE book_id = ?";
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
