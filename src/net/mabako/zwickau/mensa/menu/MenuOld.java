package net.mabako.zwickau.mensa.menu;

import net.mabako.zwickau.mensa.Mensa;
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
	public void updateTitle(Mensa mensa) {
		activity.setTitle(mensa.getName());
	}

	/**
	 * Erstellt das Menü.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		for( int i = 0; i < options.size(); ++ i )
		{
			menu.add( Menu.NONE, i, i, options.get(i).getText() );
		}
		return true;
	}

	/**
	 * Wird bei Aufruf des Menüs aufgerufen.
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		for( int i = 0; i < menu.size(); ++ i )
		{
			menu.getItem(i).setVisible(options.get(i).isVisible(i));
		}
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
