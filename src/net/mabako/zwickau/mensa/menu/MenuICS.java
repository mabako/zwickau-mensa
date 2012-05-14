package net.mabako.zwickau.mensa.menu;

import java.util.LinkedList;
import java.util.List;

import net.mabako.zwickau.mensa.R;
import android.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * IceCream Sandwich-ActionBar-Menü.
 * 
 * Gibt nur das Datum, Wochentag und Mensa in der Actionbar aus.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class MenuICS extends MenuHelper {
	private ActionBar actionBar;

	/**
	 * Aktualisiert den View für die ActionBar und passt diese an den heutigen
	 * Tag an.
	 */
	public MenuICS() {
		actionBar = activity.getActionBar();

		// Keinen Titel anzeigen
		actionBar.setDisplayShowTitleEnabled(false);

		// Liste einrichten
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		Adapter adapter = new Adapter();
		Callback callback = new Callback();
		actionBar.setListNavigationCallbacks(adapter, callback);
		actionBar.setSelectedNavigationItem(findCurrentMensa());
	}

	/**
	 * Gibt alle sichtbaren Optionen zurück.
	 * 
	 * @return Liste aller sichtbaren Optionen.
	 */
	private List<Option> getVisibleOptions() {
		List<Option> visibleOptions = new LinkedList<Option>();
		int i = 0;
		for (Option o : options) {
			if (o.isVisible(i++)) {
				visibleOptions.add(o);
			}
		}
		return visibleOptions;
	}

	private class Adapter extends BaseAdapter {
		public int getCount() {
			return getVisibleOptions().size();
		}

		public Option getItem(int position) {
			return getVisibleOptions().get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		/**
		 * Versucht, den angegeben View wiederzuverwenden. Spart Speicher,
		 * bringt Performance.
		 * 
		 * @param view
		 * @param resource
		 * @param parent
		 * @return
		 */
		private View getRecycledView(View view, int resource, ViewGroup parent) {
			if (view == null || view.getId() != resource)
				return activity.getLayoutInflater().inflate(resource, parent, false);
			else
				return view;
		}

		/**
		 * Hauptview, falls dieses Item in der ActionBar gezeigt wird.
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View child = getRecycledView(convertView, R.layout.actionbar, parent);

			TextView text = (TextView) child.findViewById(R.id.actionbar_text);
			text.setText(getItem(position).getText());

			return child;
		}

		/**
		 * Dropdown-View, ist ähnlich dem normalen (Haupt-)View, jedoch ist das
		 * Datum abgekürzt.
		 */
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			View child = getRecycledView(convertView, R.layout.actionbar_dropdown, parent);

			TextView text = (TextView) child.findViewById(R.id.actionbar_text);
			text.setText(getItem(position).getText());

			return child;
		}
	}
	
	private class Callback implements ActionBar.OnNavigationListener {
		public boolean onNavigationItemSelected(int position, long itemId) {
			// Kann man die Option auswählen (Mensa gibt hier 'false' zurück)?
			if (!getVisibleOptions().get(position).onSelected()) {
				actionBar.setSelectedNavigationItem(findCurrentMensa());
			}
			return true;
		}
	}

	/**
	 * Liefert die Position des aktuellen Tages zurück.
	 * 
	 * @return
	 */
	private int findCurrentMensa() {
		for (int i = 0; i < getVisibleOptions().size(); ++i) {
			Option option = getVisibleOptions().get(i);
			if (option instanceof OptionMensa) {
				if (((OptionMensa)option).getMensa() == activity.getMensa()) {
					return i;
				}
			}
		}
		return 0;
	}
}
