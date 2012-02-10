package net.mabako.zwickau.mensa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class MensaTask extends AsyncTask<String, Void, Void> {
	/** Bitte warten-Dialog */
	ProgressDialog progressDialog;

	boolean naechsteWoche = false;

	private boolean background = false;

	private MensaPlan plan;

	public MensaTask(MensaPlan plan, boolean naechsteWoche, boolean background) {
		this.naechsteWoche = naechsteWoche;
		this.plan = plan;
		this.background = background;
	}

	@Override
	protected void onPreExecute() {
		// Dialog zum Warten zeigen.
		if (!background) {
			String[] messages = MensaActivity.getInstance().getResources().getStringArray(R.array.loading);
			progressDialog = ProgressDialog.show(MensaActivity.getInstance(), "", MensaActivity.getInstance().getString(R.string.loading_main) + "\n" + messages[new Random().nextInt(messages.length)], true);
		}
	}

	@Override
	protected Void doInBackground(String... params) {
		try {
			// Ne HTTP-Anfrage, ist doch fast ganz einfach.
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(params[0]));
			HttpResponse response = client.execute(request);

			// Inhalt auslesen. Wichtig ist hierbei das encoding, da sonst für
			// ä, ö, ü usw. nur unbekannte Zeichen stehen.
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "windows-1252"));

			// Zeilenweise einlesen.
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
			in.close();

			plan.parse(sb.toString(), naechsteWoche);
		} catch (Exception e) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(MensaActivity.getInstance());
			dialog.setMessage("Exception:\n" + e.getClass().toString() + "\n" + e.getMessage());
			dialog.setCancelable(false);
			AlertDialog alert = dialog.create();
			alert.show();
			// System.exit(0);
		}
		return null;
	}

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
