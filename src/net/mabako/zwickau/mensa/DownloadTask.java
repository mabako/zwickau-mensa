﻿package net.mabako.zwickau.mensa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

/**
 * Task zum Herunterladen der Mensa-Webseiten und füllen des jeweiligen
 * Essenplans.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class DownloadTask extends AsyncTask<Void, Void, Void> {
	/** Encoding der Mensa-Seiten. */
	private static final String ENCODING = "windows-1252";

	/** Holt dieser Task den Fraß dieser Woche oder der nächsten Woche? */
	private boolean naechsteWoche = false;

	/** Die entsprechende Mensa. */
	private Mensa mensa;

	/**
	 * Erzeugt den Task.
	 * 
	 * @param mensa
	 *            die entsprechende Mensa.
	 * @param naechsteWoche
	 *            <code>true</code>, falls das Essen der nächsten Woche geholt
	 *            werden soll.
	 */
	public DownloadTask(Mensa mensa, boolean naechsteWoche) {
		this.mensa = mensa;
		this.naechsteWoche = naechsteWoche;
	}

	/**
	 * Webseite herunterladen, parsen, Plan füllen.
	 */
	@Override
	protected Void doInBackground(Void... params) {
		try {
			// Seite laden
			InputStreamReader response = fetchSite(mensa.getURL(naechsteWoche));

			// String bauen
			String responseText = readAllContent(response);

			// In den Plan einfügen.
			mensa.getPlan().parse(responseText, naechsteWoche);
			
			// In Datenbank speichern
			MensaActivity.getInstance().db.savePlan(mensa, naechsteWoche);
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
		BufferedReader in = new BufferedReader(inputStream, 2048);

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
	 * Sagt der MensaActivity, dass wir fertig sind und versteckt den
	 * Fortschrittsdialog.
	 */
	@Override
	public void onPostExecute(Void result) {
		// Aktuelle Woche ODER es ist schon Samstag/Sonntag, also wird der Plan
		// aktualisiert.
		MensaActivity.getInstance().update(naechsteWoche);
	}
}
