package model;

public class Book {

	private String bookTitle;
	private String bookISBN;
	private String bookSummary;
	private int bookYearPublished;
	private int id;
	
	public Book()
	{
		bookTitle = "";
		bookISBN = "";
		bookSummary = "";
		bookYearPublished = 0;
		id = 0;
	}
	public Book(String title, String isbn, String summary, int year)
	{
		bookTitle = title;
		bookISBN = isbn;
		bookSummary = summary;
		bookYearPublished = year;
	}
	public Book(String title, String isbn, String summary, int year, int id)
	{
		bookTitle = title;
		bookISBN = isbn;
		bookSummary = summary;
		bookYearPublished = year;
		this.id = id;
	}
	public String getTitle()
	{
		return bookTitle;
	}
	public String getISBN()
	{
		return bookISBN;
	}
	public String getSummary()
	{
		return bookSummary;
	}
	public int getYear()
	{
		return bookYearPublished;
	}
	public int getId()
	{
		return id;
	}
	public void setTitle(String title)
	{
		bookTitle = title;
	}
	public void setISBN(String isbn)
	{
		bookISBN = isbn;
	}
	public void setSummary(String summary)
	{
		bookSummary = summary;
	}
	public void setYear(int year)
	{
		bookYearPublished = year;
	}
	public void setId(int bookId)
	{
		id = bookId;
	}
	public String toString()
	{
		String output = "Book ID: " + id + "\n";
		output += "\tTitle: " + bookTitle + "\n";
		output += "\tIsbn: " + bookISBN + "\n";
		output += "\tSummary: " + bookSummary + "\n";
		output += "\tYear Published: " + bookYearPublished;
		return output;
	}
}
