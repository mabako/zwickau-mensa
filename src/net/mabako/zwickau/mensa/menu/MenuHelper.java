package net.mabako.zwickau.mensa.menu;

import net.mabako.zwickau.mensa.MensaActivity;
import net.mabako.zwickau.mensa.R;
import net.robotmedia.billing.BillingController;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Simpler Menühelfer für verschiedene Androidversionen.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class MenuHelper {
	/** Activity */
	protected MensaActivity activity = MensaActivity.getInstance();

	/**
	 * Liefert die für die jeweilige Plattform kompatible Instanz zurück.
	 * 
	 * @return
	 */
	public static MenuHelper getInstance() {
		return new MenuHelper();
	}
	
	/**
	 * Erstellt das Menü.
	 * 
	 * @param menu
	 * @return
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * Wird bei drücken der Menü-Taste aufgerufen.
	 * 
	 * @param menu
	 * @return
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem donate = menu.findItem(R.id.donate);
		donate.setVisible(!activity.hasDonated());
		return true;
	}

	/**
	 * Menü-Item wurde ausgewählt.
	 * 
	 * @param item
	 * @return
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.donate:
				if(!activity.hasDonated())
					BillingController.requestPurchase(activity, MensaActivity.DONATE, true);
				break;
			case R.id.reload:
				activity.reload();
				break;
		}
		return false;
	}
}
