package controller.BookDetailController;

import java.util.Calendar;

public class RecordValidator {
	
	public static boolean isBookInfoValid(String title, String summary, int year, String isbn) 
	{
		if (!isTitleValid(title)
				|| !isSummaryValid(summary)
				|| !isYearPublishedValid(year)
				|| !isIsbnValid(isbn)) {
			return false;
		}	
		return true;
	}
	
	private static boolean isTitleValid(String title) 
	{
		if (title != null && title.length() > 0 && title.length() <= 255) {
			return true;
		}
		AlertManager.displayValidationAlert("Title must be between 1 and 255 characters");
		return false;
	}
	
	private static boolean isSummaryValid(String summary) 
	{
		if (summary != null && summary.length() <= 65535) {
			return true;
		}
		AlertManager.displayValidationAlert("Summary must be less than 65,535 characters");
		return false;
	}
	
	private static boolean isYearPublishedValid(int yearPublished) 
	{
		if (yearPublished >= 1455 && yearPublished <= Calendar.getInstance().get(Calendar.YEAR)) {
			return true;
		}
		AlertManager.displayValidationAlert("Year Published must be between 1455 and current year");
		return false;
	}
	
	private static boolean isIsbnValid(String isbn) 
	{
		if (isbn != null && isbn.replaceAll("-", "").length() <= 13) {
			return true;
		}
		AlertManager.displayValidationAlert("ISBN must be 13 characters or less");
		return false;
	}
}
