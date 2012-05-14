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
	 * 
	 * @param mensa
	 */
	public OptionMensa(Mensa mensa) {
		this.mensa = mensa;
	}

	/**
	 * Wählt eine Mensa aus und lädt den zugehörigen Plan.
	 * 
	 * @return
	 */
	@Override
	public boolean onSelected() {
		if(activity.getMensa() != mensa)
		{
			activity.setMensa(mensa);
			activity.loadMensa(activity.getToday() > Calendar.FRIDAY);
		}
		return true;
	}

	/**
	 * Gibt den Namen der Mensa zurück.
	 * 
	 * @return
	 */
	@Override
	public String getText() {
		return mensa.getName();
	}

	/**
	 * Mensa ist nur sichtbar, falls diese nicht aktuell ausgewählt ist.
	 * 
	 * @return
	 */
	@Override
	public boolean isVisible(int count) {
		//return mensa != activity.getMensa();
		return true;
	}
	
	/**
	 * Liefert die Mensa zurück, die zu dieser Option gehört.
	 * 
	 * @return
	 */
	public Mensa getMensa()
	{
		return mensa;
	}
}