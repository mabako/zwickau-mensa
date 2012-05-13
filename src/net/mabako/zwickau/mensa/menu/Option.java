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
	 * Kleinerer Text für das ICS-Menü.
	 * 
	 * @return
	 */
	public String getSubText() {
		return null;
	}

	/**
	 * Kleiner Text für das Dropdown-Menü für ICS.
	 * 
	 * @return
	 */
	public String getDropdownText() {
		return null;
	}

	/**
	 * Ist diese Option sichtbar?
	 * 
	 * @return
	 */
	public abstract boolean isVisible(int count);
}
