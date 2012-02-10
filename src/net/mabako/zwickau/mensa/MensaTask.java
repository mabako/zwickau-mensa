package net.mabako.zwickau.mensa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Task zum Herunterladen der Mensa-Webseiten und füllen des jeweiligen
 * Essenplans.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class MensaTask extends AsyncTask<String, Void, Void> {
	/** Encoding der Mensa-Seiten. */
	private static final String ENCODING = "windows-1252";

	/** Bitte warten-Dialog */
	ProgressDialog progressDialog;

	/** Holt dieser Task den Fraß dieser Woche oder der nächsten Woche? */
	boolean naechsteWoche = false;

	/** Im Hintergrund? Falls ja wird kein Fortschrittsdialog angezeigt. */
	private boolean background = false;

	/** Der zu füllende Plan */
	private MensaPlan plan;

	/**
	 * Erzeugt den Task.
	 * 
	 * @param plan
	 *            der zu füllende Plan.
	 * @param naechsteWoche
	 *            <code>true</code>, falls das Essen der nächsten Woche geholt
	 *            werden soll.
	 * @param background
	 *            falls <code>true</code>, wird kein Fortschrittsdialog
	 *            angezeigt.
	 */
	public MensaTask(MensaPlan plan, boolean naechsteWoche, boolean background) {
		this.naechsteWoche = naechsteWoche;
		this.plan = plan;
		this.background = background;
	}

	/**
	 * Zeigt den Fortschrittsdialog an, falls dieser Task nicht im Hintergrund
	 * läuft.
	 */
	@Override
	protected void onPreExecute() {
		// Dialog zum Warten zeigen.
		if (!background) {
			String[] messages = MensaActivity.getInstance().getResources().getStringArray(R.array.loading);
			progressDialog = ProgressDialog.show(MensaActivity.getInstance(), "", MensaActivity.getInstance().getString(R.string.loading_main) + "\n" + messages[new Random().nextInt(messages.length)], true);
		}
	}

	/**
	 * Webseite herunterladen, parsen, Plan füllen.
	 */
	@Override
	protected Void doInBackground(String... params) {
		try {
			// Seite laden
			InputStreamReader response = fetchSite(params[0]);

			// String bauen
			String responseText = readAllContent(response);

			// In den Plan einfügen.
			plan.parse(responseText, naechsteWoche);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Lädt eine Webseite.
	 * 
	 * @param site
	 *            URL der Seite
	 * @return Den Reader auf den Inhalt.
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private InputStreamReader fetchSite(String site) throws URISyntaxException, IOException, ClientProtocolException {
		HttpClient client = new DefaultHttpClient();

		// Wir verschicken unsere Abfrage über GET
		// TODO Die Seiten für "nächste Woche" sind normalerweise per POST zu
		// erreichen. Geht aber auch mit GET ("for the time being")
		HttpGet request = new HttpGet();

		request.setURI(new URI(site));

		// Abfrage ausführen
		HttpResponse response = client.execute(request);

		// Inhalt auslesen. Wichtig ist hierbei das encoding, da sonst für
		// ä, ö, ü usw. nur unbekannte Zeichen stehen.
		return new InputStreamReader(response.getEntity().getContent(), ENCODING);
	}

	/**
	 * Liest sämtlichen Inhalt und gibt diesen als String zurück.
	 * 
	 * @param inputStream
	 *            Eingabe
	 * @return String mit allen aus der Eingabe gelesenen Zeichen.
	 * @throws IOException
	 */
	private String readAllContent(InputStreamReader inputStream) throws IOException {
		BufferedReader in = new BufferedReader(inputStream);

		// Zeilenweise einlesen.
		String line;
		StringBuffer sb = new StringBuffer();
		while ((line = in.readLine()) != null) {
			sb.append(line + "\n");
		}
		in.close();
		return sb.toString();
	}

	/**
	 * Speichert den Plan im Cache, sagt der MensaActivity, dass wir fertig
	 * sind, versteckt den Fortschrittsdialog.
	 */
	@Override
	public void onPostExecute(Void result) {
		Cache.set(plan);

		// Aktuelle Woche ODER es ist schon Samstag/Sonntag, also wird der Plan
		// aktualisiert.
		MensaActivity.getInstance().update(naechsteWoche);

		// Sofern überhaupt eine Fortschrittsanzeige da war, schließen wir
		// diese.
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
}
