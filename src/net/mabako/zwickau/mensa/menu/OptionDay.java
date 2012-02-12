package net.mabako.zwickau.mensa.menu;

import java.util.Calendar;

import android.text.format.DateUtils;

/**
 * Option zur Auswahl eines Tages.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class OptionDay extends Option {
	/** Der Tag */
	private int day;
	
	private Calendar calendar;

	/**
	 * Konstruktor
	 * 
	 * @param day
	 */
	public OptionDay(int day) {
		this.day = day;
		
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, day - activity.getToday());
	}

	/**
	 * Zeigt einen einzelnen Tag an.
	 */
	@Override
	public boolean onSelected() {
		activity.showDay(day);
		return true;
	}

	/**
	 * Gibt den Namen des tages zurück.
	 * 
	 * @return
	 */
	@Override
	public String getText() {
		return activity.getNameOfDay(day);
	}

	/**
	 * Gibt das Datum des Tages und den Namen der Mensa zurück.
	 * 
	 * @return
	 */
	@Override
	public String getSubText() {
		return DateUtils.formatDateTime(activity, calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_YEAR) + ", " + activity.getMensa().getName();
	}

	/**
	 * Gibt einen kurzen Datums-String für das Dropdown-Menü zurück.
	 * 
	 * @return
	 */
	@Override
	public String getDropdownText()
	{
		return DateUtils.formatDateTime(activity, calendar.getTimeInMillis(), DateUtils.FORMAT_ABBREV_MONTH);
	}
	
	/**
	 * Gibt den Tag zurück, der durch diese Option repräsentiert wird.
	 * 
	 * @return
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Tage sind immer sichtbar.
	 * 
	 * @return true
	 */
	@Override
	public boolean isVisible() {
		return true;
	}
}