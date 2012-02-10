package net.mabako.zwickau.mensa;

import java.util.Calendar;

import android.app.ActionBar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

public class MenuICS extends MenuHelper {
	private MensaActivity activity;

	public MenuICS() {
		activity = MensaActivity.getInstance();

		ActionBar actionBar = activity.getActionBar();
		View spinner = activity.getLayoutInflater().inflate(R.layout.spinner, null);
		actionBar.setCustomView(spinner);
		actionBar.setDisplayShowCustomEnabled(true);

		updateTitle(activity.getToday());
	}

	public void updateTitle(int day) {
		TextView text = (TextView) activity.findViewById(R.id.actionbar_text);
		text.setText(activity.getNameOfDay(day));

		TextView subtext = (TextView) activity.findViewById(R.id.actionbar_subtext);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, day - activity.getToday());
		subtext.setText(DateUtils.formatDateTime(activity, c.getTimeInMillis(), DateUtils.FORMAT_SHOW_YEAR));
	}
}
