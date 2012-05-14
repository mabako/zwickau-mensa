package net.mabako.zwickau.mensa.menu;

import net.mabako.zwickau.mensa.MensaActivity;

abstract class Option {
	/** Activity */
	protected MensaActivity activity = MensaActivity.getInstance();

	/**
	 * Wird bei Auswahl des entsprechenden Items ausgeführt.
	 */
	public abstract boolean onSelected();

	/**
	 * Haupt-Text der Option, wird im einfachen Menü als einzige angezeigt.
	 * 
	 * @return
	 */
	public abstract String getText();

	/**
	 * Ist diese Option sichtbar?
	 * 
	 * @return
	 */
	public abstract boolean isVisible(int count);
}
