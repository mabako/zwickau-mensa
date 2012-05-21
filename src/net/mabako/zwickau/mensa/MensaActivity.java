package net.mabako.zwickau.mensa;

import java.util.Calendar;
import java.util.List;

import com.viewpagerindicator.TitlePageIndicator;

import net.mabako.zwickau.mensa.menu.MenuHelper;
import net.robotmedia.billing.BillingController;
import net.robotmedia.billing.BillingRequest.ResponseCode;
import net.robotmedia.billing.helper.AbstractBillingObserver;
import net.robotmedia.billing.model.Transaction.PurchaseState;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;

/**
 * Die Hauptklasse unserer MensaApp.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class MensaActivity extends Activity {
	/** Instanz dieser Aktivität. */
	private static MensaActivity instance;

	/** Heutiger Wochentag. */
	private int today;

	/** Aktuell angezeigter Wochentag. */
	private int currentDay;

	/** zu nutzende Mensa. */
	private Mensa mensa = Mensa.RING;

	/** Menü */
	private MenuHelper menu;

	/** Hat der Benutzer gespendet? */
	private boolean donated = false;

	/** Datenbank-Helfer */
	DatabaseHandler db = new DatabaseHandler(this);

	private AbstractBillingObserver billingObserver;

	public final static String DONATE = "net.mabako.zwickau.mensa.donate";

	/** alle Views */
	private MensaPagerAdapter pagerAdapter;

	/**
	 * Berechnet den heutigen Tag, erstellt das Menü und lädt den Speiseplan.
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance = this;
		setContentView(R.layout.loading);

		// Gespeicherte Mensa (d.h. zuletzt aufgerufene) laden
		SharedPreferences pref = getPreferences(0);
		String m = pref.getString("mensa", null);
		if (m != null) {
			try {
				mensa = Mensa.valueOf(m);
			} catch (IllegalArgumentException e) {
			}
		}

		// Alte Daten löschen
		db.deleteOldFood();

		// Menü erstellen.
		if (menu == null) {
			menu = MenuHelper.getInstance();
		}
		setTitle(mensa.getName());

		// Den Plan der aktuellen Mensa laden.
		loadMensa(isNextWeek());

		// In-App-Billing
		setupBilling();
	}

	/**
	 * In-App-Billing
	 */
	private void setupBilling() {
		BillingController.setDebug(true);
		BillingController.setConfiguration(new BillingConfiguration());
		BillingController.checkBillingSupported(this);

		billingObserver = new AbstractBillingObserver(this) {
			public void onRequestPurchaseResponse(String itemId,
					ResponseCode response) {
				Log.d("psc", itemId + " => " + response.toString());
				if (itemId.equals(DONATE))
					setDonated(response == ResponseCode.RESULT_OK);
			}

			public void onPurchaseStateChanged(String itemId,
					PurchaseState state) {
				Log.d("psc", itemId + " => " + state.toString());
				if (itemId.equals(DONATE))
					setDonated(state == PurchaseState.PURCHASED);
			}

			public void onBillingChecked(boolean supported) {
			}
		};
		BillingController.registerObserver(billingObserver);

		if (!billingObserver.isTransactionsRestored())
			BillingController.restoreTransactions(this);
	}

	/**
	 * Seiten initialisieren
	 */
	private void initializePages() {
		setContentView(R.layout.main);
		pagerAdapter = new MensaPagerAdapter();
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(pagerAdapter);
		pager.setOnPageChangeListener(pagerAdapter);

		TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
		titleIndicator.setViewPager(pager);
	}

	/**
	 * Gibt zurück, ob der Plan für diese oder die nächste Woche auszugeben ist.
	 * 
	 * @return
	 */
	private boolean isNextWeek() {
		today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		boolean naechsteWoche = false;
		if (today == Calendar.SATURDAY || today == Calendar.SUNDAY) {
			currentDay = 7 + Calendar.MONDAY;
			naechsteWoche = true;

			// Der Anfang der aktuellen Woche ist Sonntag (1) und das Ende der
			// Woche Samstag (7)...
			// Also müssen wir das hier korrigieren.
			if (today == Calendar.SUNDAY)
				today += 7;
		} else
			currentDay = today;

		return naechsteWoche;
	}

	/**
	 * Setzt das Layout und fügt dem "Neu laden"-Button einen Listener hinzu.
	 */
	private void initializeReloadButton() {
		setContentView(R.layout.loading_failed);
		
		Button reload = (Button) findViewById(R.id.reload_button);
		reload.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				loadMensa(isNextWeek());
			}
		});
	}

	/**
	 * Wird gelöscht.
	 */
	@Override
	protected void onDestroy() {
		BillingController.unregisterObserver(billingObserver);
		db.close();
		super.onDestroy();
	}

	/**
	 * Lädt die Mensa bzw. den Plan.
	 * 
	 * @param naechsteWoche
	 *            ob der Plan für nächste Woche geladen werden soll
	 */
	public void loadMensa(boolean naechsteWoche) {
		if (!mensa.hasAnyFood(naechsteWoche)) {
			// Mensa-Webseite laden.
			loadPlan(naechsteWoche, false);
		} else {
			// Schon ein Plan da, nehmen wir doch den.
			update(naechsteWoche);
		}
	}

	/**
	 * Liefert diese Activity-Instanz zurück.
	 * 
	 * @return die Instanz
	 */
	public static MensaActivity getInstance() {
		return instance;
	}

	/**
	 * Lädt einen Mensaplan.
	 * 
	 * @param naechsteWoche
	 *            ob der Plan für nächste Woche geladen werden soll
	 * @param background
	 *            ob im Hintergrund geladen wird, bei <code>false</code> wird
	 *            ein Dialogfeld angezeigt.
	 */
	private void loadPlan(boolean naechsteWoche, boolean background) {
		// Aus der Datenbank laden, sofern möglich
		if (db.loadPlan(mensa, naechsteWoche))
			update(naechsteWoche);
		else
		{
			if(!background)
				setContentView(R.layout.loading);
			
			new DownloadTask(mensa, naechsteWoche).execute();
		}
	}

	/**
	 * Aktualisiert die Liste.
	 * 
	 * @param naechsteWoche
	 */
	public void update(boolean naechsteWoche) {
		if (mensa.hasAnyFood(naechsteWoche)) {
			if (pagerAdapter == null)
				initializePages();

			for (int day = Calendar.MONDAY + (naechsteWoche ? 7 : 0); day <= Calendar.FRIDAY
					+ (naechsteWoche ? 7 : 0); ++day) {
				if (pagerAdapter.getCount() >= 3 && !donated)
					break;

				List<Essen> essen = mensa.getPlan().get(day);
				if (essen.size() > 0 && day >= today)
					pagerAdapter.addPage(day, this,
							new SimpleAdapter(this, essen,
									R.layout.listitem,
									new String[] { Essen.TEXT, Essen.TEXT2 },
									new int[] { android.R.id.text1,
											android.R.id.text2 }) {
								@Override
								public boolean isEnabled(int position) {
									return false;
								}
							});
			}
		} else if (!mensa.hasAnyFood(false) && !mensa.hasAnyFood(true))
			initializeReloadButton();

		// Noch keine Einträge für nächste Woche geladen, also tun wir das.
		if (!naechsteWoche && !mensa.hasAnyFood(true)) {
			loadPlan(true, currentDay <= Calendar.FRIDAY);
		}
	}

	/**
	 * Liefert den heutigen (relativen) Tag zurück.
	 * 
	 * 2-6 entspricht Montag bis Freitag der aktuellen Woche, 9-13 entspricht
	 * Montag bis Freitag der nächsten Woche.
	 * 
	 * @return heutiger Tag.
	 */
	public int getToday() {
		return instance.today;
	}

	/**
	 * Gibt den zurzeit angezeigten Tag zurück.
	 * 
	 * @see MensaActivity#getToday()
	 * @return zurzeit angezeigter Tag
	 */
	public int getCurrentDay() {
		return currentDay;
	}

	/**
	 * Liefert die aktuelle Mensa zurück.
	 * 
	 * @return aktuelle Mensa
	 */
	public Mensa getMensa() {
		return instance.mensa;
	}

	/**
	 * Speichert die Mensa.
	 * 
	 * @param mensa
	 *            zu setzende Mensa.
	 */
	public void setMensa(Mensa mensa) {
		this.mensa = mensa;

		// Speichern.
		SharedPreferences pref = getPreferences(0);
		Editor editor = pref.edit();
		editor.putString("mensa", mensa.toString());
		editor.commit();
		setTitle(mensa.getName());

		pagerAdapter.clear();
	}

	/**
	 * Stellt ein Standard-Android-Menü bereit, welches mittels Menütaste
	 * erreichbar ist.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return this.menu.onCreateOptionsMenu(menu);
	}

	/**
	 * Handler für das Anzeigen des Standard-Android-Menüs.
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		return this.menu.onPrepareOptionsMenu(menu);
	}

	/**
	 * Handler für das Drücken des Standard-Android-Menüs.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return menu.onOptionsItemSelected(item);
	}

	/**
	 * Gibt den Namen des Tages zurück.
	 * 
	 * Dies ist im Allgemeinen entweder Heute, (Wochentag) oder nächster
	 * (Wochentag).
	 * 
	 * @param day
	 * @return Name des Tages
	 * @see MensaActivity#getToday()
	 */
	public String getNameOfDay(int day) {
		if (day == today)
			return MensaActivity.getInstance().getString(R.string.today);
		else
			return MensaActivity.getInstance().getResources()
					.getStringArray(R.array.daysOfWeek)[day];
	}

	/**
	 * Setzt donated-Flag
	 * 
	 * @param donated
	 */
	protected void setDonated(boolean donated) {
		this.donated = donated;
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			// Lädt sonst die Klasse nicht, VerifyError, da invalidateOptionsMenu erst ab SDK 11 existiert.
			new Object(){public void run(){instance.invalidateOptionsMenu();}}.run();
	}

	public boolean hasDonated() {
		return donated;
	}

	/**
	 * Lädt den Speiseplan erneut.
	 */
	public void reload() {
		db.deleteAllFood();
		
		initializeReloadButton();
		
		if(mensa != null)
		mensa.getPlan().clear();
		
		if(pagerAdapter != null)
		{
			pagerAdapter.clear();
			pagerAdapter = null;
		}
		
		loadMensa(isNextWeek());
	}
}
