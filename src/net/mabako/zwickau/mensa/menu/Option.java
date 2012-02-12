package net.mabako.zwickau.mensa.menu;

import net.mabako.zwickau.mensa.MensaActivity;

abstract class Option {
	/** Haupt-Text */
	public String text;
	
	/** Kleinerer Text */
	public String subtext;
	
	/** Activity */
	protected MensaActivity activity = MensaActivity.getInstance();

	/**
	 * Wird bei Auswahl des entsprechenden Items ausgeführt.
	 */
	public abstract boolean onSelected();
}