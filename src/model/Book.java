package model;

public class Book {

	private String bookTitle;
	private String bookISBN;
	private String bookSummary;
	private int bookYearPublished;
	
	public Book()
	{
		bookTitle = "";
		bookISBN = "";
		bookSummary = "";
		bookYearPublished = 0;
	}
	public Book(String title, String isbn, String summary, int year)
	{
		bookTitle = title;
		bookISBN = isbn;
		bookSummary = summary;
		bookYearPublished = year;
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
}
