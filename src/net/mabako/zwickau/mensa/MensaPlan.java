package net.mabako.zwickau.mensa;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Eine HashMap, über welche man Listen mit Essen an einem Tag erhalten kann.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class MensaPlan extends HashMap<Integer, List<Essen>> {
	/**
	 * Automatisch generierte Serial
	 */
	private static final long serialVersionUID = 6632375863527629317L;

	public MensaPlan() {
		for (int i = 0; i < 14; ++i)
			put(i, new LinkedList<Essen>());
	}

	/**
	 * Speichert den Plan für eine Woche als HashMap.
	 * 
	 * @param htmlSource
	 *            Quelltext der Mensa-Webseite.
	 * @param naechsteWoche
	 *            ob der Plan für diese oder nächste Woche gilt.
	 */
	public void parse(String htmlSource, boolean naechsteWoche) {
		// Angebote mit Tagespreis filtenr.
		Pattern tagespreis = Pattern.compile("( zum Tagespreis)", Pattern.CASE_INSENSITIVE);
		Pattern feiertag = Pattern.compile("(Feiertag)", Pattern.CASE_INSENSITIVE);
		
		// Wir versuchen, Hauptessen X, Name des Gerichts und eventuell den
		// Preis auszulesen.
		Pattern p = Pattern.compile("<td valign=\"top\" class=\"headline\" >(.*?)</(.*?)<td colspan=\"2\">(.*?)</td>(.*?)<td width=\"195\" align=\"right\" valign=\"bottom\" class=\"smalltext2\">(.*?)<", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = p.matcher(htmlSource);

		int tabellenEintrag = 0;
		while (m.find()) {
			// Pro Tag abspeichern -> erstmal die Tagesliste auslesen
			List<Essen> essen = get(Calendar.MONDAY + (tabellenEintrag % 5) + (naechsteWoche ? 7 : 0));

			// Falls kein Titel wie Hauptessen 1 da ist, gibt es auch kein
			// Essen.
			String titel = m.group(1).trim();
			if (titel.length() > 0) {
				// Unnötige HTML-Zeilenumbrüche entfernen
				String was = m.group(3).trim().replaceAll("<br>", "");

				// Geld anzeigen, sofern überhaupt welches bezahlt werden muss.
				String geld = ", " + m.group(5).trim().replaceAll("&nbsp;", " ");

				// Wenn's was zum Tagespreis gibt, steht sinnvollerweise nichts
				// dabei.
				Matcher tagespreisMatcher = tagespreis.matcher(was);
				if (tagespreisMatcher.find())
				{
					was = tagespreisMatcher.replaceAll("");
					geld = ", Tagespreis";
				}
				
				if (feiertag.matcher(was).find() || feiertag.matcher(titel).matches())
				{
					was = "Feiertag";
					titel = "-";
					geld = "";
				}

				essen.add(new Essen(was, titel + geld));
			}

			++tabellenEintrag;
		}
	}
}
