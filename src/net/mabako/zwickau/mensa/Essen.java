package net.mabako.zwickau.mensa;

import java.util.HashMap;

/**
 * Einfache HashMap, um später daraus die Liste der Essen zu generieren.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class Essen extends HashMap< String, String >{
	public static final String TEXT = "TEXT";
	public static final String TEXT2 = "TEXT2";
	
	private static final long serialVersionUID = 8623935790598908608L;

	public Essen( String a, String b )
	{
		put( TEXT, a );
		put( TEXT2, b );
	}
}
