package model;

import java.sql.Date;
import java.time.LocalDate;

public class Author {
	private int id;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private Character gender; 
	private String webSite;
	
	private final int DEFAULT_ID = -1;
	private final String DEFAULT_FIRST_NAME = "";
	private final String DEFAULT_LAST_NAME = "";
	private final LocalDate DEFAULT_DOB = LocalDate.now();
	private final Character DEFAULT_GENDER = '\0';
	
	public Author() {
		this.id = DEFAULT_ID;
		this.firstName = DEFAULT_FIRST_NAME;
		this.lastName = DEFAULT_LAST_NAME;
		this.dateOfBirth = DEFAULT_DOB;
		this.gender = DEFAULT_GENDER;
		this.webSite = null;
	}
	
	public Author(int id, String firstName, String lastName, Date dateOfBirth, Character gender) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth.toLocalDate();
		this.gender = gender;
		this.webSite = null;
	}
	
	public Author(int id, String firstName, String lastName, Date dateOfBirth, Character gender, String webSite) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth.toLocalDate();
		this.gender = gender;
		this.webSite = webSite;
	}
	public Author(String firstName, String lastName, Date dateOfBirth, Character gender, String webSite)
	{
		this.id = DEFAULT_ID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth.toLocalDate();
		this.gender = gender;
		this.webSite = webSite;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		java.util.Date date = java.sql.Date.valueOf(dateOfBirth);
		return (Date) date;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	@Override
	public String toString() {
		return "Author [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth="
				+ dateOfBirth + ", gender=" + gender + ", webSite=" + webSite + "]";
	}
	
}
