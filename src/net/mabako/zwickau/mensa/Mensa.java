﻿package net.mabako.zwickau.mensa;

import java.util.Calendar;

/**
 * Verwaltet die einzelnen Mensen.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public enum Mensa {
	RING("Mensa Ring", "http://www.tu-chemnitz.de/stuwe/speiseplan_public/web/web_Ring.php", "http://www.tu-chemnitz.de/stuwe/speiseplan_public/web/web_Ring_nextweek.php?");
	// Plan für Scheffelberg gibt es momentan nur als PDF (Zelt-Mensa usw.)
	//SCHEFFELBERG("Scheffelberg", "http://www.tu-chemnitz.de/stuwe/speiseplan_public/web/web_Scheffelberg.php", "http://www.tu-chemnitz.de/stuwe/speiseplan_public/web/web_Scheffelberg_nextweek.php");

	/** Name der Mensa */
	private String name;

	/** URL für die aktuelle Woche */
	private String current;

	/** URL für die nächste Woche */
	private String next;

	/** Der Speiseplan */
	private MensaPlan plan;

	/**
	 * Erstellt eine Mensa mit leerem Plan.
	 * 
	 * @param name Name der Mensa
	 * @param current Web-Addresse für aktuellen Plan
	 * @param next Web-Adresse für Plan der nächsten Woche
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
	 * @param naechsteWoche ob die URL für nächste Woche zurückgegeben werden soll.
	 * @return Web-Addresse zur Abfrage des Planes.
	 */
	public String getURL(boolean naechsteWoche) {
		if (naechsteWoche) {
			Calendar c = Calendar.getInstance();
			
			// Aufgrund unterschiedlicher Wochenanfänge (Sonntag, Montag) hier der folgende Code:
			if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			{
				// von Sonntag -> Montag springen.
				c.add(Calendar.DAY_OF_YEAR, 1);
			}
			else
			{
				// Ist kein Sonntag, daher entfällt auch das Anfang-der-Woche prüfen.
				c.add(Calendar.DAY_OF_YEAR, 7);
			}
			
			return next + "year=" + c.get(Calendar.YEAR) + "&week=" + c.get(Calendar.WEEK_OF_YEAR);
		} else
			return current;
	}

	/**
	 * Liefert den Namen der Mensa.
	 * 
	 * @return Name der Mensa
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Liefert den Speiseplan.
	 * 
	 * @return Speiseplan
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
		// Offset in Tagen
		int start = naechsteWoche ? 7 : 0;
		
		// Alle Tage durchlaufen
		for (int i = start; i < start + 7; ++i) {
			if (plan.get(i) != null && plan.get(i).size() > 0) {
				return true;
			}
		}
		return false;
	}
}
