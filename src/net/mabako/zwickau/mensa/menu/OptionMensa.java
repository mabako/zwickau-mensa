package net.mabako.zwickau.mensa.menu;

import java.util.Calendar;

import net.mabako.zwickau.mensa.Mensa;

/**
 * Option im Menü zur Auswahl einer Mensa.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class OptionMensa extends Option {
	/** Referenzierte Mensa */
	private Mensa mensa;

	/**
	 * Konstruktor
	 * @param mensa
	 */
	public OptionMensa(Mensa mensa) {
		this.mensa = mensa;
		text = mensa.getName();
		subtext = "";
	}

	/**
	 * Wählt eine Mensa aus und lädt den zugehörigen Plan.
	 */
	@Override
	public boolean onSelected() {
		activity.setMensa(mensa);
		activity.loadMensa(activity.getToday() > Calendar.FRIDAY);
		return false;
	}
}