package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import gateway.BookTableGateway;
import gateway.PublisherTableGateway;

public class Book {

	private String title;
	private String isbn;
	private String summary;
	private int yearPublished;
	private LocalDateTime lastModified;
	private int id;
	private int publisher_id;
	
	private final String DEFAULT_TITLE = "";
	private final String DEFAULT_ISBN = "ISBN-0000-0000";
	private final String DEFAULT_SUMMARY = "";
	private final int DEFAULT_YEAR_PUBLISHED = Calendar.getInstance().get(Calendar.YEAR);
	private final int DEFAULT_ID = -1;
	private final int DEFAULT_PID = 1;
	
	public Book() 
	{
		this.title = DEFAULT_TITLE;
		this.isbn = DEFAULT_ISBN;
		this.summary = DEFAULT_SUMMARY;
		this.yearPublished = DEFAULT_YEAR_PUBLISHED;
		this.lastModified = null;
		this.id = DEFAULT_ID;
		this.publisher_id = DEFAULT_PID;
	}
	
	public Book(String title, String isbn, String summary, int yearPublished, LocalDateTime lastModified) 
	{
		this.title = title;
		this.isbn = isbn;
		this.summary = summary;
		this.yearPublished = yearPublished;
		this.lastModified = lastModified;
	}
	
	public Book(String title, String isbn, String summary, int yearPublished, LocalDateTime lastModified, int id, int publisherId) 
	{
		this.title = title;
		this.isbn = isbn;
		this.summary = summary;
		this.yearPublished = yearPublished;
		this.lastModified = lastModified;
		this.id = id;
		this.publisher_id = publisherId;
	}
	public String diffBook(Book book1, Book book2)
	{
		String diff = "";
		if(book2.getISBN().compareTo(book1.getISBN()) != 0)
		{
			diff += "ISBN changed from " + book1.getISBN() + " to " + book2.getISBN() + "\n";
		}
		if(book2.getPublisherId() != book1.getPublisherId())
		{
			String old = PublisherTableGateway.getInstance().getPublisherName(book1.getPublisherId());
			String newP = PublisherTableGateway.getInstance().getPublisherName(book2.getPublisherId());
			diff += "Publisher changed from " + old + " to " + newP + "\n";
		}
		if(book2.getSummary().compareTo(book1.getSummary()) != 0)
		{
			diff += "Summary changed from " + book1.getSummary() + " to " + book2.getSummary() + "\n";
		}
		if(book2.getTitle().compareTo(book1.getTitle()) != 0)
		{
			diff += "Title changed from " + book1.getTitle() + " to " + book2.getTitle() + "\n";
		}
		if(book2.getYearPublished() != book1.getYearPublished())
		{
			diff += "Year Published changed from " + book1.getYearPublished()+ " to " + book2.getYearPublished() + "\n"; 
		}
		return diff;
	}
	
	public ArrayList<AuthorBook> getAuthors() {
		ArrayList<AuthorBook> authorBooks = new ArrayList<>();
		try {
			BookTableGateway.getInstance().getAuthorsForBook(this);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return authorBooks;
	}
	
	public boolean isNewBook()
	{
		return getId() == -1;
	}
	
	public String getTitle() 
	{
		return title;
	}
	
	public String getISBN() 
	{
		return isbn;
	}
	
	public String getSummary() 
	{
		return summary;
	}
	
	public int getYearPublished() 
	{
		return yearPublished;
	}
	
	public LocalDateTime getLastModified()
	{
		return lastModified;
	}
	
	public int getId() 
	{
		return id;
	}
	public int getPublisherId()
	{
		return publisher_id;
	}
	public ArrayList<AuditTrailEntry> getAuditTrail()
	{
		ArrayList<AuditTrailEntry> auditTrail = new ArrayList<AuditTrailEntry>();
		auditTrail = BookTableGateway.getInstance().getSpecificAuditTrail(this);
		return auditTrail;
	}
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public void setISBN(String isbn) 
	{
		this.isbn = isbn;
	}
	
	public void setSummary(String summary) 
	{
		this.summary = summary;
	}
	
	public void setYear(int yearPublished) 
	{
		this.yearPublished = yearPublished;
	}
	
	public void setLastModified(LocalDateTime lastModified)
	{
		this.lastModified = lastModified; 
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	public void setPublisherId(int pId)
	{
		this.publisher_id = pId;
	}
	public String toString() 
	{
		StringBuilder bookInfo = new StringBuilder();
		bookInfo.append("ID: " + id + "\n");
		bookInfo.append("\tTitle: " + title + "\n");
		bookInfo.append("\tISBN: " + isbn + "\n");
		bookInfo.append("\tSummary: " + summary + "\n");
		bookInfo.append("\tYear Published: " + yearPublished);
		return bookInfo.toString();
	}
}
