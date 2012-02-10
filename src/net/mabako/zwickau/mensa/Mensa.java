package net.mabako.zwickau.mensa;

import java.util.Calendar;

/**
 * Verwaltet die einzelnen Mensen.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public enum Mensa {
	RING("Mensa Ring", "http://www.tu-chemnitz.de/stuwe/speiseplan_public/web/web_Ring.php", "http://www.tu-chemnitz.de/stuwe/speiseplan_public/web/web_Ring_nextweek.php?"), SCHEFFELBERG("Scheffelberg", "http://www.tu-chemnitz.de/stuwe/speiseplan_public/web/web_Scheffelberg.php", "http://www.tu-chemnitz.de/stuwe/speiseplan_public/web/web_Scheffelberg_nextweek.php");

	/** Name der Mensa */
	private String name;

	/** URL für die aktuelle Woche */
	private String current;

	/** URL für die nächste Woche */
	private String next;

	/** Der Speiseplan */
	private MensaPlan plan;

	/**
	 * 
	 * @param name
	 * @param current
	 * @param next
	 */
	private Mensa(String name, String current, String next) {
		this.name = name;
		this.current = current;
		this.next = next;

		plan = new MensaPlan();
	}

	/**
	 * Liefert die URL der Mensawebseite zurück.
	 * 
	 * @param naechsteWoche
	 * @return
	 */
	public String getURL(boolean naechsteWoche) {
		if (naechsteWoche) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 7);
			return next + "year=" + c.get(Calendar.YEAR) + "&week=" + c.get(Calendar.WEEK_OF_YEAR);
		} else
			return current;
	}

	/**
	 * Liefert den Namen der Mensa.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Liefert den Speiseplan.
	 */
	public MensaPlan getPlan() {
		return plan;
	}

	/**
	 * @param naechsteWoche
	 * @return <code>true</code>, falls es in der entsprechenden Woche etwas zu
	 *         Essen gibt.
	 */
	public boolean hasAnyFood(boolean naechsteWoche) {
		int start = naechsteWoche ? 7 : 0;
		for (int i = start; i < start + 7; ++i) {
			if (plan.get(i) != null && plan.get(i).size() > 0) {
				return true;
			}
		}
		return false;
	}
}
