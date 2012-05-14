package net.mabako.zwickau.mensa.menu;

import java.util.ArrayList;
import java.util.List;

import net.mabako.zwickau.mensa.Mensa;
import net.mabako.zwickau.mensa.MensaActivity;

import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Simpler Menühelfer für verschiedene Androidversionen.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public abstract class MenuHelper {
	/** Activity */
	protected MensaActivity activity = MensaActivity.getInstance();

	/** Liste mit Optionen */
	protected List<Option> options = new ArrayList<Option>();

	/**
	 * Liefert die für die jeweilige Plattform kompatible Instanz zurück.
	 * 
	 * @return
	 */
	public static MenuHelper getInstance() {
		// Anhand der SDK-Version das zu verwendende Menü auswählen.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return new MenuICS();
		} else {
			return new MenuOld();
		}
	}

	/**
	 * Erstellt alle Optionen.
	 */
	protected MenuHelper() {
		for (Mensa mensa : Mensa.values()) {
			options.add(new OptionMensa(mensa));
		}
		
		options.add(new OptionDonate());
	}

	/**
	 * Aktualisiert den Titel der Anwendung.
	 * 
	 * @param mensa
	 */
	public void updateTitle(Mensa mensa) {
	}

	/**
	 * Erstellt ein (altes) Menü.
	 * 
	 * @param menu
	 * @return
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	/**
	 * Wird bei drücken der Menü-Taste aufgerufen.
	 * 
	 * @param menu
	 * @return
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		return false;
	}

	/**
	 * Menü-Item wurde ausgewählt.
	 * 
	 * @param item
	 * @return
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
}
