package net.mabako.zwickau.mensa.menu;

import android.view.Menu;
import android.view.MenuItem;

/**
 * Altes Android-Menü für Versionen < 3.0
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class MenuOld extends MenuHelper {
	/**
	 * Aktualisiert den Titel der Anwendung
	 */
	@Override
	public void updateTitle(int day) {
		activity.setTitle(activity.getNameOfDay(day) + ", " + activity.getMensa().getName());
	}

	/**
	 * Erstellt das Menü.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		for( int i = 0; i < options.size(); ++ i )
		{
			menu.add( Menu.NONE, i, i, options.get(i).text );
		}
		return true;
	}

	/**
	 * Wird bei Aufruf des Menüs aufgerufen.
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Die Mensa verstecken, welche aktuell angezeigt wird.
		//TODO fix, aber nicht hier.
		/*
		for (Mensa mensa : Mensa.values()) {
			menu.findItem(mensa.hashCode()).setVisible(activity.getMensa() != mensa);
		}
		*/
		return true;
	}

	/**
	 * Wird bei Auswahl eines Menüitems aufgerufen.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Option o = options.get(item.getItemId());
		if(o != null)
		{
			o.onSelected();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
