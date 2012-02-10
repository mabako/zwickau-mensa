package net.mabako.zwickau.mensa;

import java.util.Calendar;

public enum Mensa {
	RING("Ring", "http://www.tu-chemnitz.de/stuwe/speiseplan_public/web/web_Ring.php", "http://www.tu-chemnitz.de/stuwe/speiseplan_public/web/web_Ring_nextweek.php?"),
	SCHEFFELBERG("Scheffelberg", "http://www.tu-chemnitz.de/stuwe/speiseplan_public/web/web_Scheffelberg.php", "http://www.tu-chemnitz.de/stuwe/speiseplan_public/web/web_Scheffelberg_nextweek.php");
	
	private String name;
	private String current;
	private String next;
	private Mensa( String name, String current, String next )
	{
		this.name = name;
		this.current = current;
		this.next = next;
	}
	
	public String getURL( boolean naechsteWoche )
	{
		if (naechsteWoche)
		{
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 7);
			return next + "year=" + c.get(Calendar.YEAR) + "&week=" + c.get(Calendar.WEEK_OF_YEAR);
		}
		else
			return current;		
	}
	
	public String toString()
	{
		return name;
	}
}
