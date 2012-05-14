package net.mabako.zwickau.mensa;

import android.content.Context;
import android.widget.ListView;

/**
 * Listview mit Titel
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class TitledListView extends ListView {
	private int day;
	
	public TitledListView(int day, Context context) {
		super(context);
		this.day = day;
	}

	public int getDay() {
		return day;
	}
}
