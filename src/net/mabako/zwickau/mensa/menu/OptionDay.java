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

	/**
	 * Konstruktor
	 * @param day
	 */
	public OptionDay(int day) {
		this.day = day;
		text = activity.getNameOfDay(day);

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, day - activity.getToday());
		subtext = DateUtils.formatDateTime(activity, c.getTimeInMillis(), DateUtils.FORMAT_SHOW_YEAR) + ", " + activity.getMensa().getName();
	}

	/**
	 * Zeigt einen einzelnen Tag an.
	 */
	@Override
	public boolean onSelected() {
		activity.showDay(day);
		return true;
	}
}