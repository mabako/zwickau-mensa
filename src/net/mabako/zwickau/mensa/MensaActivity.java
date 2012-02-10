package net.mabako.zwickau.mensa;

import java.util.Calendar;
import java.util.List;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

public class MensaActivity extends ListActivity {
	/**
	 * Instanz dieser Aktivität.
	 */
	private static MensaActivity instance;

	/**
	 * Heutiger Wochentag
	 */
	private int today;

	/**
	 * zu nutzende Mensa.
	 */
	private Mensa mensa = Mensa.RING;

	/**
	 * Menü
	 */
	private MenuHelper menu;

	/**
	 * Setzt die Liste und lädt die Einträge.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance = this;

		// Tag ausrechnen, der angezeigt werden soll.
		today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		boolean naechsteWoche = false;
		if (today == Calendar.SATURDAY || today == Calendar.SUNDAY) {
			today = 7 + Calendar.MONDAY;
			naechsteWoche = true;
		}

		// Menü erstellen.
		if (menu == null) {
			menu = MenuHelper.getInstance();
		}

		if (!Cache.isSet()) {
			// Mensa-Webseite laden.
			loadPlan(new MensaPlan(), naechsteWoche, false);
		} else {
			// Schon ein Plan da, nehmen wir doch den.
			update(naechsteWoche);
		}
	}

	/**
	 * Liefert diese Activity zurück.
	 * 
	 * @return
	 */
	public static MensaActivity getInstance() {
		return instance;
	}

	/**
	 * Lädt einen Mensaplan
	 * 
	 * @param plan
	 * @param naechsteWoche
	 * @param background
	 */
	private void loadPlan(MensaPlan plan, boolean naechsteWoche, boolean background) {
		new MensaTask(plan == null ? Cache.get() : plan, naechsteWoche, background).execute(mensa.getURL(naechsteWoche));
	}

	/**
	 * Aktualisiert die Liste.
	 * 
	 * @param naechsteWoche
	 */
	public void update(boolean naechsteWoche) {
		showDay(today);

		// Noch keine Einträge für nächste Woche geladen, also tun wir das.
		if (!naechsteWoche && Cache.get().get(9).size() == 0) {
			loadPlan(null, true, true);
		}
	}

	public void showDay(int day) {
		// Im Titel den Tag anzeigen
		setTitle(getNameOfDay(day));
		menu.updateTitle(day);

		// Liste mit Essen anzeigen.
		MensaPlan plan = Cache.get();
		List<Essen> essen = plan.get(day);
		SimpleAdapter adapter = new SimpleAdapter(this, essen, android.R.layout.simple_list_item_2, new String[] { Essen.TEXT, Essen.TEXT2 }, new int[] { android.R.id.text1, android.R.id.text2 });
		setListAdapter(adapter);
	}

	public int getToday() {
		return instance.today;
	}

	public Mensa getMensa() {
		return instance.mensa;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		for( int i = today; i <= 7 + Calendar.FRIDAY; ++ i )
		{
			if( i % 7 > Calendar.SUNDAY )
			{
				menu.add(Menu.NONE, i, i, getNameOfDay(i));
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		showDay(item.getItemId());
		return true;
	}
	
	public String getNameOfDay(int day) {
		if (day == today)
			return MensaActivity.getInstance().getString(R.string.today);
		else
			return MensaActivity.getInstance().getResources().getStringArray(R.array.daysOfWeek)[day];
	}
}
