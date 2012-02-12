package net.mabako.zwickau.mensa.menu;

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
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		Adapter adapter = new Adapter();
		Callback callback = new Callback();
		actionBar.setListNavigationCallbacks(adapter, callback);
	}

	private class Adapter extends BaseAdapter {
		public int getCount() {
			return options.size();
		}

		public Option getItem(int position) {
			return options.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View child = activity.getLayoutInflater().inflate(R.layout.spinner, parent, false);

			TextView text = (TextView) child.findViewById(R.id.actionbar_text);
			text.setText(getItem(position).text);

			TextView subtext = (TextView) child.findViewById(R.id.actionbar_subtext);
			subtext.setText(getItem(position).subtext);

			return child;
		}

	}

	private class Callback implements ActionBar.OnNavigationListener {
		public boolean onNavigationItemSelected(int position, long itemId) {
			options.get(position).onSelected();
			return true;
		}
	}
}
