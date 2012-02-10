package net.mabako.zwickau.mensa;

import java.util.Calendar;

/**
 * Speichert den MensaPlan zwischendurch.
 * 
 * Das ist empfehlenswert, da sich die Ansicht sonst bei jedem Drehen der
 * Ansicht verändert.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class Cache {
	/** Der gespeicherte PLan */
	private static MensaPlan mensaPlan;

	/**
	 * Tag, an dem der Plan aktuell war. Ändert sich der Tag im Jahr, wird ein
	 * neuer Plan geladen
	 */
	private static int day = -1;

	/**
	 * Speichert den Plan.
	 * 
	 * @param mensaPlan
	 *            Plan
	 */
	public static void set(MensaPlan mensaPlan) {
		Cache.mensaPlan = mensaPlan;
		day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * Gibt den Plan zurück
	 * 
	 * @return Plan
	 */
	public static MensaPlan get() {
		return mensaPlan;
	}

	/**
	 * Gibt zurück, ob wir einen zwischengespeicherten Plan haben und ob dieser
	 * Heute geladen wurde.
	 * 
	 * @return <code>true</code> falls der Plan aktuell und von Heute ist.
	 */
	public static boolean isSet() {
		return mensaPlan != null
				&& Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == day;
	}
}
