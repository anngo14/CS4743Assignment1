package model;

public class AuthorBook {
	private Author author;
	private Book book;
	private int royalty;
	private boolean newRecord = true;
	
	public AuthorBook() {
		
	}
	public AuthorBook(Book book)
	{
		this.book = book;
		author = new Author();
		
	}
	public AuthorBook(Author author, Book book, int royalty) {
		this.author = author;
		this.book = book;
		this.royalty = royalty * 100000;
	}
	
	public AuthorBook(Author author, Book book, int royalty, boolean newRecord) {
		this.author = author;
		this.book = book;
		this.royalty = royalty * 100000;
		this.newRecord = newRecord;
	}

	public Author getAuthor() {
		return author;
	}

	public String getAuthorName() {
		return author.getFirstName() + " " + author.getLastName();
	}
	public void setAuthor(Author author) {
		this.author = author;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public int getRoyalty() {
		return royalty;
	}

	public void setRoyalty(int royalty) {
		this.royalty = royalty;
	}

	public boolean isNewRecord() {
		return newRecord;
	}

	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}

	@Override
	public String toString() {
		return "AuthorBook [author=" + author + ", book=" + book + ", royalty=" + royalty + ", newRecord=" + newRecord
				+ "]";
	}
	
}
